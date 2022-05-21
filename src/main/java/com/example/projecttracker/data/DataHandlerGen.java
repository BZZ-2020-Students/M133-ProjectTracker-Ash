package com.example.projecttracker.data;

import com.example.projecttracker.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * reads and writes the data in the JSON-files
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-20
 *
 * @param <T>
 */

@Getter
@Setter
@RequiredArgsConstructor
public class DataHandlerGen<T> {
    @NonNull
    private final Class<T> tClass;

    /**
     * reads all the data from the JSON-file
     * @param propertyName the name of the property that tells us which JSON-file to read
     * @return an ArrayList of the data
     * @throws IOException when the file cannot be read/is not found
     */
    public ArrayList<T> getGenericJSON(String propertyName) throws IOException {
        String filePath = Config.getProperty(propertyName);
        byte[] jsonData = Files.readAllBytes(
                Paths.get(filePath)
        );
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonData, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass));
    }

    /**
     * reads specific data from the JSON-file
     * @param propertyName the name of the property that tells us which JSON-file to read
     * @param keyName the name of the key that we want to read
     * @param key
     * @param <Key> the type of Object that is getting read
     * @return the data that was read
     * @throws IOException when the file cannot be read/is not found
     * @throws NoSuchFieldException when the field cannot be found
     * @throws IllegalAccessException when the field cannot be accessed
     */
    public <Key>T getSingleFromJsonArray(String propertyName, String keyName, Key key) throws IOException, NoSuchFieldException, IllegalAccessException {
        ArrayList<T> arrayList = getGenericJSON(propertyName);

        for (T t : arrayList) {
            Field privateField = t.getClass().getDeclaredField(keyName);
            privateField.setAccessible(true);
            if (privateField.get(t).equals(key)) {
                return t;
            }
        }

        return null;
    }


}
