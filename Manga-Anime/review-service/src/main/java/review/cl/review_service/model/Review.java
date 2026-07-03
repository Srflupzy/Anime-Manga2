package review.cl.review_service.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El comentario no puede estar vacío")
    @Size(max = 300,
            message = "El comentario no puede superar los 300 caracteres")
    private String comentario;

    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 1,
            message = "La puntuación mínima es 1")
    @Max(value = 10,
            message = "La puntuación máxima es 10")
    private Integer puntuacion;

    @NotNull(message = "El usuario es obligatorio")
    @Column(name = "id_usuario")
    private Long usuarioId;

    @NotNull(message = "El anime es obligatorio")
    @Column(name = "id_anime")
    private Long animeId;
}
