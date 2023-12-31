package com.websocket.demo.repository;

import com.websocket.demo.domain.Friend;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @EntityGraph(attributePaths = "user")
    List<Friend> findByUserNickname(String nickname);

    @Modifying(flushAutomatically = true)
    @Query("delete from Friend f where f.user.nickname=:userNickname and f.friendNickname=:friendNickname")
    void deleteByUserNicknameAndFriendNickname(@Param("userNickname") String userNickname,@Param("friendNickname") String friendNickname);
}