package fullstack.caso_semestral.academic.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// Entity y table transforman esta clase en una tabla llamada profesores en la base de datos
@Entity
@Table(name = "profesores")
// Data es la libreria de lombok, ahorra lineas de codigos de getters y setters
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profesor {

    // id y generatedValue configuran la llave primaria
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

    // columna con campo obligatorio y no se puede repetir
    @Column(nullable = false, unique = false, length = 150)
    private String correo;


}
