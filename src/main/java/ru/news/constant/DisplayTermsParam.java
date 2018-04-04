package ru.news.constant;

public enum DisplayTermsParam {
    TITLE(NewsPortletConstant.DISPLAY_TERMS_PARAM_TITLE),
    TAG(NewsPortletConstant.DISPLAY_TERMS_PARAM_TAG),
    CATEGORY(NewsPortletConstant.DISPLAY_TERMS_PARAM_CATEGORY);

    private String param;

    DisplayTermsParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
}


