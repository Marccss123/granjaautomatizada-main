package granjaautomatizada.modelo;


import java.time.LocalDateTime;

// Representa una lectura de humedad hecha por un sensor
public class LecturaHumedad {

    private LocalDateTime fecha;
    private int porcentajeHumedad;

    public LecturaHumedad(LocalDateTime fecha, int porcentajeHumedad) {
        this.fecha = fecha;
        this.porcentajeHumedad = porcentajeHumedad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getPorcentajeHumedad() {
        return porcentajeHumedad;
    }
}