package com.example.resultapi.service;

import com.example.resultapi.model.Result;
import com.example.resultapi.repository.ResultRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResultService {

    private final ResultRepository repository;

    public ResultService(ResultRepository repository) {
        this.repository = repository;
    }

    @Cacheable("readonlyResults")
    public List<Result> getReadOnlyResults() {
        return repository.findByReadOnlyTrue();
    }

    public List<Result> getWritableResults() {
        return repository.findByReadOnlyFalse();
    }

    public Optional<Result> getById(Long id) {
        return repository.findById(id);
    }

    public Result save(Result result) {
        return repository.save(result);
    }
    public List<Result> getAllResults() {
        return repository.findAll();
    }

}
