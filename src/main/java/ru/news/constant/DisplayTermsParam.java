package ru.news.constant;

public enum DisplayTermsParam {
    TITLE("title"),
    TAG("tag"),
    CATEGORY("category");

    private String param;

    DisplayTermsParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}


