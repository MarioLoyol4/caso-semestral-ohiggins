package cl.duoc.colegio.bff.client;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class MicroservicioClient {

    private final RestTemplate restTemplate;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public MicroservicioClient(RestTemplate restTemplate,
                               CircuitBreakerRegistry circuitBreakerRegistry) {
        this.restTemplate = restTemplate;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    // GET con Circuit Breaker
    public Object llamarConCircuitBreaker(String nombreServicio, String url) {
        var cb = circuitBreakerRegistry.circuitBreaker(nombreServicio);
        return cb.executeSupplier(() -> restTemplate.getForObject(url, Object.class));
    }

    // POST con Circuit Breaker
    public Object llamarConCircuitBreaker(String nombreServicio, String url, Object body) {
        var cb = circuitBreakerRegistry.circuitBreaker(nombreServicio);
        return cb.executeSupplier(() -> restTemplate.postForObject(url, body, Object.class));
    }

    // GET seguro con fallback
    public Object llamarSeguro(String nombreServicio, String url) {
        try {
            return llamarConCircuitBreaker(nombreServicio, url);
        } catch (CallNotPermittedException e) {
            return Map.of(
                    "disponible", false,
                    "servicio", nombreServicio,
                    "mensaje", "Servicio temporalmente no disponible"
            );
        } catch (Exception e) {
            return Map.of(
                    "disponible", false,
                    "servicio", nombreServicio,
                    "mensaje", "Error al contactar el servicio: " + e.getMessage()
            );
        }
    }
}