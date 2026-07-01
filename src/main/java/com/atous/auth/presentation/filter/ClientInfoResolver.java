package com.atous.auth.presentation.filter;

import jakarta.servlet.http.HttpServletRequest; import org.springframework.stereotype.Component;

@Component public class ClientInfoResolver { public String ipAddress(HttpServletRequest r){var f=r.getHeader("X-Forwarded-For"); return f!=null&&!f.isBlank()?f.split(",")[0].trim():r.getRemoteAddr();} public String userAgent(HttpServletRequest r){return r.getHeader("User-Agent");} }
