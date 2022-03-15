/**
 *
 * Main.js
 * @author Dlamini Lindelwa [01/01/22]
 *
 * Show the main content for the whole app
 * ---------------------------------------------------------------------------------------------------------------------
 */

import React from "react";
import Container from "@mui/material/Container";

import "./css/custom_css/Main.css"

export default class Main extends React.Component {
    render() {
        return (
            <Container sx={{mb: "56px"}}>
                {this.props.children}
            </Container>
        )
    }
}




