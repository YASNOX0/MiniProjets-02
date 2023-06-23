package com.example.mini_projets_02.models;

public class BgColor {
    private String name;
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BgColor(String name, String code) {
        this.setName(name);
        this.setCode(code);
    }
}
