import React from "react";
import Grid from "@mui/material/Grid"
import Typography from "@mui/material/Typography";


export default class ErrorPage extends React.Component {
    render() {
        return (
            <Grid justifyContent="center" container sx={{mt: 15}}>
                <Grid item justifySelf={"center"} alignItems={"center"}>
                    <Typography variant={"h1"} color={"danger"} component={"h1"} align={"center"}>
                        404
                    </Typography>
                    <Typography variant={"subtitle1"} align={"center"}>
                        Page Not Found
                    </Typography>
                </Grid>
            </Grid>
        );
    };
}

