package granjaautomatizada.negocio;


import granjaautomatizada.modelo.*;

import java.time.LocalDateTime;
import java.util.Scanner;

public class GestorAspersores {

    private GestorGranja gestorGranja;

    public GestorAspersores(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
    }

    // Evalúa humedad y riega automáticamente si es necesario
    public void evaluarYRiegoAutomatico() {

        for (Parcela parcela : gestorGranja.getParcelas()) {

            Cultivo cultivo = parcela.getCultivo();

            if (cultivo == null) continue;


            if (parcela.getSensores().isEmpty()) {
                System.out.println(" " + parcela.getId()
                        + " no tiene sensor.");
                continue;
            }

            SensorHumedad sensor = parcela.getSensores().get(0);
            LecturaHumedad lectura = sensor.leerHumedad();

            if (lectura == null) {
                System.out.println(" Sensor desconectado en "
                        + parcela.getId());
                continue;
            }

            int humedad = lectura.getPorcentajeHumedad();

            Aspersor aspersor = null;
            if (!parcela.getAspersores().isEmpty()) {
                aspersor = parcela.getAspersores().get(0);
            }

            if (humedad < cultivo.getHumedadMinima()) {


                if (aspersor == null) {
                    System.out.println(parcela.getId() + " necesita riego, pero NO tiene aspersor.");
                } else if (!aspersor.isConectado()) {
                    System.out.println(" Aspersor desconectado en " + parcela.getId());
                } else {
                    if (!aspersor.isEncendido()) {
                        aspersor.encender();
                        System.out.println(" RIEGO ACTIVADO en " + parcela.getId()
                                + " | Humedad actual: " + humedad + "%");
                    } else {
                        System.out.println(parcela.getId() + " sigue regando...");
                    }
                }
            } else {
                // CASO: HUMEDAD CORRECTA (Intenta apagar)
                if (aspersor != null && aspersor.isEncendido()) {
                    aspersor.apagar();
                    System.out.println("Riego DETENIDO en " + parcela.getId()
                            + " (Humedad recuperada: " + humedad + "%)");
                } else {
                    System.out.println(parcela.getId()
                            + " humedad correcta: " + humedad + "%");
                }
            }
        }
    }


