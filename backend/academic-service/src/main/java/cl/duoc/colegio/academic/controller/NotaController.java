package cl.duoc.colegio.academic.controller;

import cl.duoc.colegio.academic.model.Nota;
import cl.duoc.colegio.academic.repository.NotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notas")
@RequiredArgsConstructor
public class NotaController {

    private final NotaRepository notaRepository;

    @GetMapping
    public List<Nota> listarTodas() {
        return notaRepository.findAll();
    }

    @PostMapping
    public Nota guardar(@RequestBody Nota nota) {
        return notaRepository.save(nota);
    }

    @GetMapping("/estudiante/{id}")
    public List<Nota> listarPorEstudiante(@PathVariable Long id){
        return notaRepository.findByEstudianteId(id);
    }
}