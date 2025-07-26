package hn.unah.proyecto.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDTO implements IdentificableDTO {
    
    private Integer idEmpleado;
    private String nombre;
    private Integer idTienda;

    @Override
    public Integer getId() {
        return idEmpleado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmpleadoDTO)) return false;
        EmpleadoDTO other = (EmpleadoDTO) o;
        return idEmpleado.equals(other.idEmpleado) && nombre.equals(other.nombre) && idTienda.equals(other.idTienda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmpleado, nombre, idTienda);
    }   
}