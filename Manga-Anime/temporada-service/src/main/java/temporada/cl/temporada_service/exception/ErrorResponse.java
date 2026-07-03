package temporada.cl.temporada_service.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private LocalDateTime fechaHora;
    private int estado;
    private String error;
    private String mensaje;
    private String ruta;

    public ErrorResponse(LocalDateTime fechaHora,
                         int estado,
                         String error,
                         String mensaje,
                         String ruta) {

        this.fechaHora = fechaHora;
        this.estado = estado;
        this.error = error;
        this.mensaje = mensaje;
        this.ruta = ruta;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public int getEstado() {
        return estado;
    }

    public String getError() {
        return error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getRuta() {
        return ruta;
    }
}