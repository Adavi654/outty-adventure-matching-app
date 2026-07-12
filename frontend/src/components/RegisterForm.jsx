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

  const handleChange = (event) => {
    const { name, value } = event.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setStatusMessage("");

    try {
      const response = await registerUser(formData);

      setStatusMessage(response.message || "Registration successful.");

      setFormData({
        firstName: "",
        lastName: "",
        email: "",
        password: "",
      });

      navigate("/login");
    } catch (error) {
      setStatusMessage(
        error.response?.data?.message || error.message || "Unable to register.",
      );
    }
  };

  return (
    <div className="register-container">
      <div className="register-card">
        <h2>Create Account</h2>
        <form className="register-form" onSubmit={handleSubmit}>
          <div className="form-field">
            <label htmlFor="firstName">First Name</label>
            <input
              id="firstName"
              name="firstName"
              type="text"
              value={formData.firstName}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-field">
            <label htmlFor="lastName">Last Name</label>
            <input
              id="lastName"
              name="lastName"
              type="text"
              value={formData.lastName}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-field">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-field">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
          </div>

          <button className="register-button" type="submit">
            Register
          </button>

          {statusMessage && <p className="status-message">{statusMessage}</p>}

          <p className="login-link">
            Already have an account? <Link to="/login">Login</Link>
          </p>
        </form>
      </div>
    </div>
  );
}

export default RegisterForm;
