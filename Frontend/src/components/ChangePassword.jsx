import { useAuth } from "../services/AuthContext";
import axios from "axios";
import React, { useState } from "react";
import { Form, Button, Container, Row, Col, InputGroup, Modal } from "react-bootstrap";
import '../components/LoginDetails.css';
import { FaEyeSlash } from "react-icons/fa";
import { BsEye } from "react-icons/bs";
import { useNavigate } from "react-router-dom";

function ChangePassword() {
  const [formData, setFormData] = useState({
    username: "",
    oldPassword: "",
    newPassword: "",
  });
  const [showPassword1, setShowPassword1] = useState(false);
  const [showPassword2, setShowPassword2] = useState(false);
  const [showSuccessModal, setShowSuccessModal] = useState(false); // State for success modal
  const [showErrorModal, setShowErrorModal] = useState(false); // State for error modal
  const [modalMessage, setModalMessage] = useState(""); // State for modal message
  const navigate = useNavigate();

  // Handle input change
  const handleChange = (event) => {
    setFormData({ ...formData, [event.target.name]: event.target.value });
  };

  // Handle form submission
  const handleGoheadSubmit = async (event) => {
    event.preventDefault();
    try {
      const response = await axios.post("http://localhost:8999/authenticate/changepassword", formData);
      setModalMessage("Password changed successfully!"); // Set success message
      setShowSuccessModal(true); // Show success modal
      setTimeout(() => {
        navigate("/login"); // Redirect to login after 2 seconds
      }, 2000);
    } catch (error) {
      setModalMessage("Invalid credentials. Please try again."); // Set error message
      setShowErrorModal(true); // Show error modal
    }
  };

  return (
    <Container fluid className="login-container">
      <Row className="justify-content-center align-items-center">
        <Col md={6} className="login-col">
          <div className="login-card">
            <h2 className="text-center">Change Password</h2>

            <Form onSubmit={handleGoheadSubmit}>
              {/* Username Field */}
              <Form.Group className="mb-3" controlId="formBasicUsername">
                <Form.Label>Username</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter username"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  required
                  style={{
                    borderRadius: "6px", // Reduced border-radius
                    height: "38px", // Consistent height
                    border: "1px solid #ced4da", // Consistent border
                  }}
                />
              </Form.Group>

              {/* Old Password Field */}
              <Form.Group className="mb-3" controlId="formBasicOldPassword">
                <Form.Label>Old Password</Form.Label>
                <InputGroup>
                  <Form.Control
                    type={showPassword1 ? "text" : "password"}
                    placeholder="Enter old password"
                    name="oldPassword"
                    value={formData.oldPassword}
                    onChange={handleChange}
                    required
                    style={{
                      borderRadius: "6px 0 0 6px", // Reduced border-radius
                      height: "38px", // Consistent height
                      border: "1px solid #ced4da", // Consistent border
                      borderRight: "none", // Remove right border for seamless alignment
                    }}
                  />
                  <Button
                    variant="outline-secondary"
                    onClick={() => setShowPassword1(!showPassword1)}
                    style={{
                      borderRadius: "0 6px 6px 0", // Reduced border-radius
                      height: "38px", // Consistent height
                      border: "1px solid #ced4da", // Consistent border
                      borderLeft: "none", // Remove left border for seamless alignment
                      backgroundColor: "white", // Match input field background
                      padding: "0 12px", // Add padding for better spacing
                    }}
                  >
                    {showPassword1 ? <FaEyeSlash /> : <BsEye />}
                  </Button>
                </InputGroup>
              </Form.Group>

              {/* New Password Field */}
              <Form.Group className="mb-3" controlId="formBasicNewPassword">
                <Form.Label>New Password</Form.Label>
                <InputGroup>
                  <Form.Control
                    type={showPassword2 ? "text" : "password"}
                    placeholder="Enter new password"
                    name="newPassword"
                    value={formData.newPassword}
                    onChange={handleChange}
                    required
                    style={{
                      borderRadius: "6px 0 0 6px", // Reduced border-radius
                      height: "38px", // Consistent height
                      border: "1px solid #ced4da", // Consistent border
                      borderRight: "none", // Remove right border for seamless alignment
                    }}
                  />
                  <Button
                    variant="outline-secondary"
                    onClick={() => setShowPassword2(!showPassword2)}
                    style={{
                      borderRadius: "0 6px 6px 0", // Reduced border-radius
                      height: "38px", // Consistent height
                      border: "1px solid #ced4da", // Consistent border
                      borderLeft: "none", // Remove left border for seamless alignment
                      backgroundColor: "white", // Match input field background
                      padding: "0 12px", // Add padding for better spacing
                    }}
                  >
                    {showPassword2 ? <FaEyeSlash /> : <BsEye />}
                  </Button>
                </InputGroup>
              </Form.Group>

              {/* Submit Button */}
              <div className="text-center">
                <Button
                  variant="primary"
                  type="submit"
                  className="w-100"
                  style={{
                    borderRadius: "6px", // Reduced border-radius
                    height: "38px", // Consistent height
                    border: "1px solid #ced4da", // Consistent border
                  }}
                >
                  Change Password
                </Button>
              </div>
            </Form>
          </div>
        </Col>
      </Row>

      {/* Success Modal */}
      <Modal show={showSuccessModal} onHide={() => setShowSuccessModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Success</Modal.Title>
        </Modal.Header>
        <Modal.Body>{modalMessage}</Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={() => setShowSuccessModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Error Modal */}
      <Modal show={showErrorModal} onHide={() => setShowErrorModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Error</Modal.Title>
        </Modal.Header>
        <Modal.Body>{modalMessage}</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowErrorModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
}

export default ChangePassword;
