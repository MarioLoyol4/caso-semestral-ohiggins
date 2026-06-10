import { render, screen, waitFor, fireEvent } from '@testing-library/react';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import DashboardDocente from './DashboardDocente';
import { getDashboardCurso } from '../services/api';
import { BrowserRouter } from 'react-router-dom';

vi.mock('../services/api', () => ({
    getDashboardCurso: vi.fn(),
    obtenerRol: vi.fn(),
}));

describe('DashboardDocente Component', () => {
    beforeEach(() => {
        vi.clearAllMocks();
    });

    it('debe mostrar estado de carga inicial', () => {
        getDashboardCurso.mockReturnValue(new Promise(() => {})); // Que se quede cargando
        render(<BrowserRouter><DashboardDocente /></BrowserRouter>);
        expect(screen.getByText(/Cargando información del curso.../i)).toBeInTheDocument();
    });

    it('debe mostrar error de la API', async () => {
        getDashboardCurso.mockResolvedValueOnce({ error: 'Error al buscar' });
        render(<BrowserRouter><DashboardDocente /></BrowserRouter>);
        
        await waitFor(() => {
            expect(screen.getByText('Error al buscar')).toBeInTheDocument();
        });
    });

    it('debe cargar y mostrar los estudiantes del curso e interactuar con la búsqueda', async () => {
        getDashboardCurso.mockResolvedValueOnce({
            estudiantes: [{ nombre: 'Juan', apellido: 'Perez', rut: '111', email: 'j@j.cl' }]
        });
        
        render(<BrowserRouter><DashboardDocente /></BrowserRouter>);

        // Comprobamos la carga inicial
        await waitFor(() => {
            expect(screen.getByText('Juan')).toBeInTheDocument();
            expect(screen.getByText('Perez')).toBeInTheDocument();
        });

        // Busqueda de otro curso
        getDashboardCurso.mockResolvedValueOnce({
            estudiantes: [{ nombre: 'Maria', apellido: 'Gomez', rut: '222', email: 'm@m.cl' }]
        });

        fireEvent.change(screen.getByLabelText(/Curso ID:/i), { target: { value: '2' } });
        fireEvent.click(screen.getByRole('button', { name: /Buscar/i }));

        await waitFor(() => {
            expect(getDashboardCurso).toHaveBeenCalledWith("2");
            expect(screen.getByText('Maria')).toBeInTheDocument();
        });
    });
});