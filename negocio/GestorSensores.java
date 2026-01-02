package granjaautomatizada.negocio;

import granjaautomatizada.modelo.*;

import java.util.Scanner;

public class GestorSensores {

    private GestorGranja gestorGranja;

    public GestorSensores(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
    }

    // Simula lecturas de todos los sensores
    public void simularLecturas() {

        for (Parcela parcela : gestorGranja.getParcelas()) {

            for (SensorHumedad sensor : parcela.getSensores()) {

                LecturaHumedad lectura = sensor.leerHumedad();

                if (lectura != null) {
                    System.out.println("Sensor " + sensor.getId()
                            + " | Parcela " + parcela.getId()
                            + " | Humedad: "
                            + lectura.getPorcentajeHumedad() + "%");
                } else {
                    System.out.println("⚠ Sensor " + sensor.getId()
                            + " está desconectado.");
                }
            }
        }
    }
    // Muestra información de todos los sensores
    public void mostrarInfoSensores() {

        System.out.println("\n=== INFORMACIÓN DE SENSORES DE HUMEDAD ===");
        System.out.println("Cantidad total de sensores: "
                + gestorGranja.getSensoresInventario().size());

        for (SensorHumedad sensor : gestorGranja.getSensoresInventario()) {

            System.out.println("\n" + sensor.getId() + ":");

            if (sensor.getParcela() != null) {
                System.out.println("  Se encuentra en: "
                        + sensor.getParcela().getId());
            } else {
                System.out.println("  Se encuentra en: INVENTARIO");
            }

            if (!sensor.getLecturas().isEmpty()) {
                LecturaHumedad ultima =
                        sensor.getLecturas().get(sensor.getLecturas().size() - 1);
                System.out.println("  Última lectura: "
                        + ultima.getPorcentajeHumedad() + "%");
            } else {
                System.out.println("  Última lectura: SIN LECTURAS");
            }

            System.out.println("  Conexión: "
                    + (sensor.isConectado() ? "CONECTADO" : "DESCONECTADO"));
        }
    }
    // Agregar nuevos sensores al inventario
    public void agregarSensoresAlInventario(int cantidad) {

        int inicio = gestorGranja.getSensoresInventario().size() + 1;

        for (int i = 0; i < cantidad; i++) {
            String id = granjaautomatizada.utilitario.Util.generarId("SENSOR", inicio + i);
            gestorGranja.getSensoresInventario().add(new SensorHumedad(id));
        }

        System.out.println("✔ Se agregaron " + cantidad + " sensores al inventario.");
    }
    // Asignar un sensor del inventario a una parcela
    public void asignarSensorAParcela(String idParcela) {

        SensorHumedad disponible = null;

        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            if (s.getParcela() == null) {
                disponible = s;
                break;
            }
        }

        if (disponible == null) {
            System.out.println("⚠ No hay sensores disponibles en el inventario.");
            return;
        }

        for (granjaautomatizada.modelo.Parcela parcela : gestorGranja.getParcelas()) {
            if (parcela.getId().equals(idParcela)) {
                disponible.setParcela(parcela);
                parcela.getSensores().add(disponible);
                System.out.println("✔ Sensor " + disponible.getId()
                        + " asignado a " + parcela.getId());
                return;
            }
        }

        System.out.println("⚠ Parcela no encontrada.");
    }
    // Muestra la humedad actual de todas las parcelas
    public void mostrarHumedadParcelas() {

        System.out.println("\n=== HUMEDAD ACTUAL DE LAS PARCELAS ===");

        for (Parcela parcela : gestorGranja.getParcelas()) {

            if (parcela.getSensores().isEmpty()) {
                System.out.println(parcela.getId()
                        + ": SIN SENSOR");
                continue;
            }

            SensorHumedad sensor = parcela.getSensores().get(0);

            if (!sensor.isConectado()) {
                System.out.println(parcela.getId()
                        + ": SENSOR DESCONECTADO");
                continue;
            }

            if (sensor.getLecturas().isEmpty()) {
                System.out.println(parcela.getId()
                        + ": SIN LECTURAS");
                continue;
            }

            LecturaHumedad ultima =
                    sensor.getLecturas()
                            .get(sensor.getLecturas().size() - 1);

            System.out.println(parcela.getId()
                    + ": Humedad: "
                    + ultima.getPorcentajeHumedad() + "%");
        }
    }
    // Muestra el historial de lecturas de un sensor
    public void mostrarLecturasSensor(Scanner scanner) {

        System.out.println("\n=== HISTORIAL DE LECTURAS DE SENSOR ===");

        if (gestorGranja.getSensoresInventario().isEmpty()) {
            System.out.println("⚠ No hay sensores registrados.");
            return;
        }

        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            System.out.println(s.getId()
                    + " - Parcela: "
                    + (s.getParcela() != null ? s.getParcela().getId() : "SIN PARCELA"));
        }

        System.out.print("\nIngrese el ID del sensor: ");
        String id = scanner.next();

        SensorHumedad sensor = null;

        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            if (s.getId().equals(id)) {
                sensor = s;
                break;
            }
        }

        if (sensor == null) {
            System.out.println("⚠ Sensor no encontrado.");
            return;
        }

        if (sensor.getLecturas().isEmpty()) {
            System.out.println("⚠ El sensor no tiene lecturas.");
            return;
        }

        System.out.println("\nLecturas del " + sensor.getId() + ":");

        for (LecturaHumedad lectura : sensor.getLecturas()) {
            System.out.println("Fecha: " + lectura.getFecha()
                    + " | Humedad: "
                    + lectura.getPorcentajeHumedad() + "%");
        }
    }
    // Conectar o desconectar sensor
    public void cambiarConexionSensor(Scanner scanner) {

        System.out.println("\n=== CONECTAR / DESCONECTAR SENSOR ===");

        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            System.out.println(s.getId()
                    + " | Conectado: " + s.isConectado());
        }

        System.out.print("\nIngrese el ID del sensor: ");
        String id = scanner.next();

        SensorHumedad sensor = null;

        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            if (s.getId().equals(id)) {
                sensor = s;
                break;
            }
        }

        if (sensor == null) {
            System.out.println("⚠ Sensor no encontrado.");
            return;
        }

        sensor.setConectado(!sensor.isConectado());

        System.out.println("✔ Estado cambiado. Ahora está "
                + (sensor.isConectado() ? "CONECTADO" : "DESCONECTADO"));
    }
    // Eliminar sensor completamente
    public void eliminarSensor(Scanner scanner) {

        System.out.println("\n=== ELIMINAR SENSOR ===");

        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            System.out.println(s.getId());
        }

        System.out.print("\nIngrese el ID del sensor a eliminar: ");
        String id = scanner.next();

        SensorHumedad sensor = null;

        for (SensorHumedad s : gestorGranja.getSensoresInventario()) {
            if (s.getId().equals(id)) {
                sensor = s;
                break;
            }
        }

        if (sensor == null) {
            System.out.println("⚠ Sensor no encontrado.");
            return;
        }

        if (sensor.getParcela() != null) {
            sensor.getParcela().getSensores().remove(sensor);
            System.out.println("⚠ "
                    + sensor.getParcela().getId()
                    + " quedó sin sensor.");
        }

        gestorGranja.getSensoresInventario().remove(sensor);
        System.out.println("✔ Sensor eliminado correctamente.");
    }

}
