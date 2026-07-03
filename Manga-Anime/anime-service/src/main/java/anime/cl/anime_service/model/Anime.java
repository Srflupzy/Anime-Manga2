package anime.cl.anime_service.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "animes")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El titulo no puede estar vacio")
    @Size(max = 100, message = "El titulo puede tener maximo 100 caracteres")
    private String titulo;

    @NotBlank(message = "La descripcion no puede estar vacia.")
    @Size(max = 300, message = "La descripcion puede tener maximo 300 caracteres")
    private String descripcion;

    @NotBlank(message = "El estudio no puede estar vacio.")
    @Size(max = 100, message = "El  estudio puede tener maximo 100 caracteres")
    private String estudio;

    @NotNull(message = "Temporadas no puede estar vacio.")
    @Positive(message = "Temporadas debe ser mayor a 0")
    private Integer temporadas;

    @NotNull(message =
            "Genero no puede estar vacio.")
    @Column(name = "id_genero")
    private Long generoId;
}
