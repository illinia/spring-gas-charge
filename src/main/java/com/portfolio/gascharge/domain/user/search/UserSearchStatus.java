package com.portfolio.gascharge.domain.user.search;

import com.portfolio.gascharge.enums.user.UserAuthority;
import com.portfolio.gascharge.enums.user.UserEmailVerified;
import com.portfolio.gascharge.oauth.entity.AuthProvider;
import lombok.Data;

@Data
public class UserSearchStatus {

    private String email;
    private UserEmailVerified emailVerified;
    private String name;
    private AuthProvider authProvider;
    private UserAuthority userAuthority;
}
