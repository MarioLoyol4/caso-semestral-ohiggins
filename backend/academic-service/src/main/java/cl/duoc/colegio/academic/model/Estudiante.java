package cl.duoc.colegio.academic.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "estudiantes")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String rut;

    @Column(nullable = false)
    private String nombre;

    private String segundoNombre;

    @Column(nullable = false)
    private String apellido;

    private String segundoApellido;

    @Column(unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;
}
