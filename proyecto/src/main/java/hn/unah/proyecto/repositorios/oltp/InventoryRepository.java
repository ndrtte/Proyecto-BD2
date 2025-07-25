package hn.unah.proyecto.repositorios.oltp;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.oltp.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}
