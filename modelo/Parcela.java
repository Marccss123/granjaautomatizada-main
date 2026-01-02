package granjaautomatizada.modelo;

import java.util.ArrayList;

// Representa una parcela de terreno
public class Parcela {

    private String id;
    private double metrosCuadrados;

    // Cada parcela puede tener un cultivo
    private Cultivo cultivo;

    // Una parcela puede tener varios aspersores y sensores
    private ArrayList<Aspersor> aspersores;
    private ArrayList<SensorHumedad> sensores;

    public Parcela(String id, double metrosCuadrados) {
        this.id = id;
        this.metrosCuadrados = metrosCuadrados;
        this.aspersores = new ArrayList<>();
        this.sensores = new ArrayList<>();
    }

    // Getters y setters básicos
    public String getId() {
        return id;
    }

    public double getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(double metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public Cultivo getCultivo() {
        return cultivo;
    }

    public void setCultivo(Cultivo cultivo) {
        this.cultivo = cultivo;
    }

    public ArrayList<Aspersor> getAspersores() {
        return aspersores;
    }

    public ArrayList<SensorHumedad> getSensores() {
        return sensores;
    }
}

