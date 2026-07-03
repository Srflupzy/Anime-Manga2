package anime.cl.anime_service.exception;

public class AnimeDuplicadoException extends RuntimeException {

    public AnimeDuplicadoException(
            String titulo) {

        super(
                "Ya existe un anime registrado con el título: "
                        + titulo
        );
    }
}
