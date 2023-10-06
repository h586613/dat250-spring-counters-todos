package no.hvl.dat250.rest.todos;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Rest-Endpoint for todos.
 */
@RestController
public class TodoController {

  private static List<Todo> todos = new ArrayList<>();

  private static long id = 0;

  public static final String TODO_WITH_THE_ID_X_NOT_FOUND = "Todo with the id %s not found!";

  @GetMapping("/todos")
  public List<Todo> getTodos() {
    return TodoController.todos;
  }

  @GetMapping("/todos/{id}")
  public ResponseEntity<?> getTodo(@PathVariable long id) {
    for (Todo t : TodoController.todos) {
      if (t.getId() == id) {
        return ResponseEntity.ok(t);
      }
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, id));
  }

  @PutMapping("/todos/{id}")
  public ResponseEntity<?> putTodo(@RequestBody Todo newTodo, @PathVariable long id) {
    Todo oldTodo;
    for (Todo t : TodoController.todos) {
      if (t.getId() == id) {
        oldTodo = t;

        oldTodo.setDescription(newTodo.getDescription());
        oldTodo.setSummary(newTodo.getSummary());

        return ResponseEntity.ok(oldTodo);
      }
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, id));
  }

  @PostMapping("/todos")
  public Todo postTodo(@RequestBody Todo todo) {
    todo.setId(setId());

    todos.add(todo);

    return todo;
  }


  @DeleteMapping("/todos/{id}")
  public void deleteTodo(@PathVariable long id) {
    TodoController.todos = TodoController.todos
            .stream()
            .filter(
                    t -> t.getId() != id
            )
            .collect(Collectors.toList());
  }

  /**
   * Returns the new id, and increments the id counter
   * @return new id
   */
  private long setId() {
    long id = TodoController.id;

    TodoController.id += 1;

    return id;
  }
}