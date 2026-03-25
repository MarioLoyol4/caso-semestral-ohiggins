package fullstack.caso_semestral.academic.repositories;

import fullstack.caso_semestral.academic.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

}
