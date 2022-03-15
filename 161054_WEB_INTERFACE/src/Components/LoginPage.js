/**
 * @author Dlamini Lindelwa [02/02/22]
 *
 * LoginPage.js
 * ---------------------------------------------------------------------------------------------------------------------
 *
 * Login page for authentication users.
 */

import React from "react";
import TextField from "@mui/material/TextField"
import Typography from "@mui/material/Typography"
import Button from "@mui/material/Button"
import Paper from "@mui/material/Paper";
import Grid from "@mui/material/Grid"
import {Link} from "react-router-dom";

export default class LoginPage extends React.Component {

    render() {
        return (
            <Paper sx={{mt: 2, pb: 4}} variant="outlined">

                <Grid
                    container
                    justifyContent="center"
                    alignContent={"center"}
                    sx={{m: 'auto', px: 2}}
                    minHeight={"60vh"}>

                    <Grid item textAlign={"center"} xs={12} md={5}>
                        <Typography variant="h4" sx={{fontFamily: 'Poppins', my: 4}}>
                            Enter your credentials
                        </Typography>

                        <TextField variant="outlined" label="Username" fullWidth={true} sx={{mb: 3}}/>

                        <TextField variant="outlined" label="Password" fullWidth={true} type={"password"} sx={{mb: 3}}/>

                        <Button
                            variant="contained"
                            type={"submit"}
                            size={"large"}
                            to={"/"} component={Link}
                            sx={{py: '15pt'}} fullWidth={true}>
                            Log In
                        </Button>
                    </Grid>

                </Grid>

            </Paper>
        );
    };
}