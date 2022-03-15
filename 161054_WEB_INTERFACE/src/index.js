import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Routes, Route} from "react-router-dom";
import './index.css';

import App from "./components/App";
import Main from "./components/Main";
import Header from "./components/Header";
import WelcomeText from "./components/WelcomeText";
import ErrorPage from "./components/ErrorPage"

import LoginPage from "./components/LoginPage";
import BottomNavigationBar from "./components/BottomNavigationBar";
import StationsScreen from "./components/StationsScreen";

ReactDOM.render(
    <React.StrictMode>
        <App>
            <BrowserRouter>
                <Header/>

                <Main>
                    <Routes>
                        <Route exact path={"/"} element={<WelcomeText/>}/>
                        <Route exact path={"/login"} element={<LoginPage/>}/>
                        <Route exact path={"/stations"} element={<StationsScreen />}/>
                        <Route path={"*"} element={<ErrorPage/>}/>
                    </Routes>
                </Main>

                <BottomNavigationBar selected={0}/>
            </BrowserRouter>
        </App>
    </React.StrictMode>,
    document.getElementById('root')
);
