package ru.edu.authorizationservice.filter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter : OncePerRequestFilter() {

    private val secretKey: String = "LOH1337"
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath.equals("/api/login")) {
            filterChain.doFilter(request, response)
        } else {
            val authorizationHeader = request.getHeader(AUTHORIZATION)
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    val decodedJWT =
                        JWT.require(Algorithm.HMAC256(secretKey.toByteArray())).build()
                            .verify(authorizationHeader.substring("Bearer ".length))

                    val username = decodedJWT.subject
                    val authorities = mutableListOf<SimpleGrantedAuthority>()

                    decodedJWT.claims["roles"]?.asArray(String::class.java)?.forEach {
                        authorities.add(SimpleGrantedAuthority(it))
                    }
                    val authenticationToken = UsernamePasswordAuthenticationToken(username, null, authorities)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                    filterChain.doFilter(request, response)
                } catch (ex: Exception) {
                    logger.info(ex.message)
                    with(response) {
                        setHeader("error", ex.message)
                        status = HttpStatus.FORBIDDEN.value()
                        contentType = MediaType.APPLICATION_JSON_VALUE
                    }
                    ObjectMapper().writeValue(response.outputStream, hashMapOf("error" to ex.message))
                }
            } else {
                filterChain.doFilter(request, response)
            }
        }
    }
}