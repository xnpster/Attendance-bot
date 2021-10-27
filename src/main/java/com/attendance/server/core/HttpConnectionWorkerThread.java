package com.attendance.server.core;

import com.attendance.action.Task;
import com.attendance.action.TaskManager;
import com.attendance.server.config.ConfigurationException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HttpConnectionWorkerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            StringBuffer sb = new StringBuffer();
            int i;

            try {
                while ( ( i = inputStream.read()) != -1) {
                    sb.append((char)i);
                }
            } catch (IOException e) {
                throw new ConfigurationException(e);
            }

            String content = sb.toString();
            LOGGER.info("scanned json: " + content);

//            content = new Scanner(new File("src/main/resources/test_request.json")).useDelimiter("\\Z").next();
            LOGGER.info("handled json: " + content);

            JSONObject request = new JSONObject(content);
            JSONObject answer = new JSONObject();

            try {
                Task task = TaskManager.getInstance().taskFromId(request.getInt("method_id"));
                task.initialize(request);
                task.perform();
                answer.append("status", "OK");
            } catch (Exception e) {
                //TODO make exact exception
                e.printStackTrace();
                answer.append("status", "ERROR ON HANDLING TASK");
            }

            outputStream.write(answer.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        }  finally {
            if (inputStream!= null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket!= null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
