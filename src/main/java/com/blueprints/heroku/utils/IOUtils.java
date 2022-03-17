package com.blueprints.heroku.utils;

public class IOUtils {
    public static String joinPaths(String... values) {
        StringBuilder stringBuilder = new StringBuilder(values[0]);
        for (int i =1; i < values.length; i++) {
            if (values[i-1].endsWith("/") ^ values[i].startsWith("/"))
                stringBuilder.append(values[i]);
            else if ((!values[i-1].endsWith("/")) && (!values[i].startsWith("/")))
                stringBuilder.append("/".concat(values[i]));
            else stringBuilder.append(values[i].substring(1));
        }
        return stringBuilder.toString();
    }
}
