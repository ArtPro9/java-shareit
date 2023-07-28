package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UnknownUserException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserOptional(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public User getUser(Integer userId) {
        Optional<User> user = getUserOptional(userId);
        return user.orElseThrow(() -> new UnknownUserException(userId));
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User updateUser(Integer userId, User updatedUser) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Unknown user id: " + userId);
        }
        if (updatedUser.getEmail() != null) {
            userRepository.updateUserEmail(userId, updatedUser.getEmail());
        }
        if (updatedUser.getName() != null) {
            userRepository.updateUserName(userId, updatedUser.getName());
        }
        return getUserOptional(userId).orElseThrow(() -> new IllegalArgumentException("Unknown user id: " + userId));
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}
