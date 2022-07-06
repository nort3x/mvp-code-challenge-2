package com.github.nort3x.backendchallenge2.configuration

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.core.session.SessionRegistry
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.session.HttpSessionEventPublisher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.NegatedRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)

class SecurityConfig {

    /**
     *  according to spring-docs component based security-configuration is the standard way in favor of
     *  [WebSecurityConfigurerAdapter]
     */


    @Bean
    fun passwordHashEncoder(): PasswordEncoder = BCryptPasswordEncoder()


    @Bean
    fun filterChain(
        http: HttpSecurity,
        sessionReg: SessionRegistry
    ): SecurityFilterChain {

        http.authorizeRequests()
        http.invoke {
            authorizeRequests {
                authorize(anyRequest,permitAll)
            }

            csrf {
                disable()
            }
        }
        return http.build()
    }

    @Bean
    fun sessionRegistry(): SessionRegistry {
        return SessionRegistryImpl()
    }

    @Bean
    fun httpSessionEventPublisher(): ServletListenerRegistrationBean<*> {    //(5)
        return ServletListenerRegistrationBean(HttpSessionEventPublisher())
    }

    @Bean
    fun authManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}