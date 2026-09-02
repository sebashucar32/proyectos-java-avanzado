package org.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JsonFileManager {
    private final ObjectMapper objectMapper;

    public JsonFileManager() {
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Hace que el JSOn sea mas legible
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Lee una lista de objetos desde un archivo JSON.
     *
     * Si el archivo no existe, devuelve una lista vacía.
     */
    public <T> List<T> readList(String filePath, TypeReference<List<T>> typeReference) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            return Collections.emptyList();
        }

        return objectMapper.readValue(file, typeReference);
    }

    /**
     * Escribe una lista completa de objetos en un archivo JSON.
     *
     * El contenido anterior del archivo será reemplazado
     * por la lista recibida.
     */
    public <T> void write(String filePath, List<T> data) throws IOException {

        File file = new File(filePath);

        // Crea las carpetas necesarias si no existen
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        objectMapper.writeValue(file, data);
    }
}
