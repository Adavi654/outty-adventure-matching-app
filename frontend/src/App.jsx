import { useState } from 'react'
import './App.css'
import CreateProfile from './components/CreateProfile'
import UpdateProfile from './components/UpdateProfile'

function App() {
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

export default App
