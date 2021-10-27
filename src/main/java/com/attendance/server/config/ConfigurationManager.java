package com.attendance.server.config;

import org.json.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {
    private static ConfigurationManager myConfigurationManager;
    private static Configuration myCurrentConfiguration;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        if (myConfigurationManager==null)
            myConfigurationManager = new ConfigurationManager();
        return myConfigurationManager;
    }

    /**
     *  Used to load a configuration file by the path provided
     */
    public void loadConfigurationFile(String filePath)  {
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new ConfigurationException(e);
        }

        StringBuffer sb = new StringBuffer();
        int i;

        try {
            while ( ( i = fileReader.read()) != -1) {
                sb.append((char)i);
            }
        } catch (IOException e) {
            throw new ConfigurationException(e);
        }


        JSONObject conf = new JSONObject(sb.toString());
        myCurrentConfiguration = new Configuration(conf);
    }

    /**
     * Returns the Current loaded Configuration
     */
    public Configuration getCurrentConfiguration() {
        if ( myCurrentConfiguration == null) {
            throw new ConfigurationException("No Current Configuration Set.");
        }
        return myCurrentConfiguration;
    }
}
