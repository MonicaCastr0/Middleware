package filter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Order(1)
public class LoggerFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Exibe o método HTTP e a URL da requisição
        System.out.println("[LOGGER] Método: " + httpRequest.getMethod()
                + " | URL: " + httpRequest.getRequestURI());

        chain.doFilter(request, response); // chama o próximo middleware
    }
}