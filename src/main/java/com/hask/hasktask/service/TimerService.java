package com.hask.hasktask.service;

import com.hask.hasktask.customException.GeneralException;
import com.hask.hasktask.model.Timer;
import com.hask.hasktask.repository.TimerRepository;
import com.hask.hasktask.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimerService {

    final private TimerRepository timerRepository;
    private final UserRepository userRepository;

    public TimerService(TimerRepository timerRepository, UserRepository userRepository) {
        this.timerRepository = timerRepository;
        this.userRepository = userRepository;
    }

    public Timer setTimer(Timer timer) {
        var user = userRepository.findById(timer.getUser().getId())
                .orElseThrow(() -> new GeneralException("User", "User not found"));
        timer.setUser(user);

        return timerRepository.save(timer);
    }

    public Timer getTimerById(Long timerId) {
        return timerRepository.findById(timerId).orElse(null);
    }

    public List<Timer> getTimersByUserId(Long userId) {
        return timerRepository.findByUserId(userId);
    }

    public Timer updateTimer(Long timerId, Timer timer) {
        timer.setTimerId(timerId);
        return timerRepository.save(timer);
    }

    public void deleteTimer(Long timerId) {
        timerRepository.deleteById(timerId);
    }
}
