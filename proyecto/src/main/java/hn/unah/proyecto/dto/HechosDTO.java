package hn.unah.proyecto.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HechosDTO implements IdentificableDTO {

    private Integer idHechos;
    private Integer idRenta;
    //private Integer idCliente;
    private Integer idEmpleado;
    private Integer idPelicula;
    private Integer idTienda;
    private Integer idTiempo;
    //private Integer idPago;

    private Double montoPago;
    private String audiencia; //clasificacion
    private Integer cantidad;
    private Double tiempoRenta;
    private String unidadTiempo;

    @Override
    public Integer getId() {
        return idHechos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HechosDTO)) return false;
        HechosDTO other = (HechosDTO) o;
        return idHechos.equals(other.idHechos) && 
                idRenta.equals(other.idRenta) &&
                idEmpleado.equals(other.idEmpleado) &&
                idPelicula.equals(other.idPelicula) &&
                idTienda.equals(other.idTienda) &&  
                idTiempo.equals(other.idTiempo) && 
                montoPago.equals(other.montoPago) &&
                audiencia.equals(other.audiencia) &&
                cantidad.equals(other.cantidad) &&
                tiempoRenta.equals(other.tiempoRenta) &&
                unidadTiempo.equals(other.unidadTiempo)                                                                                                
        ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idHechos, idRenta, idEmpleado, idPelicula, idTienda, idTiempo, montoPago, audiencia, cantidad, tiempoRenta, unidadTiempo);
    }       
}