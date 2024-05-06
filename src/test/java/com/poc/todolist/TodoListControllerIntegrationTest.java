package com.poc.todolist;

import com.poc.todolist.db.dao.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.poc.todolist.exceptions.ApiResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // Uses application-test.yml from src/test/resources
public class TodoListControllerIntegrationTest {

    @LocalServerPort
    private int port;
    private String baseUrl;

    @Autowired
    private WebClient webClient;

    private static final Logger log = LoggerFactory.getLogger(TodoListControllerIntegrationTest.class);

    @BeforeEach
    public void setup() {
        // Initialize base URL
        baseUrl = "http://localhost:" + port + "/todos";
    }

    @Test
    public void testUpdateTodo() {
        String jsonBody = "{\"description\": \"Update the report\", \"isCompleted\": true}";
        String url = baseUrl + "/2";
        log.debug("url: {}, req: {}", url, jsonBody);
        // INSERT INTO todos (id, description, is_completed) VALUES (2, 'Sample Todo2', false);
        // WebClient setup and API call
        ApiResponse<Todo> result = webClient.patch()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonBody))
                .retrieve() // Initiates the request
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Todo>>() {}) // way to handle generic types
                .block(); // Block and wait for the single item

        // Updated ro -> INSERT INTO todos (id, description, is_completed) VALUES (2, 'Update the report', true);
        assert result != null;
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getDescription()).isEqualTo("Update the report");
        assertThat(result.getData().getIsCompleted()).isTrue();
        assertThat(result.getData().getId()).isEqualTo(2);
    }

    @Test
    public void testCreateTodo() {
        String jsonBody = "{\"description\": \"Finish the report\", \"isCompleted\": false}";
        ApiResponse<Todo> result  = webClient.post()
                .uri(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(jsonBody))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Todo>>() {}) // way to handle generic types
                .block(); // Block to subscribe and wait for the result

        assert result != null;
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getDescription()).isEqualTo("Finish the report");
        assertThat(result.getData().getIsCompleted()).isFalse();
    }

    @Test
    public void testGetTodoById1() {
        ApiResponse<Todo> result  = webClient.get()
                .uri(baseUrl + "/1")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Todo>>() {}) // way to handle generic types
                .block(); // Block to subscribe and wait for the result
        // INSERT INTO todos (id, description, is_completed) VALUES (1, 'Sample Todo1', false);
        assert result != null;
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getDescription()).isEqualTo("Sample Todo1");
        assertThat(result.getData().getIsCompleted()).isFalse();
    }

    @Test
    public void testGetTodoById3() {
        ApiResponse<Todo> result  = webClient.get()
                .uri(baseUrl + "/3")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Todo>>() {}) // way to handle generic types
                .block(); // Block to subscribe and wait for the result
        // INSERT INTO todos (id, description, is_completed) VALUES (3, 'Sample Todo3', true);
        assert result != null;
        assertThat(result.getData()).isNotNull();
        assertThat(result.getData().getDescription()).isEqualTo("Sample Todo3");
        assertThat(result.getData().getIsCompleted()).isTrue();
    }

    @Test
    public void testDeleteTodo() {
        int recordId2Delete = 4;
        String url = baseUrl + "/" + recordId2Delete;
        log.debug("Delete url: {}.", url);
        // Delete -> INSERT INTO todos (id, description, is_completed) VALUES (4, 'Sample Todo4', false);
        ApiResponse<Todo> result = webClient.delete()
                .uri(url)
                .retrieve() // Initiates the request
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Todo>>() {}) // way to handle generic types
                .block(); // Block and wait for the single item

        assertThat(result).isNull();

        // Retrieve all records minus one (removed)
        ApiResponse<List<Todo>> resultList  = webClient.get()
                .uri(baseUrl)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<Todo>>>() {}) // way to handle generic types
                .block(); // Block to subscribe and wait for the result
        assert resultList != null;
        assertThat(resultList.getData()).isNotNull();
        assertThat(resultList.getData().size()).isEqualTo(5); // 6-1
        for(Todo todo:resultList.getData())
        {   // Verify that record with id=recordId2Delete was removed
            assertNotEquals(recordId2Delete, todo.getId(), "The record with id=" + recordId2Delete + " is expected to be deleted.");
        }
    }

}

