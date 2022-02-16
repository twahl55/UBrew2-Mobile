package config;

import logging.Logger;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class gets the config properties for the program.
 *
 * Author: Tyler Wahl
 * Date: February 15, 2022
 * Course:CS-622
 * */
public class Properties {
    /**
     * gets the properies into the prop object
     */
    private  static java.util.Properties props = getProperties();

    /**
     * Gets all of the properties in the config.properties file
     */
    private static java.util.Properties getProperties() {

        java.util.Properties p = new java.util.Properties();
        System.out.println(Paths.get("."));
        Path pa = Paths.get("src/Config/config.properties");
        File f = new File(pa.toAbsolutePath().toString() );
        try {
            FileReader file = new FileReader(pa.toAbsolutePath().toString());
            p.load(file);
        }
        catch(Exception ex){
            Logger.writeToLog(ex);
        }
        return p;
    }

    /**
     * This method gets a property value from config.properties
     */
    public static String getProperty(String key){
        return props.getProperty(key);
    }

}
