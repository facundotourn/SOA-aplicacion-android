package com.example.soa2020ea3.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EventRequestBody {
    private String env;
    private String type_events;
    private String description;

    public EventRequestBody(String env, String type_events, String description) {
        this.env = env;
        this.type_events = type_events;
        this.description = new SimpleDateFormat("dd/MM/yyyy hh:mm").format(Calendar.getInstance().getTime()) + ": " + description;
    }

    public EventRequestBody(String type_events, String description) {
        this.env = "PROD";
        this.type_events = type_events;
        this.description = new SimpleDateFormat("dd/MM/yyyy hh:mm").format(Calendar.getInstance().getTime()) + ": " + description;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getType_events() {
        return type_events;
    }

    public void setType_events(String type_events) {
        this.type_events = type_events;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
