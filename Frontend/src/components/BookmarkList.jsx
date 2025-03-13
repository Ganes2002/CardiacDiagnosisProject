import React, { useState, useEffect } from 'react';
import BookmarkCard from './BookmarkCard';
import { Modal, Button } from 'react-bootstrap';
import axios from 'axios';
import './BookmarkCard.css';

const fetchBookmarks = async (username) => {
  const token = sessionStorage.getItem('token');
  try {
    const response = await axios.get(`http://localhost:8999/bookmark/user/${username}`,{
      headers: {
        'Authorization': `Bearer ${token}`  // Add the token in the Authorization header
      }
    });
    return response.data;
  } catch (err) {
    console.log("Error fetching bookmarks:", err);
    return [];
  }
};

const deleteBookmark = async (username, bookmarkId) => {
  const token = sessionStorage.getItem('token'); 
  try {
    const response = await axios.delete(`http://localhost:8999/bookmark/delete/${bookmarkId}/${username}`,{
      headers: {
        'Authorization': `Bearer ${token}`  // Add the token in the Authorization header
      }
    });
    console.log("Bookmark deleted:", response.data);
    return response.data;
  } catch (err) {
    console.log("Error deleting bookmark:", err);
  }
};

const updateBookmark = async (username, bookmarkId, updatedBookmark) => {
  try {
    const response = await axios.put(`http://localhost:8999/bookmark/update/${bookmarkId}/${username}`, updatedBookmark);
    console.log("Bookmark updated:", response.data);
    return response.data;
  } catch (err) {
    console.log("Error updating bookmark:", err);
    throw new Error('Failed to update bookmark. Please check your inputs.');
  }
};

function BookmarkList({  }) {

  const username = sessionStorage.getItem("username");

  const [bookmarks, setBookmarks] = useState([]);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [currentBookmark, setCurrentBookmark] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');

  // Add state for modal messages
  const [modalMessage, setModalMessage] = useState('');
  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    const getBookmarks = async () => {
      const data = await fetchBookmarks(username);
      setBookmarks(data);
    };
    getBookmarks();
  }, [username]);

  const handleDelete = async (username, bookmarkId) => {
    setCurrentBookmark({ username, bookmarkId });
    setShowDeleteModal(true);
  };

  const confirmDelete = async () => {
    const { username, bookmarkId } = currentBookmark;
    const deletedBookmark = await deleteBookmark(username, bookmarkId);
    if (deletedBookmark) {
      setBookmarks((prevBookmarks) =>
        prevBookmarks.filter((bookmark) => bookmark.bookmarkId !== bookmarkId)
      );
      setShowDeleteModal(false);
    }
  };

  const handleUpdate = async (username, updatedBookmark) => {
    try {
      const updated = await updateBookmark(username, updatedBookmark.bookmarkId, updatedBookmark);
      if (updated) {
        setBookmarks((prevBookmarks) =>
          prevBookmarks.map((bookmark) =>
            bookmark.bookmarkId === updated.bookmarkId ? updated : bookmark
          )
        );
        setModalMessage('Bookmark updated successfully!');
        setShowModal(true);  // Show success modal after update
      }
    } catch (error) {
      setErrorMessage(error.message);
      setShowErrorModal(true);
    }
  };

  return (
    <div className="container mt-4">
      <h1 className="bookmark-header text-center mb-4">
  <span>{username}</span>, here’s a list of your saved bookmarks.
</h1>

      <div className="row">
        {bookmarks.length > 0 ? (
          bookmarks.map((bookmark) => (
            <div className="col-md-4 mb-3" key={bookmark.bookmarkId}>
              <BookmarkCard
                bookmark={bookmark}
                onUpdate={handleUpdate}
                onDelete={handleDelete}
              />
            </div>
          ))
        ) : (
          <p>Oops! No bookmarks found.</p>
        )}
      </div>

      {/* Success Modal for Update */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Update Successful</Modal.Title>
        </Modal.Header>
        <Modal.Body>{modalMessage}</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Confirmation Modal for Delete */}
      <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Delete Confirmation</Modal.Title>
        </Modal.Header>
        <Modal.Body>Are you sure you want to delete this bookmark?</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
            Cancel
          </Button>
          <Button variant="danger" onClick={confirmDelete}>
            Confirm Delete
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Error Modal for Update */}
      <Modal show={showErrorModal} onHide={() => setShowErrorModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Error</Modal.Title>
        </Modal.Header>
        <Modal.Body>{errorMessage}</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowErrorModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default BookmarkList;