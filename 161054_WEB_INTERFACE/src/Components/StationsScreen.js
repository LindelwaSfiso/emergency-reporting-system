import React, {Component, Fragment} from "react";
import ListItem from "@mui/material/ListItem";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import List from "@mui/material/List";
import ListItemText from "@mui/material/ListItemText";
import {Avatar, Fab, ListItemAvatar} from "@mui/material";
import {Add, Delete, Edit, LocalFireDepartment, LocalHospitalOutlined, LocalPoliceOutlined} from "@mui/icons-material";
import IconButton from "@mui/material/IconButton";

import { stations } from "../config/configData";

function IconType({type}) {
    let icon, bg;
    switch (type) {
        case "POLICE":
            bg = 'blue'
            icon = <LocalPoliceOutlined/>
            break
        case "FIRE":
            bg = 'red'
            icon = <LocalFireDepartment/>
            break
        default:
            bg = 'green'
            icon = <LocalHospitalOutlined/>
    }

    return (
        <Avatar sx={{width: '56px', height: '56px', mr: 2, background: bg}}>
            {icon}
        </Avatar>
    );
}


export default class StationsScreen extends Component {
    constructor(props) {
        super(props);

        this.state = {
            stations: stations
        }

        this.addStation = this.addStation.bind(this)
        this.removeStation = this.removeStation.bind(this)
    }

    addStation() {
        this.setState({
            stations: [
                {
                    name: "Matsapha Police Station",
                    type: "POLICE",
                    location: "32.1313, 13.1313",
                    phone_number: "7648 0479"
                },
                ...this.state.stations
            ]
        })
    }

    removeStation(key) {
        console.log(key, this.state.stations[key])
        this.setState(state => {
            return state.stations.splice(key, 1)
        })
    }

    render() {
        return (
            <Fragment>
                <Typography variant={"h4"} justifyContent={"center"} textAlign={"center"}>
                    List of Available Emergency Stations
                </Typography>

                <Divider sx={{mt: 2}}/>

                <List>
                    {
                        this.state.stations.map((station, index) => {
                            return (
                                <Fragment key={index}>
                                    <ListItem key={index} secondaryAction={
                                        <Fragment>
                                            <IconButton aria-label={"edit-action"}>
                                                <Edit/>
                                            </IconButton>

                                            <IconButton aria-label={"delete-action"}
                                                        onClick={() => this.removeStation(index)}>
                                                <Delete/>
                                            </IconButton>
                                        </Fragment>
                                    }>
                                        <ListItemAvatar>
                                            <IconType type={station.type}/>
                                        </ListItemAvatar>
                                        <ListItemText primary={station.name} secondary={
                                            `(${station.location})   ${station.phone_number}`
                                        } primaryTypographyProps={{
                                            color: 'primary',
                                            fontWeight: 'medium',
                                            variant: 'h5'
                                        }}/>
                                    </ListItem>

                                    <Divider/>
                                </Fragment>
                            )
                        })
                    }
                </List>

                <Fab color={"primary"}
                     sx={{left: "auto", right: "10px", bottom: "60px", position: "fixed", zIndex: "10000"}}
                     onClick={this.addStation}
                >
                    <Add/>
                </Fab>

            </Fragment>
        );
    }
}


