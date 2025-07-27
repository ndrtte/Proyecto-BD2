package hn.unah.proyecto.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import hn.unah.proyecto.dto.IdentificableDTO;

public class IncrementalETLHelper {

    public static <DTO extends IdentificableDTO, ENT> void sincronizar(
            List<DTO> origenDTO,
            List<ENT> destinoEntidades,
            Function<DTO, ENT> mapperDTOaEntidad,
            Function<ENT, Integer> getIdEntidad,
            Function<ENT, DTO> mapperEntidadADTO,
            Consumer<List<ENT>> insertOrUpdate,
            Consumer<List<ENT>> delete) {

        Map<Integer, DTO> mapOrigen = origenDTO.stream()
            .collect(Collectors.toMap(DTO::getId, Function.identity()));

        Map<Integer, ENT> mapDestino = destinoEntidades.stream()
            .collect(Collectors.toMap(getIdEntidad, Function.identity()));

        List<ENT> nuevos = new ArrayList<>();
        List<ENT> actualizados = new ArrayList<>();
        List<ENT> eliminados = new ArrayList<>();

        for (DTO dto : origenDTO) {
            ENT entidadExistente = mapDestino.get(dto.getId());
            if (entidadExistente == null) {
                nuevos.add(mapperDTOaEntidad.apply(dto));
            } else {
                DTO dtoExistente = mapperEntidadADTO.apply(entidadExistente);
                if (!dto.equals(dtoExistente)) {
                    actualizados.add(mapperDTOaEntidad.apply(dto));
                }
            }
        }

        for (ENT entidad : destinoEntidades) {
            if (!mapOrigen.containsKey(getIdEntidad.apply(entidad))) {
                eliminados.add(entidad);
            }
        }

        insertOrUpdate.accept(nuevos);
        insertOrUpdate.accept(actualizados);
        delete.accept(eliminados);
    }
}
