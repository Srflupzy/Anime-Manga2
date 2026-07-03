package manga.cl.manga_service.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mangas")
@Data
public class Manga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100,
            message = "El título no puede superar los 100 caracteres")
    private String titulo;

    @NotBlank(message = "El autor no puede estar vacío")
    @Size(max = 100,
            message = "El autor no puede superar los 100 caracteres")
    private String autor;

    @NotNull(message = "La cantidad de tomos es obligatoria")
    @Min(value = 1,
            message = "Debe existir al menos 1 tomo")
    private Integer tomos;

    @NotNull(message = "El género es obligatorio")
    @Column(name = "id_genero")
    private Long generoId;
}
