package co.edu.usco.peerlink.service;

import co.edu.usco.peerlink.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Set;

/**
 * Valida reglas minimas de seguridad para contrasenas de usuarios.
 */
@Service
public class PasswordPolicyService {
    private static final Set<String> COMMON_PASSWORDS = Set.of(
            "password123",
            "password1234",
            "admin12345",
            "admin123456",
            "qwerty1234",
            "qwerty12345",
            "peerlink123",
            "peerlink1234",
            "usuario123",
            "usuario1234",
            "estudiante123",
            "estudiante1234",
            "tutor12345",
            "temporal123",
            "temporal1234"
    );

    /**
     * Valida longitud, complejidad, contrasenas comunes y relacion con el correo.
     *
     * @param password contrasena en texto plano recibida desde el formulario
     * @param email correo asociado al usuario
     */
    public void validate(String password, String email) {
        if (password == null) {
            return;
        }

        if (!password.equals(password.trim())) {
            throw new BusinessException("error.usuario.passwordEspacios", HttpStatus.BAD_REQUEST);
        }

        String normalizedPassword = password.toLowerCase(Locale.ROOT);
        if (COMMON_PASSWORDS.contains(normalizedPassword)) {
            throw new BusinessException("error.usuario.passwordComun", HttpStatus.BAD_REQUEST);
        }

        if (email == null || email.isBlank()) {
            return;
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        String emailUser = normalizedEmail.contains("@")
                ? normalizedEmail.substring(0, normalizedEmail.indexOf("@"))
                : normalizedEmail;

        if (normalizedPassword.contains(normalizedEmail) || (emailUser.length() >= 4 && normalizedPassword.contains(emailUser))) {
            throw new BusinessException("error.usuario.passwordContieneCorreo", HttpStatus.BAD_REQUEST);
        }
    }
}
