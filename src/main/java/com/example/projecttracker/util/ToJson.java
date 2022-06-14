package com.example.projecttracker.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * This class is used to remove redundant code in other classes.
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-06-07
 */
public class ToJson {

    /**
     * This method is used to add an object to a json file with a filterprovider.
     *
     * @param t              he object to add to the json file
     * @param filterProvider the filterprovider to use
     * @param <T>            the type of the object
     * @return the objectwriter with the filterprovider
     * @throws JsonProcessingException if the object cannot be written to the json file
     */
    public static <T> String toJson(T t, FilterProvider filterProvider) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return objectMapper
                .writer(filterProvider)
                .writeValueAsString(t);
    }
}
