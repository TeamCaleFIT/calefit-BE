package com.calefit.auth;

import com.calefit.auth.jwt.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static com.calefit.auth.jwt.JwtConst.ACCESS_TOKEN_EXPIRATION_PERIOD;
import static com.calefit.auth.jwt.JwtConst.REFRESH_TOKEN_EXPIRATION_PERIOD;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/oauth/callback")
    public ResponseEntity<?> oauthLogin(@RequestParam String code, @RequestParam String providerType) {
        Token token = loginService.createToken(code, providerType);

        ResponseCookie cookie = ResponseCookie.from("access_token", token.getAccessToken())
                .maxAge(ACCESS_TOKEN_EXPIRATION_PERIOD)
                .path("/")
                .build();

        ResponseCookie cookie2 = ResponseCookie.from("refresh_token", token.getRefreshToken())
                .maxAge(REFRESH_TOKEN_EXPIRATION_PERIOD)
                .path("/")
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.SET_COOKIE, cookie2.toString())
                .build();
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        loginService.deleteStoredToken(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("access_token", null)
                .maxAge(0)
                .path("/")
                .build();

        ResponseCookie cookie2 = ResponseCookie.from("refresh_token", null)
                .maxAge(0)
                .path("/")
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(HttpHeaders.SET_COOKIE, cookie2.toString())
                .build();
    }
}
