package main;

import main.model.Task;
import main.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping(value = "/tasks/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity add (@RequestBody Task task) {
        task.setCreationTime(LocalDateTime.now());
        task.setIsDone(false);
        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping(value = "/tasks/{id}")
    public ResponseEntity<Task> get(@PathVariable int id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.isPresent() ? new ResponseEntity<>(task.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/tasks/")
    public ResponseEntity<List<Task>> list() {
        Iterable<Task> taskIterable = taskRepository.findAll();
        ArrayList<Task> tasks = new ArrayList<>();
        for (Task task: taskIterable){
            tasks.add(task);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @DeleteMapping(value = "/tasks/{id}")
    public ResponseEntity delete(@PathVariable int id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            taskRepository.delete(task.get());
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @PatchMapping(value = "/tasks/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@PathVariable int id, @RequestBody Task newData){
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (!taskOptional.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Task task = taskOptional.get();
        if (newData.getIsDone()!=null) {
            task.setIsDone(newData.getIsDone());
        }
        if (newData.getDescription()!=null) {
            task.setDescription(newData.getDescription());
        }
        if (newData.getTitle()!=null) {
            task.setTitle(newData.getTitle());
        }
        taskRepository.save(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}