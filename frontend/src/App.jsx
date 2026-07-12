import { useState } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import './App.css'
import LoginPage from './components/LoginPage'
import RegisterForm from './components/RegisterForm'
import CreateProfile from './components/CreateProfile'
import UpdateProfile from './components/UpdateProfile'

function ProfilePages() {
  const [currentPage, setCurrentPage] = useState('create')

  return (
    <div>
      <header>
        <h1>Outty Adventure Matching</h1>

        <nav>
          <button
            type="button"
            className={currentPage === 'create' ? 'active' : ''}
            onClick={() => setCurrentPage('create')}
          >
            Create Profile
          </button>

          <button
            type="button"
            className={currentPage === 'update' ? 'active' : ''}
            onClick={() => setCurrentPage('update')}
          >
            Update Profile
          </button>
        </nav>
      </header>

      {currentPage === 'create' ? <CreateProfile /> : <UpdateProfile />}
    </div>
  )
}

function App() {
  return (
    <Routes>
      <Route path="/register" element={<RegisterForm />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/profile" element={<ProfilePages />} />
      <Route path="/" element={<Navigate to="/register" />} />
      <Route path="*" element={<Navigate to="/register" />} />
    </Routes>
  )
}

export default App
