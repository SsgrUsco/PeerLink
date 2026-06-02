package co.edu.usco.peerlink;

import co.edu.usco.peerlink.security.AuthenticatedUser;
import co.edu.usco.peerlink.security.CustomUserDetailsService;
import co.edu.usco.peerlink.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAuthorizationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Test
    void swaggerRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void studentCannotAccessAdminUsers() throws Exception {
        mockMvc.perform(get("/api/usuarios").cookie(authCookie("ESTUDIANTE")))
                .andExpect(status().isForbidden());
    }

    @Test
    void tutorCannotAccessAdminUsers() throws Exception {
        mockMvc.perform(get("/api/usuarios").cookie(authCookie("TUTOR")))
                .andExpect(status().isForbidden());
    }

    @Test
    void studentCannotDownloadAdminReports() throws Exception {
        mockMvc.perform(get("/api/reportes/admin/usuarios.pdf").cookie(authCookie("ESTUDIANTE")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCannotUseStudentOnlyReport() throws Exception {
        mockMvc.perform(get("/api/reportes/mis-reservas.pdf").cookie(authCookie("ADMIN")))
                .andExpect(status().isForbidden());
    }

    private Cookie authCookie(String rol) {
        String correo = rol.toLowerCase() + "@peerlink.test";
        AuthenticatedUser user = new AuthenticatedUser(1, correo, "password", rol, "Usuario " + rol);
        when(userDetailsService.loadUserByUsername(eq(correo))).thenReturn(user);
        return new Cookie("AUTH_TOKEN", jwtUtil.generateToken(user));
    }
}
