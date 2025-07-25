// package hn.unah.proyecto.servicios;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;

// import hn.unah.proyecto.dto.IdiomaDTO;
// import hn.unah.proyecto.entidades.olap.dimIdioma;
// import hn.unah.proyecto.entidades.oltp.Language;
// import hn.unah.proyecto.repositorios.olap.DimIdiomaRepository;
// import hn.unah.proyecto.repositorios.oltp.LanguageRepository;

// public class IdiomaETLService {
    
//     @Autowired
//     private DimIdiomaRepository dimIdiomaRepository;

//     @Autowired
//     private LanguageRepository languageRepository;

//     private List<Language> extraerLenguajeOLTP() {
//         return languageRepository.findAll();
//     }
    
//     public List<IdiomaDTO> transformarIdiomas(List<Language> idiomasOrigen) {
//         List<IdiomaDTO> idiomasDTO = new ArrayList<>();

//         for(Language idioma : idiomasOrigen) {
//             IdiomaDTO dto = new IdiomaDTO();
//             dto.setIdIdioma(idioma.getId());
//             dto.setNombreIdioma(idioma.getNombre());
        
//             idiomasDTO.add(dto);
//         }

//         return idiomasDTO; 
//     }

//     private void cargarIdiomasOLAP(List<IdiomaDTO> idiomasDTO) {

//         List<dimIdioma> idiomasDestino = new ArrayList<>();
        
//         for (IdiomaDTO dto : idiomasDTO) {
//             dimIdioma dimIdioma = new dimIdioma();
//             dimIdioma.setIdIdioma(dto.getIdIdioma());
//             dimIdioma.setNombreIdioma(dto.getNombreIdioma());

//             idiomasDestino.add(dimIdioma);
//         }
//         dimIdiomaRepository.saveAll(idiomasDestino);
//     }

//     public void ejecutarETL() {
//         List<Language> origen = extraerLenguajeOLTP();
//         List<IdiomaDTO> transformacion = transformarIdiomas(origen);
//         cargarIdiomasOLAP(transformacion);
//     }

//     public List<dimIdioma> getAllIdiomas() {
//         return dimIdiomaRepository.findAll();
//     }
// }

