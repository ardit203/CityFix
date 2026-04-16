package finki.ukim.backend.auth_and_access.web.filter;

import finki.ukim.backend.auth_and_access.constants.JwtConstants;
import finki.ukim.backend.auth_and_access.helper.JwtHelper;
import finki.ukim.backend.auth_and_access.model.domain.User;
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
import java.util.Optional;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtHelper jwtHelper;
    private final UserService userService;
    private final HandlerExceptionResolver handlerExceptionResolver;


    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String headerValue = request.getHeader(JwtConstants.HEADER);
        if (headerValue == null || !headerValue.startsWith(JwtConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = headerValue.substring(JwtConstants.TOKEN_PREFIX.length());

        try {
            String username = jwtHelper.extractUsername(token);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (username == null || authentication != null) {
                filterChain.doFilter(request, response);
                return;
            }

            Optional<User> user = userService.findByUsername(username);
            if (user.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtHelper.isExpired(token)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user.get(),
                    null,
                    user.get().getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (JwtException jwtException) {
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