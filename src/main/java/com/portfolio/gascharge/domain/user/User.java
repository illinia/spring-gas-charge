package com.portfolio.gascharge.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.gascharge.enums.user.UserAuthority;
import com.portfolio.gascharge.oauth.entity.AuthProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USER", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String imageUrl;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @Enumerated(EnumType.STRING)
    @ColumnDefault(value = "'ROLE_USER'")
    private UserAuthority userAuthority;

    @Builder
    public User(String name, String email, String imageUrl, Boolean emailVerified, String password, AuthProvider provider, String providerId, UserAuthority userAuthority) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.emailVerified = emailVerified;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.userAuthority = userAuthority;
    }
}
