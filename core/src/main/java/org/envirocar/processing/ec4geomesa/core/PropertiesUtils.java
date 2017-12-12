package org.envirocar.processing.ec4geomesa.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author dewall
 */
public class PropertiesUtils {
    
    private PropertiesUtils() {
    }

    public static Properties getProperties(String propertiesFile) throws IOException {
        Properties result = new Properties();
        InputStream inputStream = GeoMesaDataStoreModule.class.getResourceAsStream(propertiesFile);

        if (inputStream != null) {
            try {
                result.load(inputStream);
            } finally {
                inputStream.close();
            }
        }

        return result;
    }

}
