package cl.duoc.colegio.bff.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/estudiante/{id}")
    @CircuitBreaker(name = "colegioCB", fallbackMethod = "fallbackResumenEstudiante")
    public Map<String, Object> obtenerResumenEstudiante(@PathVariable Long id) {
        Map<String, Object> respuestaFinal = new HashMap<>();

        String urlNotas = "http://localhost:8081/api/notas/estudiante/" + id;
        Object notasDelAlumno = restTemplate.getForObject(urlNotas, Object.class);


        String urlAsistencias = "http://localhost:8082/api/asistencias/estudiante/" + id;
        Object asistenciaDelAlumno = restTemplate.getForObject(urlAsistencias, Object.class);

        respuestaFinal.put("notas", notasDelAlumno);
        respuestaFinal.put("historialAsistencias", asistenciaDelAlumno);

        return  respuestaFinal;
    }

    public Map<String, Object> fallbackResumenEstudiante(Long id, Throwable t) {
        Map<String, Object> respuestaCaida = new HashMap<>();

        respuestaCaida.put("mensaje", "Estamos experimentando alta demanda. Algunos datos academicos podrian no estar disponibles en este momento.");
        respuestaCaida.put("estudianteId", id);
        respuestaCaida.put("estado", "PARCIALMENTE_DISPONIBLE");

        System.out.println("Error capturado por Circuit Breaker: " + t.getMessage());

        return respuestaCaida;
    }
}
