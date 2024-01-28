package com.api.parking.security;

import com.api.parking.dto.request.LoginRequestDto;
import com.api.parking.dto.response.JwtResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenUtils tokenUtils;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        LoginRequestDto loginRequestDto;
        try {
            loginRequestDto = new ObjectMapper().readValue(request.getReader(), LoginRequestDto.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Error login request body", e);
        }

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword(),
                        Collections.emptyList()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        Object[] authorities = userDetails.getAuthorities().toArray();
        String token = tokenUtils.createToken(userDetails.getUsername(), authorities[0].toString(),
                userDetails.getId());

        response.addHeader("Authorization", "Bearer " + token);

        JwtResponseDto jwtToken = new JwtResponseDto();
        jwtToken.setToken("Bearer " + token);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(jwtToken));
        response.getWriter().flush();

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
