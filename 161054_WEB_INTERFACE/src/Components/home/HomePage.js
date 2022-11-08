import React, {useState} from "react";
import Typography from "@mui/material/Typography";

import "../css/custom_css/Main.css"
import Drawer from "./Drawer";
import Divider from "@mui/material/Divider";
import {useLocation, useNavigate} from "react-router-dom";
import {Alert, Card, CardActions, CardContent, Paper, Snackbar} from "@mui/material";
import Grid from "@mui/material/Grid";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";
import {
    Facebook,
    Instagram,
    OpenInFull,
    OpenInNew,
    OpenInNewOutlined,
    OpenInNewRounded,
    OpenWith,
    Twitter
} from "@mui/icons-material";
import IconButton from "@mui/material/IconButton";


export default function HomePage() {

    const location = useLocation();
    const navigate = useNavigate();
    const welcomeMessage = location.state != null ? location.state.welcomeMessage : null
    const [open, setOpen] = useState(false)

    /*if (welcomeMessage != null) {
        setOpen(true);
    }*/

    const onCloseSnackBar = () => {
        setOpen(false)
    }

    return (
        <React.Fragment>
            <Snackbar
                anchorOrigin={{vertical: "bottom", horizontal: "right"}}
                open={open} autoHideDuration={600}
                onClose={onCloseSnackBar}>
                <Alert severity="success" sx={{width: '100%'}}>
                    {welcomeMessage}
                </Alert>
            </Snackbar>
            <Drawer>
                <Typography variant="h4" color="primary" component={"h4"}>
                    Welcome to Eswatini Emergency Services.
                </Typography>
                <Typography color="gray" component={"h6"} mt={1} style={{fontSize: "0.9em", fontWeight: "bold"}}>
                    A system portal to access logged emergencies.
                </Typography>
                <Divider/>

                <Grid container mt={4} spacing={1}>
                    <Grid item xs={4}>
                        <Card variant={"outlined"}>
                            <CardContent>
                                <Typography gutterBottom variant="h5" component="div">
                                    Stations List
                                </Typography>
                                <Typography variant="body2" color="text.secondary">
                                    Browse and edit registered emergency stations.
                                </Typography>
                            </CardContent>
                            <CardActions>
                                <IconButton size="small" onClick={ (e) => {
                                    e.preventDefault()
                                    navigate("/all-stations")
                                }}><OpenInNewOutlined /></IconButton>
                            </CardActions>
                        </Card>
                    </Grid>
                    <Grid item xs={4}>
                        <Card variant={"outlined"}>
                            <CardContent>
                                <Typography gutterBottom variant="h5" component="div">
                                    Emergencies
                                </Typography>
                                <Typography variant="body2" color="text.secondary">
                                    Browse all logged emergencies and respond to them.
                                </Typography>
                            </CardContent>
                            <CardActions>
                                <IconButton size="small" onClick={ (e) => {
                                    e.preventDefault()
                                    navigate("/notifications")
                                }}><OpenInNewOutlined /></IconButton>
                            </CardActions>
                        </Card>
                    </Grid>
                    <Grid item xs={4}>
                        <Card variant={"outlined"}>
                            <CardContent>
                                <Typography gutterBottom variant="h5" component="div">
                                    Public Notice
                                </Typography>
                                <Typography variant="body2" color="text.secondary">
                                    Push emergency public notifications warning the general public.
                                </Typography>
                            </CardContent>
                            <CardActions>
                                <IconButton size="small" onClick={ (e) => {
                                    e.preventDefault()
                                    navigate("/warnings")
                                }}><OpenInNewOutlined /></IconButton>
                            </CardActions>
                        </Card>
                    </Grid>
                </Grid>


                {/*Begin Footer */}
                <Paper variant={"outlined"} style={{marginTop: 10, background: "rgb(0,0,0,0.12)"}}>
                    <Box display={"block"} sx={{ justifyContent: 'center' }} py={2}>
                        <Grid container display={"flex"} justifyContent={"center"} spacing={0}>
                            <Grid item>
                                <IconButton color={"primary"}>
                                    <Instagram />
                                </IconButton>
                            </Grid>
                            <Grid item>
                                <IconButton color={"primary"}>
                                    <Twitter />
                                </IconButton>
                            </Grid>
                            <Grid item>
                                <IconButton color={"primary"}>
                                    <Facebook />
                                </IconButton>
                            </Grid>
                        </Grid>
                        <Typography variant={"subtitle2"} textAlign={"center"} fontFamily={"Poppins"}
                                    fontWeight={"bold"} color={"#1976d2"}>
                            Copyright &copy; 2022
                        </Typography>
                    </Box>
                </Paper>
            </Drawer>
        </React.Fragment>
    );

}