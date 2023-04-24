package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static int id = 0;
    Map<Integer, User> users = new LinkedHashMap<>();

    private static int getId() {
        return ++id;
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        int newId = getId();
        user.setId(newId);
        users.put(newId, user);
        return user;
    }

    @Override
    public User updateUser(Integer userId, User updatedUser) {
        User user = users.get(userId);
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getEmail() != null) {
            user.setEmail(updatedUser.getEmail());
        }
        users.put(userId, user);
        return user;
    }

    @Override
    public void deleteUserById(Integer userId) {
        users.remove(userId);
    }

    @Override
    public List<String> getAllEmails() {
        return users.values().stream().map(User::getEmail).collect(Collectors.toList());
    }
}
