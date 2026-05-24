package co.edu.usco.peerlink.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthenticatedUser implements UserDetails {

    private final Integer id;
    private final String correo;
    private final String password;
    private final String rol;
    private final String nombreCompleto;
    private final List<SimpleGrantedAuthority> authorities;

    public AuthenticatedUser(Integer id, String correo, String password, String rol, String nombreCompleto) {
        this.id = id;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    public Integer getId() {
        return id;
    }

    public String getRol() {
        return rol;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
