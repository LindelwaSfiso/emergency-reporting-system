import React from "react";
import Typography from "@mui/material/Typography";

import "../css/custom_css/Main.css"
import Drawer from "../home/Drawer";


export default function SendWarnings() {

    return (
        <React.Fragment>
            <Drawer>
                <Typography className={"welcome-text"} variant="h3" color="primary" align="center" component={"h4"}>
                    Edit Profile page
                </Typography>
            </Drawer>
        </React.Fragment>
    );
}

