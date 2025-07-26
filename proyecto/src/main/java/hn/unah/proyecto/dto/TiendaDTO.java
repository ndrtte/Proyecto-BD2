package hn.unah.proyecto.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiendaDTO implements IdentificableDTO {
    
    private Integer idTienda;
    private String nombreTienda;
    private Integer idCiudad;

    @Override
    public Integer getId() {
        return idTienda;
    }  
  
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TiendaDTO)) return false;
        TiendaDTO other = (TiendaDTO) o;
        return idTienda.equals(other.idTienda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTienda);
    }    
}

