package com.example.mini_projets_02.models;

public class Setting {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Setting(String name, String value) {
        this.setName(name);
        this.setValue(value);
    }
}