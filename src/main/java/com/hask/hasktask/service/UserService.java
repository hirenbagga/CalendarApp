package com.hask.hasktask.service;

import com.hask.hasktask.customException.EntityNotFoundException;
import com.hask.hasktask.model.User;
import com.hask.hasktask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z]{2,6}$";
        return StringUtils.hasText(email) && Pattern.matches(emailRegex, email);
    }

    public List<User> getAll(Pageable pageable) {

        Page<User> userPageable = userRepository.findAll(pageable);
        if (!userPageable.hasContent()) {
            throw new EntityNotFoundException(User.class, "Pagination", pageable.next().toString());
        }

        return userPageable
                .stream()
                .collect(Collectors.toList());
    }

    /*
     * GET User details by ID OR userEmail */
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new EntityNotFoundException(User.class, "Email", email)
                );
    }

    public User getLoggedInUser(Principal loggedInUser) {
        return (User) ((UsernamePasswordAuthenticationToken) loggedInUser).getPrincipal();
    }

    public void update(User update) {
        Integer id = update.getId();
        var query = id.toString().isBlank() ?
                userRepository.findByEmail(update.getEmail())
                : userRepository.findById(id);

    }

    public void deleteByIdOrEmail(String any) {
        Optional<User> user = userRepository.findByEmail(any);

        if (user.isEmpty()) {
            throw new EntityNotFoundException(User.class, isValidEmail(any) ? "Email" : "Id ", any);
        }
        userRepository.delete(user.get());
    }

}
