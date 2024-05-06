package com.poc.todolist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoListApplication
{
	private static final Logger log = LoggerFactory.getLogger(TodoListApplication.class);
	public static void main(String[] args)
	{
		SpringApplication app = new SpringApplication(TodoListApplication.class);
		app.run(args);
		if(log.isDebugEnabled())
			log.debug("Application {} started successfully. Arguments: {}.",
					app.getMainApplicationClass().getCanonicalName(), String.join(",",args));
	}
}
