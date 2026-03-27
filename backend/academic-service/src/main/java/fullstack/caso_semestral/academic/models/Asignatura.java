package fullstack.caso_semestral.academic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entity y table transforman esta clase en una tabla llamada asignaturas en la base de datos
@Entity
@Table(name = "asignaturas")
// Data es la libreria de lombok, ahorra lineas de codigos de getters y setters
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asignatura {

    // id y generatedValue configuran la llave primaria
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    // Columna con campo obligatorio
    @Column(nullable = false, length = 100)
    private String nombre; // Matematicas, Lenguaje, etc.
}
