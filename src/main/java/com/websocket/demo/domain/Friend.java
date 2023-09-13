package com.websocket.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "FRIENDS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Friend {
    @EmbeddedId
    private FriendFields fields;

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Embeddable
    public class FriendFields {
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        @Column
        private Long friendId;


        private FriendFields(User user, Long friendId) {
            this.user = user;
            this.friendId = friendId;
        }
    }

    @Builder
    private Friend(User user, Long friendId) {
        this.fields = new FriendFields(user, friendId);
    }

    public User getUser() {
        return fields.user;
    }

    public Long getFriendId() {
        return fields.friendId;
    }
}