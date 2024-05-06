package com.poc.todolist.controller;

import com.poc.todolist.db.dao.Todo;
import com.poc.todolist.exceptions.ApiResponse;
import com.poc.todolist.service.TodoListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.poc.todolist.exceptions.StackTraceUtil;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoListRestController
{
    private static final Logger log = LoggerFactory.getLogger(TodoListRestController.class);

    private final TodoListService todoService;

    public TodoListRestController(TodoListService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Todo>>> getAllTodos() {
        try {
            List<Todo> todos = todoService.findAllTodos();
            if (null != todos)
                log.debug("Found {} todo records", todos.size());
            return ResponseEntity.ok(ApiResponse.ok(todos));
        } catch(Exception ex) {
            log.error("GET Request returned error: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("GET Request returned error: " +
                    StackTraceUtil.getRootCause(ex)));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Todo>> getTodoById(@PathVariable Long id)
    {
        try
        {
            Todo todo = todoService.findTodoById(id);
            if (todo != null) {
                log.debug("Found todo record for id={}.", id);
                return ResponseEntity.ok(ApiResponse.ok(todo));
            } else {
                log.debug("Not found todo record for id={}.", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch(Exception ex) {
            log.error("GET one record Request returned error: {}", StackTraceUtil.getRootCause(ex));
            return ResponseEntity.badRequest().body(ApiResponse.error("GET one record Request returned error: " +
                    StackTraceUtil.getRootCause(ex)));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Todo>> createTodo(@RequestBody Todo todo) {
        try
        {
            Todo insertedTodo = todoService.addTodoReturnTodo(todo);
            log.debug("Added todo record for  id={}.", todo.getId());
            return ResponseEntity.ok(ApiResponse.ok(insertedTodo));
        } catch(Exception ex) {
            log.error("Not added todo record for  id={}. {}", todo.getId(), StackTraceUtil.getRootCause(ex));
            return ResponseEntity.badRequest().body(ApiResponse.error("POST(Insert) one record Request returned error: " +
                    StackTraceUtil.getRootCause(ex)));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Todo>> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        try
        {
            Todo updatedTodo = todoService.updateTodo(id, todo);
            if (updatedTodo != null) {
                log.debug("Updated todo record for  id={}.", id);
                return ResponseEntity.ok(ApiResponse.ok(updatedTodo));
            } else {
                log.warn("Not updated todo record for  id={}.", id);
                return ResponseEntity.notFound().build();
            }
        } catch(Exception ex) {
            log.error("Not added todo record for  id={}. {}", todo.getId(), StackTraceUtil.getRootCause(ex));
            return ResponseEntity.badRequest().body(ApiResponse.error("PATCH(update) one record Request returned error: " +
                    StackTraceUtil.getRootCause(ex)));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodoById(@PathVariable Long id) {
        try
        {
            boolean isDeleted = todoService.deleteTodoById(id);
            if (isDeleted) {
                log.debug("Deleted todo record for  id={}.", id);
                return ResponseEntity.ok().build();
            } else {
                log.warn("Not deleted todo record for  id={}.", id);
                return ResponseEntity.notFound().build();
            }
        } catch(Exception ex) {
            log.error("Can not delete todo record for id={}. Reason: {}", id, StackTraceUtil.getRootCause(ex));
            return ResponseEntity.badRequest().body(ApiResponse.error("DELETE one record Request returned error: " +
                    StackTraceUtil.getRootCause(ex)));
        }
    }

    // to get error by url: http://localhost:8081/todos/error
    @GetMapping("/error")
    public ResponseEntity<ApiResponse<List<Todo>>> getAllTodosError() {
        try {
            List<Todo> todos = todoService.findAllTodosError();
            if (null != todos)
                log.debug("Found {} todo records", todos.size());
            return ResponseEntity.ok(ApiResponse.ok(todos));
        } catch(Exception ex) {
            log.error("GET Request returned error: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(ApiResponse.error("GET Request returned error: " +
                    StackTraceUtil.getRootCause(ex)));
        }
    }
}
