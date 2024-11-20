package org.example.expert.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.expert.domain.common.entity.Timestamped;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.security.UserDetailsImpl;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "users")
@Table(name = "users", indexes =
@Index(name = "idx_user_nickname", columnList = "nickname"))
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column
    private String imagePath;

    public User(String email, String password, String nickname, UserRole userRole) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.userRole = userRole;
    }

    public static User fromAuthUser(UserDetailsImpl userDetail) {
        return new User(
                userDetail.getId(),
                userDetail.getEmail(),
                userDetail.getPassword(),
                userDetail.getNickname(),
                userDetail.getUserRole(),
                null);
    }

    public void updateNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void updateImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
