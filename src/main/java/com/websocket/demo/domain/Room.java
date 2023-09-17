package com.websocket.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String title;
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomUserData> data = new ArrayList<>();
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    private List<Chat> chatList = new ArrayList<>();

    @Builder
    private Room(String title) {
        this.title = title;
    }

    public RoomUserData addUser(String userNickname) {
        RoomUserData entity = RoomUserData.builder()
                .room(this)
                .backgroundColor("white")
                .userNickname(userNickname)
                .build();
        data.add(entity);
        return entity;
    }

    public boolean containsUser(String host) {
        return data.stream().map(RoomUserData::getUserNickname)
                .anyMatch(user -> user.equals(host));
    }
}
