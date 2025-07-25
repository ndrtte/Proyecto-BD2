package hn.unah.proyecto.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentaDTO {
    
    private Integer idRenta;
    private Date fechaRenta;
    private Date fechaDevolucion;
}

