package com.bankingsystem.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for JSON operations
 */
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    
    static {
        // Configure ObjectMapper for proper LocalDateTime serialization
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    
    // Write a list of objects to a JSON file
    public static <T> void writeListToFile(List<T> list, String filePath) {
        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, list);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to JSON file: " + filePath, e);
        }
    }
    
    // Read a list of objects from a JSON file
    public static <T> List<T> readListFromFile(String filePath, TypeReference<List<T>> typeReference) {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        
        try {
            return mapper.readValue(file, typeReference);
        } catch (IOException e) {
            throw new RuntimeException("Error reading from JSON file: " + filePath, e);
        }
    }
}