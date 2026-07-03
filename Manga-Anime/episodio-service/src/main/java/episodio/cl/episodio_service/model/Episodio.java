package episodio.cl.episodio_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "episodios")
@Data
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100,
            message = "El título no puede tener más de 100 caracteres")
    private String titulo;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1,
            message = "La duración debe ser mayor a 0")
    private Integer duracion;

    @NotNull(message = "La temporada es obligatoria")
    @Column(name = "id_temporada")
    private Long temporadaId;
}
