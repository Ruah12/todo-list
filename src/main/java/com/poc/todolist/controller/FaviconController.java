package com.poc.todolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;

@Controller
public class FaviconController
{
    @GetMapping("favicon.ico")
    ResponseEntity<Resource> favicon() {
        Resource resource = new ClassPathResource("/static/favicon.ico");
        return ResponseEntity.ok().body(resource);
    }
}
