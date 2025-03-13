import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import NavDropdown from 'react-bootstrap/NavDropdown';
import { useAuth } from "../services/AuthContext"; // Ensure you are using the auth context
import './Header.css';
import { NavLink, Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react'; // Importing useState and useEffect hooks
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBookmark } from '@fortawesome/free-regular-svg-icons';
import { faUserDoctor } from '@fortawesome/free-solid-svg-icons';
import { faSearchengin } from '@fortawesome/free-brands-svg-icons';

function Header() {
  const { isLoggedIn, logout } = useAuth(); // Get login state and logout function
  const navigate = useNavigate();
  const username = sessionStorage.getItem("username");

  const handleLogout = () => {
    logout(); // Call logout from context
    sessionStorage.removeItem("username");
    sessionStorage.removeItem("token");
    navigate("/login");  // Redirect to login page after logout
  };

  // useEffect to check if the user is logged in (via sessionStorage)
  useEffect(() => {
    const loggedIn = sessionStorage.getItem("username") && sessionStorage.getItem("token");
    if (loggedIn) {
      // Handle login-related state (if necessary)
    }
  }, [isLoggedIn]);  // Re-render when `isLoggedIn` state changes

  return (
    <Navbar expand="lg" className="navbar">
      <Container>
        {/* Navbar Brand with custom color */}
        <Navbar.Brand as={NavLink} to="/patientlist" className="navbar-brand">
          <FontAwesomeIcon icon={faUserDoctor} style={{ marginRight: '8px' }} />  {/* Add the icon with some spacing */}
          Heart-Sync
        </Navbar.Brand>

        <Navbar.Toggle aria-controls="basic-navbar-nav" />

        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            {/* Link to My Bookmarks */}
            {/* <Nav.Link as={NavLink} to="/bookmarks" className="nav-link">
              My Bookmarks
            </Nav.Link> */}
            <Nav.Link as={NavLink}
              to="/bookmarks"
              style={{ color: 'white', textDecoration: 'none' }}
              onMouseEnter={(e) => e.target.style.color = '#3b82f6'}
              onMouseLeave={(e) => e.target.style.color = 'white'}>
              <FontAwesomeIcon icon={faBookmark} style={{ marginRight: '8px' }} />
              My Bookmarks
            </Nav.Link>

            {/* //suggestor */}
            <Nav.Link as={NavLink}
              to="/treatment"
              style={{ color: 'white', textDecoration: 'none' }}
              onMouseEnter={(e) => e.target.style.color = '#3b82f6'}
              onMouseLeave={(e) => e.target.style.color = 'white'}>
              <FontAwesomeIcon icon={faSearchengin} style={{ marginRight: '8px' }} />
              Mr.Suggestor
            </Nav.Link>



          </Nav>

          <Nav>
            {isLoggedIn ? (
              <>

                <NavDropdown title="Profile" id="profile-dropdown" align="end" className="profile-dropdown">
                  <NavDropdown.Item as={NavLink} to="/getuser">View Profile</NavDropdown.Item>
                  <NavDropdown.Item as={NavLink} to={`/updateuser/:${username}`}>Edit Profile</NavDropdown.Item>
                </NavDropdown>

                <Nav.Link onClick={handleLogout} style={{ cursor: "pointer" }}>
                  Logout
                </Nav.Link>
              </>
            ) : (
              <>
                <Nav.Link as={Link} to="/register">Register</Nav.Link>
                <Nav.Link as={Link} to="/login">Login</Nav.Link>
              </>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default Header;