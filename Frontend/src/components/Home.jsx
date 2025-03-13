import React from 'react';
import './Home.css'; // Import CSS for this component
import { useNavigate } from 'react-router-dom'; // For routing

const Home = () => {
  const navigate = useNavigate();

  return (
    <div className="home-container">
      {/* Left Section */}
      <div className="left-section">
        <div className="content">
          <h1>HeartSync</h1>
          <p className="caption">Happiness Begins With Good Health</p>
          <div className="description">
            <p>
              Welcome to HeartSync – Your Smart Cardiac Data Companion
            </p>
            <p>
              Empowering Healthcare with Data-Driven Insights
            </p>
            <p>
              HeartSync is an advanced medical data filtering and analysis platform designed to assist healthcare professionals and researchers in analyzing cardiac health trends. Our platform enables users to filter patient data based on key health parameters such as age, gender, smoking status, diabetic condition, pain type, and more, making patient assessment more efficient and insightful.
            </p>
          </div>
        </div>
      </div>

      {/* Right Section */}
      <div className="right-section">
        <div className="button-container">
          <button className="register" onClick={() => navigate('/register')}>
            Register
          </button>
          <button className="login" onClick={() => navigate('/login')}>
            Login
          </button>
        </div>
      </div>
    </div>
  );
};

export default Home;