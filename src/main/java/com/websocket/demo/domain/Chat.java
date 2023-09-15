package com.websocket.demo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, updatable = false)
    private Long roomId;
    @Column(nullable = false, updatable = false)
    private String senderNickname;
    @Column
    private String message;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createAt;

    @Builder
    private Chat(Long roomId, String senderNickname, String message) {
        this.roomId = roomId;
        this.senderNickname = senderNickname;
        this.message = message;
    }
}
