package temporada.cl.temporada_service.exception;

public class TemporadaDuplicadaException extends RuntimeException {

    public TemporadaDuplicadaException(
            Integer numero) {

        super(
                "Ya existe una temporada número "
                        + numero +
                        " para este anime"
        );
    }
}
