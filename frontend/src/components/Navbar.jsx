import { Link, useLocation, useNavigate } from 'react-router-dom';
import '../styles/Navbar.css';

const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const currentUserId = localStorage.getItem('userId');

  const handleLogout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userId');
    navigate('/login');
  };

  return (
    <nav className="navbar" aria-label="Main navigation">
      <div className="navbar__inner">
        <div className="navbar__brand">
          <span className="navbar__logo">Outty</span>
        </div>
        <ul className="navbar__actions nav-links">
          {currentUserId ? (
            <li>
              <button
                type="button"
                onClick={handleLogout}
                className="logout-button"
              >
                Logout
              </button>
            </li>
          ) : (
            <li>
              <Link
                to="/login"
                className={`navbar__link${
                  location.pathname === '/login' ? ' navbar__link--active' : ''
                }`}
              >
                Login
              </Link>
            </li>
          )}
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;
