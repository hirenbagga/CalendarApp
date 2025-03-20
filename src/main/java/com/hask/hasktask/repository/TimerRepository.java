package com.hask.hasktask.repository;

import com.hask.hasktask.model.Timer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimerRepository extends JpaRepository<Timer, Long> {
    List<Timer> findByUserId(Long userId);
}
