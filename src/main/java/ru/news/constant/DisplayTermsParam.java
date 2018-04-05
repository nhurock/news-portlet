package ru.news.constant;

public enum DisplayTermsParam {
    TITLE("title"),
    TAG("tag"),
    CATEGORY("category");

    private String name;

    DisplayTermsParam(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}


