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

@Getter
@Setter
@RequiredArgsConstructor
public class DataHandlerGen<T> {
    @NonNull
    private final Class<T> tClass;

    public ArrayList<T> getGenericJSON(String propertyName) throws IOException {
        String filePath = Config.getProperty(propertyName);
        byte[] jsonData = Files.readAllBytes(
                Paths.get(filePath)
        );
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonData, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass));
    }

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
