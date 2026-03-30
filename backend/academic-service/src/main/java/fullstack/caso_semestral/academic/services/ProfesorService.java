package fullstack.caso_semestral.academic.services;

import fullstack.caso_semestral.academic.models.Profesor;
import fullstack.caso_semestral.academic.repositories.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorService {

    // Autowired conecta automaticamente con el repository
    @Autowired
    private ProfesorRepository profesorRepository;

    // Metodo para obtener todos los profesores
    public List<Profesor> obtenerTodosLosProfesores() { return profesorRepository.findAll(); }

    // Metodo para guardar los profesores
    public Profesor guardarProfesor(Profesor profesor) { return profesorRepository.save(profesor); }
}
