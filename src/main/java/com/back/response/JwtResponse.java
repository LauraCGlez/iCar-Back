package com.back.response;

import com.back.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private String jwt;

    private String message;

    private boolean isAuthorized;

    private boolean isError;

    private String errorDetails;

    private UserRole type;;

}
