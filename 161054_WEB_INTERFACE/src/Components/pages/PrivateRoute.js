import {Navigate, Route} from"react-router-dom";
import {AuthContext} from "../../context/AuthContext";
import {useContext} from "react";

const PrivateRoute = ({ children }) => {
    const {currentUser} = useContext(AuthContext)

    if (!currentUser) {
        return <Navigate to='/login'/>;
    }
    return children;
};

export default ProtectedRoute;