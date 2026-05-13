package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class AuthFilter implements Filter {

    @Value("${auth.token:token=123}")
    private String expectedToken;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if ("OPTIONS".equalsIgnoreCase(method)
                || path.startsWith("/actuator")
                || path.startsWith("/public")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.trim().equals(expectedToken)) {
            chain.doFilter(request, response);
            return;
        }

        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write("error: Unauthorized");
    }
}


