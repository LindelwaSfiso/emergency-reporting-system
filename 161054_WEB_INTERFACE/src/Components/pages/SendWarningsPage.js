import React, {useState} from "react";
import Typography from "@mui/material/Typography";

import "../css/custom_css/Main.css"
import Drawer from "../home/Drawer";
import Divider from "@mui/material/Divider";
import Radio from '@mui/material/Radio';
import RadioGroup from '@mui/material/RadioGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import FormControl from '@mui/material/FormControl';
import FormLabel from '@mui/material/FormLabel';
import Grid from "@mui/material/Grid";
import {Alert, Snackbar, TextField} from "@mui/material";
import Button from "@mui/material/Button";
import {Send} from "@mui/icons-material";
import {doc, collection, addDoc, Timestamp, serverTimestamp} from "firebase/firestore";
import {db} from "../../config/firebaseConfig";


export default function SendWarningsPage() {
    const [values, setValues] = useState({
        broadCastType: "",
        broadCastMessage: "",
        isError: "success", // should either be success (successfully sent broadcast) or error (couldn't send)
        feedbackMessage: "",
        open: false
    })

    const handleChange = (prop) => (event) => {
        setValues({...values, [prop]: event.target.value});
    };

    const submitForm = async (e) => {
        e.preventDefault()
        console.log(values)
        
        if (values.broadCastMessage === ""  || values.broadCastMessage === "") {
            setValues({
                ...values,
                isError: "error",
                feedbackMessage: "Message can not be empty!",
                open: true
            })
            return;
        }

        try {
            await addDoc(collection(db, "PUBLIC_NOTICE"), {
                message: values.broadCastMessage,
                timeStamp:  serverTimestamp(),
                noticeType: values.broadCastType
            });

            setValues({
                ...values,
                broadCastType: "",
                broadCastMessage: "",
                isError: "success",
                feedbackMessage: "Successfully sent broadcast message to all users!",
                open: true
            })

        } catch (e) {
            console.log(e)
            setValues({
                ...values,
                broadCastType: "",
                broadCastMessage: "",
                isError: "error",
                feedbackMessage: "Error sending public notice message! Try again.",
                open: true
            })
        }
    }

    return (
        <React.Fragment>
            <Drawer>
                <Typography variant="h4" color="primary" component={"h4"}>
                    Welcome to public notifications page.
                </Typography>
                <Typography color="gray" component={"h6"} mt={1} style={{fontSize: "0.9em"}}>
                    Send notification to the general public.
                </Typography>
                <Divider/>

                <form onSubmit={submitForm}>
                    <Grid container mt={2}>
                        <Grid item xs>
                            <FormControl>
                                <FormLabel id="type-label">Type</FormLabel>
                                <RadioGroup
                                    row
                                    aria-labelledby="type-label"
                                    name="row-radio-buttons-group" onChange={handleChange("broadCastType")}>
                                    <FormControlLabel value="MEDICAL" control={<Radio/>} label="Medical"/>
                                    <FormControlLabel value="POLICE" control={<Radio/>} label="Police"/>
                                    <FormControlLabel value="FIRE" control={<Radio/>} label="Fire"/>
                                </RadioGroup>
                            </FormControl>
                        </Grid>
                        <Grid item xs ml={"auto"} alignItems={"center"} display={"flex"}>
                            <Button type={"submit"} variant="contained" endIcon={<Send/>} sx={{ml: 'auto'}}
                                    color={"success"}>
                                Broadcast
                            </Button>
                        </Grid>
                    </Grid>

                    <TextField
                        sx={{mt: 3}}
                        fullWidth
                        id="filled-multiline-static"
                        multiline
                        rows={7}
                        placeholder="Public Notice Warning"
                        variant="outlined"
                        onChange={handleChange("broadCastMessage")}
                    />
                </form>
                <Snackbar
                    anchorOrigin={{vertical: "bottom", horizontal:"right"}}
                    open={values.open} autoHideDuration={1000}>
                    <Alert severity={values.isError} sx={{ width: '100%' }} >
                        {values.feedbackMessage}
                    </Alert>
                </Snackbar>
            </Drawer>
        </React.Fragment>
    );
}

