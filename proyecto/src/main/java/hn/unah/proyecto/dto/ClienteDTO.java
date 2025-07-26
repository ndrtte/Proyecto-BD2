package hn.unah.proyecto.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO implements IdentificableDTO{
    
    private Integer idCliente;
    private Integer idCiudad;     

    @Override
    public Integer getId() {
        return idCliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClienteDTO)) return false;
        ClienteDTO other = (ClienteDTO) o;
        return idCliente.equals(other.idCliente) && idCiudad.equals(other.idCiudad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente, idCiudad);
    }   
}

