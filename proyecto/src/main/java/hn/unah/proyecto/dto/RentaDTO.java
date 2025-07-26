package hn.unah.proyecto.dto;

import java.util.Date;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentaDTO implements IdentificableDTO{
    
    private Integer idRenta;
    private Date fechaRenta;
    private Date fechaDevolucion;

    @Override
    public Integer getId() {
        return idRenta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RentaDTO)) return false;
        RentaDTO other = (RentaDTO) o;
        return idRenta.equals(other.idRenta) 
            && fechaRenta.equals(other.fechaRenta)
            && fechaDevolucion.equals(other.fechaDevolucion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRenta, fechaRenta, fechaDevolucion);
    }    
}

