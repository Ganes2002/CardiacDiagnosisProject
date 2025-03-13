import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import { Carousel } from 'react-responsive-carousel';
import 'react-responsive-carousel/lib/styles/carousel.min.css';
import { Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, ArcElement, Title, Tooltip, Legend } from 'chart.js';
import PatientCard from './PatientCard'; // Assuming PatientCard is in the same directory
import { Modal, Button } from 'react-bootstrap'; // Import the Modal and Button components from React Bootstrap
import './TreatmentSuggestor.css';
// Register the necessary Chart.js components
ChartJS.register(
    CategoryScale,
    LinearScale,
    ArcElement,  // Register ArcElement for Pie charts
    Title,
    Tooltip,
    Legend
);
const fetchPatients = async () => {
    try {
        const response = await axios.get("http://localhost:3232/diagnosis");
        return response.data;
    } catch (error) {
        console.error("Error fetching the data", error);
        return [];
    }
};
function TreatmentSuggestor() {
    const [patients, setPatients] = useState([]);
    const [age, setAge] = useState('');
    const [gender, setGender] = useState('');
    const [painType, setPainType] = useState('');
    const [filteredPatients, setFilteredPatients] = useState([]);
    const [treatments, setTreatments] = useState([]);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const [selectedTreatment, setSelectedTreatment] = useState('');
    const [chartData, setChartData] = useState(null);
    const [showPatientCards, setShowPatientCards] = useState(true);
    const [bookmarkMessage, setBookmarkMessage] = useState('');
    const [showModal, setShowModal] = useState(false); // State to control modal visibility
    const chartRef = useRef(null);  // useRef to hold chart instance
    const patientCardsRef = useRef(null); // useRef to hold patient cards section
    useEffect(() => {
        const fetchData = async () => {
            setLoading(true);
            const data = await fetchPatients();
            setPatients(data);
            setLoading(false);
        };
        fetchData();
    }, []);
    const handleAgeChange = (e) => setAge(e.target.value);
    const handleGenderChange = (e) => setGender(e.target.value);
    const handlePainTypeChange = (e) => setPainType(e.target.value);
    const fetchTreatment = () => {
        if (!age || !gender || !painType) {
            setError('Please fill in all fields');
            return;
        }
        const lowerAgeLimit = parseInt(age) - 10;
        const upperAgeLimit = parseInt(age) + 10;
        const filteredPatients = patients.filter(patient =>
            patient.pain_type.toLowerCase() === painType.toLowerCase() &&
            patient.gender.toLowerCase() === gender.toLowerCase() &&
            patient.age >= lowerAgeLimit &&
            patient.age <= upperAgeLimit
        );
        setFilteredPatients(filteredPatients);
        if (filteredPatients.length > 0) {
            const treatmentList = filteredPatients.map(patient => patient.treatment);
            const uniqueTreatments = [...new Set(treatmentList)];
            const treatmentCount = uniqueTreatments.map(treatment => {
                return {
                    treatment,
                    count: treatmentList.filter(t => t === treatment).length
                };
            });
            setTreatments(treatmentCount);
            setError('');
            // Prepare chart data
            const data = {
                labels: treatmentCount.map(item => item.treatment),
                datasets: [
                    {
                        data: treatmentCount.map(item => item.count),
                        backgroundColor: ['#FF9999', '#66B3FF', '#99FF99', '#FFCC99', '#FF6347', '#FF4500'],
                        hoverBackgroundColor: ['#FF6666', '#3399FF', '#66FF66', '#FF9966', '#FF7F50', '#FF5733']
                    }
                ]
            };
            // If chart already exists, destroy it before creating a new one
            if (chartRef.current && chartRef.current.chartInstance) {
                chartRef.current.chartInstance.destroy();
            }
            setChartData(data);
        } else {
            setTreatments([]);
            setChartData(null);
            setError('No treatments found for the given criteria');
        }
    };
    const handleTreatmentClick = (treatment) => {
        setSelectedTreatment(treatment);
        setShowPatientCards(true);
        setTimeout(() => {
            if (patientCardsRef.current) {
                patientCardsRef.current.scrollIntoView({ behavior: 'smooth' });
            }
        }, 100); // Delay to ensure the patient cards are rendered before scrolling
    };
    const handleClosePatientCards = () => {
        setShowPatientCards(false);
    };
    const handleBookmark = (success, message) => {
        setBookmarkMessage(message);
        setShowModal(true);
    };
    const filteredPatientsByTreatment = filteredPatients.filter(patient => patient.treatment === selectedTreatment);
    return (
        <div className="treatment-suggestor">
            <div className="input-container">
                <input
                    type="number"
                    value={age}
                    onChange={handleAgeChange}
                    aria-label="Age"
                    placeholder="Enter your age"
                />
                <select value={gender} onChange={handleGenderChange} aria-label="Gender">
                    <option value="">Select gender</option>
                    <option value="male">Male</option>
                    <option value="female">Female</option>
                </select>
                <input
                    type="text"
                    value={painType}
                    onChange={handlePainTypeChange}
                    aria-label="Pain Type"
                    placeholder="Enter pain type"
                />
                <button className="btn-get-treatment" onClick={fetchTreatment}>Get Treatment</button>
            </div>
            {error && <div className="error-message" role="alert">{error}</div>}
            {bookmarkMessage && (
                <Modal show={showModal} onHide={() => setShowModal(false)}>
                    <Modal.Header closeButton>
                        <Modal.Title>Bookmark Status</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>{bookmarkMessage}</Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={() => setShowModal(false)}>
                            Close
                        </Button>
                    </Modal.Footer>
                </Modal>
            )}
            {loading ? (
                <div className="loading">Loading...</div>
            ) : (
                <>
                    {treatments.length > 0 && (
                        <div className="treatment-result">
                            <h3>Suggested Treatments:</h3>
                            <ul>
                                {treatments.map((item, index) => (
                                    <li key={index} onClick={() => handleTreatmentClick(item.treatment)}>
                                        {item.treatment} <span>({item.count})</span>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    )}
                    {chartData && (
                        <div className="treatment-chart">
                            <h3>Treatment Distribution</h3>
                            <Pie ref={chartRef} data={chartData} />
                        </div>
                    )}
                    {selectedTreatment && showPatientCards && (
                        <div className="patient-cards fade-in" ref={patientCardsRef}>
                            <div className="patient-cards-header">
                                <h3>Patients for Treatment: {selectedTreatment}</h3>
                                <button className="btn-close-cards" onClick={handleClosePatientCards}>Close</button>
                            </div>
                            {filteredPatientsByTreatment.length > 0 ? (
                                <Carousel showThumbs={false} showStatus={false} infiniteLoop useKeyboardArrows>
                                    {filteredPatientsByTreatment.map((patient) => (
                                        <div key={patient.id}>
                                            <PatientCard patient={patient} onBookmark={handleBookmark} />
                                        </div>
                                    ))}
                                </Carousel>
                            ) : (
                                <p className="text-center text-muted">No patients found for the selected treatment.</p>
                            )}
                        </div>
                    )}
                </>
            )}
        </div>
    );
}
export default TreatmentSuggestor;









