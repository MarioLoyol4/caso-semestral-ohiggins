package cl.duoc.colegio.bff.controller;

import cl.duoc.colegio.bff.client.MicroservicioClient;
import cl.duoc.colegio.bff.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MicroservicioClient client;

    @Value("${admin.rut}")
    private String adminRut;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${services.academic.url}")
    private String academicUrl;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String rut = credenciales.get("rut");
        String password = credenciales.get("password");

        // Superadmin hardcodeado
        if (rut.equals(adminRut)) {
            if (!password.equals(adminPassword)) {
                return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));
            }
            return ResponseEntity.ok(Map.of(
                    "token", jwtUtil.generarToken("admin-1", "ADMIN", List.of()),
                    "rol", "ADMIN"
            ));
        }

        // Resto de usuarios — consultar academic-service
        try {
            Object respuesta = client.llamarConCircuitBreaker(
                    "academic-service",
                    academicUrl + "/api/auth-academic/validar",
                    credenciales  // enviamos rut + password
            );

            if (respuesta instanceof Map<?, ?> datos) {
                String rol = (String) datos.get("rol");
                String referenciaId = (String) datos.get("referenciaId");

                @SuppressWarnings("unchecked")
                List<Long> estudiantesACargo = (List<Long>) datos.get("estudiantesACargo");

                return ResponseEntity.ok(Map.of(
                        "token", jwtUtil.generarToken(referenciaId, rol, estudiantesACargo),
                        "rol", rol
                ));
            }

            return ResponseEntity.status(401).body(Map.of("error", "Credenciales incorrectas"));

        } catch (Exception e) {
            return ResponseEntity.status(503).body(Map.of("error", "Servicio no disponible"));
        }
    }
}