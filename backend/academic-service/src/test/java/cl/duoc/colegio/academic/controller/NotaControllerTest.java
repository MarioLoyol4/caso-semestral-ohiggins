package cl.duoc.colegio.academic.controller;

import cl.duoc.colegio.academic.model.Nota;
import cl.duoc.colegio.academic.repository.NotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotaControllerTest {

    @Mock
    private NotaRepository notaRepository;

    private NotaController notaController;

    @BeforeEach
    void setUp() {
        notaController = new NotaController(notaRepository);
    }

    @Test
    @DisplayName("listarTodas debe retornar todas las notas")
    void testListarTodas() {
        when(notaRepository.findAll())
                .thenReturn(List.of(new Nota(), new Nota()));

        List<Nota> resultado = notaController.listarTodas();

        assertEquals(2, resultado.size());
    }

    @Test
    @DisplayName("guardar debe persistir la nota y retornarla")
    void testGuardar() {
        Nota nota = new Nota();
        nota.setValor(6.5);

        when(notaRepository.save(nota)).thenReturn(nota);

        Nota resultado = notaController.guardar(nota);

        assertEquals(6.5, resultado.getValor());
        verify(notaRepository, times(1)).save(nota);
    }

    @Test
    @DisplayName("listarPorEstudiante debe retornar las notas del estudiante")
    void testListarPorEstudiante() {
        Nota nota = new Nota();
        nota.setValor(5.0);

        when(notaRepository.findByEstudianteId(1L)).thenReturn(List.of(nota));

        List<Nota> resultado = notaController.listarPorEstudiante(1L);

        assertEquals(1, resultado.size());
        assertEquals(5.0, resultado.get(0).getValor());
    }

    @Test
    @DisplayName("listarPorEstudiante debe retornar lista vacia si no hay notas")
    void testListarPorEstudianteSinNotas() {
        when(notaRepository.findByEstudianteId(99L)).thenReturn(List.of());

        List<Nota> resultado = notaController.listarPorEstudiante(99L);

        assertTrue(resultado.isEmpty());
    }
}