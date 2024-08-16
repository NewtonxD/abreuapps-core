package abreuapps.core.control.utils;

import lombok.Getter;

/**
 *
 * @author cabreu
 */

@Getter
public class TipoReporte{

    enum TipoReporteEnum {
        PDF,
        CSV,
        XLSX,
        HTML,
        XML,
        DOC
    }
    
    private TipoReporteEnum obtenerTipo(String extension){
        return switch (extension) {
            case ".docx" -> TipoReporteEnum.DOC;
            case ".xml" -> TipoReporteEnum.XML;
            case ".html" -> TipoReporteEnum.HTML;
            case ".csv" -> TipoReporteEnum.CSV;
            case ".xlsx" -> TipoReporteEnum.XLSX;
            case "",".pdf" -> TipoReporteEnum.PDF;
            default -> TipoReporteEnum.PDF;
        };
    }
    
    public TipoReporte(String extension) {
        this.extension = extension;
        this.tipo = obtenerTipo(extension);
    }
    
    private final TipoReporteEnum tipo;
    private final String extension;
    
    
}
