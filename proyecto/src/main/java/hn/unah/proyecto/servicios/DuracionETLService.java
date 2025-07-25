// package hn.unah.proyecto.servicios;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;

// import hn.unah.proyecto.dto.DuracionDTO;
// import hn.unah.proyecto.entidades.olap.dimDuracion;
// import hn.unah.proyecto.entidades.oltp.Film;
// import hn.unah.proyecto.entidades.oltp.Rental;
// import hn.unah.proyecto.repositorios.olap.DimDuracionRepository;
// import hn.unah.proyecto.repositorios.oltp.RentalRepository;

// public class DuracionETLService {

//     @Autowired
//     private RentalRepository rentalRepository;

//     @Autowired
//     private DimDuracionRepository dimDuracionRepository; 
    
//     // private List<Film> extraerPeliculasOLTP() {
//     //     return filmRepository.findAll();
//     // }

//     private List<Rental> extraerRentasOLTP() {
//         return rentalRepository.findAll();
//     }

//     public List<DuracionDTO> transformarDuracionDesdeRenta(List<Rental> rentas){
//         int corta = 0;
//         int media = 0;
//         int larga = 0;
//         int desconocida = 0;

//         List<DuracionDTO> resultado = new ArrayList<>();

//         for (Rental renta : rentas) {
            
//             Film pelicula = renta.getInventario().getPelicula();
//             Integer duracion = pelicula.getDuracion();

//             if (duracion <= 60) {
//                 corta++;
//             } else if (duracion <= 100) {
//                 media++;
//             } else if (duracion > 100){
//                 larga++;
//             }else {
//                 desconocida++;
//             }
//         }

//         // resultado.add(new DuracionDTO(1, "CORTA", "0-60 minutos", corta));
//         // resultado.add(new DuracionDTO(2, "MEDIA", "61-100 minutos", media));
//         // resultado.add(new DuracionDTO(3, "LARGA", "100+ minutos", larga));       
//         // resultado.add(new DuracionDTO(4, "DESCONOCIDA", "N/A", desconocida));
        
//         resultado.add(new DuracionDTO(1, "CORTA", "0-60 minutos"));
//         resultado.add(new DuracionDTO(2, "MEDIA", "61-100 minutos"));
//         resultado.add(new DuracionDTO(3, "LARGA", "100+ minutos"));        
//         resultado.add(new DuracionDTO(4, "DESCONOCIDA", "N/A"));
        

//         return resultado;
//     }

//     private void cargarDuracionesOLAP(List<DuracionDTO> duracionesDTO ){

//         List<dimDuracion> duraciones = new ArrayList<>();

//         for (DuracionDTO dto : duracionesDTO) {

//             dimDuracion duracion = dimDuracionRepository.findById(dto.getIdDuracion()).orElse(null);

//             duracion.setIdDuracion(dto.getIdDuracion());
//             duracion.setDuracion(dto.getDuracion());
//             duracion.setRangoMinutos(dto.getRangoMinutos());

//             duraciones.add(duracion);
//         }

//         dimDuracionRepository.saveAll(duraciones);
//     }    
    
//     public void ejecutarETL() {
//         List<Rental> rentas = extraerRentasOLTP();
//         List<DuracionDTO> duracionesTransformadas = transformarDuracionDesdeRenta(rentas);
//         cargarDuracionesOLAP(duracionesTransformadas);
//     }

//     public List<dimDuracion> getAllDimDuraciones() {
//         return dimDuracionRepository.findAll();        
//     }
// }
