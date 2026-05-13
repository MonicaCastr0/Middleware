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

            boolean temNome = body.contains("\"nome\"");
            boolean temEmail = body.contains("\"email\"");

            // 2. Verifica se os valores estão vazios ou com espaços: "" ou "  "
            boolean nomeVazio = body.contains("\"nome\":\"\"") || body.contains("\"nome\": \"\"") || body.contains("\"nome\": \" \"");
            boolean emailVazio = body.contains("\"email\":\"\"") || body.contains("\"email\": \"\"") || body.contains("\"email\": \" \"");

            boolean temArroba = body.contains("@");

            //Se não tem as chaves OU se os valores estão vazios OU não tem @
            if (!temNome || !temEmail || nomeVazio || emailVazio || !temArroba) {
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.setContentType("application/json");
                httpResponse.getWriter().write("{\"error\":\"Dados invalidos ou incompletos, nome e email obrigatorios e preenchidos\"}");
                return;
            }

            chain.doFilter(wrappedRequest, response);
            return;
        }

        chain.doFilter(request, response);
    }
}