import { createContext, useContext, useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(!!sessionStorage.getItem("token"));//changing token into boolean value

    


    useEffect(() => {
        const checkAuth = () => setIsLoggedIn(!!sessionStorage.getItem("token"));
        window.addEventListener("storage", checkAuth);//Listens for sessionStorage changes in any browser tab.
        // If a user logs in or logs out in another tab, it detects the change and updates the isLoggedIn state.
        return () => window.removeEventListener("storage", checkAuth);
    }, []);

    const login = (token,username) => {
        sessionStorage.setItem("token", token);
        sessionStorage.setItem("username",username);
        setIsLoggedIn(true);

    };
    
    const logout = () => {
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("username");
        setIsLoggedIn(false);
    };

    return (
        <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
