package fullstack.caso_semestral.academic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// Entity y table transforman esta clase en una tabla llamada curso en la base de datos
@Entity
@Table(name = "cursos")
// Data es de la libreria de lombok, ahorra lineas de codigos de getters y setters
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    // Id y GeneratedValue configuran la llave primaria
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Columna con campo obligatorio
    @Column(nullable = false, length = 50)
    private String nivel; // 1ero Basico, 2do Medio, etc.

    // Columna con campo obligatorio
    @Column(nullable = false, length = 1)
    private String letra; // A, B, C, etc.

    // Columna con campo obligatorio
    @Column(name = "año_academico", nullable = false)
    private Integer añoAcademico; // 2026, 2010, etc
}
