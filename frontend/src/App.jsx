import { Routes, Route, Navigate, useLocation } from 'react-router-dom'
import './App.css'
import Navbar from './components/Navbar'
import LoginPage from './components/LoginPage'
import RegisterForm from './components/RegisterForm'
import ProfileManager from './components/ProfileManager'
import PotentialMatches from './components/PotentialMatches'

function App() {
  const location = useLocation();
  const hideNavbar = ['/register', '/login', '/'].includes(location.pathname);

  return (
    <>
      {!hideNavbar && <Navbar />}
      <Routes>
        <Route path="/register" element={<RegisterForm />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/profile" element={<ProfileManager />} />
        <Route path="/matches" element={<PotentialMatches />} />
        <Route path="/" element={<Navigate to="/register" />} />
        <Route path="*" element={<Navigate to="/register" />} />
      </Routes>
    </>
  );
}

export default App
