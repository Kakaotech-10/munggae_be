package com.ktb10.munggaebe.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktb10.munggaebe.auth.exception.JwtErrorException;
import com.ktb10.munggaebe.error.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtErrorException ex) {
            log.warn(ex.getMessage() + " Authorization header = " + request.getHeader("Authorization"));
            response.setStatus(ex.getErrorCode().getStatus());
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter()
                    .write(objectMapper.writeValueAsString(ErrorResponse.from(ex.getErrorCode())));
        }
    }
}
