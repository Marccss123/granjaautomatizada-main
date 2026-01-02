package granjaautomatizada.modelo;


import java.time.LocalDateTime;
import java.util.ArrayList;

// Representa un aspersor de riego
public class Aspersor {

    private String id;
    private boolean conectado;
    private boolean encendido;

    // Parcela donde está instalado (asociación)
    private Parcela parcela;

    // Historial de encendidos
    private ArrayList<LocalDateTime> historialEncendidos;

    public Aspersor(String id) {
        this.id = id;
        this.conectado = true;
        this.encendido = false;
        this.historialEncendidos = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public boolean isEncendido() {
        return encendido;
    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    // Enciende el aspersor si está conectado
    public void encender() {
        if (conectado) {
            this.encendido = true;
            historialEncendidos.add(LocalDateTime.now());
        }
    }

    public void apagar() {
        this.encendido = false;
    }

    public ArrayList<LocalDateTime> getHistorialEncendidos() {
        return historialEncendidos;
    }
}