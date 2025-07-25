package hn.unah.proyecto.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MigrationDataDTO {
    
    private String method;
    private String sourceTable;
    private String destinationTable;
    private List<String> listColumn;
}
