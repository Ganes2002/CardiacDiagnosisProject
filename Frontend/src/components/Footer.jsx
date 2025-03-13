import React from 'react';
import { Container, Row, Col, Navbar, Nav } from 'react-bootstrap';
const Footer = () => {
    return (
        <footer style={{ backgroundColor: '#0056B3', color: '#fff', padding: '20px 0' }}>
            <Container>
                <Row className="justify-content-center">
                    <Col md={4} sm={12} className="mb-3">
                        <h5>About Us</h5>
                        <p>
                        HeartSync is a medical data analysis platform that filters cardiac health data based on key parameters and includes Mr. Suggestor, which recommends personalized treatments and visualizes trends with a pie chart.
                        </p>
                    </Col>
                    <Col md={4} sm={12} className="mb-3">
                        <h5>Quick Links</h5>
                        <Nav className="flex-column">
                            <Nav.Link href="/home" style={{ color: '#fff' }}>Home</Nav.Link>
                            <Nav.Link href="/bookmarks" style={{ color: '#fff' }}>Bookmarks</Nav.Link>
                            <Nav.Link href="/treatment" style={{ color: '#fff' }}>Mr. Suggestor</Nav.Link>
                        </Nav>
                    </Col>
                    <Col md={4} sm={12} className="mb-3">
                        <h5>Contact Us</h5>
                        <p>Email: contact@heart_sync.com</p>
                        <p>Phone: +1 234 567 890</p>
                        <p>Address: ITC Tripthi building Block A</p>
                    </Col>
                </Row>
                <Row className="text-center mt-4">
                    <Col>
                        <Navbar.Brand style={{ color: '#fff' }}>
                            &copy; {new Date().getFullYear()} Heart-sync | All Rights Reserved.
                        </Navbar.Brand>
                    </Col>
                </Row>
            </Container>
        </footer>
    );
};
export default Footer;



