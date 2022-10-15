package com.portfolio.gascharge.controller.user.dto;

import com.portfolio.gascharge.controller.reservation.dto.GetReservationResponseDto;
import com.portfolio.gascharge.domain.user.User;
import com.portfolio.gascharge.enums.user.UserAuthority;
import com.portfolio.gascharge.enums.user.UserEmailVerified;
import com.portfolio.gascharge.oauth.entity.AuthProvider;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SearchUserResponseDto {

    private String imageUrl;
    private String name;
    private String email;
    private UserAuthority authority;
    private UserEmailVerified emailVerified;
    private AuthProvider authProvider;
    private List<GetReservationResponseDto> reservations;

    @Builder
    public SearchUserResponseDto(String imageUrl, String name, String email, UserAuthority authority, UserEmailVerified emailVerified, AuthProvider authProvider, List<GetReservationResponseDto> reservations) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.email = email;
        this.authority = authority;
        this.emailVerified = emailVerified;
        this.authProvider = authProvider;
        this.reservations = reservations;
    }

    public static SearchUserResponseDto toResponseDto(User user) {
        return SearchUserResponseDto.builder()
                .imageUrl(user.getImageUrl())
                .name(user.getName())
                .email(user.getEmail())
                .authority(user.getUserAuthority())
                .emailVerified(user.getEmailVerified())
                .authProvider(user.getProvider())
                .reservations(user.getReservations().stream().map(r -> GetReservationResponseDto.toResponseDto(r)).collect(Collectors.toList()))
                .build();
    }
}
