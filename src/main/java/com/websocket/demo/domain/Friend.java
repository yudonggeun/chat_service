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
    @Column
    private String userNickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_nickname")
    private User friend;

    @Builder
    private Friend(String userNickname, User friend) {
        this.userNickname = userNickname;
        this.friend = friend;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public User getFriend() {
        return this.friend;
    }

    public FriendInfo toInfo() {
        return new FriendInfo(getFriend().getNickname());
    }
}