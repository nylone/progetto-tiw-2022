package com.rampeo.tiw.progetto2022.Utils;

public class PathBuilder {
    private final StringBuilder pathBuilder;
    private boolean noParams;

    public PathBuilder(String location) {
        pathBuilder = new StringBuilder();
        pathBuilder.append(location.equals("") ? "/" : location);
        noParams = true;
    }

    public PathBuilder addParam(String key, Object value) {
        if (noParams) {
            pathBuilder.append("?");
            noParams = false;
        } else {
            pathBuilder.append("&");
        }
        pathBuilder.append(key).append("=").append(value);
        return this;
    }

    @Override
    public String toString() {
        return pathBuilder.toString();
    }
}
