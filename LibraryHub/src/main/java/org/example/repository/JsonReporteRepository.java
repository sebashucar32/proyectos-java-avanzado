package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.util.ComunRepository;
import org.example.util.JsonFileManager;

public class JsonReporteRepository extends ComunRepository<Long> implements ReporteRepository {
    private static final String FILE_PATH = "data/Reporte.json";

    public JsonReporteRepository(JsonFileManager jsonFileManager) {
        super(jsonFileManager,FILE_PATH, new TypeReference<>() {});
    }
}
