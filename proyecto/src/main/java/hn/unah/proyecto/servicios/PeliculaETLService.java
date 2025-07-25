package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import hn.unah.proyecto.dto.DuracionDTO;
import hn.unah.proyecto.dto.PeliculaDTO;
import hn.unah.proyecto.entidades.oltp.Film;
import hn.unah.proyecto.entidades.oltp.FilmCategory;
import hn.unah.proyecto.repositorios.olap.DimCategoriaRepository;
import hn.unah.proyecto.repositorios.olap.DimPeliculaReposiory;
import hn.unah.proyecto.repositorios.oltp.FilmCategoryRepository;
import hn.unah.proyecto.repositorios.oltp.FilmRepository;

public class PeliculaETLService {
    
    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmCategoryRepository filmCategoryRepository;

    @Autowired
    private DimPeliculaReposiory dimPeliculaReposiory;

    @Autowired
    private DimCategoriaRepository dimCategoriaRepository;

    private List<Film> extraerPeliculasOLTP(){
        return filmRepository.findAll();
    }

    private List<PeliculaDTO> transformarPeliculas( List<Film> peliculasOrigen) {

        List<PeliculaDTO> peliculaDTO = new ArrayList();

        for (Film film : peliculasOrigen) {
            PeliculaDTO dtoPelicula = new PeliculaDTO();

            FilmCategory filmCategory = filmCategoryRepository.findByFilmId(film.getId()).orElse(null);
            

            Integer idCategoria = filmCategory.getCategory().getId();

            PeliculaDTO dto = new PeliculaDTO();
            dto.setIdPelicula(film.getId());
            dto.setTitulo(film.getTitulo());
            dto.setIdCategoria(idCategoria);
            dto.setAudiencia(film.getClasificacion());

            peliculaDTO.add(dto);
        }
        return peliculaDTO;
    }
}


//     // @Id
//     // @Column(name = "id_pelicula")
//     // private Integer idPelicula;

//     // @Column(name = "titulo")
//     // private String titulo;

//     // @ManyToOne
//     // @JoinColumn(name = "id_categoria")
//     // private dimCategoria categoria;

//     // @ManyToOne
//     // @JoinColumn(name = "id_idioma")
//     // private dimIdioma idioma;

//     //     @ManyToOne
//     // @JoinColumn(name = "id_duracion")
//     // private dimDuracion duracion;

//     // @Column(name = "audiencia")
//     // private String audiencia;

