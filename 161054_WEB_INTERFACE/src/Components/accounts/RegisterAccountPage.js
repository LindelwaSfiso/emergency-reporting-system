/**
 * @author Dlamini Lindelwa [02/02/22]
 *
 * LoginPage.js
 * ---------------------------------------------------------------------------------------------------------------------
 *
 * Login page for authentication users.
 */

import React, {useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import TextField from "@mui/material/TextField"
import Typography from "@mui/material/Typography"
import Button from "@mui/material/Button"
import Grid from "@mui/material/Grid"

import {
    Alert,
    FormControl,
    InputAdornment,
    InputLabel,
    MenuItem,
    OutlinedInput,
    Select,
    Snackbar
} from "@mui/material";
import {Visibility, VisibilityOff} from "@mui/icons-material";
import IconButton from "@mui/material/IconButton";

import {createUserWithEmailAndPassword, updateProfile} from "firebase/auth";
import {auth, db} from "../../config/firebaseConfig";
import {doc,setDoc} from "firebase/firestore";

export default function RegisterAccountPage() {
    const navigate = useNavigate()

    const [registerValues, setRegisterValues] = useState({
        email: "",
        password: "",
        stationName: "",
        stationPhoneNumber: "",
        coordinates: "-26.480380, 31.306819",
        showError: false,
        errorMessage: "",
        showPassword: false,
        stationType: ""
    })

    // todo: add progress bar

    const handleChange = (prop) => (event) => {
        setRegisterValues({ ...registerValues, [prop]: event.target.value });
    };

    const onClose = () => {
        setRegisterValues({...registerValues, showError: false})
    }

    const handleClickShowPassword = () => {
        setRegisterValues({
            ...registerValues,
            showPassword: !registerValues.showPassword,
        });
    };

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const handleOnSubmitLoginData = async (e) => {
        e.preventDefault()

        console.log(registerValues)

        if (!registerValues.email || !registerValues.stationName || !registerValues.password ||
         !registerValues.coordinates || !registerValues.stationPhoneNumber || !registerValues.stationType) {
            setRegisterValues({
                ...registerValues,
                showError: true,
                errorMessage: "Invalid inputs. Check, and try again!"
            })
            return;
        }

        try {
            const res = await createUserWithEmailAndPassword(auth, registerValues.email, registerValues.password);
            await updateProfile(res.user, {
                displayName: registerValues.stationName
            })

            await setDoc(doc(db, "EMERGENCY_STATIONS", res.user.uid), {
                uid: res.user.uid,
                stationName: registerValues.stationName,
                stationType: registerValues.stationType,
                stationTelephone: registerValues.stationPhoneNumber,
                stationCoordinates: registerValues.coordinates
            });

            // set it to empty, no conversations yet
            await setDoc(doc(db, "USER_DASHBOARD", res.user.uid),{
                created: "true"
            })

            navigate("/", {
                state: {
                    welcomeMessage: "Congratulations. You have successfully registered an account!"
                }
            })

        } catch (error) {
            console.log("ERROR", error)
            setRegisterValues({
                ...registerValues,
                showError: true,
                errorMessage: error.message != null ? error.message :  "Something went wrong. Check network. Try again"
            })
        }
    }

    return (
        <form onSubmit={handleOnSubmitLoginData} style={{marginTop: 15}}>
            <Snackbar
                anchorOrigin={{vertical: "bottom", horizontal:"right"}}
                open={registerValues.showError}
            onClose={onClose}>
                <Alert severity="error" sx={{ width: '100%' }}>
                    {registerValues.errorMessage}
                </Alert>
            </Snackbar>

            <Grid
                container item xs={12} md={6} mt={6}
                sx={{m: 'auto', px: 2}}
                textAlign={"center"}
                justifyContent="center"
                alignContent={"center"}>

                <Typography variant="h5" sx={{fontFamily: 'Poppins', mb: 6}}>
                    Create account
                </Typography>

                <Grid container item>
                    <Grid item xs mr={2}>
                        <TextField
                            variant="outlined"
                            label="Station Name"
                            type={"text"}
                            fullWidth={true}
                            required
                            sx={{mb: 3}} onChange={handleChange("stationName")} />
                    </Grid>
                    <Grid xs={4}>
                        <FormControl required sx={{ mb: 3 }} fullWidth>
                            <InputLabel id="station-type">Type</InputLabel>
                            <Select
                                labelId="station-type-label"
                                id="station-type"
                                value={registerValues.stationType}
                                label="Type *"
                                onChange={handleChange("stationType")} >
                                <MenuItem value=""><em>None</em></MenuItem>
                                <MenuItem value={10}>MEDICAL</MenuItem>
                                <MenuItem value={11}>POLICE</MenuItem>
                                <MenuItem value={12}>FIRE</MenuItem>
                            </Select>
                        </FormControl>
                    </Grid>
                </Grid>

                <Grid container item>
                    <Grid item xs mr={1}>
                        <TextField
                            variant="outlined"
                            label="Telephone"
                            type={"telephone"}
                            fullWidth={true}
                            sx={{mb: 3}} onChange={handleChange("stationPhoneNumber")} />
                    </Grid>
                    <Grid xs ml={1}>
                        <TextField
                            variant="outlined"
                            label="Coordinates"
                            type={"text"}
                            fullWidth={true}
                            value={registerValues.coordinates}
                            sx={{mb: 3}} onChange={handleChange("coordinates")} />
                    </Grid>
                </Grid>

                <Grid container item>
                    <Grid item xs mr={1}>
                        <TextField
                            variant="outlined"
                            label="Email"
                            type={"email"}
                            fullWidth={true}
                            sx={{mb: 3}} onChange={handleChange("email")} />
                    </Grid>
                    <Grid xs ml={1}>
                        <FormControl required fullWidth={true} sx={{mb: 3}} variant="outlined">
                            <InputLabel htmlFor="outlined-adornment-password">Password</InputLabel>
                            <OutlinedInput
                                id="outlined-adornment-password"
                                type={registerValues.showPassword ? 'text' : 'password'}
                                value={registerValues.password}
                                onChange={handleChange('password')}
                                endAdornment={
                                    <InputAdornment position="end">
                                        <IconButton
                                            aria-label="toggle password visibility"
                                            onClick={handleClickShowPassword}
                                            onMouseDown={handleMouseDownPassword}
                                            edge="end">
                                            {registerValues.showPassword ? <VisibilityOff /> : <Visibility />}
                                        </IconButton>
                                    </InputAdornment>
                                }
                                label="Password"
                            />
                        </FormControl>
                    </Grid>
                </Grid>

                <Button
                    type={"submit"}
                    variant="contained"
                    size={"large"}
                    sx={{py: '10pt'}} fullWidth={true}
                    onClick={handleOnSubmitLoginData}>
                    Register
                </Button>

                <Typography mt={3} mb={5}>
                    Already have an account? <Link to={"/login"}>Login</Link>.
                </Typography>
            </Grid>

        </form>
    );
}