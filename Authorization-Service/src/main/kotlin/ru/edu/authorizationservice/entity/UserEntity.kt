package ru.edu.authorizationservice.entity

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "_user")
class UserEntity : UserDetails {
    @Id
    @GeneratedValue
    private var id: Int? = null
    private var username: String? = null
    private var email: String? = null
    private var password: String? = null
    private var isActivated: Boolean? = null

    @Enumerated(EnumType.STRING)
    private var role: Role? = null

    fun setUsername(username: String) {
        this.username = username
    }

    fun setEmail(email: String) {
        this.email = email
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun setIsActivated(isActivated: Boolean) {
        this.isActivated = isActivated
    }

    fun setRole(role: Role) {
        this.role = role
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableListOf(SimpleGrantedAuthority(role?.name))

    override fun getPassword(): String? = password

    override fun getUsername(): String? = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
