package hn.unah.proyecto.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdiomaDTO implements IdentificableDTO {
    
    private Integer idIdioma;
    private String nombreIdioma;

    @Override
    public Integer getId() {
        return idIdioma;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdiomaDTO)) return false;
        IdiomaDTO other = (IdiomaDTO) o;
        return idIdioma.equals(other.idIdioma) && nombreIdioma.equals(other.nombreIdioma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idIdioma, nombreIdioma);
    }
}

