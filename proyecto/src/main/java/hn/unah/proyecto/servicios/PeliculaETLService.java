package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import hn.unah.proyecto.dto.PeliculaDTO;
import hn.unah.proyecto.entidades.olap.dimCategoria;
import hn.unah.proyecto.entidades.olap.dimPelicula;
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

        List<PeliculaDTO> peliculasDTO = new ArrayList<PeliculaDTO>();

        for (Film film : peliculasOrigen) {
            PeliculaDTO dtoPelicula = new PeliculaDTO();

            FilmCategory filmCategory = filmCategoryRepository.findByFilmId(film.getId()).orElse(null);
            

            Integer idCategoria = filmCategory.getCategory().getId();

            dtoPelicula.setIdPelicula(film.getId());
            dtoPelicula.setTitulo(film.getTitulo());
            dtoPelicula.setIdCategoria(idCategoria);
            dtoPelicula.setAudiencia(film.getClasificacion());

            peliculasDTO.add(dtoPelicula);
        }
        return peliculasDTO;
    }

    private void cargarPeliculasOLAP(List<PeliculaDTO> peliculasDTO) {

        List<dimPelicula> peliculas = new ArrayList<>();

        for (PeliculaDTO dto : peliculasDTO) {
            
            dimCategoria categoria = dimCategoriaRepository.findById(dto.getIdCategoria()).orElse(null);

            if(categoria == null) continue;

            dimPelicula pelicula = new dimPelicula();
            pelicula.setIdPelicula(dto.getIdPelicula());
            pelicula.setTitulo(dto.getTitulo());
            pelicula.setCategoria(categoria);
            pelicula.setAudiencia(dto.getAudiencia());

            peliculas.add(pelicula);
        }
        dimPeliculaReposiory.saveAll(peliculas);
    }

    public void ejecutarETL() {
        List<Film> origen = extraerPeliculasOLTP();
        List<PeliculaDTO> transformadas = transformarPeliculas(origen);
        cargarPeliculasOLAP(transformadas);
    }

    public List<dimPelicula> getAllPeliculasOLAP() {
        return dimPeliculaReposiory.findAll();
    }    
}


