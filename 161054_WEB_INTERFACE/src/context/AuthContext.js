import {createContext, useEffect, useState} from "react";
import {onAuthStateChanged} from "firebase/auth";
import {auth} from "../config/firebaseConfig";
import {CircularProgress} from "@mui/material";
import Box from "@mui/material/Box";

export const AuthContext = createContext()

export const AuthContextProvider = ({children}) => {
    const [currentUser, setCurrentUser] = useState()
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        const unsub = onAuthStateChanged(auth, (user) => {
            setCurrentUser(user)
            setLoading(false)
        })

        return () => {
            unsub()
        }
    }, []);

    if (loading) {
        return <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: '20%'}}>
            <CircularProgress />
        </Box>
    }

    return (
        <AuthContext.Provider value={{currentUser}}>
            {children}
        </AuthContext.Provider>
    );
};
