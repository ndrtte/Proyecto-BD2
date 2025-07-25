package hn.unah.proyecto.entidades.oltp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(FilmActorId.class)
@Table(name = "Film_Actor")
public class FilmActor {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;

    @Id
    @ManyToOne
    @JoinColumn(name = "film_id", referencedColumnName = "film_id")
    private Film pelicula;

    @Column(name = "last_update")
    private Date ultimaActualizacion;
}


