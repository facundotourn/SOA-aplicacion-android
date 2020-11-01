package com.example.soa2020ea3.model;

public class EventResponse {
    private Boolean success;
    private String env;
    private Event event;

    public EventResponse(Boolean success, String env, Event event) {
        this.success = success;
        this.env = env;
        this.event = event;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    class Event {
        private Integer id;
        private String dni;
        private String type_events;
        private String description;

        public Event(Integer id, String dni, String type_events, String description) {
            this.id = id;
            this.dni = dni;
            this.type_events = type_events;
            this.description = description;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDni() {
            return dni;
        }

        public void setDni(String dni) {
            this.dni = dni;
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
}
