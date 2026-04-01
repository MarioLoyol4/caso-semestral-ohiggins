package fullstack.caso_semestral.academic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entity y table transforman esta clase en una tabla llamada estudiantes en la base de datos
@Entity
@Table(name = "estudiantes")
// Data es la libreria de lombok, ahorra lineas de codigos de getters y setters
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // columna con campo obligatorio y no se puede repetir
    @Column(nullable = false, unique = true, length = 12)
    private String rut;

    // Columna con campo obligatorio
    @Column(nullable = false, length = 100)
    private String nombre;

    // Columna con campo obligatorio
    @Column(nullable = false, length = 100)
    private String apellido;
}
