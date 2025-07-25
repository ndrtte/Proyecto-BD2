package hn.unah.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDTO {
    
    private Integer idEmpleado;
    private String nombre;
    private Integer idTienda;
}