import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import NavBar from '../components/NavBar';
import { getDashboardCurso } from '../services/api';
import '../css/DashboardDocente.css';

function DashboardDocente() {
    const [cursoId, setCursoId] = useState(1);
    const [datos, setDatos] = useState(null);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        cargarCurso(cursoId);
    }, []);

    const cargarCurso = async (id) => {
        setCargando(true);
        setError('');
        setDatos(null);
        try {
            const data = await getDashboardCurso(id);
            if (data.error) { setError(data.error); return; }
            setDatos(data);
        } catch {
            setError('Error al cargar los datos del curso');
        } finally {
            setCargando(false);
        }
    };

    const handleBuscarCurso = (e) => {
        e.preventDefault();
        cargarCurso(cursoId);
    };

    return (
        <div className="docente-page">
            <NavBar nombreUsuario="Docente" />

            <main className="docente-main">
                <div className="docente-header">
                    <div>
                        <h1>Panel del Docente</h1>
                        <p>Consulta los estudiantes y comunicados de tu curso</p>
                    </div>

                    <form onSubmit={handleBuscarCurso} className="docente-buscar">
                        <label htmlFor="cursoId">Curso ID:</label>
                        <input
                            id="cursoId"
                            type="number"
                            min="1"
                            value={cursoId}
                            onChange={(e) => setCursoId(e.target.value)}
                        />
                        <button type="submit">Buscar</button>
                    </form>
                </div>

                {cargando && (
                    <div className="docente-cargando">
                        <div className="spinner"></div>
                        <p>Cargando información del curso...</p>
                    </div>
                )}

                {error && (
                    <div className="docente-error">{error}</div>
                )}

                {datos && !cargando && (
                    <div className="docente-contenido">

                        {/* Estudiantes del curso */}
                        <div className="docente-card docente-card-grande">
                            <div className="docente-card-header">
                                <h2>Estudiantes del Curso</h2>
                                <span className="docente-badge">
                                    {Array.isArray(datos.estudiantes) ? datos.estudiantes.length : 0} estudiantes
                                </span>
                            </div>
                            <div className="docente-card-body">
                                {datos.estudiantes?.disponible === false ? (
                                    <p className="docente-no-disponible">{datos.estudiantes.mensaje}</p>
                                ) : Array.isArray(datos.estudiantes) && datos.estudiantes.length > 0 ? (
                                    <table className="docente-tabla">
                                        <thead>
                                            <tr>
                                                <th>Nombre</th>
                                                <th>Apellido</th>
                                                <th>RUT</th>
                                                <th>Email</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {datos.estudiantes.map((e, i) => (
                                                <tr key={i}>
                                                    <td>{e.nombre}</td>
                                                    <td>{e.apellido}</td>
                                                    <td>{e.rut}</td>
                                                    <td>{e.email}</td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                ) : (
                                    <p className="docente-vacio">Sin estudiantes en este curso</p>
                                )}
                            </div>
                        </div>

                        {/* Comunicados */}
                        <div className="docente-card">
                            <div className="docente-card-header">
                                <h2>Comunicados</h2>
                            </div>
                            <div className="docente-card-body">
                                {datos.comunicados?.disponible === false ? (
                                    <p className="docente-no-disponible">{datos.comunicados.mensaje}</p>
                                ) : Array.isArray(datos.comunicados) && datos.comunicados.length > 0 ? (
                                    <div className="docente-comunicados">
                                        {datos.comunicados.map((c, i) => (
                                            <div key={i} className="comunicado-item">
                                                <h3>{c.titulo}</h3>
                                                <p>{c.contenido}</p>
                                                <span className="comunicado-fecha">
                                                    {c.fechaPublicacion?.split('T')[0]}
                                                </span>
                                            </div>
                                        ))}
                                    </div>
                                ) : (
                                    <p className="docente-vacio">Sin comunicados</p>
                                )}
                            </div>
                        </div>
                    </div>
                )}
            </main>
        </div>
    );
}

export default DashboardDocente;