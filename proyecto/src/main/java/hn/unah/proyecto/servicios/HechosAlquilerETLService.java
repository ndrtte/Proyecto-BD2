package hn.unah.proyecto.servicios;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import hn.unah.proyecto.dto.HechosDTO;
import hn.unah.proyecto.entidades.olap.Hechos;
import hn.unah.proyecto.entidades.olap.DimEmpleado;
import hn.unah.proyecto.entidades.olap.DimPelicula;
import hn.unah.proyecto.entidades.olap.DimRenta;
import hn.unah.proyecto.entidades.olap.DimTiempo;
import hn.unah.proyecto.entidades.olap.DimTienda;
import hn.unah.proyecto.entidades.oltp.Rental;
import hn.unah.proyecto.repositorios.olap.DimEmpleadoRepository;
import hn.unah.proyecto.repositorios.olap.DimPeliculaRepository;
import hn.unah.proyecto.repositorios.olap.DimRentaRepository;
import hn.unah.proyecto.repositorios.olap.DimTiempoRepository;
import hn.unah.proyecto.repositorios.olap.DimTiendaRepository;
import hn.unah.proyecto.repositorios.olap.HechosAlquilerRepository;
import hn.unah.proyecto.repositorios.oltp.PaymentRepository;
import hn.unah.proyecto.repositorios.oltp.RentalRepository;

public class HechosAlquilerETLService {
    
    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DimRentaRepository dimRentaRepository;

    @Autowired
    private DimTiempoRepository dimTiempoRepository;

    @Autowired
    private DimEmpleadoRepository dimEmpleadoRepository;

    @Autowired
    private DimPeliculaRepository dimPeliculaRepository;

    @Autowired
    private DimTiendaRepository dimTiendaRepository;
    
    @Autowired
    private HechosAlquilerRepository hechosRepository;

    private List<Rental> extraerRentasOLTP() {
        return rentalRepository.findAll();
    }

    private List<HechosDTO> transformarRentas(List<Rental> rentas) {

        List<HechosDTO> hechosDTO = new ArrayList<>();

        for (Rental renta : rentas) {

            if (renta.getFechaDevolucion() == null || renta.getFechaRenta() == null) continue;

            DimRenta dimRenta = dimRentaRepository.findById(renta.getId()).orElse(null);
            if (dimRenta == null) {
                dimRenta = new DimRenta();
                dimRenta.setIdRenta(renta.getId());
                dimRenta.setFechaRenta(renta.getFechaRenta());
                dimRenta.setFechaDevolucion(renta.getFechaDevolucion());

                dimRentaRepository.save(dimRenta);
            } 
            
            DimEmpleado empleado = dimEmpleadoRepository.findById(renta.getEmpleado().getId()).orElse(null);

            DimPelicula pelicula = dimPeliculaRepository.findById(renta.getInventario().getPelicula().getId()).orElse(null);

            DimTienda tienda = dimTiendaRepository.findById(renta.getInventario().getTienda().getId()).orElse(null);

            DimTiempo tiempo = dimTiempoRepository.findByFecha(renta.getFechaRenta());
            if (tiempo == null) continue;


            //metricas
            //ingresos
            Double monto = paymentRepository.findByRenta_Id(renta.getId()).getMonto();

            //clasificacion: R, G, NC-17, PG-13, PG
            String clasificacion = renta.getInventario().getPelicula().getClasificacion();

            //cantidad:
            Integer cantidad = 1;

            //tiempo renta
            Duration duracion = Duration.between(renta.getFechaRenta().toInstant(), renta.getFechaDevolucion().toInstant());

            double horas = duracion.toHours();
            //Unidad tiempo
            String unidadTiempo;

            HechosDTO dto = new HechosDTO();

            dto.setIdRenta(dimRenta.getIdRenta());
            dto.setIdEmpleado(empleado.getIdEmpleado());
            dto.setIdPelicula(pelicula.getIdPelicula());
            dto.setIdTienda(tienda.getIdTienda());
            dto.setIdTiempo(tiempo.getIdTiempo());
            dto.setMontoPago(monto); 
            dto.setAudiencia(clasificacion);  
            dto.setCantidad(cantidad);

            if (horas < 24) {
                dto.setTiempoRenta(horas);
                unidadTiempo = "horas";
            } else {
                double dias = horas / 24.0;
                dto.setTiempoRenta(dias);
                unidadTiempo = "días";
            }
            dto.setUnidadTiempo(unidadTiempo);

            hechosDTO.add(dto);
        }
        return hechosDTO;
    }

