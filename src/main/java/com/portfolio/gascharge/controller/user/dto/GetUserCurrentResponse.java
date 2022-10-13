package com.portfolio.gascharge.controller.user.dto;

import com.portfolio.gascharge.domain.user.User;
import lombok.Builder;
import lombok.Data;

@Data
public class GetUserCurrentResponse {

    private String imageUrl;
    private String name;
    private String email;

    @Builder
    public GetUserCurrentResponse(String imageUrl, String name, String email) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.email = email;
    }

    public static GetUserCurrentResponse toResponseDto(User user) {
        return GetUserCurrentResponse.builder()
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
