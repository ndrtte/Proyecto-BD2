package hn.unah.proyecto.dto;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeliculaDTO implements IdentificableDTO{
    
    private Integer idPelicula;
    private String titulo;
    private List<CategoriaDTO> categoria;
    private String audiencia;

    @Override
    public Integer getId() {
        return idPelicula;
    } 
}
