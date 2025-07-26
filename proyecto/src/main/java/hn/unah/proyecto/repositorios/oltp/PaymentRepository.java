package hn.unah.proyecto.repositorios.oltp;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.oltp.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findByRenta_Id(Integer renta);

}
