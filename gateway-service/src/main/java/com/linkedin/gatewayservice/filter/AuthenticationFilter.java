package com.linkedin.gatewayservice.filter;

import com.linkedin.gatewayservice.service.JWTService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;


/**
 * Route Specific API-Gateway Filter to get the request and check if Authorization token is present in header or not
 * If yes, the parse and get the userId and add to the request header
 * Else set response status code to UNAUTHORIZED
 */
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger log = LogManager.getLogger(AuthenticationFilter.class);
    private final JWTService jwtService;

    public AuthenticationFilter(JWTService jwtService) {
        super(Config.class);
        this.jwtService = jwtService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("Auth request: {}", exchange.getRequest().getURI());

            final String tokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if(null == tokenHeader || !tokenHeader.startsWith("Bearer")){
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            final String token = tokenHeader.split("Bearer ")[1];

            try {
                String userId = jwtService.getUserIdFromToken(token);

                ServerWebExchange mutatedRequest = exchange.mutate()
                        .request(req -> req.header("X-User-Id", userId))
                        .build();

                return chain.filter(mutatedRequest);

            } catch (JwtException e) {
                log.error("JWT Exception {}", e.getLocalizedMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }

    public static class Config{

    }
}
