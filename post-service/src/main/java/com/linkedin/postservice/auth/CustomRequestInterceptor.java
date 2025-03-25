package com.linkedin.postservice.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Component
public class CustomRequestInterceptor implements HandlerInterceptor {

    // intercept request before going to controller (returns true to proceed to controller else false)
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("X-User-Id");
        AuthContextHolder.setCurrentUserId(Long.valueOf(userId));

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    // called once the request is successful and before sending the response from controller (not called if exception occurs)
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    // this method is called no matter what
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthContextHolder.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
