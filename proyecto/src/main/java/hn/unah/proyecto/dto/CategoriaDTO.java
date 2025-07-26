package hn.unah.proyecto.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO implements IdentificableDTO{
    
    private Integer id;
    private String nombre;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoriaDTO)) return false;
        CategoriaDTO other = (CategoriaDTO) o;
        return id.equals(other.id) && nombre.equals(other.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre);
    }    
}
