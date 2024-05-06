package com.poc.todolist;

import com.poc.todolist.controller.TodoListRestController;
import com.poc.todolist.db.dao.Todo;
import com.poc.todolist.exceptions.ApiResponse;
import com.poc.todolist.service.TodoListService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class TodoControllerTests
{
    @Mock
    private TodoListService todoService;

    @InjectMocks
    private TodoListRestController todoController;

    public TodoControllerTests() {
        openMocks(this);
    }

    @Test
    public void testGetTodoById_found() {
        Todo todo = new Todo();
        todo.setId(1L);
        todo.setDescription("Sample Todo");
        todo.setIsCompleted(false);

        when(todoService.findTodoById(1L)).thenReturn(todo);

        ResponseEntity<ApiResponse<Todo>> response = todoController.getTodoById(1L);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        assertEquals(todo, response.getBody().getData());
    }

    @Test
    public void testGetTodoById_notFound() {
        when(todoService.findTodoById(1L)).thenReturn(null);
        ResponseEntity<ApiResponse<Todo>> response = todoController.getTodoById(1L);
        assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }
}