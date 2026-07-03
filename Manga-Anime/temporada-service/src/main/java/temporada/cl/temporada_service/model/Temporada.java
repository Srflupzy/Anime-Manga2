package temporada.cl.temporada_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "temporadas")
public class Temporada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El número de temporada es obligatorio")
    @Min(value = 1, message = "La temporada debe ser mínimo 1")
    private Integer numero;

    @NotNull(message = "El anime es obligatorio")
    @Column(name = "id_anime")
    private Long animeId;
}
