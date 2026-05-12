import React, {useEffect, useState} from 'react';
import AuthContext from "../contexts/authContext.js";

const decode = (jwtToken) => {
    try {
        return JSON.parse(atob(jwtToken.split(".")[1]));
    } catch (error) {
        console.log(error);
        return null;
    }
};

const AuthProvider = ({children}) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    const login = (jwtToken) => {
        const payload = decode(jwtToken);
        if (payload) {
            localStorage.setItem("token", jwtToken);
            setUser(payload);
            setLoading(false);
        }
    };

    const logout = () => {
        const jwtToken = localStorage.getItem("token");
        if (jwtToken) {
            localStorage.removeItem("token");
            setUser(null);
            setLoading(false);
        }
    };

    useEffect(() => {
        const jwtToken = localStorage.getItem("token");
        if (jwtToken) {
            const payload = decode(jwtToken);
            if (payload) {
                setUser(payload);
                setLoading(false);
            } else {
                setUser(null);
                setLoading(false);
            }
        } else {
            setUser(null);
            setLoading(false);
        }
    }, []);

    return (
        <AuthContext.Provider value={{login, logout, user, loading, isLoggedIn: !!user}}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;