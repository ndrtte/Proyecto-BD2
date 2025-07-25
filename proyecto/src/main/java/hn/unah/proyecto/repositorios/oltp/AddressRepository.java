package hn.unah.proyecto.repositorios.oltp;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.oltp.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    
}
