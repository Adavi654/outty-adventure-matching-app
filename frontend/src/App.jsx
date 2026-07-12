import './App.css';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './components/LoginPage';
import ProfileForm from './components/ProfileForm';

function App() {
  return (
    <Router>
      <Routes>

        <Route path="/login" element={<LoginPage />} />
        
        <Route path="/profile" element={<ProfileForm />} />

        <Route path="/" element={<Navigate to="/login" />} />
        
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  );
}

export default App;