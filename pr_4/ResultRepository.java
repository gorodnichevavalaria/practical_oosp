package com.example.resultapi.repository;

import com.example.resultapi.model.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByReadOnlyTrue();
    List<Result> findByReadOnlyFalse();
}