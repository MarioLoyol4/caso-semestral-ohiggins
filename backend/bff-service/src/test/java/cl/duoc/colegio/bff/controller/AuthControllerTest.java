package cl.duoc.colegio.bff.controller;


import cl.duoc.colegio.bff.client.MicroservicioClient;
import cl.duoc.colegio.bff.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MicroservicioClient client;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(jwtUtil, client);
        
        // Inyectar valores de configuración
        ReflectionTestUtils.setField(authController, "adminRut", "admin-rut");
        ReflectionTestUtils.setField(authController, "adminPassword", "admin-pass");
        ReflectionTestUtils.setField(authController, "academicUrl", "http://localhost:8081");
    }

    @Test
    @DisplayName("login debe retornar token para admin con credenciales correctas")
    void testLoginAdminValido() {
        // Arrange
        Map<String, String> credenciales = Map.of(
                "rut", "admin-rut",
                "password", "admin-pass"
        );

        when(jwtUtil.generarToken("admin-1", "ADMIN", List.of()))
                .thenReturn("token-jwt-admin");

        // Act
        ResponseEntity<?> respuesta = authController.login(credenciales);

        // Assert
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertTrue(respuesta.getBody().toString().contains("token"));
        verify(jwtUtil, times(1)).generarToken("admin-1", "ADMIN", List.of());
    }

    @Test
    @DisplayName("login debe retornar error si credenciales admin son incorrectas")
    void testLoginAdminInvalido() {
        // Arrange
        Map<String, String> credenciales = Map.of(
                "rut", "admin-rut",
                "password", "password-incorrecta"
        );

        // Act
        ResponseEntity<?> respuesta = authController.login(credenciales);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertTrue(respuesta.getBody().toString().contains("error"));
    }

    @Test
    @DisplayName("login debe consultar academic-service para otros usuarios")
    void testLoginUsuarioNoAdmin() {
        // Arrange
        Map<String, String> credenciales = Map.of(
                "rut", "12345678-9",
                "password", "password-123"
        );

        Map<String, Object> respuestaAcademic = Map.of(
                "referenciaId", "1",
                "rol", "ESTUDIANTE",
                "estudiantesACargo", List.of()
        );

        when(client.llamarConCircuitBreaker(
                "academic-service",
                "http://localhost:8081/api/auth-academic/validar",
                credenciales
        )).thenReturn(respuestaAcademic);

        when(jwtUtil.generarToken("1", "ESTUDIANTE", List.of()))
                .thenReturn("token-jwt-estudiante");

        // Act
        ResponseEntity<?> respuesta = authController.login(credenciales);

        // Assert
        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        verify(client, times(1)).llamarConCircuitBreaker(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("login debe retornar error si academic-service falla")
    void testLoginServicioNoDisponible() {
        // Arrange
        Map<String, String> credenciales = Map.of(
                "rut", "12345678-9",
                "password", "password-123"
        );

        when(client.llamarConCircuitBreaker(
                "academic-service",
                "http://localhost:8081/api/auth-academic/validar",
                credenciales
        )).thenThrow(new RuntimeException("Servicio no disponible"));

        // Act
        ResponseEntity<?> respuesta = authController.login(credenciales);

        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, respuesta.getStatusCode());
        assertTrue(respuesta.getBody().toString().contains("error"));
    }
}
