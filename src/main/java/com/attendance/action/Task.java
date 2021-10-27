package com.attendance.action;

import com.attendance.action.utils.Statement;
import com.attendance.action.utils.Time;
import com.attendance.googleApi.GoogleApi;
import org.json.JSONObject;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.format.DateTimeFormatter;

public abstract class Task {
    TaskTypes taskType;
    JSONObject taskJson;

    Task(TaskTypes type, JSONObject json) {
        this.taskType = type;
        this.taskJson = json;
    }

    public abstract void perform() throws GeneralSecurityException, IOException;

    public abstract void initialize(JSONObject json) throws Exception;
}

abstract class TableTask extends Task {
    Integer studentId;
    String tableId;

    TableTask(TaskTypes type, JSONObject json) {
        super(type, json);
    }
}

class SubmitAttendance extends TableTask {
    int row;
    int dayOffset;
    Time time;
    private static final String OK_STATEMENT = "OK";
    private static final String FAILED_STATEMENT_PREFIX = "Late ";

    SubmitAttendance(TaskTypes type, JSONObject json) {
        super(type, json);
    }


    @Override
    public void initialize(JSONObject json) {
        tableId = taskJson.getString("table_id");
        studentId = json.getInt("id");

        row = taskJson.getInt(studentId.toString());
        dayOffset = taskJson.getInt("offset");
        time = new Time(taskJson.getJSONObject("schedule"));
    }

    @Override
    public void perform() throws GeneralSecurityException, IOException {
        System.out.println("Submit today at row " + row + " in table " + tableId + " student " + studentId);
        System.out.println("Day: " + time.start + " after " + time.after + " before " + time.before);

        String msg;
        if (time.isNow()) {
            msg = OK_STATEMENT;
        } else {
            msg = FAILED_STATEMENT_PREFIX + java.time.LocalTime.now().format(Time.shortFormat);
        }

        Statement stmt = new Statement(msg) {
            public boolean isStrong() {
                return this.statement.equals(OK_STATEMENT);
            }
        };

        int col = java.time.LocalDate.now().getDayOfYear() - dayOffset + 2;
        GoogleApi.set(tableId, row, col, stmt);
    }
}

class FillMark extends TableTask {
    int row;
    int dayOffset;
    String mark;
    Time time;


    FillMark(TaskTypes type, JSONObject json) {
        super(type, json);
    }


    @Override
    public void initialize(JSONObject json) {
        tableId = taskJson.getString("table_id");
        studentId = json.getInt("id");
        mark = json.getString("mark");

        row = taskJson.getInt(studentId.toString());
        dayOffset = taskJson.getInt("offset");
        time = new Time(taskJson.getJSONObject("schedule"));
    }

    @Override
    public void perform() throws IOException {
        System.out.println("Fill mark today at row " + row + " in table " + tableId + " student " + studentId);
        System.out.println("Day: " + time.start + " after " + time.after + " before " + time.before);

        String msg;
        if (time.isNow()) {
            msg = mark;

            Statement stmt = new Statement(msg) {
                public boolean isStrong() {
                    return true;
                }
            };

            int col = java.time.LocalDate.now().getDayOfYear() - dayOffset + 2;
            GoogleApi.set(tableId, row, col, stmt);
        }
    }
}




