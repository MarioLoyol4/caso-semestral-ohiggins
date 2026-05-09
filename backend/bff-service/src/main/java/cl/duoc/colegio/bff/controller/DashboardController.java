package cl.duoc.colegio.bff.controller;

import cl.duoc.colegio.bff.client.MicroservicioClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bff/dashboard")
public class DashboardController {

    private final MicroservicioClient client;

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
    public Map<String, Object> obtenerResumenEstudiante(@PathVariable Long id) {
        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("notas", client.llamarSeguro("academic-service", academicUrl + "/api/notas/estudiante/" + id));

        respuesta.put("historialAsistencias", client.llamarSeguro("attendance-service", attendanceUrl + "/api/asistencias/estudiante/" + id));

        respuesta.put("comunicados", client.llamarSeguro("communication-service", communicationUrl + "/api/comunicados"));

        return respuesta;
    }
}
