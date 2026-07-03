package usuario.cl.usuario_service.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import usuario.cl.usuario_service.Rol;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50,
            message = "El nombre no puede superar los 50 caracteres")
    private String nombre;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    @Size(max = 100,
            message = "El email no puede superar los 100 caracteres")
    private String email;

    @NotBlank(message = "La clave no puede estar vacía")
    @Size(min = 6,
            message = "La clave debe tener al menos 6 caracteres")
    @Size(max = 100,
            message = "La clave no puede superar los 100 caracteres")
    private String clave;


    @Enumerated(
            EnumType.STRING
    )
    private Rol rol;
}
