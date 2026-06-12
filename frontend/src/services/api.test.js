import { describe, it, expect, beforeEach, vi } from 'vitest';
import {
    guardarToken,
    obtenerToken,
    obtenerRol,
    cerrarSesion,
    login,
    getDashboardEstudiante,
    getMiPerfil,
    getDashboardCurso,
    getEstudiantesApoderado
} from './api';

const BFF_URL = 'http://localhost:9090';

// Mock estricto y transparente de SessionStorage
const mockSessionStorage = (() => {
    let store = {};
    return {
        getItem: vi.fn(key => store[key] || null),
        setItem: vi.fn((key, value) => { store[key] = value ? value.toString() : ''; }),
        removeItem: vi.fn(key => { delete store[key]; }),
        clear: vi.fn(() => { store = {}; })
    };
})();

// Reemplazamos la variable global de window por nuestro mock
Object.defineProperty(window, 'sessionStorage', {
    value: mockSessionStorage
});

// Mock de la función global de fetch
global.fetch = vi.fn();

describe('API Services (api.js)', () => {

    beforeEach(() => {
        // Limpiar storage y llamadas de los mock antes de cada prueba
        mockSessionStorage.clear();
        vi.clearAllMocks();
    });

    describe('Autenticación y Sesión (SessionStorage)', () => {
        
        it('debe guardar el token y el rol correctamente', () => {
            guardarToken('mi-super-token', 'DOCENTE');
            
            expect(mockSessionStorage.setItem).toHaveBeenCalledWith('token', 'mi-super-token');
            expect(mockSessionStorage.setItem).toHaveBeenCalledWith('rol', 'DOCENTE');
        });

        it('debe obtener el token almacenado', () => {
            mockSessionStorage.setItem('token', 'token-para-leer');
            
            const tokenLeido = obtenerToken();
            expect(tokenLeido).toBe('token-para-leer');
            expect(mockSessionStorage.getItem).toHaveBeenCalledWith('token');
        });

        it('debe obtener el rol almacenado', () => {
            mockSessionStorage.setItem('rol', 'ADMIN');
            
            const rolLeido = obtenerRol();
            expect(rolLeido).toBe('ADMIN');
            expect(mockSessionStorage.getItem).toHaveBeenCalledWith('rol');
        });

        it('debe eliminar token y rol al cerrar sesión', () => {
            cerrarSesion();
            
            expect(mockSessionStorage.removeItem).toHaveBeenCalledWith('token');
            expect(mockSessionStorage.removeItem).toHaveBeenCalledWith('rol');
        });
    });

    describe('Peticiones HTTP (Fetch) al BFF', () => {

        beforeEach(() => {
            // Simulamos que el endpoint siempre devuelve { success: true }
            fetch.mockResolvedValue({
                json: vi.fn().mockResolvedValue({ success: true })
            });
            
            // Simulamos que hay un token de sesión presente para los tests con header
            mockSessionStorage.getItem.mockImplementation((key) => {
                if(key === 'token') return 'mock-jwt-token';
                return null;
            });
        });

        it('login realiza correctamente una petición POST con los datos correctos', async () => {
            const respuesta = await login('12345678-9', 'mypass');

            expect(fetch).toHaveBeenCalledWith(`${BFF_URL}/api/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ rut: '12345678-9', password: 'mypass' })
            });
            expect(respuesta.success).toBe(true);
        });

        it('getDashboardEstudiante solicita información de un id incluyendo el Auth Header', async () => {
            await getDashboardEstudiante(15);

            expect(fetch).toHaveBeenCalledWith(`${BFF_URL}/api/bff/dashboard/estudiante/15`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer mock-jwt-token'
                }
            });
        });

        it('getMiPerfil solicita la información del perfil propio', async () => {
            await getMiPerfil();

            expect(fetch).toHaveBeenCalledWith(`${BFF_URL}/api/bff/dashboard/miperfil`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer mock-jwt-token'
                }
            });
        });

        it('getDashboardCurso solicita la información de un curso', async () => {
            await getDashboardCurso(3);

            expect(fetch).toHaveBeenCalledWith(`${BFF_URL}/api/bff/dashboard/curso/3`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer mock-jwt-token'
                }
            });
        });

        it('getEstudiantesApoderado llama a la ruta correcta', async () => {
            await getEstudiantesApoderado(9);

            expect(fetch).toHaveBeenCalledWith(`${BFF_URL}/api/academic/apoderados/9/estudiantes`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer mock-jwt-token'
                }
            });
        });
    });
});