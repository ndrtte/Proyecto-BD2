package hn.unah.proyecto.dto;

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
    private Integer idCategoria;
    // private Integer idIdioma;
    // private Integer idDuracion;
    private String audiencia;

    @Override
    public Integer getId() {
        return idPelicula;
    } 

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PeliculaDTO)) return false;
        PeliculaDTO other = (PeliculaDTO) o;
        return idPelicula.equals(other.idPelicula) && 
                titulo.equals(other.titulo) &&
                idCategoria.equals(other.idCategoria) &&
                audiencia.equals(other.audiencia);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPelicula, titulo, idCategoria, audiencia);
    }    
}
