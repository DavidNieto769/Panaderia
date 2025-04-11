package panaderia.controlador.utilidades;

// Representa el resultado de una operación con estado de éxito y mensaje opcional
public class ResultadoOperacion {
    private boolean exito;
    private String mensaje;

    public ResultadoOperacion(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public ResultadoOperacion(boolean exito) {
        this.exito = exito;
    }

    public boolean isExito() {
        return exito;
    }

    public String getMensaje() {
        return mensaje;
    }
}
