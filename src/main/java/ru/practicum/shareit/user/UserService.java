package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Optional<User> getUserOptional(Integer userId);

    User getUser(Integer userId);

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(Integer userId, User user);

    void deleteUser(Integer userId);
}
