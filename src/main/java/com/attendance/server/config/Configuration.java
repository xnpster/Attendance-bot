package com.attendance.server.config;

import org.json.JSONObject;

public class Configuration {

    private int port;
    private String webroot;

    Configuration (JSONObject conf) {
        this.port = conf.getInt("port");
        this.webroot = conf.getString("webroot");
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getWebroot() {
        return webroot;
    }

    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }
}