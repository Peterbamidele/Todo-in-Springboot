package com.springbootmongodb.controller;

import com.springbootmongodb.model.TodoDTO;
import com.springbootmongodb.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/todos")
    public ResponseEntity<?> getAllTodos() {
        List<TodoDTO> todos = todoRepository.findAll();
        if (todos.size() > 0) {
            return new ResponseEntity<List<TodoDTO>>(todos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No todos available", HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/todos")
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todo) {
        try {
            todo.setCreatedAt(new Date(System.currentTimeMillis()));
            todoRepository.save(todo);
            return new ResponseEntity<TodoDTO>(todo, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getSingleTodo(@PathVariable("id")String id){
        Optional<TodoDTO> todoDTOOptional = todoRepository.findById(id);
        if (todoDTOOptional.isPresent()){
            return new ResponseEntity<>(todoDTOOptional.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Todo not found with id"+id,HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable("id")String id , @RequestBody TodoDTO todoDTO){
        Optional<TodoDTO> todoDTOOptional = todoRepository.findById(id);
        if (todoDTOOptional.isPresent()){
            TodoDTO todoDTOToSave = todoDTOOptional.get();
            todoDTOToSave.setCompleted(todoDTOToSave.getCompleted() != null ? todoDTO.getCompleted():todoDTOToSave.getCompleted());
            todoDTOToSave.setTodo(todoDTO.getTodo() !=null ? todoDTO.getTodo() : todoDTOToSave.getTodo());
            todoDTOToSave.setDescription(todoDTO.getDescription() != null ? todoDTO.getDescription():todoDTOToSave.getDescription());
            todoDTOToSave.setUpdated(new Date(System.currentTimeMillis()));
            todoRepository.save(todoDTOToSave);
            return new ResponseEntity<>(todoDTOToSave, HttpStatus.OK);
        }else {
            return  new ResponseEntity<>("Todo not found with id"+id, HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?>deletedById(@PathVariable("id")String id){
        try {
            todoRepository.deleteById(id);
            return new ResponseEntity<>("Successfully deleted with id"+ id, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}


