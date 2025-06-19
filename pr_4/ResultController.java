package com.example.resultapi.controller;

import com.example.resultapi.model.Result;
import com.example.resultapi.service.ResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
public class ResultController {

    private final ResultService service;

    public ResultController(ResultService service) {
        this.service = service;
    }

    @GetMapping("/readonly")
    public List<Result> getReadOnlyResults() {
        return service.getReadOnlyResults();
    }
    @GetMapping("/writable")
    public List<Result> getWritableResults() {
        return service.getWritableResults();
    }
    @PostMapping
    public Result createResult(@RequestBody Result result) {
        return service.save(result);
    }

    @PutMapping("/{id}")
    public Result updateResult(@PathVariable Long id, @RequestBody Result updated) {
        return service.getById(id)
                .map(existing -> {
                    existing.setData(updated.getData());
                    existing.setReadOnly(updated.isReadOnly());
                    return service.save(existing);
                }).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.getById(id);
    }
    @GetMapping
    public List<Result> getAllResults() {
        return service.getAllResults();
    }
}