    private void cargarHechosOLAP (List<HechosDTO> hechosDTO) {

        List<Hechos> hechos = new ArrayList<>();

        for(HechosDTO dto : hechosDTO) {
            Hechos hecho = new Hechos();

            hecho.setRenta(dimRentaRepository.findById(dto.getIdRenta()).orElse(null));
            hecho.setEmpleado(dimEmpleadoRepository.findById(dto.getIdEmpleado()).orElse(null));
            hecho.setPelicula(dimPeliculaRepository.findById(dto.getIdPelicula()).orElse(null));
            hecho.setTienda(dimTiendaRepository.findById(dto.getIdTienda()).orElse(null));
            hecho.setTiempo(dimTiempoRepository.findById(dto.getIdTiempo()).orElse(null));

            hecho.setMontoPago(dto.getMontoPago());
            hecho.setAudiencia(dto.getAudiencia());
            hecho.setCantidad(dto.getCantidad());
            hecho.setTiempoRenta(dto.getTiempoRenta());
            hecho.setUnidadTiempo(dto.getUnidadTiempo());

            hechos.add(hecho);
        }

        hechosRepository.saveAll(hechos);
    }

    // public void ejecutarETL() {
    //     List<Rental> origen = extraerRentasOLTP();
    //     List<HechosDTO> transformadas = transformarRentas(origen);
    //     cargarHechosOLAP(transformadas);
    // }

    /*--- No entiendo la mayoría de esas cosas, pero tratare de encapsularlo xD ---*/
    public void ejecutarETL() {
        List<Rental> rentasOLTP = extraerRentasOLTP();
        List<Hechos> hechosOLAP = hechosRepository.findAll();

        // Mapa de rentas OLTP
        Map<Integer, Rental> mapaRentas = new HashMap<>();
        for (Rental renta : rentasOLTP) {
            if (renta.getFechaRenta() != null && renta.getFechaDevolucion() != null)
                mapaRentas.put(renta.getId(), renta);
        }

        // Mapa de hechos OLAP (clave: idRenta)
        Map<Integer, Hechos> mapaHechos = new HashMap<>();
        for (Hechos hecho : hechosOLAP) {
            mapaHechos.put(hecho.getRenta().getIdRenta(), hecho);
        }

        // Detectar eliminaciones (están en OLAP pero ya no en OLTP)
        List<Hechos> aEliminar = new ArrayList<>();
        for (Integer idRentaOLAP : mapaHechos.keySet()) {
            if (!mapaRentas.containsKey(idRentaOLAP)) {
                aEliminar.add(mapaHechos.get(idRentaOLAP));
            }
        }

        // Detectar inserciones (nuevos rentals en OLTP que no están en OLAP)
        List<Rental> aInsertar = new ArrayList<>();
        for (Integer idRentaOLTP : mapaRentas.keySet()) {
            if (!mapaHechos.containsKey(idRentaOLTP)) {
                aInsertar.add(mapaRentas.get(idRentaOLTP));
            }
        }

        // Eliminar los hechos que ya no existen en OLTP
        hechosRepository.deleteAll(aEliminar);

        // Transformar e insertar los nuevos hechos
        List<HechosDTO> transformadas = transformarRentas(aInsertar);
        cargarHechosOLAP(transformadas);
    }


    public List<Hechos> getAllHechosOLAP() {
        return hechosRepository.findAll();
    }    
}
