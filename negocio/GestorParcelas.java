package granjaautomatizada.negocio;

import granjaautomatizada.modelo.Parcela;
import granjaautomatizada.utilitario.Util;

import java.util.Scanner;

// Clase encargada de toda la lógica relacionada con parcelas
public class GestorParcelas {

    private GestorGranja gestorGranja;
    private int contadorParcelas = 0;

    public GestorParcelas(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
    }

    // Método que crea parcelas a partir del terreno ingresado
    public void crearParcelasDesdeTerreno(Scanner scanner) {


        double terrenoTotal = Util.leerEntero(scanner, "Ingrese la cantidad total de terreno en m²: ");

        double terrenoRestante = terrenoTotal;

        // Mientras haya terreno disponible
        while (terrenoRestante > 0) {

            double tamanioParcela;

            // Si hay 50 o más, se crea parcela de 50
            if (terrenoRestante >= 50) {
                tamanioParcela = 50;
            } else {
                // Si queda menos de 50, se crea una parcela más pequeña
                tamanioParcela = terrenoRestante;
            }

            contadorParcelas++;
            String idParcela = Util.generarId("PARCELA", contadorParcelas);

            Parcela parcela = new Parcela(idParcela, tamanioParcela);

            // Se agrega la parcela al sistema
            gestorGranja.getParcelas().add(parcela);

            System.out.println("Parcela creada: " + idParcela +
                    " | Tamaño: " + tamanioParcela + " m²");

            terrenoRestante -= tamanioParcela;
        }

        System.out.println("\nTotal de parcelas creadas: "
                + gestorGranja.getParcelas().size());
        // Luego de crear todas las parcelas
        asignarAspersoresYSensores(scanner);
        // Luego de asignar aspersores y sensores
        gestorGranja.getGestorCultivos().registrarCultivosEnParcelas(scanner);

    }


