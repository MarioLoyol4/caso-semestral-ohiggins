package fullstack.caso_semestral.academic.services;

import fullstack.caso_semestral.academic.models.Curso;
import fullstack.caso_semestral.academic.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Anotacion para avisar a spring boot que aqui esta la logica principal
@Service
public class CursoService {

    // Autowired conecta automaticamente con el repository
    @Autowired
    private CursoRepository cursoRepository;

    // Metodo para obtener todos los cursos
    public List<Curso> obtenerTodosLosCursos() {
        return cursoRepository.findAll();
    }

    // Metodo para guardar curso
    public Curso guardarCurso(Curso curso) {
        return cursoRepository.save(curso);
    }
}
