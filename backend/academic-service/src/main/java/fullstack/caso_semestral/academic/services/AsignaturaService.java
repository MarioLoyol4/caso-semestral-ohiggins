package fullstack.caso_semestral.academic.services;

import fullstack.caso_semestral.academic.models.Asignatura;
import fullstack.caso_semestral.academic.repositories.AsignaturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsignaturaService {

    // Autowired conecta automaticamente con el repository
    @Autowired
    private AsignaturaRepository asignaturaRepository;

    // Metodo para obtener todas las asignaturas
    public List<Asignatura> obtenerTodasLasAsignaturas() { return asignaturaRepository.findAll(); }

    // Metodo para guardar las asignaturas
    public Asignatura guardarAsignatura(Asignatura asignatura) { return asignaturaRepository.save(asignatura); }

}
