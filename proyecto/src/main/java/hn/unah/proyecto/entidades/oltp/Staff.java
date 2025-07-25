package hn.unah.proyecto.entidades.oltp;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Staff")
public class Staff {
    @Id
    @Column(name = "staff_id")
    private Integer id;

    @Column(name = "first_name")
    private String nombre;

    @Column(name = "last_name")
    private String apellido;

    @Column(name = "email")
    private String correo;
    
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store tienda;

    // @Column(name = "active")
    // private Integer activo;

    // @Column(name = "username")
    // private String nombreUsuario;

    // @Column(name = "password")
    // private String contrasenia;

    @Column(name = "last_update")
    private Date ultimaActualizacion;
}
