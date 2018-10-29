package com.chineseall.config.web;

import com.alibaba.fastjson.JSONObject;
import com.chineseall.util.base.string.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author gy1zc3@gmail.com
 * Created by zacky on 16:08.
 */
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer {

    Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Value("${token.url}")
    private String tokenUrl;
    private final static String SESSION_KEY = "user";
    private final static String COOKIE_TOKEN = "cbdb_atoken";
    private final static String LOGIN_URL = "/login";

    @Bean
    public SecurityInterceptor getSecurityInterceptor() {
        return new SecurityInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(getSecurityInterceptor());

        // 排除配置
        addInterceptor.excludePathPatterns("/favicon.ico");
        addInterceptor.excludePathPatterns("/static**");
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/login**");

        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }

    private class SecurityInterceptor extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
                throws Exception {
            HttpSession session = request.getSession();
            if (session.getAttribute(SESSION_KEY) != null) {
                return true;
            }

            String token = WebUtils.getCookieVal(COOKIE_TOKEN);
            if (StringUtils.isNotBlank(token) && isValidateToken(token, session)) {
                logger.info("[preHandle][" + request + "]" + "[" + request.getMethod()
                        + "]" + request.getRequestURI() + getParameters(request));
                return true;
            }

            response.sendRedirect(LOGIN_URL);
            return false;
        }
    }


    private String getParameters(HttpServletRequest request) {
        StringBuilder posted = new StringBuilder();
        Enumeration<?> e = request.getParameterNames();
        if (e != null) {
            posted.append("?");
        }
        assert e != null;
        while (e.hasMoreElements()) {
            if (posted.length() > 1) {
                posted.append("&");
            }
            String curr = (String) e.nextElement();
            posted.append(curr).append("=");
            if (curr.contains("password")
                    || curr.contains("pass")
                    || curr.contains("pwd")) {
                posted.append("*****");
            } else {
                posted.append(request.getParameter(curr));
            }
        }
        String ip = request.getHeader("X-FORWARDED-FOR");
        String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (ipAddr != null && !"".equals(ipAddr)) {
            posted.append("&_ip=" + ipAddr);
        }
        return posted.toString();
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            logger.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }

    private boolean isValidateToken(String token, HttpSession session) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(tokenUrl);
        get.setHeader("Authorization", "Bearer" + token);
        get.setHeader("Cookie", "x-access-token=" + token);
        CloseableHttpResponse response = httpClient.execute(get);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity, "utf-8");
        response.close();
        httpClient.close();

        Map<String, Object> result = JSONObject.parseObject(responseBody, Map.class);
        if (result.get("data") == null) {
            return false;
        }

        Map<String, Object> data = (Map) result.get("data");
        Map<String, Object> sysUser = (Map) data.get("sysUser");
        if (sysUser == null) {
            return false;
        }

        SecurityUser authUser = new SecurityUser();
        authUser.setUsername(sysUser.get("username") + "");
        authUser.setRealName(sysUser.get("realName") + "");

        List<String> roles = (List) data.get("roles");
        List<String> permissions = (List) data.get("permissions");
        for (String role : roles) {
            authUser.addRole(role);
        }
        for (String permission : permissions) {
            authUser.addPermission(permission);
        }

        session.setAttribute(SESSION_KEY, authUser);

        return true;
    }
}
