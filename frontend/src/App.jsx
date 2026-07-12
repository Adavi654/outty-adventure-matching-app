import { useState } from 'react'
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'
import './App.css'
import LoginPage from './components/LoginPage'
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
    <Router>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/profile" element={<ProfilePages />} />
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="*" element={<Navigate to="/login" />} />
      </Routes>
    </Router>
  )
}

export default App
