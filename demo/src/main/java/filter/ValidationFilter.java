package filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@Order(3)
public class ValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Foca na rota POST /usuarios
        if ("POST".equalsIgnoreCase(httpRequest.getMethod()) && "/usuarios".equalsIgnoreCase(httpRequest.getRequestURI())) {

            CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(httpRequest);
            String body = new String(wrappedRequest.getInputStream().readAllBytes());

            // Verificação se os campos 'nome' e 'email' existem e se email tem '@'
            if (!body.contains("\"nome\"") || !body.contains("\"email\"") || !body.contains("@")) {

                //Interrupção com erro 400 se dados estiverem incompletos
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\":\"Dados invalidos ou incompletos, nome e email obrigatorios\"}");
                return; // Bloqueia a requisição
            }


            chain.doFilter(wrappedRequest, response);
            return;
        }

        chain.doFilter(request, response);
    }
}