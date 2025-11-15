package com.highload.architect.soc.network.repository;

import com.highload.architect.soc.network.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<Friendship, UUID> {

    boolean existsByUserIdAndFriendId(UUID userId, UUID friendId);
}
