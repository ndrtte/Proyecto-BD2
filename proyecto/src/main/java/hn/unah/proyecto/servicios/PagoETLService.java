// package hn.unah.proyecto.servicios;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;

// import hn.unah.proyecto.dto.PagoDTO;
// import hn.unah.proyecto.entidades.olap.dimPago;
// import hn.unah.proyecto.entidades.oltp.Payment;
// import hn.unah.proyecto.repositorios.olap.DimPagoRepository;
// import hn.unah.proyecto.repositorios.oltp.PaymentRepository;

// public class PagoETLService {
    
//     @Autowired
//     private DimPagoRepository dimPagoRepository;

//     @Autowired
//     private PaymentRepository paymentRepository;

//     private List<Payment> extraerPagoOLTP() {
//         return paymentRepository.findAll();
//     }

//     public List<PagoDTO> transformarPagos(List<Payment> pagosOrigen) {
//         List<PagoDTO> pagosDTO = new ArrayList<>();

//         for(Payment pago : pagosOrigen) {
//             PagoDTO dto = new PagoDTO();
//             dto.setIdPago(pago.getId());

//             pagosDTO.add(dto);
//         }

//         return pagosDTO;
//     }

//     private void cargarPagosOLAP(List<PagoDTO> pagosDTO) {

//         List<dimPago> pagosDestino = new ArrayList<>();

//         for(PagoDTO dto : pagosDTO) {
//             dimPago dimPago = new dimPago();
//             dimPago.setIdPago(dto.getIdPago());

//             pagosDestino.add(dimPago);
//         }
//         dimPagoRepository.saveAll(pagosDestino);
//     }

//     public void ejecutarETL() {
//         List<Payment> origen = extraerPagoOLTP();
//         List<PagoDTO> transformacion = transformarPagos(origen);
//         cargarPagosOLAP(transformacion);        
//     }

//     public List<dimPago> getAllPagos(){
//         return dimPagoRepository.findAll();
//     }
// }