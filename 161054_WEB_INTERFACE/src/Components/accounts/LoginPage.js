/**
 * @author Dlamini Lindelwa [02/02/22]
 *
 * LoginPage.js
 * ---------------------------------------------------------------------------------------------------------------------
 *
 * Login page for authentication users.
 */

import React, {useContext, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import TextField from "@mui/material/TextField"
import Typography from "@mui/material/Typography"
import Button from "@mui/material/Button"
import Grid from "@mui/material/Grid"
import {Alert, FormControl, InputAdornment, InputLabel, OutlinedInput, Snackbar} from "@mui/material";
import {Visibility, VisibilityOff} from "@mui/icons-material";
import IconButton from "@mui/material/IconButton";

import {signInWithEmailAndPassword} from "firebase/auth";
import {auth} from "../../config/firebaseConfig";

export default function LoginPage() {
    const navigate = useNavigate()
    const [loginValues, setLoginValues] = useState({
            email: "",
            password: "",
            showError: false,
            errorMessage: "",
            showPassword: false
        }
    )

    const onClose = () => {
        setLoginValues({...loginValues, showError: false})
    }

    const handleChange = (prop) => (event) => {
        setLoginValues({...loginValues, [prop]: event.target.value});
    };

    const handleClickShowPassword = () => {
        setLoginValues({
            ...loginValues,
            showPassword: !loginValues.showPassword,
        });
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const handleOnSubmitLoginData = async (e) => {
        e.preventDefault()

        if (!loginValues.email || !loginValues.password) {
            setLoginValues({
                ...loginValues,
                showError: true,
                errorMessage: "Invalid input. Try again!"
            })
            return;
        }

        try {
            await signInWithEmailAndPassword(auth, loginValues.email, loginValues.password)
                .then((userCredentials) => {
                    const user = userCredentials.user
                    console.log(user)
                }).catch((error) => {
                        const errorMessage = error.message;
                        setLoginValues({
                            ...loginValues,
                            showError: true,
                            errorMessage: errorMessage
                        })
                        console.log(error)
                        // todo: uncomment error
                        throw error;
                    }
                )
            navigate("/", {
                state: {
                    welcomeMessage: "You have successfully logged in!"
                }
            })
        } catch (e) {
        }
    }

    return (
        <form onSubmit={handleOnSubmitLoginData}>
            <Grid
                container
                justifyContent="center"
                alignContent={"center"}
                sx={{m: 'auto', px: 2}}
                minHeight={"60vh"}>

                <Grid item textAlign={"center"} xs={12} md={4} mt={4}>
                    <Typography variant="h5" sx={{fontFamily: 'Poppins', my: 6}}>
                        Enter your credentials
                    </Typography>

                    <Snackbar
                        anchorOrigin={{vertical: "bottom", horizontal: "right"}}
                        open={loginValues.showError}
                        onClose={onClose}>
                        <Alert severity="error" sx={{width: '100%'}}>
                            {loginValues.errorMessage}
                        </Alert>
                    </Snackbar>

                    <TextField
                        variant="outlined"
                        label="Email"
                        error={loginValues.showError}
                        type={"email"}
                        fullWidth={true}
                        sx={{mb: 3}} onChange={handleChange("email")}/>

                    <FormControl fullWidth={true} sx={{mb: 3}} variant="outlined">
                        <InputLabel htmlFor="outlined-adornment-password">Password</InputLabel>
                        <OutlinedInput
                            id="outlined-adornment-password"
                            type={loginValues.showPassword ? 'text' : 'password'}
                            value={loginValues.password}
                            onChange={handleChange('password')}
                            error={loginValues.showError}
                            endAdornment={
                                <InputAdornment position="end">
                                    <IconButton
                                        aria-label="toggle password visibility"
                                        onClick={handleClickShowPassword}
                                        onMouseDown={handleMouseDownPassword}
                                        edge="end">
                                        {loginValues.showPassword ? <VisibilityOff/> : <Visibility/>}
                                    </IconButton>
                                </InputAdornment>
                            }
                            label="Password"
                        />
                    </FormControl>

                    <Button
                        variant="contained"
                        type={"submit"}
                        sx={{py: '10pt'}} fullWidth={true}
                        onClick={handleOnSubmitLoginData}>
                        Log In
                    </Button>

                    <Typography mt={3}>
                        Don't have an account? <Link to={"/register"}>Create account</Link>.
                    </Typography>
                </Grid>

            </Grid>
        </form>
    );
}