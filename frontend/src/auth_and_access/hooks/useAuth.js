import {useContext} from "react";
import AuthContext from "../contexts/authContext.js";

const useAuth = () => {
    return useContext(AuthContext);
};

export default useAuth;