package cl.duoc.colegio.academic.controller;

import cl.duoc.colegio.academic.model.Evaluacion;
import cl.duoc.colegio.academic.repository.EvaluacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {


    private final EvaluacionRepository evaluacionRepository;

    @GetMapping
    public List<Evaluacion> listar() {
        return evaluacionRepository.findAll();
    }

    @PostMapping
    public Evaluacion crearEvaluacion(@RequestBody Evaluacion evaluacion) {
        return evaluacionRepository.save(evaluacion);
    }
}
