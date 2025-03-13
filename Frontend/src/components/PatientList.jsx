import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { Modal, Button } from 'react-bootstrap';
import './PatientCard.css';

const fetchPatients = async () => {
  try {
    const response = await axios.get("http://localhost:3232/diagnosis");
    return response.data;
  } catch {
    console.log("Error fetching the data");
    return [];
  }
};

function PatientCard({ patient, onBookmark }) {
  const token = sessionStorage.getItem("token");
  const addToBookMark = async (patient) => {
    

    const randomUsername = sessionStorage.getItem("username");
    try {
      // Try to fetch existing bookmarks for the user
      let existingBookmarksResponse;
      try {
        existingBookmarksResponse = await axios.get(`http://localhost:8999/bookmark/user/${randomUsername}`,{
          headers: {
            'Authorization': `Bearer ${token}`  // Add the token in the Authorization header
          }
        });
      } catch (error) {
        // If 404 (Not Found), it means no bookmarks exist for the user, so we handle this and allow adding the bookmark
        if (error.response && error.response.status === 404) {
          console.log("No existing bookmarks for the user. Proceeding to add a new bookmark.");
          existingBookmarksResponse = { data: [] }; // Simulate empty bookmarks for the user
        } else {
          throw error; // Rethrow other errors
        }
      }

      // Check if the patient is already bookmarked
      const isAlreadyBookmarked = existingBookmarksResponse.data.some(
        (bookmark) => bookmark.id === patient.id
      );

      if (isAlreadyBookmarked) {
        onBookmark(false, "Bookmark already exists");
        return;
      }

      const patientWithUsername = { ...patient, userName: randomUsername };

      // Add the bookmark
      await axios.post("http://localhost:8999/bookmark/add", patientWithUsername,{
        headers: {
          'Authorization': `Bearer ${token}`  // Add the token in the Authorization header
        }
      })
        .then((res) => {
          console.log("Record added to the Bookmark:", res.data);
          onBookmark(true, "Bookmarked successfully");
        })
        .catch((err) => {
          console.log("Some problem in adding:", err);
          onBookmark(false, "Failed to add Bookmark");
        });

    } catch (error) {
      console.error("Error fetching existing bookmarks or user data:", error);
      onBookmark(false, "Error Fetching bookmarks");
    }
  };


  return (
    <div className={`card shadow-lg `}>
      <div className="bg"></div>
      <div className="blob"></div>
      <div className="card-body">
        <h5 className="card-title">{patient.gender}</h5>
        <p className="card-text"><strong>Age: </strong>{patient.age}</p>
        <p className="card-text"><strong>Blood Pressure: </strong>{patient.bp}</p>
        <p className="card-text"><strong>Cholesterol: </strong>{patient.cholesterol}</p>
        <p className="card-text"><strong>Diabetic: </strong>{patient.diabetic}</p>
        <p className="card-text"><strong>Smoking Status: </strong>{patient.smoking_status}</p>
        <p className="card-text"><strong>Pain Type: </strong>{patient.pain_type}</p>
        <p className="card-text"><strong>Treatment: </strong>{patient.treatment}</p>
        <p className="card-text"><strong>Patient Id: </strong>{patient.id}</p>
        <div>
          <button className="btn btn-primary" onClick={() => addToBookMark(patient)}>Bookmark</button>
        </div>
      </div>
    </div>

  );
}

