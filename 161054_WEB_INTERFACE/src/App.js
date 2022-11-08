import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";

import HomePage from "./components/home/HomePage";
import LoginPage from "./components/accounts/LoginPage";
import ProfilePage from "./components/accounts/ProfilePage";
import RegisterAccountPage from "./components/accounts/RegisterAccountPage";
import StationsScreen from "./components/StationsScreen";
import ErrorPage from "./components/ErrorPage";
import React, {useContext} from "react";
import {AuthContext} from "./context/AuthContext";
import NotificationsPage from "./components/pages/NotificationsPage";
import StationsListPage from "./components/pages/StationsListPage";
import SendWarningsPage from "./components/pages/SendWarningsPage";
import ViewNotificationPage from "./components/pages/ViewNotificationPage";


const ProtectedRoute = ({children}) => {
    const { currentUser } = useContext(AuthContext)
    if (!currentUser) {
        return <Navigate to={"/login"} />
    }

    return children;
}

export default function App ()  {
    return (
        <BrowserRouter>
            <div>
                <Routes>
                    <Route exact path={"/"} element={
                        <ProtectedRoute><HomePage/></ProtectedRoute>
                    }/>
                    <Route exact path={"/login"} element={<LoginPage/>}/>
                    <Route exact path={"/register"} element={<RegisterAccountPage/>} />
                    <Route exact path={"/edit-profile"} element={<ProfilePage />} />

                    <Route exact path={"/stations"} element={<StationsScreen/>}/>
                    <Route exact path={"/notifications"} element={<NotificationsPage/>}/>
                    <Route exact path={"/notification-details"} element={<ViewNotificationPage />} />
                    <Route exact path={"/all-stations"} element={<StationsListPage/>}/>
                    <Route exact path={"/warnings"} element={<SendWarningsPage/>}/>
                    <Route exact path={"/statistics"} element={<StationsListPage/>}/>

                    <Route path={"*"} element={<ErrorPage/>}/>
                </Routes>
            </div>
        </BrowserRouter>
    )
}