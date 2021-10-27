package com.attendance.action;

import org.json.JSONObject;

import java.io.File;
import java.util.Scanner;

import static com.attendance.action.TaskId.*;

public class TaskManager {
    public static final String CHECK_202_JSON = "src/main/resources/check_202.json";
    public static final String PHYSICS_202_JSON = "src/main/resources/physics_202.json";
    private static TaskManager manager = null;


    private TaskManager() {
    }

    public static TaskManager getInstance() {
        if (manager == null) {
            manager = new TaskManager();
        }
        return manager;
    }

    public Task taskFromId(int id) throws Exception {
        //TODO get type and json from database by id
        // task id -> task json
        /*  {
                "table id" = String TableId
                "user id1" = int offset1
                "user id2" = int offset2
                ...
                "user id3" = int offset3
            }
         */
        String jsonPath;
        TaskTypes type;

        if (id == CHECK_202.code) {
            type = TaskTypes.ATTENDANCE;
            jsonPath = CHECK_202_JSON;
        } else if (id == PHYSICS_202.code) {
            type = TaskTypes.SET_MARK;
            jsonPath = PHYSICS_202_JSON;
        } else {
            throw new Exception("invalid id " + id);
        }

        String content = new Scanner(new File(jsonPath)).useDelimiter("\\Z").next();
        JSONObject json = new JSONObject(content);

        switch (type) {
            case ATTENDANCE:
                return new SubmitAttendance(type, json);
            case SET_MARK:
                return new FillMark(type, json); //TODO create another class
        }

        return new SubmitAttendance(type, json);
    }


}
