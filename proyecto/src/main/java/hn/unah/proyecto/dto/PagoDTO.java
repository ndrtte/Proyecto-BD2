package hn.unah.proyecto.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO implements IdentificableDTO {

    private Integer idPago;

    @Override
    public Integer getId() {
        return idPago;
    }   

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PagoDTO)) return false;
        PagoDTO other = (PagoDTO) o;
        return idPago.equals(other.idPago);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPago);
    }
}
