import axios from 'axios';
import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { Button, Form, Alert, Container, Row, Col, Modal } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

// Validation schema using yup
const schema = yup.object().shape({
  username: yup.string().required('Username is mandatory'),
  email: yup
    .string()
    .matches(
      /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
      'Email should be valid'
    )
    .required('Email is mandatory'),
  password: yup
    .string()
    .min(8, 'Password must be at least 8 characters')
    .matches(
      /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
      'Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character'
    )
    .required('Password is mandatory'),
  firstName: yup.string().required('First name is mandatory'),
  lastName: yup.string().required('Last name is mandatory'),
  dob: yup
    .date()
    .required('Date of birth is mandatory')
    .nullable()
    .transform((curr, orig) => (orig === '' ? null : curr)) // Ensure blank dates are null
    .max(new Date(), 'Date of birth must be a past date') // Ensure dob is not in the future
    .test('is-past', 'Date of birth must be a past date', (value) => {
      return value && value < new Date();
    }),
  gender: yup.string().required('Gender is mandatory'),
  department: yup.string().required('Department is mandatory'),
});

function Register() {
  const { register, handleSubmit, formState: { errors } } = useForm({
    resolver: yupResolver(schema),
  });

  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false); // Modal state for success
  const [errorMessage, setErrorMessage] = useState('');
  const [usernameAvailable, setUsernameAvailable] = useState(true);
  const [usernameMessage, setUsernameMessage] = useState('');

  const checkUsernameAvailability = async (username) => {
    try {
      // console.log(username);
      const response = await axios.get(`http://localhost:8999/user/check/${username}`);
      console.log(response);
      if(response.data === "User Not Found"){
        setUsernameAvailable(true);
        setUsernameMessage("Username is available");
      }
      else if (response.data === "User Present"){
        setUsernameAvailable(false);
        setUsernameMessage("Username is already taken");
      }
      // setUsernameAvailable(!response.data.exists);
      // setUsernameMessage(response.data.exists ? 'Username is already taken' : 'Username is available');
    } catch (error) {
      console.error('Error checking username availability:', error);
      setUsernameAvailable(false);
      setUsernameMessage('Username cannot be empty');
    }
  };

  const onSubmit = async (data) => {
    if (!usernameAvailable) {
      setErrorMessage('Username is already taken, please choose another one.');
      return;
    }

    try {
      await axios.post('http://localhost:8999/user/add', data);
      setShowModal(true); // Show modal on successful registration
    } catch (error) {
      if (error.response && error.response.status === 403) {
        setErrorMessage(error.response.data.message);
      } else {
        setErrorMessage('An error occurred during registration');
      }
      console.log(error);
    }
  };

  return (
    <Container className="d-flex justify-content-center align-items-center min-vh-100">
      <div className="w-100 p-4 border rounded shadow-sm bg-light">
        <h2 className="text-center mb-4">Register</h2>

        {/* Display error message if available */}
        {errorMessage && (
          <Alert variant="danger" className="mb-3">
            {errorMessage}
          </Alert>
        )}

        <Form onSubmit={handleSubmit(onSubmit)}>
          {/* First Row - Username and Email */}
          <Row className="mb-3 g-3">
            <Col xs={12} sm={6}>
              <Form.Group controlId="username">
                <Form.Label>Username</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter username"
                  {...register('username')}
                  autoComplete="off"
                  className="text-start"
                  onChange={(e) => checkUsernameAvailability(e.target.value)}
                />
                {errors.username && (
                  <Alert variant="danger" className="mt-2">
                    <span className="text-danger">*</span> {errors.username.message}
                  </Alert>
                )}
                {usernameMessage && (
                  <Alert variant={usernameAvailable ? 'success' : 'danger'} className="mt-2">
                    {usernameMessage}
                  </Alert>
                )}
              </Form.Group>
            </Col>

            <Col xs={12} sm={6}>
              <Form.Group controlId="email">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="Enter email"
                  {...register('email')}
                  className="text-start"
                />
                {errors.email && (
                  <Alert variant="danger" className="mt-2">
                    <span className="text-danger">*</span> {errors.email.message}
                  </Alert>
                )}
              </Form.Group>
            </Col>
          </Row>

          {/* Second Row - Password and First Name */}
          <Row className="mb-3 g-3">
            <Col xs={12} sm={6}>
              <Form.Group controlId="password">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  type="password"
                  placeholder="Enter password"
                  {...register('password')}
                  className="text-start"
                />
                {errors.password && (
                  <Alert variant="danger" className="mt-2">
                    <span className="text-danger">*</span> {errors.password.message}
                  </Alert>
                )}
              </Form.Group>
            </Col>

            <Col xs={12} sm={6}>
              <Form.Group controlId="firstName">
                <Form.Label>First Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter first name"
                  {...register('firstName')}
                  className="text-start"
                />
                {errors.firstName && (
                  <Alert variant="danger" className="mt-2">
                    <span className="text-danger">*</span> {errors.firstName.message}
                  </Alert>
                )}
              </Form.Group>
            </Col>
          </Row>

          {/* Third Row - Last Name and Date of Birth */}
          <Row className="mb-3 g-3">
            <Col xs={12} sm={6}>
              <Form.Group controlId="lastName">
                <Form.Label>Last Name</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter last name"
                  {...register('lastName')}
                  className="text-start"
                />
                {errors.lastName && (
                  <Alert variant="danger" className="mt-2">
                    <span className="text-danger">*</span> {errors.lastName.message}
                  </Alert>
                )}
              </Form.Group>
            </Col>

            <Col xs={12} sm={6}>
              <Form.Group controlId="dob">
                <Form.Label>Date of Birth</Form.Label>
                <Form.Control
                  type="date"
                  {...register('dob')}
                  className="text-start"
                />
                {errors.dob && (
                  <Alert variant="danger" className="mt-2">
                    <span className="text-danger">*</span> {errors.dob.message}
                  </Alert>
                )}
              </Form.Group>
            </Col>
          </Row>

          {/* Fourth Row - Gender and Department */}
          <Row className="mb-3 g-3">
            <Col xs={12} sm={6}>
              <Form.Group controlId="gender">
                <Form.Label>Gender</Form.Label>
                <Form.Control as="select" {...register('gender')} className="text-start">
                  <option value="">Select gender</option>
                  <option value="Male">Male</option>
                  <option value="Female">Female</option>
                  <option value="Other">Other</option>
                </Form.Control>
                {errors.gender && (
                  <Alert variant="danger" className="mt-2">
                    <span className="text-danger">*</span> {errors.gender.message}
                  </Alert>
                )}
              </Form.Group>
            </Col>

            <Col xs={12} sm={6}>
              <Form.Group controlId="department">
                <Form.Label>Department</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="Enter department"
                  {...register('department')}
                  className="text-start"
                />
                {errors.department && (
                  <Alert variant="danger" className="mt-2">
                    <span className="text-danger">*</span> {errors.department.message}
                  </Alert>
                )}
              </Form.Group>
            </Col>
          </Row>

          {/* Submit Button */}
          <Button variant="primary" type="submit" className="w-100 mt-3">
            Register
          </Button>
        </Form>

        {/* Success Modal */}
        <Modal show={showModal} onHide={() => setShowModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Registration Success</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <p>User registered successfully!</p>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>
              Close
            </Button>
            <Button variant="primary" onClick={() => navigate('/login')}>
              Go to Login
            </Button>
          </Modal.Footer>
        </Modal>

        <div className="text-center mt-3">
          <p className="text-muted">
            Already have an account? <a href="/login" className="text-primary">Go To Login</a>
          </p>
        </div>
      </div>
    </Container>
  );
}

export default Register;
