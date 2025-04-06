package com.hask.hasktask.controller;

import com.hask.hasktask.model.Timer;
import com.hask.hasktask.service.TimerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timers")
@Tag(name = "Timer Management")
public class TimerController {

    final private TimerService timerService;

    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @PostMapping
    public ResponseEntity<Timer> setTimer(@RequestBody Timer timer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(timerService.setTimer(timer));
    }

    @GetMapping("/{timerId}")
    public ResponseEntity<Timer> getTimer(@PathVariable Long timerId) {
        return ResponseEntity.ok(timerService.getTimerById(timerId));
    }

    @GetMapping("/{userId}/user")
    public ResponseEntity<List<Timer>> getTimers(@PathVariable Long userId) {
        return ResponseEntity.ok(timerService.getTimersByUserId(userId));
    }

    @PutMapping("/{timerId}")
    public ResponseEntity<Timer> updateTimer(@PathVariable Long timerId, @RequestBody Timer timer) {
        return ResponseEntity.ok(timerService.updateTimer(timerId, timer));
    }

    @DeleteMapping("/{timerId}")
    public ResponseEntity<Void> deleteTimer(@PathVariable Long timerId) {
        timerService.deleteTimer(timerId);
        return ResponseEntity.noContent().build();
    }
}
