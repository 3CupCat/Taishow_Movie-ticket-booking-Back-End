package org.web.filter;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

@Component
public class JwtFilter implements Filter {

    @Value("${jwt.secret}")
    private String secret;

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 處理 CORS 頭
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        // 如果是 OPTIONS 預檢請求，直接返回
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 放行不需要驗證的路徑
        String path = httpRequest.getRequestURI();
        if ("/user/login".equals(path) || "/user/register".equals(path) ||"/user/sendVerification".equals(path) ||"/user/verifyCode".equals(path) ||"/user/checkAccountEmail".equals(path)) {
            chain.doFilter(request, response);
            return;
        }

        final String requestTokenHeader = httpRequest.getHeader("Authorization");

        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7); // Bearer 後的空格也要去掉
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(jwtToken)
                        .getBody();
                Integer userId = Integer.parseInt(claims.getSubject());
                httpRequest.setAttribute("userId", userId);
            } catch (IllegalArgumentException e) {
                logger.error("Unable to get JWT Token", e);
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to get JWT Token");
                return;
            } catch (ExpiredJwtException e) {
                logger.error("JWT Token has expired", e);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token has expired");
                return;
            } catch (SignatureException e) {
                logger.error("JWT Token signature validation failed", e);
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT Token signature validation failed");
                return;
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT Token does not begin with Bearer String");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 清理资源
    }
}
