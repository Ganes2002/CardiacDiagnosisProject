import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { Button, Form, Alert, Container, Row, Col, Modal } from 'react-bootstrap';

// Validation schema using yup
const schema = yup.object().shape({
  username: yup
    .string()
    .matches(/^[a-zA-Z0-9_-]+$/, 'Username can only contain letters, numbers, underscores, and dashes')
    .min(3, 'Username must be at least 3 characters')
    .max(20, 'Username must be at most 20 characters')
    .required('Username is mandatory'),
  email: yup
    .string()
    .matches(
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
      'Email should be valid'
    )
    .required('Email is mandatory'),
  firstName: yup
    .string()
    .matches(/^[A-Za-z]+$/, 'First name should contain only alphabets')
    .required('First name is mandatory'),
  lastName: yup
    .string()
    .matches(/^[A-Za-z]+$/, 'Last name should contain only alphabets')
    .required('Last name is mandatory'),
  dob: yup
    .date()
    .required('Date of birth is mandatory')
    .nullable()
    .transform((curr, orig) => (orig === '' ? null : curr)),
  gender: yup.string().required('Gender is mandatory'),
  department: yup.string().required('Department is mandatory'),
});

function UpdateUser() {
  const username = sessionStorage.getItem("username"); // Get username from URL
  const [userData, setUserData] = useState(null);
  const [showModal, setShowModal] = useState(false); // Modal visibility state
  const [modalMessage, setModalMessage] = useState(""); // Message to show in the modal
  const [modalTitle, setModalTitle] = useState(""); // Title of the modal
  const { register, handleSubmit, setValue, formState: { errors } } = useForm({
    resolver: yupResolver(schema),
  });
  const navigate = useNavigate();

  // Fetch user data when component mounts or username changes
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await axios.get(`http://localhost:8999/user/get/${username}`);
        setUserData(response.data);

        // Populate the form fields with existing user data
        for (const key in response.data) {
          setValue(key, response.data[key]);
        }
      } catch (error) {
        console.log(error);
      }
    };

    if (username) {
      fetchUserData();
    }
  }, [username, setValue]);

  const onSubmit = async (data) => {
    try {
      await axios.put(`http://localhost:8999/user/update`, data);
      setModalTitle("Update Successful");
      setModalMessage("Your user information has been updated successfully.");
      setShowModal(true); // Show the success modal
      setTimeout(() => navigate("/patientlist"), 2000); // Redirect after a short delay
    } catch (error) {
      const message = error.response ? error.response.data : error.message;
      setModalTitle("Update Failed");
      setModalMessage(message);
      setShowModal(true); // Show the error modal
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center min-vh-100">
      <div className="w-100 p-4 border rounded shadow-sm bg-light">
        <h2 className="text-center mb-4">Update User</h2>
        {userData ? (
          <Form onSubmit={handleSubmit(onSubmit)}>
            {/* Username field */}
            <Row className="mb-3 g-3">
              <Col xs={12} sm={6}>
                <Form.Group controlId="username">
                  <Form.Label>Username</Form.Label>
                  <Form.Control
                    type="text"
                    {...register('username')}
                    placeholder="Enter username"
                    disabled
                  />
                  {errors.username && <Alert variant="danger">{errors.username.message}</Alert>}
                </Form.Group>
              </Col>
              {/* Email field */}
              <Col xs={12} sm={6}>
                <Form.Group controlId="email">
                  <Form.Label>Email</Form.Label>
                  <Form.Control
                    type="email"
                    {...register('email')}
                  />
                  {errors.email && <Alert variant="danger">{errors.email.message}</Alert>}
                </Form.Group>
              </Col>
            </Row>

            {/* First Name and Last Name fields */}
            <Row className="mb-3 g-3">
              <Col xs={12} sm={6}>
                <Form.Group controlId="firstName">
                  <Form.Label>First Name</Form.Label>
                  <Form.Control
                    type="text"
                    {...register('firstName')}
                  />
                  {errors.firstName && <Alert variant="danger">{errors.firstName.message}</Alert>}
                </Form.Group>
              </Col>
              <Col xs={12} sm={6}>
                <Form.Group controlId="lastName">
                  <Form.Label>Last Name</Form.Label>
                  <Form.Control
                    type="text"
                    {...register('lastName')}
                  />
                  {errors.lastName && <Alert variant="danger">{errors.lastName.message}</Alert>}
                </Form.Group>
              </Col>
            </Row>

            {/* Date of Birth and Gender fields */}
            <Row className="mb-3 g-3">
              <Col xs={12} sm={6}>
                <Form.Group controlId="dob">
                  <Form.Label>Date of Birth</Form.Label>
                  <Form.Control
                    type="date"
                    {...register('dob')}
                  />
                  {errors.dob && <Alert variant="danger">{errors.dob.message}</Alert>}
                </Form.Group>
              </Col>
              <Col xs={12} sm={6}>
                <Form.Group controlId="gender">
                  <Form.Label>Gender</Form.Label>
                  <Form.Control as="select" {...register('gender')}>
                    <option value="">Select Gender</option>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                    <option value="Other">Other</option>
                  </Form.Control>
                  {errors.gender && <Alert variant="danger">{errors.gender.message}</Alert>}
                </Form.Group>
              </Col>
            </Row>

            {/* Department field */}
            <Row className="mb-3 g-3">
              <Col xs={12}>
                <Form.Group controlId="department">
                  <Form.Label>Department</Form.Label>
                  <Form.Control
                    type="text"
                    {...register('department')}
                  />
                  {errors.department && <Alert variant="danger">{errors.department.message}</Alert>}
                </Form.Group>
              </Col>
            </Row>

            {/* Submit button */}
            <Button variant="primary" type="submit" className="w-100 mt-3">
              Update User
            </Button>
          </Form>
        ) : (
          <Alert variant="warning">User not found</Alert>
        )}
      </div>

      {/* Modal for Success/Failure message */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{modalTitle}</Modal.Title>
        </Modal.Header>
        <Modal.Body>{modalMessage}</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
}

export default UpdateUser;
