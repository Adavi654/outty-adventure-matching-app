import { Routes, Route, Navigate } from 'react-router-dom'
import './App.css'
import LoginPage from './components/LoginPage'
import RegisterForm from './components/RegisterForm'
import ProfileManager from './components/ProfileManager'

function App() {
  return (
    <Routes>
      <Route path="/register" element={<RegisterForm />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/profile" element={<ProfileManager />} />
      <Route path="/" element={<Navigate to="/register" />} />
      <Route path="*" element={<Navigate to="/register" />} />
    </Routes>
  )
}

export default App
