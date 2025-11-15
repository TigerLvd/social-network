package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.model.DialogMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface DialogMessageRepository extends JpaRepository<DialogMessageEntity, UUID> {

    @Query("SELECT m FROM DialogMessageEntity m WHERE " +
           "(m.fromUserId = :user1 AND m.toUserId = :user2) OR " +
           "(m.fromUserId = :user2 AND m.toUserId = :user1) " +
           "ORDER BY m.createdAt DESC")
    Page<DialogMessageEntity> findByUsersPaged(@Param("user1") UUID user1,
                                              @Param("user2") UUID user2,
                                              Pageable pageable);
}
