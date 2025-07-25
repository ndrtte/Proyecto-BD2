package hn.unah.proyecto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hechos {

    private Integer id_hechos;
    private Integer idRenta;
    private Integer idCliente;
    private Integer idEmpleado;
    private Integer idPelicula;
    private Integer idTienda;
    private Integer idTiempo;
    private Integer idPago;

    private Double montoPago;
}
