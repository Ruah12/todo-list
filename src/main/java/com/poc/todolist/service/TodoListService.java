package com.poc.todolist.service;

import com.poc.todolist.db.dao.Todo;
import com.poc.todolist.exceptions.StackTraceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Service
public class TodoListService
{
    private static final Logger log = LoggerFactory.getLogger(TodoListService.class);
    private final JdbcTemplate jdbcTemplate;

    public TodoListService(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Defines how each row in the ResultSet is mapped to a Todo object.
     * This implements consistency in how Todos are constructed throughout the service
     */
    private final RowMapper<Todo> rowMapper = (rs, rowNum) ->
    {
        Todo todo = new Todo();
        todo.setId(rs.getLong("id"));
        todo.setDescription(rs.getString("description"));
        todo.setIsCompleted(rs.getBoolean("is_completed"));
        return todo;
    };

    /**
     * Returns all records from todos table
     * @return list of records
     */
    public List<Todo> findAllTodos()
    {
        try {
            return jdbcTemplate.query("SELECT * FROM todos", rowMapper);
        } catch (DataAccessException e) {
            log.error("Error fetching all todos: {}", StackTraceUtil.getRootCause(e));
            throw new RuntimeException("Error fetching all todos", e);
        }
    }

    /**
     * Search for one todo record according to id
     * @param id - id to search
     * @return todo record
     */
    public Todo findTodoById(Long id) {
        try {
            List<Todo> todos = jdbcTemplate.query(
                    "SELECT * FROM todos WHERE id = ?", rowMapper, id);
            if (todos.isEmpty()) {
                log.info("No Todo found with id: {}", id);
                return null;
            } else {
                if(todos.size() > 1)
                    log.error("Expected only one record for id={}, but found={}.", id, todos.size());
                return todos.get(0); // Return the first Todo record found (should be the only one)
            }
        } catch (DataAccessException e) {
            log.error("Error finding todo by ID: {}. {}", id, StackTraceUtil.getRootCause(e));
            throw new RuntimeException("Error finding todo by ID", e);
        }
    }

    /**
     * Add one record to todos table
     * @param todo - record to add
     * @return - true if record was added successfully
     */
    public boolean addTodo(Todo todo)
    {
        try {
            int rowsAdded = jdbcTemplate.update("INSERT INTO todos (description, is_completed) VALUES (?, ?)",
                    todo.getDescription(), todo.getIsCompleted());
            return rowsAdded > 0;
        } catch (DataAccessException e) {
            log.error("Error adding todo: {}", StackTraceUtil.getRootCause(e));
            throw new RuntimeException("Error adding todo", e);
        }
    }

    /**
     * Add one record to todos table and return added object
     * @param todo - record to add
     * @return inserted object
     */
    public Todo addTodoReturnTodo(Todo todo)
    {
        try
        {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String insertSQL = "INSERT INTO todos (description, is_completed) VALUES (?, ?)";

            jdbcTemplate.update(connection ->
            {
                PreparedStatement ps = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, todo.getDescription());
                ps.setBoolean(2, todo.getIsCompleted());
                return ps;
            }, keyHolder);

            // Check if a key is was generated
            if (null != keyHolder.getKey())
            {
                long newId = keyHolder.getKey().longValue();
                todo.setId(newId);  // Set the new ID to the todo object
                return todo;        // Return the updated todo object
            } else {
                log.error("No ID received after inserting todo");
                throw new RuntimeException("Failed to obtain ID for the new todo");
            }
        } catch (DataAccessException e) {
            log.error("Error adding todo: {}", StackTraceUtil.getRootCause(e));
            throw new RuntimeException("Error adding todo", e);
        }
    }

    /**
     * Updates records from todos table by todo list object.
     * @param todo - source object
     * @return - how many records were (are) updated
     */
    public Todo updateTodo(Long id, Todo todo) {
        try {
            int rowsUpdated = jdbcTemplate.update("UPDATE todos SET description = ?, is_completed = ? WHERE id = ?",
                    todo.getDescription(), todo.getIsCompleted(), id);
            if (rowsUpdated > 0) {
                return findTodoById(id);  // Get updated TodoList from the database
            } else {
                log.info("No TodoList was updated for id: {}", id);
                return null;
            }
        } catch (DataAccessException e) {
            log.error("Error updating todo with id: {}. {}", id, StackTraceUtil.getRootCause(e));
            throw new RuntimeException("Error updating todo", e);
        }
    }

    /**
     * Deletes records from todos table by its ID.
     * @param id the ID of the todos record to be deleted.
     * @return true if the todo was deleted successfully, false otherwise.
     */
    public boolean deleteTodoById(Long id)
    {
        try {
            int rowsRemoved = jdbcTemplate.update("DELETE FROM todos WHERE id = ?", id);
            return rowsRemoved > 0;
        } catch (DataAccessException e) {
            log.error("Error deleting todo with id: {}. {}", id, StackTraceUtil.getRootCause(e));
            throw new RuntimeException("Error deleting todo", e);
        }
    }

    /**
     * Test for SQLITE_ERROR error
     */
    public List<Todo> findAllTodosError()
    {
        try {
            return jdbcTemplate.query("SELECT * FROM non_exists_table", rowMapper);
        } catch (DataAccessException e) {
            log.error("Error fetching all non_exists_table: {}", StackTraceUtil.getRootCause(e));
            throw new RuntimeException("Error fetching all non_exists_table", e);
        }
    }
}
