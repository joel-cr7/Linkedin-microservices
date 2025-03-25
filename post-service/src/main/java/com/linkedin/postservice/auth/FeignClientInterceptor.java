package com.linkedin.postservice.auth;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        Long userId = AuthContextHolder.getCurrentUserId();
        if(userId != null){
            requestTemplate.header("X-User-Id", userId.toString());
        }
    }

}
