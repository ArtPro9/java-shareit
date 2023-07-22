package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User U set U.name = ?2 where U.id = ?1")
    void updateUserName(Integer userId, String name);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User U set U.email = ?2 where U.id = ?1")
    void updateUserEmail(Integer userId, String email);
}
