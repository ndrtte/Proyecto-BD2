package hn.unah.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hn.unah.proyecto.dto.CategoriaDTO;
import hn.unah.proyecto.entidades.olap.dimCategoria;
import hn.unah.proyecto.entidades.oltp.Category;
import hn.unah.proyecto.repositorios.olap.DimCategoriaRepository;
import hn.unah.proyecto.repositorios.oltp.CategoryRepository;

@Service
public class CategoriaETLService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DimCategoriaRepository dimCategoriaRepository;

    private List<Category> extraerCategoriasOLTP() {
        return categoryRepository.findAll();
    }

    public List<CategoriaDTO> transformarCategorias(List<Category> categoriasOrigen) {

        List<CategoriaDTO> categoriasDTO = new ArrayList<>();
        
        for (Category categoria : categoriasOrigen) {
            CategoriaDTO dto = new CategoriaDTO(
                categoria.getId(),
                categoria.getNombre()
            );
            categoriasDTO.add(dto);
        }
        
        return categoriasDTO;
    }

    private void cargarCategoriasOLAP(List<CategoriaDTO> categoriasDTO) {

        List<dimCategoria> categorias = new ArrayList<>();

        for (CategoriaDTO dto : categoriasDTO) {
            dimCategoria entidad = new dimCategoria(
                dto.getId(),
                dto.getNombre()
            );
            categorias.add(entidad);
        }

        dimCategoriaRepository.saveAll(categorias);
    }    

    public void ejecutarETL() {
        List<Category> origen = extraerCategoriasOLTP();
        List<CategoriaDTO> transformadas = transformarCategorias(origen);
        cargarCategoriasOLAP(transformadas);
    }

    public List<dimCategoria> getAllDimCategorias() {
        return dimCategoriaRepository.findAll();
    }
}
