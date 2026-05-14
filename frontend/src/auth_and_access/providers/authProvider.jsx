import React, {useState} from 'react';
import AuthContext from "../contexts/authContext.js";

const decode = (jwtToken) => {
    try {
        return JSON.parse(atob(jwtToken.split(".")[1]));
    } catch {
        return null;
    }
};

const getInitialAuthState = () => {
    const jwtToken = localStorage.getItem("token");
    const payload = jwtToken ? decode(jwtToken) : null;

    if (jwtToken && !payload) {
        localStorage.removeItem("token");
    }

    return {
        user: payload,
        loading: false
    };
};

const AuthProvider = ({children}) => {
    const [authState, setAuthState] = useState(getInitialAuthState);

    const login = (jwtToken) => {
        const payload = decode(jwtToken);
        if (payload) {
            localStorage.setItem("token", jwtToken);
            setAuthState({user: payload, loading: false});
        }
    };

    const logout = () => {
        localStorage.removeItem("token");
        setAuthState({user: null, loading: false});
    };

    return (
        <AuthContext.Provider
            value={{
                login,
                logout,
                user: authState.user,
                loading: authState.loading,
                isLoggedIn: !!authState.user
            }}
        >
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;
