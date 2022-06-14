package com.example.projecttracker.data;

import com.example.projecttracker.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * reads and writes the data in the JSON-files
 *
 * @param <T> the type of the data
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-20
 */
@Getter
@Setter
@RequiredArgsConstructor
public class DataHandlerGen<T> {
    /**
     * The class of the data
     */
    @NonNull
    private final Class<T> tClass;

    /**
     * reads all the data from the JSON-file
     *
     * @param propertyName the name of the property that tells us which JSON-file to read
     * @return an ArrayList of the data
     * @throws IOException when the file cannot be read/is not found
     * @author Alyssa Heimlicher
     * @since 1.0
     */
    public ArrayList<T> getArrayListOutOfJSON(String propertyName) throws IOException {
        String filePath = Config.getProperty(propertyName);
        byte[] jsonData = Files.readAllBytes(
                Paths.get(filePath)
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper.readValue(jsonData, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass));
    }

    /**
     * reads specific data from the JSON-file
     *
     * @param propertyName the name of the property that tells us which JSON-file to read
     * @param fieldName    the name of the field of the class that we want to read
     * @param fieldValue   the value of the field of the class that we want to read
     * @return the data that was read
     * @throws IOException            when the file cannot be read/is not found
     * @throws NoSuchFieldException   when the field cannot be found
     * @throws IllegalAccessException when the field cannot be accessed
     * @author Alyssa Heimlicher
     * @since 1.0
     */
    public T getSingleFromJsonArray(String propertyName, String fieldName, Object fieldValue) throws IOException, NoSuchFieldException, IllegalAccessException {
        ArrayList<T> arrayList = getArrayListOutOfJSON(propertyName);


        for (T t : arrayList) {
            Field privateField = t.getClass().getDeclaredField(fieldName);
            privateField.setAccessible(true);
            if (privateField.get(t).equals(fieldValue)) {
                return t;
            }
        }

        return null;
    }

    /**
     * Adds a new data to the JSON-file
     *
     * @param object       the data that we want to add
     * @param propertyName the name of the property that tells us which JSON-file to write in
     * @author Alyssa Heimlicher
     */
    public void insertIntoJson(T object, String propertyName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String path = Config.getProperty(propertyName);
            ArrayList<T> objects = getArrayListOutOfJSON(propertyName);
            objects.add(object);
            saveJson(propertyName, objects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a specific data from the JSON-file
     *
     * @param propertyName the name of the property that tells us which JSON-file to write in
     * @param fieldName    the name of the field of the class that we want to delete
     * @param fieldValue   the value of the data in the field
     * @throws IOException            when the file cannot be read/is not found
     * @throws NoSuchFieldException   when the field cannot be found
     * @throws IllegalAccessException when the field cannot be accessed
     * @author Alyssa Heimlicher
     */
    public void deleteSingleFromJson(String propertyName, String fieldName, Object fieldValue) throws IOException, NoSuchFieldException, IllegalAccessException {
        ArrayList<T> objects = getArrayListOutOfJSON(propertyName);
        T object = getSingleFromJsonArray(propertyName, fieldName, fieldValue);
        if (!objects.remove(object)) {
            throw new IllegalArgumentException("Object not found");
        }
        saveJson(propertyName, objects);
    }

    /**
     * Saves the data in the JSON-file
     *
     * @param propertyName the name of the property that tells us which JSON-file to write in
     * @param objects      the data that we want to save
     * @author Alyssa Heimlicher
     */
    public void saveJson(String propertyName, List<T> objects) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String path = Config.getProperty(propertyName);
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.writeValue(Paths.get(path).toFile(), objects);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
