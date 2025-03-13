import "bootstrap/dist/css/bootstrap.min.css";
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Register from './components/Register';
import LoginDetails from './components/LoginDetails';
import ChangePassword from "./components/ChangePassword";
import UpdateUser from './components/UpdateUser';
import GetUser from './components/GetUser';
import PatientList from './components/PatientList';
import BookmarkList from './components/BookmarkList';
import Header from './components/Header';
import { useState } from "react";
import ProtectedRoute from "./components/ProtectedRoute";
import TreatmentSuggestor from "./components/TreatmentSuggestor";
import Footer from "./components/Footer";
import Home from "./components/Home";

function App() {
  // const [username,setUsername] = useState("");
  // setUsername(sessionStorage.getItem("username"));
  const username = sessionStorage.getItem("username");

  return (
    <Router>
      <Header></Header>
      <Routes>
        <Route path="/home"element={<Home />} />
        <Route path="/login" element={<LoginDetails />} />
        <Route path="/changepassword" element={<ChangePassword/>}/>
        <Route path="/register" element={<Register />} />
        <Route path="/patientlist" element={<ProtectedRoute><PatientList /></ProtectedRoute>} />
        <Route path="/updateuser/:username" element={<ProtectedRoute><UpdateUser /></ProtectedRoute>} />
        <Route path="/getuser" element={<ProtectedRoute><GetUser /></ProtectedRoute>} />
        <Route path="/" element={<Navigate to="/home" replace />} />
        <Route path="/bookmarks" element={<ProtectedRoute><BookmarkList /></ProtectedRoute>} />
        <Route path="/treatment" element={<ProtectedRoute><TreatmentSuggestor /></ProtectedRoute>} /> 


      </Routes>
      <Footer></Footer>
    </Router>
    
  );
}

export default App;