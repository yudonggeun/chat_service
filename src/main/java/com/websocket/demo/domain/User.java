package com.websocket.demo.domain;

import com.websocket.demo.request.LoginRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USERS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(columnDefinition = "varchar(20)", unique = true)
    private String nickname;
    @Column(columnDefinition = "varchar(20)")
    private String password;
    @OneToMany(mappedBy = "fields.user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friends = new ArrayList<>();

    @Builder
    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public void addFriends(User... friends) {
        for (var friend : friends) {
            this.friends.add(Friend.builder()
                    .user(this)
                    .friendId(friend.getId())
                    .build()
            );
        }
    }

    public boolean match(LoginRequest request) {
        return request.getNickname().equals(nickname) &&
                request.getPassword().equals(password);
    }
}