    private void asignarAspersoresYSensores(Scanner scanner) {


        int cantidadAspersores = Util.leerEntero(scanner, "\nIngrese la cantidad de aspersores disponibles: ");
        int cantidadSensores = Util.leerEntero(scanner, "Ingrese la cantidad de sensores de humedad disponibles: ");

        // Crear aspersores en inventario
        for (int i = 0; i < cantidadAspersores; i++) {
            // Pedimos el siguiente número único al gestor
            int siguienteNum = gestorGranja.getSiguienteIdAspersor();
            String id = Util.generarId("ASPERSOR", siguienteNum);

            gestorGranja.getAspersoresInventario().add(
                    new granjaautomatizada.modelo.Aspersor(id)
            );
        }

        // Crear sensores en inventario
        for (int i = 0; i < cantidadSensores; i++) {
            int siguienteNum = gestorGranja.getSiguienteIdSensor();
            String id = Util.generarId("SENSOR", siguienteNum);

            gestorGranja.getSensoresInventario().add(
                    new granjaautomatizada.modelo.SensorHumedad(id)
            );
        }

        // VERIFICACIÓN CORRECTA PARA TU CÓDIGO (Uno a Uno)
        int totalParcelas = gestorGranja.getParcelas().size();

        if (cantidadAspersores < totalParcelas) {
            System.out.println("\nADVERTENCIA: Tienes " + totalParcelas + " parcelas pero solo "
                    + cantidadAspersores + " aspersores.");
            System.out.println("Las últimas " + (totalParcelas - cantidadAspersores) + " parcelas se quedarán sin aspersor.");
        }

        if (cantidadSensores < totalParcelas) {
            System.out.println("\nADVERTENCIA: Tienes " + totalParcelas + " parcelas pero solo "
                    + cantidadSensores + " sensores.");
            System.out.println("Las últimas " + (totalParcelas - cantidadSensores) + " parcelas se quedarán sin sensor.");
        }

        System.out.println("\nAsignando aspersores y sensores a parcelas...\n");

        int indiceAspersor = 0;
        int indiceSensor = 0;

        // Recorremos todas las parcelas
        for (granjaautomatizada.modelo.Parcela parcela : gestorGranja.getParcelas()) {

            // Asignar aspersor si hay
            if (indiceAspersor < gestorGranja.getAspersoresInventario().size()) {
                granjaautomatizada.modelo.Aspersor aspersor =
                        gestorGranja.getAspersoresInventario().get(indiceAspersor);
                aspersor.setParcela(parcela);
                parcela.getAspersores().add(aspersor);
                indiceAspersor++;
            } else {
                System.out.println("La " + parcela.getId()
                        + " NO tiene aspersor asignado.");
            }

            // Asignar sensor si hay
            if (indiceSensor < gestorGranja.getSensoresInventario().size()) {
                granjaautomatizada.modelo.SensorHumedad sensor =
                        gestorGranja.getSensoresInventario().get(indiceSensor);
                sensor.setParcela(parcela);
                parcela.getSensores().add(sensor);
                indiceSensor++;
            } else {
                System.out.println("La " + parcela.getId()
                        + " NO tiene sensor asignado.");
            }
        }

        System.out.println("\nAsignación finalizada.");
    }
    // Muestra información detallada de todas las parcelas
    public void mostrarInfoParcelas() {

        System.out.println("\n=== INFORMACIÓN DE PARCELAS ===");
        System.out.println("Cantidad de parcelas: " + gestorGranja.getParcelas().size());

        for (Parcela parcela : gestorGranja.getParcelas()) {

            System.out.println("\n" + parcela.getId() + ":");
            System.out.println("  Cantidad de terreno: " + parcela.getMetrosCuadrados() + " m²");

            if (parcela.getCultivo() != null) {
                System.out.println("  Cultivo: " + parcela.getCultivo().getNombre());
                System.out.println("  Humedad requerida: "
                        + parcela.getCultivo().getHumedadMinima() + "% - "
                        + parcela.getCultivo().getHumedadMaxima() + "%");
            } else {
                System.out.println("  Cultivo: SIN CULTIVO");
            }

            System.out.println("  Número de aspersores: " + parcela.getAspersores().size());
            System.out.println("  Número de sensores: " + parcela.getSensores().size());
        }
    }
    // Elimina una parcela del sistema
    public void eliminarParcela(Scanner scanner) {

        if (gestorGranja.getParcelas().isEmpty()) {
            System.out.println("No hay parcelas para eliminar.");
            return;
        }

        System.out.println("\n=== PARCELAS DISPONIBLES ===");
        for (Parcela parcela : gestorGranja.getParcelas()) {
            System.out.println(parcela.getId());
        }

        System.out.print("\nIngrese el ID de la parcela a eliminar: ");
        String id = scanner.next();

        // Buscar parcela
        Parcela parcelaEliminar = null;

        for (Parcela p : gestorGranja.getParcelas()) {
            if (p.getId().equals(id)) {
                parcelaEliminar = p;
                break;
            }
        }

        if (parcelaEliminar == null) {
            System.out.println("Parcela no encontrada.");
            return;
        }

        System.out.print("Ingrese nuevamente el ID para confirmar: ");
        String confirmacion = scanner.next();

        if (!confirmacion.equals(id)) {
            System.out.println("Confirmación incorrecta. Operación cancelada.");
            return;
        }

        // Liberar aspersores
        for (granjaautomatizada.modelo.Aspersor a : parcelaEliminar.getAspersores()) {
            a.setParcela(null);
            a.apagar();
        }

        // Liberar sensores
        for (granjaautomatizada.modelo.SensorHumedad s : parcelaEliminar.getSensores()) {
            s.setParcela(null);
        }

        // Eliminar parcela
        gestorGranja.getParcelas().remove(parcelaEliminar);

        System.out.println("✔ Parcela " + id + " eliminada correctamente.");
    }

}
