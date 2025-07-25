package hn.unah.proyecto.repositorios.oltp;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.oltp.Actor;

public interface ActorRepository extends JpaRepository<Actor, Integer> {
    
}
