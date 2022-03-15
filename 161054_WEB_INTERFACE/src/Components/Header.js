/**
 * React Component for showing header
 *
 * -> This should be reactive
 */


import React from "react";

import Menu from "@mui/icons-material/Menu"
import LoginOutlined from "@mui/icons-material/LoginOutlined";
import Home from "@mui/icons-material/Home";

import AppBar from "@mui/material/AppBar"
import Toolbar from "@mui/material/Toolbar"
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import Hidden from "@mui/material/Hidden";
// import Drawer from "@mui/material/Drawer"
// import List from "@mui/material/List";
// import Divider from "@mui/material/Divider";
// import {ListItem, ListItemText, ListItemButton} from "@mui/material";

import "./css/custom_css/Header.css"
import {Link} from "react-router-dom";


/**
 * Displays the side navigation drawer, that contains additional navigation options
 *
 */
/*class NavigationDrawer extends React.Component {
    constructor(props) {
        super(props);

        this.props = props
    }

    render() {
        return (
            <Drawer
                variant="temporary"
                anchor="left" open={this.props.isOpen}
                onClose={this.props.onClose}>

                <img src={logo} alt="logo" className="header-logo"/>

                <Divider />

                <List>

                    <ListItemButton>
                        <ListItemText primary={"Dlamini Lindelwa"}/>
                    </ListItemButton>

                    <ListItemButton>
                        <ListItemText primary={"Dlamini Lindelwa"}/>
                    </ListItemButton>

                    <ListItemButton>
                        <ListItemText primary={"Dlamini Lindelwa"}/>
                    </ListItemButton>

                    <ListItemButton>
                        <ListItemText primary={"Dlamini Lindelwa"}/>
                    </ListItemButton>

                    <ListItem>
                        <ListItemText primary={"Dlamini Lindelwa"}/>
                    </ListItem>

                </List>
            </Drawer>
        );
    };
}*/


export default class Header extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            open: false
        }

        this.handleMenuButtonClick = this.handleMenuButtonClick.bind(this)
    }

    handleMenuButtonClick() {

        this.setState(prevState => {
            return {
                open: !prevState.open
            }
        })
    }

    render() {
        return <AppBar position="static" color="primary" sx={{mb: 3}}>
            <Toolbar>
                <IconButton
                    edge="start"
                    color="inherit"
                    aria-label="menu"
                    sx={{mr: 2}} onClick={this.handleMenuButtonClick}>
                    <Menu/>
                </IconButton>

                <Hidden smDown>
                <Typography variant="h5" color="inherit"  className="brandName" component={"div"}>
                    Eswatini Emergency Services
                </Typography>
                </Hidden>

                <IconButton sx={{m: 1, ml: 'auto'}} style={{background: 'white'}} to={"/login"} component={Link}>
                    <LoginOutlined />
                </IconButton>

                <IconButton sx={{mr:1}} style={{background: 'white'}} to={"/"} component={Link}>
                    <Home />
                </IconButton>

                {/*<NavigationDrawer isOpen={this.state.open} onClose={this.handleMenuButtonClick}/>*/}
            </Toolbar>
        </AppBar>
    };
}








