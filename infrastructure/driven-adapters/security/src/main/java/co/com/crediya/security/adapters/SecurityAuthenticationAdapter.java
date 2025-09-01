package co.com.crediya.security.adapters;

import co.com.crediya.port.token.SecurityAuthenticationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SecurityAuthenticationAdapter implements SecurityAuthenticationPort {

    public Mono<String> getCurrentContextToken() {
        return getAuthenticationToken().map(OAuth2Token::getTokenValue);
    }

    @Override
    public Mono<String> getTokenSubject() {
        return getAuthenticationToken().map( JwtClaimAccessor::getSubject );
    }

    private Mono<Jwt> getAuthenticationToken() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter( JwtAuthenticationToken.class::isInstance )
                .map( JwtAuthenticationToken.class::cast )
                .map( jwtAuthenticationToken -> {
                    log.info( "Getting authentication token {}", jwtAuthenticationToken.getToken() );
                    return jwtAuthenticationToken.getToken();
                } );
    }


}
