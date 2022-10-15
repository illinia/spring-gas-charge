package com.portfolio.gascharge.controller.user.dto;

import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.enums.user.UserAuthority;
import lombok.Builder;
import lombok.Data;

@Data
public class GetUserCurrentResponse {
    private String imageUrl;
    private String name;
    private String email;
    private UserAuthority authority;

    @Builder
    public GetUserCurrentResponse(String imageUrl, String name, String email, UserAuthority authority) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.email = email;
        this.authority = authority;
    }

    public static GetUserCurrentResponse toResponseDto(User user) {
        return GetUserCurrentResponse.builder()
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .email(user.getEmail())
                .authority(user.getUserAuthority())
                .build();
    }
}
