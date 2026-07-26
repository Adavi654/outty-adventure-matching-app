import { useState } from "react";
import { registerUser } from "../services/authApi";
import { Link, useNavigate } from "react-router-dom";
import "../styles/Register.css";

function RegisterForm() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
  });

  const [statusMessage, setStatusMessage] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const handleChange = (event) => {
    const { name, value } = event.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsSubmitting(true);
    setStatusMessage("");

    try {
      await registerUser(formData);

      setStatusMessage("Registration successful! Redirecting to login...");

      setFormData({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
      });

      setTimeout(() => {
        navigate("/login");
      }, 2000);
    } catch (error) {
      setStatusMessage(
        error.response?.data?.message ||
          error.message ||
          "Unable to register. Please try again.",
      );
      setIsSubmitting(false);
    }
  };

  const isSuccessMessage = statusMessage.startsWith("Registration successful");

  return (
    <div className="auth-page">
      <div className="auth-card">
        <header className="auth-card__header">
          <h2 className="auth-card__title">Create Account</h2>
          <p className="auth-card__subtitle">
            Join Outty and connect with outdoor adventurers
          </p>
        </header>

        <form className="auth-form" onSubmit={handleSubmit}>
          <div className="auth-field">
            <label htmlFor="firstName">First Name</label>
            <input
              id="firstName"
              name="firstName"
              type="text"
              value={formData.firstName}
              onChange={handleChange}
              required
              disabled={isSubmitting}
              autoComplete="given-name"
            />
          </div>

          <div className="auth-field">
            <label htmlFor="lastName">Last Name</label>
            <input
              id="lastName"
              name="lastName"
              type="text"
              value={formData.lastName}
              onChange={handleChange}
              required
              disabled={isSubmitting}
              autoComplete="family-name"
            />
          </div>

          <div className="auth-field">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              required
              disabled={isSubmitting}
              autoComplete="email"
            />
          </div>

          <div className="auth-field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              required
              disabled={isSubmitting}
              autoComplete="new-password"
            />
          </div>

          <button className="auth-button" type="submit" disabled={isSubmitting}>
            {isSubmitting ? "Creating account..." : "Register"}
          </button>

          {statusMessage && (
            <p
              className={`auth-message ${
                isSuccessMessage
                  ? "auth-message--success"
                  : "auth-message--error"
              }`}
              role={isSuccessMessage ? "status" : "alert"}
            >
              {statusMessage}
            </p>
          )}

          <p className="auth-footer">
            Already have an account? <Link to="/login">Login</Link>
          </p>
        </form>
      </div>
    </div>
  );
}

export default RegisterForm;
