package personaje.cl.personaje_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "personajes")
@Data
public class Personaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100,
            message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "El rol no puede estar vacío")
    @Size(max = 50,
            message = "El rol no puede superar los 50 caracteres")
    private String rol;

    @NotNull(message = "El anime es obligatorio")
    @Column(name = "id_anime")
    private Long animeId;
}