    // Muestra información de todos los aspersores
    public void mostrarInfoAspersores() {

        System.out.println("\n=== INFORMACIÓN DE ASPERSORES ===");
        System.out.println("Cantidad total de aspersores: "
                + gestorGranja.getAspersoresInventario().size());

        for (Aspersor aspersor : gestorGranja.getAspersoresInventario()) {

            System.out.println("\n" + aspersor.getId() + ":");

            if (aspersor.getParcela() != null) {
                System.out.println("  Se encuentra en: "
                        + aspersor.getParcela().getId());
            } else {
                System.out.println("  Se encuentra en: INVENTARIO");
            }

            if (!aspersor.getHistorialEncendidos().isEmpty()) {
                System.out.println("  Última hora activado: "
                        + aspersor.getHistorialEncendidos()
                        .get(aspersor.getHistorialEncendidos().size() - 1));
            } else {
                System.out.println("  Última hora activado: NUNCA");
            }

            System.out.println("  Estado: "
                    + (aspersor.isEncendido() ? "PRENDIDO" : "APAGADO"));

            System.out.println("  Conexión: "
                    + (aspersor.isConectado() ? "CONECTADO" : "DESCONECTADO"));
        }
    }
    // Agregar nuevos aspersores al inventario
    public void agregarAspersoresAlInventario(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            int siguienteNum=gestorGranja.getSiguienteIdAspersor();
            String id = granjaautomatizada.utilitario.Util.generarId("ASPERSOR", siguienteNum);
            gestorGranja.getAspersoresInventario().add(new Aspersor(id));
        }
        System.out.println(" Se agregaron " + cantidad + " aspersores al inventario.");
    }

    // Asignar un aspersor del inventario a una parcela
    public void asignarAspersorAParcela(String idParcela) {

        Aspersor disponible = null;

        for (Aspersor a : gestorGranja.getAspersoresInventario()) {
            if (a.getParcela() == null) {
                disponible = a;
                break;
            }
        }

        if (disponible == null) {
            System.out.println("No hay aspersores disponibles en el inventario.");
            return;
        }

        for (granjaautomatizada.modelo.Parcela parcela : gestorGranja.getParcelas()) {
            if (parcela.getId().equals(idParcela)) {
                disponible.setParcela(parcela);
                parcela.getAspersores().add(disponible);
                System.out.println("Aspersor " + disponible.getId()
                        + " asignado a " + parcela.getId());
                return;
            }
        }

        System.out.println("Parcela no encontrada.");
    }
    // Muestra historial de activaciones de un aspersor
    public void mostrarHistorialAspersor(Scanner scanner) {

        System.out.println("\n=== HISTORIAL DE ASPERSOR ===");

        if (gestorGranja.getAspersoresInventario().isEmpty()) {
            System.out.println("No hay aspersores registrados.");
            return;
        }

        for (Aspersor a : gestorGranja.getAspersoresInventario()) {
            System.out.println(a.getId()
                    + " - Parcela: "
                    + (a.getParcela() != null ? a.getParcela().getId() : "SIN PARCELA"));
        }

        System.out.print("\nIngrese el ID del aspersor: ");
        String id = scanner.next();

        Aspersor aspersor = null;

        for (Aspersor a : gestorGranja.getAspersoresInventario()) {
            if (a.getId().equals(id)) {
                aspersor = a;
                break;
            }
        }

        if (aspersor == null) {
            System.out.println("Aspersor no encontrado.");
            return;
        }

        if (aspersor.getHistorialEncendidos().isEmpty()) {
            System.out.println("El aspersor nunca se ha encendido.");
            return;
        }

        System.out.println("\nHistorial de encendidos de "
                + aspersor.getId() + ":");

        for (LocalDateTime fecha : aspersor.getHistorialEncendidos()) {
            System.out.println("Encendido en: " + fecha);
        }
    }
    // Encendido manual de aspersor
    public void prenderAspersorManual(Scanner scanner) {

        System.out.println("\n=== ENCENDER ASPERSOR MANUALMENTE ===");

        for (Aspersor a : gestorGranja.getAspersoresInventario()) {
            System.out.println(a.getId()
                    + " | Parcela: "
                    + (a.getParcela() != null ? a.getParcela().getId() : "SIN PARCELA")
                    + " | Conectado: " + a.isConectado());
        }

        System.out.print("\nIngrese el ID del aspersor: ");
        String id = scanner.next();

        Aspersor aspersor = null;

        for (Aspersor a : gestorGranja.getAspersoresInventario()) {
            if (a.getId().equals(id)) {
                aspersor = a;
                break;
            }
        }

        if (aspersor == null) {
            System.out.println("Aspersor no encontrado.");
            return;
        }

        if (!aspersor.isConectado()) {
            System.out.println("El aspersor está DESCONECTADO. No se puede encender.");
            return;
        }

        aspersor.encender();

        System.out.println("Aspersor " + aspersor.getId()
                + " encendido manualmente.");
    }
    // Conectar o desconectar aspersor
    public void cambiarConexionAspersor(Scanner scanner) {

        System.out.println("\n=== CONECTAR / DESCONECTAR ASPERSOR ===");

        for (Aspersor a : gestorGranja.getAspersoresInventario()) {
            System.out.println(a.getId()
                    + " | Conectado: " + a.isConectado());
        }

        System.out.print("\nIngrese el ID del aspersor: ");
        String id = scanner.next();

        Aspersor aspersor = null;

        for (Aspersor a : gestorGranja.getAspersoresInventario()) {
            if (a.getId().equals(id)) {
                aspersor = a;
                break;
            }
        }

        if (aspersor == null) {
            System.out.println("Aspersor no encontrado.");
            return;
        }

        aspersor.setConectado(!aspersor.isConectado());

        System.out.println("Estado cambiado. Ahora está "
                + (aspersor.isConectado() ? "CONECTADO" : "DESCONECTADO"));
    }
    // Eliminar aspersor completamente
    public void eliminarAspersor(Scanner scanner) {

        System.out.println("\n=== ELIMINAR ASPERSOR ===");

        for (Aspersor a : gestorGranja.getAspersoresInventario()) {
            System.out.println(a.getId());
        }

        System.out.print("\nIngrese el ID del aspersor a eliminar: ");
        String id = scanner.next();

        Aspersor aspersor = null;

        for (Aspersor a : gestorGranja.getAspersoresInventario()) {
            if (a.getId().equals(id)) {
                aspersor = a;
                break;
            }
        }

        if (aspersor == null) {
            System.out.println("Aspersor no encontrado.");
            return;
        }

        if (aspersor.getParcela() != null) {
            aspersor.getParcela().getAspersores().remove(aspersor);
            System.out.println(aspersor.getParcela().getId()
                    + " quedó sin aspersor.");
        }

        gestorGranja.getAspersoresInventario().remove(aspersor);
        System.out.println("Aspersor eliminado correctamente.");
    }

}