package com.poc.todolist.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController
{
    static private Map<String, String> routes = new HashMap<>();
    static
    {
        routes.put("GET /todos", "Retrieve all todos");
        routes.put("GET /todos/{id}", "Retrieve a todo by ID");
        routes.put("POST /todos", "Create a new todo");
        routes.put("PATCH /todos/{id}", "Update an existing todo");
        routes.put("DELETE /todos   /{id}", "Delete a todo");
    }
    public static Map<String, String> getRoutes(){ return routes; }
    /**
     * Display all possible commands
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> getAllApiOptions()
    {
        return ResponseEntity.ok(routes);
    }
}