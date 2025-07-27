package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.CategoriaDTO;
// import hn.unah.proyecto.dto.EmpleadoDTO;
import hn.unah.proyecto.dto.PeliculaDTO;
import hn.unah.proyecto.entidades.olap.DimCategoria;
// import hn.unah.proyecto.entidades.olap.DimEmpleado;
import hn.unah.proyecto.entidades.olap.DimPelicula;
import hn.unah.proyecto.entidades.oltp.Category;
import hn.unah.proyecto.entidades.oltp.Film;
// import hn.unah.proyecto.entidades.olap.DimTienda;
import hn.unah.proyecto.repositorios.olap.DimCategoriaRepository;
import hn.unah.proyecto.repositorios.olap.DimPeliculaRepository;
import hn.unah.proyecto.repositorios.oltp.FilmRepository;
// import hn.unah.proyecto.repositorios.oltp.FilmRepository;
import hn.unah.proyecto.util.IncrementalETLHelper;

@Service
public class PeliculaETLService {

    @Autowired
    private DimPeliculaRepository dimPeliculaRepository;

    @Autowired
    private DimCategoriaRepository dimCategoriaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FilmRepository filmRepository;

    private List<Map<String, Object>> extraerPeliculas(String sqlQuery) {
        List<Map<String, Object>> registros = jdbcTemplate.queryForList(sqlQuery);
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

    private List<PeliculaDTO> transformarPeliculasConsulta(List<Map<String, Object>> datos) {
        List<PeliculaDTO> peliculasDTO = new ArrayList<>();
        /*
         * for (Map<String, Object> fila : datos) {
         * PeliculaDTO dto = new PeliculaDTO();
         * 
         * dto.setIdPelicula(((Number) fila.get("ID_PELICULA")).intValue());
         * dto.setTitulo((String) fila.get("TITULO"));
         * dto.setIdCategoria(((Integer) fila.get("ID_CATEGORIA")).intValue());
         * dto.setAudiencia((String) fila.get("AUDIENCIA"));
         * 
         * peliculasDTO.add(dto);
         * }
         */

        return peliculasDTO;
    }

    private void cargarPeliculasOLAP(List<PeliculaDTO> peliculasDTO) {
        List<DimPelicula> peliculas = new ArrayList<>();

        for (PeliculaDTO dto : peliculasDTO) {
            if (dto.getCategoria().isEmpty())
                continue;

            CategoriaDTO catDTO = dto.getCategoria().get(0);
            DimCategoria categoria = dimCategoriaRepository.findById(catDTO.getId()).orElse(null);
            if (categoria == null)
                continue;

            DimPelicula pelicula = new DimPelicula();
            pelicula.setIdPelicula(dto.getIdPelicula());
            pelicula.setTitulo(dto.getTitulo());
            pelicula.setCategoria(categoria);
            pelicula.setAudiencia(dto.getAudiencia());

            peliculas.add(pelicula);
        }

        dimPeliculaRepository.saveAll(peliculas);
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

    /*
     * public void sincronizarETL(String sqlQuery) {
     * List<Map<String, Object>> origen = extraerPeliculas(sqlQuery);
     * List<PeliculaDTO> peliculasDTO = transformarPeliculasTabla(origen);
     * List<DimPelicula> existentes = dimPeliculaRepository.findAll();
     * 
     * IncrementalETLHelper.sincronizar(
     * peliculasDTO,
     * existentes,
     * dto -> {
     * DimCategoria categoria =
     * dimCategoriaRepository.findById(dto.getId()).orElse(null);
     * return new DimPelicula(dto.getIdPelicula(), dto.getTitulo(), categoria,
     * dto.getAudiencia());
     * },
     * DimPelicula::getIdPelicula,
     * entidad -> new PeliculaDTO(entidad.getIdPelicula(), entidad.getTitulo(),
     * entidad.getCategoria().getIdCategoria(), entidad.getAudiencia()),
     * lista -> dimPeliculaRepository.saveAll(lista),
     * lista -> dimPeliculaRepository.deleteAll(lista));
     * }
     */
}
