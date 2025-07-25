package hn.unah.proyecto.entidades.oltp;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    private Integer id;

    @Column(name = "name")
    private String nombre;

    @Column(name = "last_update")
    private Date ultimaActualizacion;
}
