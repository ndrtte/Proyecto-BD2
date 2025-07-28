package hn.unah.proyecto.dto;

import java.util.Date;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiempoDTO implements IdentificableDTO {
    
    private Integer idTiempo;
    private Date fecha;
    private Integer diaSemana;
    private Integer mes;
    private Integer anio;
    private Integer trimestre;

    @Override
    public Integer getId() {
        return idTiempo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TiempoDTO)) return false;
        TiempoDTO other = (TiempoDTO) o;
        return idTiempo.equals(other.idTiempo) 
            && fecha.equals(other.fecha)
            && diaSemana.equals(other.diaSemana)
            && mes.equals(other.mes)
            && anio.equals(other.anio)
            && trimestre.equals(other.trimestre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTiempo, fecha, diaSemana, mes, anio, trimestre);
    }    
}

