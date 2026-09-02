package org.example.util;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public abstract class ComunRepository<T> {
    protected final JsonFileManager jsonFileManager;
    private final String filePath;
    private final TypeReference<List<T>> typeReference;

    protected ComunRepository(JsonFileManager jsonFileManager, String filePath, TypeReference<List<T>> typeReference) {
        this.jsonFileManager = jsonFileManager;
        this.filePath = filePath;
        this.typeReference = typeReference;
    }

    public List<T> listar() {
        try {
            return jsonFileManager.readList(filePath, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("No se puede leer el archivo: " + filePath, e);
        }
    }

    public void registrar(T entidad) {
        try {
            List<T> entidades = listar();
            entidades.add(entidad);
            jsonFileManager.write(filePath, entidades);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo registrar la entidad", e);
        }
    }

    public void eliminar(T entidad, Function<T, Integer> idExtractor) {
        try {
            List<T> entidades = listar();

            List<T> entidadesFiltradas = entidades.stream()
                .filter(e -> !Objects.equals(
                    idExtractor.apply(e),
                    idExtractor.apply(entidad)
                )).toList();

            jsonFileManager.write(filePath, entidadesFiltradas);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo eliminar la entidad", e);
        }
    }
}
