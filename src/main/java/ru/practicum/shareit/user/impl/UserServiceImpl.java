package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailDuplicationException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
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
    public Optional<User> getUser(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User createUser(User user) {
        List<String> emails = userRepository.findAllEmails();
        if (emails.contains(user.getEmail())) {
            throw new EmailDuplicationException("Email has already existed: " + user);
        }
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(Integer userId, User updatedUser) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Unknown user id: " + userId);
        }
        User user = getUser(userId).orElseThrow(() -> new IllegalArgumentException("Unknown user id: " + userId));
        if (updatedUser.getEmail() != null) {
            if (!user.getEmail().equals(updatedUser.getEmail())) {
                List<String> emails = userRepository.findAllEmails();
                if (emails.contains(updatedUser.getEmail())) {
                    throw new EmailDuplicationException("Email has already existed: " + updatedUser);
                }
                userRepository.updateUserEmail(userId, updatedUser.getEmail());
            }
        }
        if (updatedUser.getName() != null) {
            userRepository.updateUserName(userId, updatedUser.getName());
        }
        return getUser(userId).orElseThrow(() -> new IllegalArgumentException("Unknown user id: " + userId));
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
