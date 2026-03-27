package fullstack.caso_semestral.academic.repositories;

import fullstack.caso_semestral.academic.models.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignaturaRepository extends JpaRepository <Asignatura, Long> {
}
