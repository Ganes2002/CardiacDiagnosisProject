import { useAuth } from "../services/AuthContext";
import axios from "axios";
import React, { useState } from "react";
import { Form, Button, Container, Row, Col, InputGroup, Alert, Modal } from "react-bootstrap";
import { FaEyeSlash } from "react-icons/fa";
import { BsEye } from "react-icons/bs";
import { useNavigate, useLocation, Link } from "react-router-dom";
import "../components/LoginDetails.css";

function LoginDetails() {
  const { login } = useAuth();
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [isEditing, setIsEditing] = useState(false); // Manage the state for "Forgot Password" form
  const [showPassword, setShowPassword] = useState(false);
  const [isInvalid, setIsInvalid] = useState(false);
  const [getUser, setGetUser] = useState("");
  const [showLoginErrorModal, setShowLoginErrorModal] = useState(false); // State for login error modal
  const [showForgotPasswordErrorModal, setShowForgotPasswordErrorModal] = useState(false); // State for forgot password error modal
  const [showLoginSuccessModal, setShowLoginSuccessModal] = useState(false); // State for login success modal
  const [showForgotPasswordSuccessModal, setShowForgotPasswordSuccessModal] = useState(false); // State for forgot password success modal
  const [tempPassword, setTempPassword] = useState("");
  const navigate = useNavigate();
  const location = useLocation();

  // Handle login form change
  const handleChange = (event) => {
    setFormData({ ...formData, [event.target.name]: event.target.value });
  };

  // Handle submit login form
  const handleSubmit = async (event) => {
    event.preventDefault();
    axios
      .post("http://localhost:8999/authenticate/login", formData)
      .then((response) => {
        login(response.data, formData.username);
        setShowLoginSuccessModal(true); // Show login success modal
        const from = location.state?.from?.pathname || "/patientlist";
        setTimeout(() => {
          navigate(from, { replace: true }); // Redirect after showing the modal
        }, 2000); // Delay navigation to allow the user to see the success message
      })
      .catch((error) => {
        setIsInvalid(true);
        setShowLoginErrorModal(true); // Show login error modal
      });
  };

  // Handle forgot password action
  const handleForgotPasswordClick = async (e) => {
    e.preventDefault();
    setIsEditing(true); // Show "forgot password" form
  };

  // Handle forgot password form submission
  const handleForgotPasswordSubmit = async (e) => {
    e.preventDefault();
    if (!getUser) {
      alert("Please enter a valid username!");
      return;
    }

    // API request to check if username is valid
    axios
      .post(`http://localhost:8999/authenticate/forget/${getUser}`)
      .then((response) => {
        console.log(response);
        setTempPassword(response.data);
        setShowForgotPasswordSuccessModal(true); // Show forgot password success modal
        setTimeout(() => {
          navigate("/changepassword"); // Redirect after showing the modal
        }, 2000); // Delay navigation to allow the user to see the success message
      })
      .catch((err) => {
        setShowForgotPasswordErrorModal(true); // Show forgot password error modal
      });
  };

  return (
    <Container fluid className="login-container">
      <Row className="justify-content-center align-items-center">
        <Col md={6} className="login-col">
          <div className="login-card">
            <h2 className="text-center">Login</h2>

            {/* Main Login Form */}
            {!isEditing ? (
              <Form onSubmit={handleSubmit}>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                  <Form.Label>Username</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter username"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    required
                    style={{
                      borderRadius: "6px", // Less rounded corners
                      height: "38px", // Consistent height
                      border: "1px solid #ced4da", // Consistent border
                    }}
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="formBasicPassword">
                  <Form.Label>Password</Form.Label>
                  <InputGroup>
                    <Form.Control
                      type={showPassword ? "text" : "password"}
                      placeholder="Enter password"
                      name="password"
                      value={formData.password}
                      onChange={handleChange}
                      required
                      style={{
                        borderRadius: "6px 0 0 6px", // Less rounded corners on the left
                        borderRight: "none", // Remove right border to blend with the button
                        height: "38px", // Consistent height
                        border: "1px solid #ced4da", // Consistent border
                      }}
                    />
                    <Button
                      variant="outline-secondary"
                      onClick={() => setShowPassword(!showPassword)}
                      style={{
                        borderRadius: "0 6px 6px 0", // Less rounded corners on the right
                        borderLeft: "none", // Remove left border to blend with the input field
                        backgroundColor: "white",
                        display: "flex",
                        alignItems: "center",
                        padding: "0 12px",
                        height: "38px", // Consistent height
                        border: "1px solid #ced4da", // Consistent border
                      }}
                    >
                      {showPassword ? <FaEyeSlash /> : <BsEye />}
                    </Button>
                  </InputGroup>
                </Form.Group>

                <div className="text-center">
                  <Button
                    variant="primary"
                    type="submit"
                    className="w-100"
                    style={{
                      borderRadius: "6px", // Less rounded corners
                      height: "38px", // Consistent height
                      border: "1px solid #ced4da", // Consistent border
                    }}
                  >
                    Login
                  </Button>
                </div>
              </Form>
            ) : (
              // Forgot Password Form
              <Form onSubmit={handleForgotPasswordSubmit}>
                <Form.Group className="mb-3" controlId="formBasicUsername">
                  <Form.Label>Enter Username</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter username"
                    value={getUser}
                    onChange={(e) => setGetUser(e.target.value)}
                    required
                    style={{
                      borderRadius: "6px", // Less rounded corners
                      height: "38px", // Consistent height
                      border: "1px solid #ced4da", // Consistent border
                    }}
                  />
                </Form.Group>
                <div className="text-center">
                  <Button
                    variant="primary"
                    type="submit"
                    className="w-100"
                    style={{
                      borderRadius: "6px", // Less rounded corners
                      height: "38px", // Consistent height
                      border: "1px solid #ced4da", // Consistent border
                    }}
                  >
                    Submit
                  </Button>
                </div>
              </Form>
            )}

            <div className="text-center mt-3">
              {!isEditing ? (
                <Link to="/changepassword" className="text-muted" onClick={handleForgotPasswordClick}>
                  Forgot Password?
                </Link>
              ) : (
                <Button
                  variant="secondary"
                  onClick={() => setIsEditing(false)}
                  style={{
                    borderRadius: "6px", // Less rounded corners
                    height: "38px", // Consistent height
                    border: "1px solid #ced4da", // Consistent border
                  }}
                >
                  Back to Login
                </Button>
              )}
            </div>

            <div className="text-center mt-3">
              <p className="text-muted">
                Don't have an account? <a href="/register" className="text-primary">Register here</a>
              </p>
            </div>
          </div>
        </Col>
      </Row>

      {/* Login Error Modal */}
      <Modal show={showLoginErrorModal} onHide={() => setShowLoginErrorModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Login Error</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>Invalid username or password. Please try again.</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowLoginErrorModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Forgot Password Error Modal */}
      <Modal show={showForgotPasswordErrorModal} onHide={() => setShowForgotPasswordErrorModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Forgot Password Error</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>You entered an incorrect username. Please try again.</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowForgotPasswordErrorModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Login Success Modal */}
      <Modal show={showLoginSuccessModal} onHide={() => setShowLoginSuccessModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Login Successful</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>You have successfully logged in!</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={() => setShowLoginSuccessModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Forgot Password Success Modal */}
      <Modal show={showForgotPasswordSuccessModal} onHide={() => setShowForgotPasswordSuccessModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Password Reset</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>Your temporary password is share to your Email. Use it to log in and change your password.</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={() => setShowForgotPasswordSuccessModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
}

export default LoginDetails;
