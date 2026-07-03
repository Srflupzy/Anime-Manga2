package review.cl.review_service.exception;

public class ReviewNoEncontradaException
        extends RuntimeException {

    public ReviewNoEncontradaException(
            String mensaje) {

        super(mensaje);
    }
}
