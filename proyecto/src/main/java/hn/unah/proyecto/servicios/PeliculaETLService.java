package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.PeliculaDTO;
import hn.unah.proyecto.entidades.olap.dimCategoria;
import hn.unah.proyecto.entidades.olap.dimPelicula;
import hn.unah.proyecto.entidades.oltp.FilmCategory;
import hn.unah.proyecto.repositorios.olap.DimCategoriaRepository;
import hn.unah.proyecto.repositorios.olap.DimPeliculaRepository;
import hn.unah.proyecto.repositorios.oltp.FilmCategoryRepository;
import hn.unah.proyecto.repositorios.oltp.FilmRepository;

@Service
public class PeliculaETLService {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmCategoryRepository filmCategoryRepository;

    @Autowired
    private DimPeliculaRepository dimPeliculaReposiory;

    @Autowired
    private DimCategoriaRepository dimCategoriaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private List<Map<String, Object>> extraerPeliculas(String sqlQuery) {
        List<Map<String, Object>> registros = jdbcTemplate.queryForList(sqlQuery);
        return registros;
    }

    private List<PeliculaDTO> transformarPeliculasTabla(List<Map<String, Object>> peliculasOrigen) {
        List<PeliculaDTO> peliculasDTO = new ArrayList<>();

        for (Map<String, Object> fila : peliculasOrigen) {
            PeliculaDTO dto = new PeliculaDTO();

            Integer filmId = (Integer) fila.get("film_id"); // Usa los nombres de columnas correctos
            String titulo = (String) fila.get("title");
            String clasificacion = (String) fila.get("rating");

            FilmCategory filmCategory = filmCategoryRepository.findByFilmId(filmId).orElse(null);
            if (filmCategory == null)
                continue;

            Integer idCategoria = filmCategory.getCategory().getCategoria_id();

            dto.setIdPelicula(filmId);
            dto.setTitulo(titulo);
            dto.setIdCategoria(idCategoria);
            dto.setAudiencia(clasificacion);

            peliculasDTO.add(dto);
        }

        return peliculasDTO;
    }

    private List<PeliculaDTO> transformarPeliculasConsulta(List<Map<String, Object>> datos) {
        List<PeliculaDTO> peliculasDTO = new ArrayList<>();

        for (Map<String, Object> fila : datos) {
            PeliculaDTO dto = new PeliculaDTO();

            dto.setIdPelicula((Integer) fila.get("idPelicula"));
            dto.setTitulo((String) fila.get("titulo"));
            dto.setIdCategoria((Integer) fila.get("idCategoria"));
            dto.setAudiencia((String) fila.get("audiencia"));

            peliculasDTO.add(dto);
        }

        return peliculasDTO;
    }

    private void cargarPeliculasOLAP(List<PeliculaDTO> peliculasDTO) {
        List<dimPelicula> peliculas = new ArrayList<>();

        for (PeliculaDTO dto : peliculasDTO) {
            dimCategoria categoria = dimCategoriaRepository.findById(dto.getIdCategoria()).orElse(null);
            if (categoria == null)
                continue;

            dimPelicula pelicula = new dimPelicula();
            pelicula.setIdPelicula(dto.getIdPelicula());
            pelicula.setTitulo(dto.getTitulo());
            pelicula.setCategoria(categoria);
            pelicula.setAudiencia(dto.getAudiencia());

            peliculas.add(pelicula);
        }

        dimPeliculaReposiory.saveAll(peliculas);
    }

    public void ejecutarETL(String sqlQuery, String metodo) {
        List<Map<String, Object>> peliculaOrigen = extraerPeliculas(sqlQuery);
        List<PeliculaDTO> peliculasTransformadas;

        if (metodo.equalsIgnoreCase("Table")) {
            peliculasTransformadas = transformarPeliculasTabla(peliculaOrigen);
        } else {
            peliculasTransformadas = transformarPeliculasConsulta(peliculaOrigen);
        }

        cargarPeliculasOLAP(peliculasTransformadas);
    }

}
