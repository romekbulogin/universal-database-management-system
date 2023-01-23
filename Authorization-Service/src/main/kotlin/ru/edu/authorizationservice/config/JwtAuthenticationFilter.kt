package ru.edu.authorizationservice.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import ru.edu.authorizationservice.service.JwtService

@Component
class JwtAuthenticationFilter(private val jwtService: JwtService, private val userDetailsService: UserDetailsService) :
    OncePerRequestFilter() {

    private val logger = KotlinLogging.logger { }
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val authHeader: String? = request.getHeader("Authorization")
        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }
        if (authHeader == "" || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return
        }
        val jwt = authHeader.substring(7)
        val userEmail = jwtService.extractUsername(jwt)

        if (userEmail != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = this.userDetailsService.loadUserByUsername(userEmail)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(userEmail, null, userDetails.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authToken
            }
        }
        filterChain.doFilter(request, response)
    }
}