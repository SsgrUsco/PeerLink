package co.edu.usco.peerlink.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Adaptador de usuario autenticado usado por Spring Security.
 */
public class AuthenticatedUser implements UserDetails {

    private final Integer id;
    private final String correo;
    private final String password;
    private final String rol;
    private final String nombreCompleto;
    private final List<SimpleGrantedAuthority> authorities;

    /**
     * Construye el principal autenticado a partir de las tablas satelite del usuario.
     *
     * @param id identificador del usuario
     * @param correo correo de acceso
     * @param password contrasena cifrada
     * @param rol rol autorizado
     * @param nombreCompleto nombre mostrado en paneles
     */
    public AuthenticatedUser(Integer id, String correo, String password, String rol, String nombreCompleto) {
        this.id = id;
        this.correo = correo;
        this.password = password;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    /**
     * @return identificador interno del usuario
     */
    public Integer getId() {
        return id;
    }

    /**
     * @return rol funcional del usuario
     */
    public String getRol() {
        return rol;
    }

    /**
     * @return nombre completo del usuario
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    @Override
    /**
     * @return autoridad Spring Security derivada del rol
     */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    /**
     * @return contrasena cifrada usada por el proveedor de autenticacion
     */
    public String getPassword() {
        return password;
    }

    @Override
    /**
     * @return correo usado como nombre de usuario
     */
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
