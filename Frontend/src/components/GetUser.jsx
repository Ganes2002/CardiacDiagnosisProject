import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { Container, Modal, Button, Card } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { useAuth } from "../services/AuthContext"; // Import useAuth for handling authentication state
import Header from './Header'; // If you need to display the Header component

function GetUser() {
    const [userData, setUserData] = useState(null);
    const [showDeleteModal, setShowDeleteModal] = useState(false); // Show confirmation modal
    const [showModal, setShowModal] = useState(false); // Show success/failure modal
    const [modalMessage, setModalMessage] = useState(''); // Message for success/failure modal
    const navigate = useNavigate();
    const { logout } = useAuth(); // Import logout function from context

    // Function to fetch user data using the username from sessionStorage
    const getUser = async () => {
        try {
            const username = sessionStorage.getItem("username");
            const token = sessionStorage.getItem("token"); // Ensure token is available if necessary

            const response = await axios.get(`http://localhost:8999/user/get/${username}`);
            setUserData(response.data);
        } catch (error) {
            console.error("Error fetching user data:", error);
        }
    };

    // Trigger the getUser function when the component is mounted
    useEffect(() => {
        getUser();
    }, []); // Empty dependency array ensures this runs only once on mount

    // Function to handle deleting user from all services
    const removeData = async () => {
        const username = sessionStorage.getItem("username");
        const token = sessionStorage.getItem("token");

        try {
            await axios.all([ // Delete user from all services
                axios.delete(`http://localhost:8999/user/delete/${username}`, { headers: { Authorization: `Bearer ${token}` } }),
                axios.delete(`http://localhost:8999/authenticate/remove/${username}`, { headers: { Authorization: `Bearer ${token}` } }),
                axios.delete(`http://localhost:8999/bookmark/delete/all/${username}`, { headers: { Authorization: `Bearer ${token}` } })
            ]);

            // Clear sessionStorage and logout
            sessionStorage.removeItem("token");
            sessionStorage.removeItem("username");

            // Call logout context to update global state
            logout();

            setModalMessage("Account successfully deleted from all services.");
            setShowModal(true); // Show the success modal

            // Redirect to login page after a short delay
            setTimeout(() => {
                navigate("/login");
            }, 2000); // Adding delay for the success message to be visible
        } catch (error) {
            console.error("Error deleting user from services:", error);
            setModalMessage("Failed to delete account from one or more services.");
            setShowModal(true); // Show the error modal
        }
    };

    return (
        <Container className='mt-4'>
            <h2>View Profile</h2>

            {userData ? (
                <Card className="mt-4" style={{ width: '100%' }}>
                    <Card.Body>
                        <Card.Title>Profile Information</Card.Title>
                        <Card.Text>
                            <strong>Username:</strong> {userData.username}
                        </Card.Text>
                        <Card.Text>
                            <strong>First Name:</strong> {userData.firstName}
                        </Card.Text>
                        <Card.Text>
                            <strong>Last Name:</strong> {userData.lastName}
                        </Card.Text>
                        <Card.Text>
                            <strong>Email:</strong> {userData.email}
                        </Card.Text>
                        <Card.Text>
                            <strong>Date of Birth:</strong> {userData.dob}
                        </Card.Text>
                        <Card.Text>
                            <strong>Gender:</strong> {userData.gender}
                        </Card.Text>
                        <Card.Text>
                            <strong>Department:</strong> {userData.department}
                        </Card.Text>

                        <div>
                            <Button variant="danger" onClick={() => setShowDeleteModal(true)}>
                                Delete Account
                            </Button>
                        </div>
                    </Card.Body>
                </Card>
            ) : (
                <p>Loading...</p>
            )}

            {/* Confirmation modal */}
            <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Are you sure you want to delete your account?</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>This action cannot be undone.</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={removeData}>
                        Delete User
                    </Button>
                </Modal.Footer>
            </Modal>

            {/* Success/Error modal */}
            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>Account Deletion Status</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <p>{modalMessage}</p>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
}

export default GetUser;
