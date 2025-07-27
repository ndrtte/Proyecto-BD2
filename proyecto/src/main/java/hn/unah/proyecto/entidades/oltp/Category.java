package hn.unah.proyecto.entidades.oltp;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Category")
public class Category {
    
    @Id
    @Column(name = "category_id")
    private Integer categoria_id;

    @Column(name = "name")
    private String nombre;

    @Column(name = "last_update")
    private Date ultimaActualizacion;

    @ManyToMany(mappedBy = "categorias")
    private List <Film> peliculas;
}
