package com.poc.todolist.db.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a todos table record
 */
@Getter
@Setter
@ToString
public class Todo {
    private Long id;
    private String description;
    private Boolean isCompleted;
}
