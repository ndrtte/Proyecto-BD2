// package hn.unah.proyecto.servicios;

// import java.util.ArrayList;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;

// import hn.unah.proyecto.dto.ClienteDTO;
// import hn.unah.proyecto.entidades.olap.dimCiudad;
// import hn.unah.proyecto.entidades.olap.dimCliente;
// import hn.unah.proyecto.entidades.oltp.Address;
// import hn.unah.proyecto.entidades.oltp.City;
// import hn.unah.proyecto.entidades.oltp.Customer;
// import hn.unah.proyecto.repositorios.olap.DimCiudadRepository;
// import hn.unah.proyecto.repositorios.olap.DimClienteRepository;
// import hn.unah.proyecto.repositorios.oltp.CustomerRepository;

// public class ClienteETLService {

    
//     @Autowired
//     private CustomerRepository customerRepository;

//     @Autowired
//     private DimCiudadRepository dimCiudadRepository;

//     @Autowired
//     private DimClienteRepository dimClienteRepository;
    

//     private List<Customer> extraerClientesOLTP() {
//         return customerRepository.findAll();
//     }

//     public List<ClienteDTO> transformarClientes(List<Customer> clientesOrigen){

//         List<ClienteDTO> clientesTransformados = new ArrayList<>();

//         for (Customer cliente : clientesOrigen) {

//             // Address direccion = addresRepository.findById(cliente.getDireccion().getId()).orElse(null);
//             Address direccion = cliente.getDireccion();
//             if(direccion == null) continue;

//             // City ciudad = cityRepository.findById(direccion.getId()).orElse(null);
//             City ciudad = direccion.getCiudad();
//             if(ciudad == null) continue;
            
//             ClienteDTO dto = new ClienteDTO();
//             dto.setIdCliente(cliente.getId());
//             dto.setIdCiudad(ciudad.getId());

//             clientesTransformados.add(dto);
//         }

//         return clientesTransformados;
//     } 

//     private void cargarClientesOLAP(List<ClienteDTO> clientesDTO) {

//         List<dimCliente> clientes = new ArrayList<>();

//         for (ClienteDTO dto : clientesDTO) {
            
//             dimCiudad ciudad = dimCiudadRepository.findById(dto.getIdCiudad()).orElse(null);

//             if (ciudad == null) continue;

//             dimCliente cliente = new dimCliente();

//             cliente.setIdCliente(dto.getIdCliente());
//             cliente.setCiudad(ciudad);

//             clientes.add(cliente);
//         }

//         dimClienteRepository.saveAll(clientes);
//     }

//     public void ejecutarETL() {

//         List<Customer> origen = extraerClientesOLTP();
//         List<ClienteDTO> clientesTransformados = transformarClientes(origen);
//         cargarClientesOLAP(clientesTransformados);
//     }

//     public List<dimCiudad> getAllCiudades() {
//         return dimCiudadRepository.findAll();
//     }
// }
