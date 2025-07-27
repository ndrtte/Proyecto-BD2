package hn.unah.proyecto.entidades.olap;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_pelicula")
public class DimPelicula {
    @Id
    @Column(name = "id_pelicula")
    private Integer idPelicula;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "audiencia")
    private String audiencia;

    @ManyToMany()
    @JoinTable(
        name = "tbl_peli_cate",
        joinColumns = @JoinColumn(name = "id_pelicula"),
        inverseJoinColumns = @JoinColumn(name = "id_categoria"))
    private List<DimCategoria> dimCategorias;
}
