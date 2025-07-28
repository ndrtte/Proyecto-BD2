package hn.unah.proyecto.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PeliculaDTO {
    
    private Integer idPelicula;
    private String titulo;
    private List<CategoriaDTO> categoria;
    private String audiencia;

   
}
