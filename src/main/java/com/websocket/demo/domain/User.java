package com.websocket.demo.domain;

import com.websocket.demo.request.LoginRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(columnDefinition = "varchar(20)")
    private String nickname;
    @Column(columnDefinition = "varchar(20)")
    private String password;
    @OneToMany(mappedBy = "friend", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friends = new ArrayList<>();

    @Builder
    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public void addFriends(User... friends) {
        for (var friend : friends) {
            this.friends.add(Friend.builder()
                    .userNickname(getNickname())
                    .friend(friend)
                    .build()
            );
        }
    }

    public boolean match(LoginRequest request) {
        return request.getNickname().equals(nickname) &&
                request.getPassword().equals(password);
    }


    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(nickname, user.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}
