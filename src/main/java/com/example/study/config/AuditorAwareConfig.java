package com.example.study.config;

import java.util.Collection;
import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@EnableJpaAuditing
@Configuration
public class AuditorAwareConfig implements AuditorAware<Long> {

    public Optional<Long> getCurrentAuditor() {

        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(authentication -> {

                    Collection<? extends GrantedAuthority> auths = authentication.getAuthorities();
                    boolean isAnonymous = auths.contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));

                    if (!isAnonymous) {
//                        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//                        return userDetails.userId;
                    }

                    return null;

                });
    }
}
