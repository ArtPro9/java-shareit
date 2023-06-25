package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Item I set I.name = ?2 where I.id = ?1")
    void editItemName(Integer itemId, String name);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Item I set I.description = ?2 where I.id = ?1")
    void editItemDescription(Integer itemId, String description);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Item I set I.isAvailable = ?2 where I.id = ?1")
    void editItemAvailability(Integer itemId, boolean isAvailable);
}
