package com.portfolio.gascharge.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.portfolio.gascharge.domain.reservation.Reservation;
import com.portfolio.gascharge.enums.user.UserAuthority;
import com.portfolio.gascharge.enums.user.UserEmailVerified;
import com.portfolio.gascharge.oauth.entity.AuthProvider;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString(exclude = {"password"})
@Entity
@Table(name = "USERS",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"USER_ID", "email"})
})
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    @JsonIgnore
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Setter
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @ColumnDefault(value = "'UNVERIFIED'")
    private UserEmailVerified emailVerified;

    @JsonIgnore
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    @Enumerated(EnumType.STRING)
    @ColumnDefault(value = "'ROLE_USER'")
    private UserAuthority userAuthority;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonManagedReference
    private List<Reservation> reservations = new ArrayList<>();

    @Builder
    public User(String name, String email, String imageUrl, UserEmailVerified emailVerified, String password, AuthProvider provider, String providerId, UserAuthority userAuthority) {
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
