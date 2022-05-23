package com.example.projecttracker;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Config File that is used to read the properties file
 *
 * @author Alyssa Heimlicher
 * @version 1.0
 * @since 2022-05-22
 */
@ApplicationPath("/api")
public class Config extends Application {
    /**
     * A property Object that is used to read and save the properties file data
     */
    private static Properties properties;

    /**
     * Gets the value of a property
     *
     * @param property the key of the property to be read
     * @return the value of the property
     * @author Alyssa Heimlicher
     */
    public static String getProperty(String property) {
        if (Config.properties == null) {
            readProperties();
        }
        String value = Config.properties.getProperty(property);
        if (value == null) {
            return "";
        }
        return value;
    }

    /**
     * reads the properties file
     *
     * @author Alyssa Heimlicher
     */
    private static void readProperties() {
        if (properties == null) {
            properties = new Properties();
            try (InputStream input = new FileInputStream(Config.class.getClassLoader().getResource("project.properties").getFile())) {
                properties.load(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Sets the properties
     *
     * @param properties the value to set
     * @author Alyssa Heimlicher
     */
    private static void setProperties(Properties properties) {
        Config.properties = properties;
    }
}