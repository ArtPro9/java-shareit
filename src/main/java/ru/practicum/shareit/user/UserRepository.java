package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserById(Integer userId);

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(Integer userId, User user);

    void deleteUserById(Integer userId);

    List<String> getAllEmails();
}
