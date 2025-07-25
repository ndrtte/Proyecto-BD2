package hn.unah.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeliculaDTO {
    
    private Integer idPelicula;
    private String titulo;
    private Integer idCategoria;
    // private Integer idIdioma;
    // private Integer idDuracion;
    private String audiencia;
}
