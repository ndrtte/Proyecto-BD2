package hn.unah.proyecto.repositorios.oltp;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.oltp.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Payment findByRental_Id(Integer rentalId);
}
