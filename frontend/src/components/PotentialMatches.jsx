import { useEffect, useRef, useState } from 'react'
import { Link } from 'react-router-dom'
import { getPotentialMatches } from '../services/matchApi'
import '../styles/PotentialMatches.css'

const SWIPE_THRESHOLD = 100
const DECISION_DELAY = 350

function formatInterestedIn(value) {
  if (!value) {
    return 'Not provided'
  }

  return value.charAt(0) + value.slice(1).toLowerCase()
}

function getRequestError(status) {
  if (status === 404) {
    return {
      message: 'Complete your profile before viewing potential matches.',
      retryable: false,
    }
  }

  if (status === 401 || status === 403) {
    return {
      message: 'Your session has expired. Please log in again.',
      retryable: false,
    }
  }

  if (status === 503) {
    return {
      message:
        'Potential matches are temporarily unavailable. Please try again later.',
      retryable: true,
    }
  }

  return {
    message: 'Unable to load potential matches. Please try again.',
    retryable: true,
  }
}

function PotentialMatches() {
  const userId = localStorage.getItem('userId')
  const token = localStorage.getItem('authToken')
  const [matches, setMatches] = useState([])
  const [currentIndex, setCurrentIndex] = useState(0)
  const [isLoading, setIsLoading] = useState(Boolean(userId && token))
  const [error, setError] = useState(null)
  const [retryCount, setRetryCount] = useState(0)
  const [decision, setDecision] = useState(null)
  const [dragOffset, setDragOffset] = useState(0)
  const [isDragging, setIsDragging] = useState(false)
  const [imageFailed, setImageFailed] = useState(false)
  const dragStartRef = useRef(0)
  const decisionTimerRef = useRef(null)

  useEffect(() => {
    if (!userId || !token) {
      return
    }

    let cancelled = false

    getPotentialMatches(userId, token)
      .then((potentialMatches) => {
        if (!cancelled) {
          setMatches(potentialMatches)
          setCurrentIndex(0)
          setImageFailed(false)
        }
      })
      .catch((requestError) => {
        if (!cancelled) {
          setError(getRequestError(requestError.status))
        }
      })
      .finally(() => {
        if (!cancelled) {
          setIsLoading(false)
        }
      })

    return () => {
      cancelled = true
    }
  }, [userId, token, retryCount])

  useEffect(() => {
    return () => {
      if (decisionTimerRef.current) {
        window.clearTimeout(decisionTimerRef.current)
      }
    }
  }, [])

  const advanceCard = (nextDecision) => {
    if (decision) {
      return
    }

    setIsDragging(false)
    setDragOffset(0)
    setDecision(nextDecision)

    decisionTimerRef.current = window.setTimeout(() => {
      setCurrentIndex((index) => index + 1)
      setDecision(null)
      setImageFailed(false)
    }, DECISION_DELAY)
  }

  const handlePointerDown = (event) => {
    if (!event.isPrimary || decision) {
      return
    }

    dragStartRef.current = event.clientX
    setIsDragging(true)
    event.currentTarget.setPointerCapture(event.pointerId)
  }

  const handlePointerMove = (event) => {
    if (!isDragging || !event.isPrimary) {
      return
    }

    setDragOffset(event.clientX - dragStartRef.current)
  }

  const handlePointerUp = () => {
    if (!isDragging) {
      return
    }

    if (dragOffset <= -SWIPE_THRESHOLD) {
      advanceCard('reject')
      return
    }

    if (dragOffset >= SWIPE_THRESHOLD) {
      advanceCard('interested')
      return
    }

    setIsDragging(false)
    setDragOffset(0)
  }

  const handlePointerCancel = () => {
    setIsDragging(false)
    setDragOffset(0)
  }

  const handleRetry = () => {
    setError(null)
    setIsLoading(true)
    setRetryCount((count) => count + 1)
  }

  if (!userId || !token) {
    return (
      <div className="matches-page">
        <h1>Potential Matches</h1>
        <p>Please log in to view potential matches.</p>
        <Link className="matches-link" to="/login">
          Go to login
        </Link>
      </div>
    )
  }

  if (isLoading) {
    return (
      <div className="matches-page">
        <h1>Potential Matches</h1>
        <p>Finding potential matches...</p>
      </div>
    )
  }

  if (error) {
    return (
      <div className="matches-page">
        <h1>Potential Matches</h1>
        <p className="matches-error">{error.message}</p>
        {error.retryable ? (
          <button
            className="matches-retry-button"
            type="button"
            onClick={handleRetry}
          >
            Try Again
          </button>
        ) : (
          <Link
            className="matches-link"
            to={error.message.startsWith('Your session') ? '/login' : '/profile'}
          >
            {error.message.startsWith('Your session')
              ? 'Go to login'
              : 'Back to profile'}
          </Link>
        )}
      </div>
    )
  }

  if (matches.length === 0) {
    return (
      <div className="matches-page">
        <h1>Potential Matches</h1>
        <p>No potential matches are available right now.</p>
        <Link className="matches-link" to="/profile">
          Back to profile
        </Link>
      </div>
    )
  }

  if (currentIndex >= matches.length) {
    return (
      <div className="matches-page">
        <h1>Potential Matches</h1>
        <p>You’ve reviewed all potential matches.</p>
        <Link className="matches-link" to="/profile">
          Back to profile
        </Link>
      </div>
    )
  }

  const match = matches[currentIndex]
  const location = [match.city, match.state, match.country]
    .filter(Boolean)
    .join(', ')
  const previewDecision =
    dragOffset <= -40
      ? 'reject'
      : dragOffset >= 40
        ? 'interested'
        : null
  const visibleDecision = decision || previewDecision

  return (
    <div className="matches-page">
      <h1>Potential Matches</h1>
      <p className="matches-success">Potential matches found.</p>
      <p className="matches-progress">
        Profile {currentIndex + 1} of {matches.length}
      </p>

      <article
        className={`match-card ${
          isDragging ? 'match-card--dragging' : ''
        } ${decision ? `match-card--${decision}` : ''}`}
        style={
          isDragging
            ? {
                transform: `translateX(${dragOffset}px) rotate(${
                  dragOffset / 20
                }deg)`,
              }
            : undefined
        }
        onPointerDown={handlePointerDown}
        onPointerMove={handlePointerMove}
        onPointerUp={handlePointerUp}
        onPointerCancel={handlePointerCancel}
      >
        {visibleDecision && (
          <div
            className={`match-decision match-decision--${visibleDecision}`}
            aria-live="polite"
          >
            {visibleDecision === 'reject' ? 'Reject' : 'Interested'}
          </div>
        )}

        <div className="match-photo-wrapper">
          {!imageFailed ? (
            <img
              className="match-photo"
              src={match.photoUrl}
              alt={`Fictional demo profile for ${match.firstName}`}
              draggable="false"
              onError={() => setImageFailed(true)}
            />
          ) : (
            <div
              className="match-photo-fallback"
              role="img"
              aria-label={`Photo unavailable for ${match.firstName}`}
            >
              Photo unavailable
            </div>
          )}
        </div>

        <h2>{match.firstName}</h2>
        <p className="match-location">{location}</p>

        <dl className="match-details">
          <div>
            <dt>Gender</dt>
            <dd>{match.gender || 'Not provided'}</dd>
          </div>
          <div>
            <dt>Birth date</dt>
            <dd>{match.birthDate || 'Not provided'}</dd>
          </div>
          <div>
            <dt>Interested in</dt>
            <dd>{formatInterestedIn(match.interestedIn)}</dd>
          </div>
          <div>
            <dt>Relationship goal</dt>
            <dd>{match.relationshipGoal}</dd>
          </div>
        </dl>

        <p className="match-bio">{match.bio}</p>

        {match.demoData && (
          <p className="match-demo-notice">
            Demo profile — fictional data for the Sprint 2 demonstration.
          </p>
        )}
      </article>

      <div className="match-actions">
        <button
          className="match-action match-action--reject"
          type="button"
          disabled={Boolean(decision)}
          onClick={() => advanceCard('reject')}
        >
          Reject
        </button>
        <button
          className="match-action match-action--interested"
          type="button"
          disabled={Boolean(decision)}
          onClick={() => advanceCard('interested')}
        >
          Interested
        </button>
      </div>

      <Link className="matches-link" to="/profile">
        Back to profile
      </Link>
    </div>
  )
}

export default PotentialMatches
