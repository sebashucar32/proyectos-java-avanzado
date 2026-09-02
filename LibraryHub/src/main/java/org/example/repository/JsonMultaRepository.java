package org.example.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.example.records.Multa;
import org.example.util.ComunRepository;
import org.example.util.JsonFileManager;

public class JsonMultaRepository extends ComunRepository<Multa> implements MultaRepository {
    private static final String FILE_PATH = "data/Multa.json";

    public JsonMultaRepository(JsonFileManager jsonFileManager) {
        super(jsonFileManager, FILE_PATH, new TypeReference<>() {});
    }
}
