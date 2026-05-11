package cl.duoc.colegio.bff.controller;

import cl.duoc.colegio.bff.client.MicroservicioClient;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bff/dashboard")
public class DashboardController {

    @Autowired
    private MicroservicioClient client;

    @Value("${services.academic.url}")
    private String academicUrl;

    @Value("${services.attendance.url}")
    private String attendanceUrl;

    @Value("${services.communication.url}")
    private String communicationUrl;

    public DashboardController(MicroservicioClient client) {
        this.client = client;
    }

    @GetMapping("/estudiante/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE', 'APODERADO')")
    public ResponseEntity<?> obtenerResumenEstudiante(
            @PathVariable Long id,
            Authentication auth) {
        String rol = auth.getAuthorities().iterator().next().getAuthority();

        if (rol.equals("ROLE_APODERADO")) {
            @SuppressWarnings("unchecked")
            List<Integer> estudiantesACargo = (List<Integer>) auth.getDetails();
            boolean tieneAcceso = estudiantesACargo.stream()
                    .anyMatch(eId -> eId.longValue() == id);

            if (!tieneAcceso) {
                return ResponseEntity.status(403)
                        .body(Map.of("error", "No tienes accesos a los datos de este estudiante"));
            }
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("notas",
                client.llamarSeguro("academic-service", academicUrl + "/api/notas/estudiante/" + id));
        respuesta.put("anotaciones" ,
                client.llamarSeguro("attendance-service", attendanceUrl + "/api/anotaciones/estudiante/" + id));
        respuesta.put("comunicados" ,
                client.llamarSeguro("communication-service", communicationUrl + "/api/comunicados/destinatario/APODERADOS"));

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/miperfil")
    @PreAuthorize("hasRole('ESTUDIANTE')")
    public ResponseEntity<?> miPerfil(Authentication auth) {
        String estudianteId = (String) auth.getPrincipal();

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("notas",
                client.llamarSeguro("academic-service", academicUrl + "/api/notas/estudiante/" + estudianteId));
        respuesta.put("historialAsistencias",
                client.llamarSeguro("attendance-service", attendanceUrl + "/api/asistencias/estudiante/" + estudianteId));
        respuesta.put("comunicados",
                client.llamarSeguro("communication-service", communicationUrl + "/api/comunicados/destinatario/APODERADOS"));

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/curso/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCENTE')")
    public ResponseEntity<?> resumenCurso(
            @PathVariable Long id,
            Authentication authentication) {
        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("estudiantes",
                client.llamarSeguro("academic-service", academicUrl + "/api/estudiantes/curso/" + id));
        respuesta.put("comunicados",
                client.llamarSeguro("communication-service", communicationUrl + "/api/comunicados"));

        return ResponseEntity.ok(respuesta);
    }

}
