package utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {
    public static String getProperty(String property) throws Exception{
        InputStream inputStream;
        String propertiesFile = "config.properties";

        Properties properties = new Properties();
        inputStream = ApplicationProperties.class.getClassLoader().getResourceAsStream(propertiesFile);

        if(inputStream != null){
            properties.load(inputStream);
        }else {
            throw new FileNotFoundException("Properties file " + propertiesFile + " not found");
        }

        return properties.getProperty(property);
    }
}
