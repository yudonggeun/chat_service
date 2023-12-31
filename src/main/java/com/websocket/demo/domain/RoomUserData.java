package com.websocket.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomUserData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", updatable = false)
    private Room room;
    @Column(updatable = false)
    private String userNickname;
    @Column
    private LocalDateTime checkTime;
    @Column
    private String backgroundColor;

    @Builder
    private RoomUserData(Room room, String userNickname, String backgroundColor) {
        this.room = room;
        this.userNickname = userNickname;
        this.checkTime = LocalDateTime.now();
        this.backgroundColor = backgroundColor;
    }

    public void setCheckTime(LocalDateTime checkTime) {
        this.checkTime = checkTime;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
