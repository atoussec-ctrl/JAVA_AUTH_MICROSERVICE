package com.atous.auth.presentation.filter;

import jakarta.servlet.*; import jakarta.servlet.http.HttpServletResponse; import org.springframework.stereotype.Component; import java.io.IOException; import java.util.UUID;

@Component public class RequestIdFilter implements Filter { public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException { if(res instanceof HttpServletResponse http) http.setHeader("X-Request-Id", UUID.randomUUID().toString()); chain.doFilter(req,res);} }
