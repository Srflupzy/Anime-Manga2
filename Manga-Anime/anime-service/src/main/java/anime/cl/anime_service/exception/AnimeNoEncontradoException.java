package anime.cl.anime_service.exception;



public class AnimeNoEncontradoException
        extends RuntimeException {

    public AnimeNoEncontradoException(
            String mensaje) {

        super(mensaje);
    }


}