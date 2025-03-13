import React, { useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import './BookmarkCard.css';



function BookmarkCard({ bookmark, onUpdate, onDelete }) {

  // const username = sessionStorage.getItem("username");
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({ ...bookmark });
  const [errorMessage, setErrorMessage] = useState('');
  const [showErrorModal, setShowErrorModal] = useState(false); // For modal visibility

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({ ...prevData, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Reset error message and modal before validating
    setErrorMessage('');
    setShowErrorModal(false);

    // Basic validation check for required fields
    if (!formData.gender || !formData.age || !formData.bp || !formData.cholesterol || !formData.diabetic || !formData.smoking_status || !formData.pain_type || !formData.treatment) {
      setErrorMessage('Please fill in all the fields.');
      setShowErrorModal(true); // Show modal with error
      return;
    }

    // Example of more specific validation
    if (isNaN(formData.age)) {
      setErrorMessage('Age must be a number.');
      setShowErrorModal(true); // Show modal with error
      return;
    }
    if (!isNaN(formData.treatment)) {
      setErrorMessage('Treatment cannot be a number.');
      setShowErrorModal(true);
      return;
    }
  
    if (!isNaN(formData.pain_type)) {
      setErrorMessage('Pain Type cannot be a number.');
      setShowErrorModal(true);
      return;
    }

    // If no error, proceed with the update
    onUpdate(bookmark.userName, formData);
    setIsEditing(false);
  };

  const closeErrorModal = () => {
    setShowErrorModal(false); // Close the error modal
  };
  const [showModal, setShowModal] = useState(false);

  return (
    <div className="card shadow-lg">
      <div className="card-body">
        {isEditing ? (
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label">Gender</label>
              <input
                type="text"
                name="gender"
                className="form-control"
                value={formData.gender}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Age</label>
              <input
                type="number"
                name="age"
                className="form-control"
                value={formData.age}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Blood Pressure</label>
              <input
                type="text"
                name="bp"
                className="form-control"
                value={formData.bp}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Cholesterol</label>
              <input
                type="text"
                name="cholesterol"
                className="form-control"
                value={formData.cholesterol}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Diabetic</label>
              <input
                type="text"
                name="diabetic"
                className="form-control"
                value={formData.diabetic}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Smoking Status</label>
              <input
                type="text"
                name="smoking_status"
                className="form-control"
                value={formData.smoking_status}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Pain Type</label>
              <input
                type="text"
                name="pain_type"
                className="form-control"
                value={formData.pain_type}
                onChange={handleChange}
                required
              />
            </div>
            <div className="mb-3">
              <label className="form-label">Treatment</label>
              <input
                type="text"
                name="treatment"
                className="form-control"
                value={formData.treatment}
                onChange={handleChange}
                required
              />
            </div>

            <button type="submit" className="btn btn-success">
              Save Changes
            </button>
            <button
              type="button"
              className="btn btn-secondary ml-2"
              onClick={() => setIsEditing(false)}
            >
              Cancel
            </button>
          </form>
        ) : (
          <>
           <h5 
  className={`card-title text-center fw-bold text-uppercase ${bookmark.gender.toLowerCase() === 'female' ? 'female' : 'male'}`} 
  style={{ fontSize: '1.5rem', letterSpacing: '1px' }}
>
  {bookmark.gender}
</h5>

            <p className="card-text"><strong>Age: </strong> {bookmark.age}</p>
            <p className="card-text"><strong>Blood Pressure: </strong> {bookmark.bp}</p>
            <p className="card-text"><strong>Cholesterol: </strong> {bookmark.cholesterol}</p>
            <p className="card-text"><strong>Diabetic: </strong> {bookmark.diabetic}</p>
            <p className="card-text"><strong>Smoking Status: </strong> {bookmark.smoking_status}</p>
            <p className="card-text"><strong>Pain Type: </strong> {bookmark.pain_type}</p>
            <p className="card-text"><strong>Treatment: </strong> {bookmark.treatment}</p>
            <p className="card-text"><strong>Patient Id: </strong> {bookmark.id}</p>
{/* 
            {<div>
              <button
                className="btn btn-warning"
                onClick={() => setIsEditing(true)}
              >
                Update
              </button>
              <button
                className="btn btn-danger ml-4"  style={{ marginLeft: '150px' }}
                onClick={() => onDelete(bookmark.userName, bookmark.bookmarkId)}
              >
                Delete
              </button>
            </div> } */}
            <div>
  {/* <button
    className="btn btn-custom-warning"
    onClick={() => setIsEditing(true)}
  >
    Update
  </button> */}
  <button
    className="btn btn-custom-danger ml-3"
    onClick={() => onDelete(bookmark.userName, bookmark.bookmarkId)}
  >
    Delete
  </button>
</div>

            

          </>
        )}
      </div>

      {/* Error Modal */}
      <Modal show={showErrorModal} onHide={closeErrorModal}>
        <Modal.Header closeButton>
          <Modal.Title>Error</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {errorMessage}
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={closeErrorModal}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default BookmarkCard;