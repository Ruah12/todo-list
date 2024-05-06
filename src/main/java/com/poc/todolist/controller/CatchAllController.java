package com.poc.todolist.controller;

import com.poc.todolist.exceptions.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CatchAllController
{
    private static final Logger log = LoggerFactory.getLogger(CatchAllController.class);

    // to catch all rest requests that are not processed by app
    @RequestMapping("/**")
    public ResponseEntity<String> handleUnmappedRequests(HttpServletRequest request) {
        // Get the full URL of the incoming request
        String requestURL = getRequestURL(request);
        log.info("Unhandled url received: {}.",  requestURL);
        // Response that indicates the URL was not found
        return ResponseEntity.badRequest().body("URL not supported: " + requestURL + ". " + RootController.getRoutes());
    }

    private String getRequestURL(HttpServletRequest request)
    {
        // Build the full URL (including query strings if necessary)
        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append('?').append(request.getQueryString());
        }
        return url.toString();
    }
}