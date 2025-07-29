package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.CategoriaDTO;
import hn.unah.proyecto.dto.PeliculaDTO;
import hn.unah.proyecto.entidades.olap.DimCategoria;
import hn.unah.proyecto.entidades.olap.DimPelicula;
import hn.unah.proyecto.entidades.oltp.Category;
import hn.unah.proyecto.entidades.oltp.Film;
import hn.unah.proyecto.repositorios.olap.DimPeliculaRepository;
import hn.unah.proyecto.repositorios.oltp.FilmRepository;

@Service
public class PeliculaETLService {

    @Autowired
    private DimPeliculaRepository dimPeliculaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FilmRepository filmRepository;

    private List<Map<String, Object>> extraerPeliculas(String sqlQuery, String metodo) {

        List<Map<String, Object>> registrosOLTP = jdbcTemplate.queryForList(sqlQuery);
        List<Map<String, Object>> registrosOLAP = jdbcTemplate.queryForList("SELECT ID_PELICULA FROM TBL_PELICULA");
        List<Map<String, Object>> registros = new ArrayList<>();
        for (Map<String, Object> filaOLTP : registrosOLTP) {
            Integer idOLTP = ((Number) filaOLTP.get("FILM_ID")).intValue();
            boolean existeEnOlap = false;

            for (Map<String, Object> filaOLAP : registrosOLAP) {
                Integer idOLAP = ((Number) filaOLAP.get("ID_PELICULA")).intValue();

                if (idOLTP.equals(idOLAP)) {
                    existeEnOlap = true;
                    break;
                }

                if (!existeEnOlap) {
                    registros.add(filaOLTP);
                }

            }
        }

        return registros;
    }

    private List<PeliculaDTO> transformarPeliculasTabla(List<Map<String, Object>> peliculasOrigen) {
        List<PeliculaDTO> peliculasDTO = new ArrayList<>();

        for (Map<String, Object> fila : peliculasOrigen) {
            PeliculaDTO dto = new PeliculaDTO();

            Integer filmId = ((Number) fila.get("FILM_ID")).intValue();
            String titulo = (String) fila.get("TITLE");
            String clasificacion = (String) fila.get("RATING");

            Film pelicula = filmRepository.findById(filmId).get();
            List<Category> categorias = pelicula.getCategorias();
            List<CategoriaDTO> categoriaDTOs = new ArrayList<>();

            for (Category categoria : categorias) {
                CategoriaDTO categoriaDTO = new CategoriaDTO();
                categoriaDTO.setId(categoria.getCategoria_id());
                categoriaDTO.setNombre(categoria.getNombre());
                categoriaDTOs.add(categoriaDTO);
            }

            dto.setIdPelicula(filmId);
            dto.setCategoria(categoriaDTOs);
            dto.setTitulo(titulo);
            dto.setAudiencia(clasificacion);

            peliculasDTO.add(dto);
        }

        return peliculasDTO;
    }

    private List<PeliculaDTO> transformarPeliculasConsulta(List<Map<String, Object>> peliculasOrigen) {
        Map<Integer, PeliculaDTO> peliculaMap = new HashMap<>();

        for (Map<String, Object> fila : peliculasOrigen) {
            Integer idPelicula = ((Number) fila.get("FILM_ID")).intValue();
            String titulo = (String) fila.get("TITLE");
            String rating = (String) fila.get("RATING");
            Integer categoriaId = ((Number) fila.get("CATEGORY_ID")).intValue();
            String nombreCategoria = (String) fila.get("CATEGORY_NAME");

            PeliculaDTO peliculaDTO = peliculaMap.get(idPelicula);

            peliculaDTO = new PeliculaDTO();
            peliculaDTO.setIdPelicula(idPelicula);
            peliculaDTO.setTitulo(titulo);
            peliculaDTO.setAudiencia(rating);
            peliculaDTO.setCategoria(new ArrayList<>());
            peliculaMap.put(idPelicula, peliculaDTO);

            CategoriaDTO categoriaDTO = new CategoriaDTO();
            categoriaDTO.setId(categoriaId);
            categoriaDTO.setNombre(nombreCategoria);

            peliculaDTO.getCategoria().add(categoriaDTO);
        }

        List<PeliculaDTO> peliculasDTO = new ArrayList<>();
        for (PeliculaDTO dto : peliculaMap.values()) {
            peliculasDTO.add(dto);
        }

        return peliculasDTO;

    }

    private void cargarPeliculasOLAP(List<PeliculaDTO> peliculasDTO) {
        List<DimPelicula> peliculas = new ArrayList<>();

        for (PeliculaDTO dto : peliculasDTO) {
            DimPelicula pelicula = new DimPelicula();
            List<DimCategoria> categories = new ArrayList<>();
            List<CategoriaDTO> categoriasDTO = dto.getCategoria();

            for (CategoriaDTO categoriaDTO : categoriasDTO) {
                DimCategoria dimCategoria = new DimCategoria();
                dimCategoria.setIdCategoria(categoriaDTO.getId());
                dimCategoria.setNombreCategoria(categoriaDTO.getNombre());
                categories.add(dimCategoria);
            }

            pelicula.setIdPelicula(dto.getIdPelicula());
            pelicula.setDimCategorias(categories);
            pelicula.setTitulo(dto.getTitulo());
            pelicula.setAudiencia(dto.getAudiencia());

            peliculas.add(pelicula);
        }

        dimPeliculaRepository.saveAll(peliculas);
    }

    public void ejecutarETL(String sqlQuery, String metodo) {
        List<Map<String, Object>> peliculaOrigen = extraerPeliculas(sqlQuery, metodo);
        List<PeliculaDTO> peliculasTransformadas;

        if (metodo.equalsIgnoreCase("Table")) {
            peliculasTransformadas = transformarPeliculasTabla(peliculaOrigen);
        } else {
            peliculasTransformadas = transformarPeliculasConsulta(peliculaOrigen);
        }

        cargarPeliculasOLAP(peliculasTransformadas);
    }
}
