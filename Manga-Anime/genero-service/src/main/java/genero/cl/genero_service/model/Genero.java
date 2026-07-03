package genero.cl.genero_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "generos")
public class Genero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nombre no puede estar vacio.")
    @Size(max = 50, message = "Nombre puede tener maximo 50 caracteres")
    private String nombre;

    @Size(max = 150, message = "Descripcion puede tener maximo 150 caracteres")
    private String descripcion;
}