function PatientList() {
  const [patients, setPatients] = useState([]);
  const [filteredPatients, setFilteredPatients] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const recordsPerPage = 12;
  const [showModal, setShowModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');

  const [searchQuery, setSearchQuery] = useState('');
  const [genderFilter, setGenderFilter] = useState('');
  const [painTypeFilter, setPainTypeFilter] = useState('');
  const [diabeticFilter, setDiabeticFilter] = useState('');
  const [smokingStatusFilter, setSmokingStatusFilter] = useState('');
  const [treatmentFilter, setTreatmentFilter] = useState("");
  const [ageRange, setAgeRange] = useState([0, 100]);
  const [bpRange, setBpRange] = useState([80, 180]);
  const [cholesterolRange, setCholesterolRange] = useState([100, 300]);
  const [showAdvancedFilters, setShowAdvancedFilters] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      const data = await fetchPatients();
      setPatients(data);
      setFilteredPatients(data); // Initially set filtered patients to all patients
    };
    fetchData();
  }, []);

  const handleBookmarkAction = (isSuccess, message) => {
    setModalMessage(message);
    setShowModal(true);
  };

  const filterPatients = () => {
    const filtered = patients.filter((patient) => {
      const matchesSearch = searchQuery ? patient.name.toLowerCase().includes(searchQuery.toLowerCase()) : true;
      const matchesGender = genderFilter ? patient.gender === genderFilter : true;
      const matchesPainType = painTypeFilter ? patient.pain_type === painTypeFilter : true;
      const matchesDiabetic = diabeticFilter ? patient.diabetic === diabeticFilter : true;
      const matchesTreatment = treatmentFilter ? patient.treatment === treatmentFilter : true;
      const matchesSmokingStatus = smokingStatusFilter ? patient.smoking_status === smokingStatusFilter : true;
      const matchesAge = patient.age >= ageRange[0] && patient.age <= ageRange[1];
      const matchesBP = patient.bp >= bpRange[0] && patient.bp <= bpRange[1];
      const matchesCholesterol = patient.cholesterol >= cholesterolRange[0] && patient.cholesterol <= cholesterolRange[1];
      return matchesSearch && matchesGender && matchesPainType && matchesDiabetic && matchesSmokingStatus && matchesTreatment && matchesAge && matchesBP && matchesCholesterol;
    });
    setFilteredPatients(filtered);
  };

  useEffect(() => {
    filterPatients(); // Re-filter patients when any filter state changes
  }, [searchQuery, genderFilter, painTypeFilter, diabeticFilter, smokingStatusFilter, ageRange, bpRange, cholesterolRange]);

  // Pagination Logic
  const indexOfLastRecord = currentPage * recordsPerPage;
  const indexOfFirstRecord = indexOfLastRecord - recordsPerPage;
  const currentRecords = filteredPatients.slice(indexOfFirstRecord, indexOfLastRecord);
  const totalPages = Math.ceil(filteredPatients.length / recordsPerPage);

  // Get unique values for filters
  const uniquePainTypes = patients.length > 0 ? [...new Set(patients.map((p) => p.pain_type))] : [];
  const uniqueSmokingStatuses = patients.length > 0 ? [...new Set(patients.map((p) => p.smoking_status))] : [];

  return (
    <div className="container mt-4">
      <h1 className="text-center mb-4">Patient Records</h1>
      <button
        className="btn btn-primary mb-3"
        onClick={() => setShowAdvancedFilters(!showAdvancedFilters)}
      >
        {showAdvancedFilters ? "Hide Advanced Filters" : "Show Advanced Filters"}
      </button>

      {/* Filter Section */}
      <div className="row g-3">
        <div className="col-md-2">
          <label>Gender</label>
          <select className="form-control" value={genderFilter} onChange={(e) => setGenderFilter(e.target.value)}>
            <option value="">All</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
          </select>
        </div>

        <div className="col-md-2">
          <label>Min Age</label>
          <input type="number" className="form-control" value={ageRange[0]} onChange={(e) => setAgeRange([+e.target.value, ageRange[1]])} />
        </div>

        <div className="col-md-2">
          <label>Max Age</label>
          <input type="number" className="form-control" value={ageRange[1]} onChange={(e) => setAgeRange([ageRange[0], +e.target.value])} />
        </div>

        <div className="col-md-3">
          <label>Pain Type</label>
          <select className="form-control" value={painTypeFilter} onChange={(e) => setPainTypeFilter(e.target.value)}>
            <option value="">All</option>
            {uniquePainTypes.map((painType, idx) => (
              <option key={idx} value={painType}>{painType}</option>
            ))}
          </select>
        </div>

        <div className="col-md-3">
          <label>Treatment</label>
          <select className="form-control" value={treatmentFilter} onChange={(e) => setTreatmentFilter(e.target.value)}>
            <option value="">All</option>
            <option value="Angioplasty">Angioplasty</option>
            <option value="Lifestyle Changes">Lifestyle Changes</option>
            <option value="Medication">Medication</option>
            <option value="Coronary Artery Bypass Graft (CABG)">Coronary Artery Bypass Graft (CABG)</option>
          </select>
        </div>
        {showAdvancedFilters && (
          <>

        <div className="col-md-3">
          <label>Smoking Status</label>
          <select className="form-control" value={smokingStatusFilter} onChange={(e) => setSmokingStatusFilter(e.target.value)}>
            <option value="">All</option>
            {uniqueSmokingStatuses.map((status, idx) => (
              <option key={idx} value={status}>{status}</option>
            ))}
          </select>
        </div>

        

        <div className="col-md-3">
          <label>Min BP</label>
          <input type="number" className="form-control" value={bpRange[0]} onChange={(e) => setBpRange([+e.target.value, bpRange[1]])} />
        </div>

        <div className="col-md-3">
          <label>Max BP</label>
          <input type="number" className="form-control" value={bpRange[1]} onChange={(e) => setBpRange([bpRange[0], +e.target.value])} />
        </div>

        <div className="col-md-3">
          <label>Min Cholesterol</label>
          <input type="number" className="form-control" value={cholesterolRange[0]} onChange={(e) => setCholesterolRange([+e.target.value, cholesterolRange[1]])} />
        </div>

        <div className="col-md-3">
          <label>Max Cholesterol</label>
          <input type="number" className="form-control" value={cholesterolRange[1]} onChange={(e) => setCholesterolRange([cholesterolRange[0], +e.target.value])} />
        </div>

        <div className="col-md-2">
          <label>Diabetic</label>
          <select className="form-control" value={diabeticFilter} onChange={(e) => setDiabeticFilter(e.target.value)}>
            <option value="">All</option>
            <option value="Yes">Yes</option>
            <option value="No">No</option>
          </select>
        </div>
        </>
      )}
      </div>

      {/* Patient Cards */}
      <div className="row mt-4">
        {currentRecords.length > 0 ? (
          currentRecords.map((patient) => (
            <div key={patient.id} className="col-md-4 mb-3">
              <PatientCard patient={patient} onBookmark={handleBookmarkAction} />
            </div>
          ))
        ) : (
          <p className="text-center text-muted">No patients found matching the criteria.</p>
        )}
      </div>

      {/* Pagination Controls */}
      <div className="d-flex justify-content-center mt-4">
        <button
          className="btn btn-outline-dark"
          onClick={() => setCurrentPage(currentPage - 1)}
          disabled={currentPage === 1}
        >
          Previous
        </button>
        <span className="fw-bold mx-3">Page {currentPage} of {totalPages}</span>
        <button
          className="btn btn-outline-dark"
          onClick={() => setCurrentPage(currentPage + 1)}
          disabled={currentPage === totalPages}
        >
          Next
        </button>
      </div>

      {/* Modal for showing success or failure message */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Bookmark Status</Modal.Title>
        </Modal.Header>
        <Modal.Body>{modalMessage}</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default PatientList;