import React from 'react';
import axios from 'axios';
import './PatientCard.css';
function PatientCard({ patient, onBookmark }) {
  const token = sessionStorage.getItem('token');
  const addToBookMark = async (patient) => {
    //const randomUsername = "XHPZSsMs"; // Generate a random username
    const randomUsername = sessionStorage.getItem('username');
    try {
      const existingBookmarks = await axios.get(`http://localhost:8999/bookmark/user/${randomUsername}`,{
        headers: {
          'Authorization': `Bearer ${token}`  // Add the token in the Authorization header
        }
      });
      const bookmarks = existingBookmarks.data || [];
      const isAlreadyBookmarked = bookmarks.some(
        (bookmark) => bookmark.id === patient.id
      );
      if (isAlreadyBookmarked) {
        // If it's already bookmarked, show a message
        onBookmark(false, "Bookmark already exists");
        return;
      }
      // Create a new object including the random username
      const patientWithUsername = {
        ...patient,
        userName: randomUsername,
      };
      // Send the updated data to the backend
      await axios.post("http://localhost:8999/bookmark/add", patientWithUsername,{
        headers: {
          'Authorization': `Bearer ${token}`  // Add the token in the Authorization header
        }
      });
      onBookmark(true, "Bookmarked successfully"); // Success
    } catch (error) {
      if (error.response && error.response.status === 404) {
        // If the bookmark list is empty (404), proceed with adding the bookmark
        const patientWithUsername = {
          ...patient,
          userName: randomUsername,
        };
        await axios.post("http://localhost:8999/bookmark/add", patientWithUsername,{
          headers: {
            'Authorization': `Bearer ${token}`  // Add the token in the Authorization header
          }
        });
        onBookmark(true, "Bookmarked successfully"); // Success
      } else {
        console.error("Error adding bookmark:", error);
        onBookmark(false, "Failed to add Bookmark"); // Failure
      }
    }
  };
  return (
    <div className='card shadow-lg'>
      <div className='card-body'>
        <h5 className='card-title'>{patient.gender}</h5>
        <p className='card-text'><strong>Age : </strong>{patient.age}</p>
        <p className='card-text'><strong>Blood Pressure: </strong>{patient.bp}</p>
        <p className='card-text'><strong>Cholesterol: </strong>{patient.cholesterol}</p>
        <p className='card-text'><strong>Diabetic: </strong>{patient.diabetic}</p>
        <p className='card-text'><strong>Smoking Status: </strong>{patient.smoking_status}</p>
        <p className='card-text'><strong>Pain Type: </strong>{patient.pain_type}</p>
        <p className='card-text'><strong>Treatment: </strong>{patient.treatment}</p>
        <p className='card-text'><strong>Patient Id: </strong>{patient.id}</p>
        <div>
          <button className='btn btn-primary' onClick={() => addToBookMark(patient)}>Bookmark</button>
        </div>
      </div>
    </div>
  );
}
export default PatientCard;






