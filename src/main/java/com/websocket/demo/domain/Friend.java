package com.websocket.demo.domain;

import com.websocket.demo.response.FriendInfo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FRIENDS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_nickname", nullable = false)
    private User user;

    @Column(nullable = false)
    private String friendNickname;

    @Builder
    private Friend(User user, String friend) {
        this.user = user;
        this.friendNickname= friend;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return this.friendNickname;
    }

    public FriendInfo toInfo() {
        return new FriendInfo(getName());
    }


}