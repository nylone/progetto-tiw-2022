package com.rampeo.tiw.progetto2022.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.function.Function;

public class HTTPParameterChecker {
    public static boolean checkExistsUnique(HttpServletRequest request, String param) {
        return checkExists(request, param) && checkUnique(request, param);
    }

    public static boolean checkExists(HttpServletRequest request,  String param) {
        return request.getParameterMap().containsKey(param);
    }

    public static boolean checkUnique(HttpServletRequest request, String param) {
        return request.getParameterMap().get(param).length == 1;
    }

    public static boolean checkType(HttpServletRequest request, String param, Function<String, ?> parser) {
        for (String value : request.getParameterMap().get(param)) {
            try {
                parser.apply(value);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
}
