package finki.ukim.backend.auth_and_access.web.filter;

import finki.ukim.backend.auth_and_access.constants.JwtProperties;
import finki.ukim.backend.auth_and_access.helper.JwtHelper;
import finki.ukim.backend.auth_and_access.model.domain.User;
import finki.ukim.backend.auth_and_access.model.exception.UserNotFoundException;
import finki.ukim.backend.auth_and_access.service.domain.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserService userService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        String headerValue = request.getHeader(jwtProperties.getHeader());

        if (headerValue == null || !headerValue.startsWith(jwtProperties.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = headerValue.substring(jwtProperties.getTokenPrefix().length());

        try {
            Long userId = jwtHelper.extractUserId(token);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userId == null || authentication != null) {
                filterChain.doFilter(request, response);
                return;
            }

            User user;

            try {
                user = userService.findById(userId);
            } catch (UserNotFoundException e) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtHelper.isExpired(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (JwtException | NumberFormatException jwtException) {
            handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    jwtException
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}