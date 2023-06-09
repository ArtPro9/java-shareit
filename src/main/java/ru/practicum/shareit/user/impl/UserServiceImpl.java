package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailDuplicationException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(Integer userId) {
        return userRepository.getUserById(userId).orElse(null);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User createUser(User user) {
        List<String> emails = userRepository.getAllEmails();
        if (emails.contains(user.getEmail())) {
            throw new EmailDuplicationException("Email has already existed: " + user);
        }
        return userRepository.createUser(user);
    }

    @Override
    public User updateUser(Integer userId, User updatedUser) {
        if (!checkIfUserExists(userId)) {
            throw new IllegalArgumentException("Unknown user id: " + userId);
        }
        User user = getUser(userId);
        if (!user.getEmail().equals(updatedUser.getEmail())) {
            List<String> emails = userRepository.getAllEmails();
            if (emails.contains(updatedUser.getEmail())) {
                throw new EmailDuplicationException("Email has already existed: " + updatedUser);
            }
        }
        return userRepository.updateUser(userId, updatedUser);

    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteUserById(userId);
    }

    private boolean checkIfUserExists(int id) {
        Optional<User> user = userRepository.getUserById(id);
        return user.isPresent();
    }
}
