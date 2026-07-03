package usuario.cl.usuario_service.exception;

public class EmailDuplicadoException
        extends RuntimeException {

    public EmailDuplicadoException(
            String email) {

        super(
                "Ya existe un usuario registrado con el correo: "
                        + email
        );
    }
}