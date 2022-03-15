/**
 * @author Dlamini Lindelwa [02/02/22]
 *
 * WelcomeText.js
 * ---------------------------------------------------------------------------------------------------------------------
 *
 * Component for displaying main page welcome text
 *
 */
import React from "react";
import Typography from "@mui/material/Typography";

import "./css/custom_css/Main.css"


export default function WelcomeText() {

    return (
        <React.Fragment>
            <Typography className={"welcome-text"} variant="h3" color="primary" align="center" component={"h4"}>
                Welcome to Eswatini Emergency Services
            </Typography>
        </React.Fragment>
    );

}