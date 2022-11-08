import React from "react";
import Grid from "@mui/material/Grid"
import Typography from "@mui/material/Typography";
import {Link} from "react-router-dom";
import {Home} from "@mui/icons-material";
import IconButton from "@mui/material/IconButton";
import {Container} from "@mui/material";


export default class ErrorPage extends React.Component {
    render() {
        return (
            <Container>
                <IconButton
                    style={{background: '#e8dede', margin: 5}} to={"/"} component={Link}>
                    <Home />
                </IconButton>
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
            </Container>
        );
    };
}

