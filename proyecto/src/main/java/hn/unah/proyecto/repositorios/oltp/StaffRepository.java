package hn.unah.proyecto.repositorios.oltp;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.oltp.Staff;

public interface StaffRepository  extends JpaRepository<Staff, Integer> {    
}
