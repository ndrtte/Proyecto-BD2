package hn.unah.proyecto.repositorios.oltp;

import org.springframework.data.jpa.repository.JpaRepository;

import hn.unah.proyecto.entidades.oltp.Language;

public interface LanguageRepository  extends JpaRepository<Language, Integer>{
    
}
