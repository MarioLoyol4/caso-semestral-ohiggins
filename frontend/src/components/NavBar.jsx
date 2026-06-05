import { useNavigate } from "react-router-dom";
import {cerrarSesion, obtenerRol} from "../services/api";
import '../css/NavBar.css';

function NavBar({nombreUsuario}) {
    const navigate = useNavigate();
    const rol = obtenerRol();

    const handleLogout = () => {
        cerrarSesion();
        navigate("/login");
    };

    const etiquetaRol = {
        ADMIN: "Administrador",
        DOCENTE: "Docente",
        APODERADO: "Apoderado",
        ESTUDIANTE: "Estudiante"
    };

    return (
        <nav className="navbar">
            <div className="navbar-izquierda">
                <div className="navbar-logo">O</div>
                <div className="navbar-info">
                    <span className="navbar-colegio">Colegio O'Higgins</span>
                    <span className="navbar-subtitulo">Portal digital</span>
                </div>
            </div>

            <div className="navbar-derecha">
                <div className="navbar-usuario">
                    <div className="navbar-avatar">
                        {nombreUsuario ? nombreUsuario.charAt(0).toUpperCase() : "?"}
                    </div>
                    <div className="navbar-usuario-info">
                        <span className="navbar-nombre">
                            {nombreUsuario || "Usuario"}
                        </span>
                        <span className="navbar-rol">
                            {etiquetaRol[rol] || rol}
                        </span>
                    </div>
                </div>
                <button className="navbar-logout" onClick={handleLogout}>
                    Cerrar sesión
                </button>
            </div>
        </nav>
    );
}

export default NavBar;