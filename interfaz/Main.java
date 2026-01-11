package granjaautomatizada.interfaz;

import java.util.Scanner;
import granjaautomatizada.negocio.GestorGranja;
import granjaautomatizada.utilitario.Util;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        GestorGranja gestor = new GestorGranja();

        int opcion;

        do {
            try{

            Util.mostrarMenuPrincipal();
            opcion = Util.leerEntero(scanner, "");

            switch (opcion) {
                case 1://Añadir más terreno
                    System.out.println("----------------");
                    gestor.getGestorParcelas().crearParcelasDesdeTerreno(scanner);
                    pausa();
                    break;
                case 2://Mostrar información de las parcelas
                    System.out.println("----------------");
                    gestor.getGestorParcelas().mostrarInfoParcelas();
                    pausa();
                    break;
                case 3://Mostrar información de los aspersores
                    System.out.println("----------------");
                    gestor.getGestorAspersores().mostrarInfoAspersores();
                    pausa();
                    break;
                case 4://Mostrar información de los sensores de humedad
                    System.out.println("----------------");
                    gestor.getGestorSensores().mostrarInfoSensores();
                    pausa();
                    break;
                case 5://Añadir más aspersores a una parcela
                    System.out.println("----------------");
                    System.out.print("Ingrese ID de la parcela: ");
                    gestor.getGestorAspersores()
                            .asignarAspersorAParcela(scanner.next());
                    pausa();
                    break;
                case 6://Añadir más sensores de humedad a una parcela
                    System.out.println("----------------");
                    System.out.print("Ingrese ID de la parcela: ");
                    gestor.getGestorSensores()
                            .asignarSensorAParcela(scanner.next());
                    pausa();
                    break;
                case 7: // Agregar más aspersores al inventario
                    System.out.println("----------------");
                    int cantidadAsp = Util.leerEntero(scanner, "¿Cuántos aspersores desea agregar?: ");
                    gestor.getGestorAspersores().agregarAspersoresAlInventario(cantidadAsp);
                    pausa();
                    break;
                case 8://Agregar más sensores de humedad al inventario
                    System.out.println("----------------");
                    int cantidadSensores = Util.leerEntero(scanner, "¿Cuántos sensores desea agregar?: ");
                    gestor.getGestorSensores().agregarSensoresAlInventario(cantidadSensores);
                    pausa();
                    break;
                case 9://Mostrar lista de cultivos
                    System.out.println("----------------");
                    gestor.getGestorCultivos().mostrarCultivosDisponibles();
                    pausa();
                    break;
                case 10://Eliminar una parcela
                    System.out.println("----------------");
                    gestor.getGestorParcelas().eliminarParcela(scanner);
                    pausa();
                    break;
                case 11://Cambiar cultivo en una parcela
                    System.out.println("----------------");
                    gestor.getGestorCultivos()
                            .cambiarCultivoParcela(scanner);
                    pausa();
                    break;
                case 12: // Verificar humedad y actuar
                    System.out.println("----------------");
                    gestor.getGestorAspersores().evaluarYRiegoAutomatico();
                    gestor.getGestorSensores().mostrarHumedadParcelas();
                    pausa();
                    break;
                case 13://Mostrar lecturas de un sensor
                    System.out.println("----------------");
                    gestor.getGestorSensores()
                            .mostrarLecturasSensor(scanner);
                    pausa();
                    break;
                case 14://Mostrar historial de activación de un aspersor
                    System.out.println("----------------");
                    gestor.getGestorAspersores()
                            .mostrarHistorialAspersor(scanner);
                    pausa();
                    break;
                case 15://Prender manualmente un aspersor
                    System.out.println("----------------");
                    gestor.getGestorAspersores()
                            .prenderAspersorManual(scanner);
                    pausa();
                    break;
                case 16://Conectar o desconectar un aspersor
                    System.out.println("----------------");
                    gestor.getGestorAspersores()
                            .cambiarConexionAspersor(scanner);
                    pausa();
                    break;
                case 17://Conectar o desconectar un sensor
                    System.out.println("----------------");
                    gestor.getGestorSensores()
                            .cambiarConexionSensor(scanner);
                    pausa();
                    break;
                case 18://Eliminar un aspersor del sistema
                    System.out.println("----------------");
                    gestor.getGestorAspersores()
                            .eliminarAspersor(scanner);
                    pausa();
                    break;
                case 19://Eliminar un sensor del sistema
                    System.out.println("----------------");
                    gestor.getGestorSensores()
                            .eliminarSensor(scanner);
                    pausa();
                    break;
                case 20://Salir
                    System.out.println("-----------------");
                    System.out.println("Saliendo del sistema...");
                default:
                    System.out.println("----------------");
                    System.out.println("Opción inválida.");
            }
            } catch (Exception e) {
                System.out.println("\n⚠ ¡Ups! Ocurrió un error inesperado: " + e.getMessage());
                System.out.println("🔄 Volviendo al menú principal...");
                opcion = 0;
            }
        } while (opcion != 20);

        scanner.close();
    }

    public static void pausa() {
        System.out.println("__________Presione enter para continuar__________");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }
}


