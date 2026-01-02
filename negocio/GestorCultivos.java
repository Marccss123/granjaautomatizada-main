package granjaautomatizada.negocio;

import granjaautomatizada.modelo.Cultivo;
import granjaautomatizada.modelo.Parcela;
import granjaautomatizada.utilitario.Util;

import java.util.ArrayList;
import java.util.Scanner;

// Clase encargada de toda la lógica de cultivos
public class GestorCultivos {

    private GestorGranja gestorGranja;

    // Lista general de cultivos (para opción 9 del menú)
    private ArrayList<Cultivo> cultivosRegistrados;

    public GestorCultivos(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
        this.cultivosRegistrados = new ArrayList<>();
    }

    // Registrar cultivos para todas las parcelas
    public void registrarCultivosEnParcelas(Scanner scanner) {

        System.out.println("\n=== REGISTRO DE CULTIVOS ===");

        for (Parcela parcela : gestorGranja.getParcelas()) {

            System.out.println("\nParcela: " + parcela.getId()
                    + " (" + parcela.getMetrosCuadrados() + " m²)");

            System.out.print("¿Desea sembrar un cultivo aquí? (1=Sí / 0=No): ");
            int opcion = Util.leerEntero(scanner, "  --> ");


            if (opcion == 1) {

                System.out.print("Nombre del cultivo: ");
                String nombre = scanner.nextLine();

                System.out.print("Humedad mínima requerida (%): ");
                int humedadMin = Util.leerEntero(scanner, "  --> ");

                System.out.print("Humedad máxima permitida (%): ");
                int humedadMax = Util.leerEntero(scanner, "  --> ");

                System.out.print("Cada cuántas horas se riega: ");
                int frecuencia = Util.leerEntero(scanner, "  --> ");


                Cultivo cultivo = new Cultivo(
                        nombre,
                        humedadMin,
                        humedadMax,
                        frecuencia
                );

                // Asociamos cultivo a la parcela
                parcela.setCultivo(cultivo);

                // Guardamos cultivo en la lista general
                cultivosRegistrados.add(cultivo);

                System.out.println("✔ Cultivo " + nombre
                        + " registrado en " + parcela.getId());

            } else {
                System.out.println("⚠ Parcela sin cultivo.");
            }
        }
    }

    // Muestra todos los cultivos disponibles en el sistema
    public void mostrarCultivosDisponibles() {

        System.out.println("\n=== LISTA DE CULTIVOS DISPONIBLES ===");

        boolean hayCultivos = false;

        for (granjaautomatizada.modelo.Parcela parcela : gestorGranja.getParcelas()) {

            if (parcela.getCultivo() != null) {
                hayCultivos = true;
                System.out.println("\nCULTIVO: " + parcela.getCultivo().getNombre());
                System.out.println("  Donde se encuentra: " + parcela.getId());
                System.out.println("  Humedad requerida: "
                        + parcela.getCultivo().getHumedadMinima() + "% - "
                        + parcela.getCultivo().getHumedadMaxima() + "%");
                System.out.println("  Cada cuánto se riega: "
                        + parcela.getCultivo().getFrecuenciaRiegoHoras() + " horas");
            }
        }

        if (!hayCultivos) {
            System.out.println("⚠ No hay cultivos registrados.");
        }
    }
    // Cambia el cultivo de una parcela
    public void cambiarCultivoParcela(Scanner scanner) {

        System.out.println("\n=== CAMBIAR CULTIVO DE PARCELA ===");

        boolean hayParcelasConCultivo = false;

        for (Parcela parcela : gestorGranja.getParcelas()) {
            if (parcela.getCultivo() != null) {
                hayParcelasConCultivo = true;
                System.out.println(parcela.getId()
                        + " - Cultivo actual: "
                        + parcela.getCultivo().getNombre());
            }
        }

        if (!hayParcelasConCultivo) {
            System.out.println("⚠ No hay parcelas con cultivo.");
            return;
        }

        System.out.print("\nIngrese el ID de la parcela: ");
        String id = scanner.next();
        scanner.nextLine(); // limpiar buffer

        Parcela parcelaSeleccionada = null;

        for (Parcela p : gestorGranja.getParcelas()) {
            if (p.getId().equals(id)) {
                parcelaSeleccionada = p;
                break;
            }
        }

        if (parcelaSeleccionada == null || parcelaSeleccionada.getCultivo() == null) {
            System.out.println("⚠ Parcela inválida o sin cultivo.");
            return;
        }

        // Eliminamos cultivo anterior
        parcelaSeleccionada.setCultivo(null);

        System.out.println("Ingrese los datos del nuevo cultivo:");

        System.out.print("Nombre del cultivo: ");
        String nombre = scanner.nextLine();

        System.out.print("Humedad mínima (%): ");
        int min = scanner.nextInt();

        System.out.print("Humedad máxima (%): ");
        int max = scanner.nextInt();

        System.out.print("Frecuencia de riego (horas): ");
        int freq = scanner.nextInt();

        Cultivo nuevo = new Cultivo(nombre, min, max, freq);
        parcelaSeleccionada.setCultivo(nuevo);

        System.out.println("✔ Cultivo cambiado correctamente en "
                + parcelaSeleccionada.getId());
    }


}
