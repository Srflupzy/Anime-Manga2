package favoritos.cl.favoritos_service.exception;

public class FavoritosNoEncontradoException extends RuntimeException {

    public FavoritosNoEncontradoException(
            String mensaje) {

        super(mensaje);
    }
}
