package info.gratour.common.rest;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletHelper {

    public static final String HEADER_X_AUTH_TOKEN = "X-Auth-Token";

    public static String getAuthToken(HttpServletRequest request) {
        if (request == null)
            return null;

        return request.getHeader(HEADER_X_AUTH_TOKEN);
    }

    public static void addCorsHeaders(HttpServletRequest request, HttpServletResponse response) {
//        String origin = request.getHeader(HttpHeaders.ORIGIN);
//        if (origin != null) {
//            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
//        } else {
            if (!response.containsHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN))
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
//        }

        String requestHeaders = request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        if (requestHeaders != null)
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders);
        else {
            if (!response.containsHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS))
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        }

        String requestMethod = request.getHeader(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD);
        if (requestMethod != null)
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod);
        else {
            if (!response.containsHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS))
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,DELETE,PUT,OPTIONS");
        }
    }
}
