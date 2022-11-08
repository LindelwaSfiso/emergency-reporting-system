// config file for firebase
// @author: Dlamini Lindelwa 16:09:22

import { initializeApp } from "firebase/app";
import {getAuth,setPersistence, indexedDBLocalPersistence} from "firebase/auth";
import {getFirestore} from "firebase/firestore";

// import { getAnalytics } from "firebase/analytics";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyCMa32zrkXBduA-7MIJ0WkAjZYrEQtKcgQ",
    authDomain: "project-174520748804977289.firebaseapp.com",
    databaseURL: "https://project-174520748804977289-default-rtdb.firebaseio.com",
    projectId: "project-174520748804977289",
    storageBucket: "project-174520748804977289.appspot.com",
    messagingSenderId: "550156214573",
    appId: "1:550156214573:web:1125a26620af419cd3b3a4",
    measurementId: "G-S8HQ4R3LT7"
};

// Initialize Firebase
export const app = initializeApp(firebaseConfig);
// const analytics = getAnalytics(app);
export const auth = getAuth(app);
export const db = getFirestore();

// set up firebase auth persist
setPersistence(auth, indexedDBLocalPersistence)

