package panaderia.controlador.utilidades;

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
