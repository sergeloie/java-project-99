package hexlet.code.app.controller;

import hexlet.code.app.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.app.dto.taskstatus.TaskStatusDTO;
import hexlet.code.app.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.app.exception.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
@AllArgsConstructor(onConstructor_ = @__(@Autowired))
public class TaskStatusController {

    private final TaskStatusMapper taskStatusMapper;
    private final TaskStatusRepository taskStatusRepository;

    @GetMapping
    public ResponseEntity<List<TaskStatusDTO>> index(@RequestParam(defaultValue = "0") int _start,
                                                    @RequestParam(defaultValue = "10") int _end,
                                                    @RequestParam(defaultValue = "id") String _sort,
                                                    @RequestParam(defaultValue = "ASC") String _order) {
        int page = _start / (_end - _start);
        Sort.Direction direction = Sort.Direction.fromString(_order);
        Pageable pageable = PageRequest.of(page, _end - _start, Sort.by(direction, _sort));

        var result =  taskStatusRepository.findAll(pageable).stream()
                .map(taskStatusMapper::map)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(result.size()))
                .body(result);

    }

    @GetMapping(path = "/{id}")
    public TaskStatusDTO show(@PathVariable long id) {
        TaskStatus result = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task status with id %d not found", id)));
        return taskStatusMapper.map(result);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@Valid @RequestBody TaskStatusCreateDTO taskStatusCreateDTO) {
        TaskStatus taskStatus = taskStatusMapper.map(taskStatusCreateDTO);
        try {
            taskStatusRepository.save(taskStatus);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(
                    String.format("Task status with name = '%s' or slug = '%s' already exists",
                    taskStatus.getName(),
                    taskStatus.getSlug()));
        }
        return taskStatusMapper.map(taskStatus);
    }

    @PutMapping(path = "/{id}")
    public TaskStatusDTO update(@PathVariable long id, @Valid @RequestBody TaskStatusUpdateDTO taskStatusUpdateDTO) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Task status with id %d not found", id)));
        taskStatusMapper.update(taskStatusUpdateDTO, taskStatus);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable long id) {
        taskStatusRepository.deleteById(id);
    }
}
