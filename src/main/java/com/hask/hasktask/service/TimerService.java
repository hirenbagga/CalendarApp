package com.hask.hasktask.service;

import com.hask.hasktask.model.Timer;
import com.hask.hasktask.repository.TimerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimerService {

    final private TimerRepository timerRepository;

    public TimerService(TimerRepository timerRepository) {
        this.timerRepository = timerRepository;
    }

    public Timer setTimer(Timer timer) {
        return timerRepository.save(timer);
    }

    public Timer getTimerById(Long timerId) {
        return timerRepository.findById(timerId).orElse(null);
    }

    public Timer updateTimer(Long timerId, Timer timer) {
        timer.setTimerId(timerId);
        return timerRepository.save(timer);
    }

    public void deleteTimer(Long timerId) {
        timerRepository.deleteById(timerId);
    }
}
