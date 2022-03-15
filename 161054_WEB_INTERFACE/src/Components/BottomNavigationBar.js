/**
 * Bottom navigation bar for navigating around the app
 */

import React, {useState} from "react";
import BottomNavigation from "@mui/material/BottomNavigation"
import BottomNavigationAction from "@mui/material/BottomNavigationAction";
import {DashboardOutlined, HomeRounded, LocationSearching, Settings} from "@mui/icons-material";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import {useNavigate} from "react-router-dom";

export default function BottomNavigationBar(props) {

    const [selected, setSelected] = useState(props.selected)
    const navigation = useNavigate()

    function handleOnChange(value) {

        setSelected(value)

        switch (value) {
            case 1:
                navigation("/", {replace: true})
                break
            case 2:
                navigation("/stations", {replace: true})
                break
            default:
                navigation("/login", {replace:true})
        }
    }

    return (
        <React.Fragment>
            <List sx={{width: "100%", position: "fixed", bottom: "56px", padding:0}}>
                <Divider/>
            </List>

            <BottomNavigation
                showLabels={true}
                value={selected}
                onChange={(event, newValue) => {
                    handleOnChange(newValue)
                }}
                sx={{width: "100%", position: "fixed", bottom: "0pt"}}>

                <BottomNavigationAction label={"Home"} icon={<HomeRounded/>}/>
                <BottomNavigationAction label={"Emergencies"} icon={<DashboardOutlined/>}/>
                <BottomNavigationAction label={"Stations"} icon={<LocationSearching/>}/>
                <BottomNavigationAction label={"Settings"} icon={<Settings/>}/>
            </BottomNavigation>
        </React.Fragment>
    );
}