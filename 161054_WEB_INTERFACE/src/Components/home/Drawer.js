import * as React from 'react';
import { useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import List from '@mui/material/List';
import CssBaseline from '@mui/material/CssBaseline';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';

import {AppBar, Drawer, DrawerHeader, miniDrawerMenuItems, settingsMenuItems} from "./configDrawer";
import {useNavigate, Link} from "react-router-dom";
import {AccountCircleRounded} from "@mui/icons-material";


export default function MiniDrawer({children}) {
    const theme = useTheme();
    const [open, setOpen] = React.useState(false);
    const navigate = useNavigate();

    const handleDrawerOpen = () => {
        setOpen(true);
    };

    const handleDrawerClose = () => {
        setOpen(false);
    };

    return (
        <Box sx={{ display: 'flex' }}>
            <CssBaseline />
            <AppBar position="fixed" open={open}>
                <Toolbar>
                    <IconButton
                        color="inherit"
                        aria-label="open drawer"
                        onClick={handleDrawerOpen}
                        edge="start"
                        sx={{marginRight: 5, ...(open && { display: 'none' }),}}>
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" noWrap component="div">
                        Eswatini Emergency Service
                    </Typography>

                    <IconButton sx={{ml: 'auto'}} style={{background: '#c6bfbf'}} to={"/edit-profile"} component={Link}>
                        <AccountCircleRounded />
                    </IconButton>
                </Toolbar>
            </AppBar>
            <Drawer variant="permanent" open={open}>
                <DrawerHeader>
                    <IconButton onClick={handleDrawerClose}>
                        {theme.direction === 'rtl' ? <ChevronRightIcon /> : <ChevronLeftIcon />}
                    </IconButton>
                </DrawerHeader>
                <Divider />
                <List>
                    {miniDrawerMenuItems.map((item, index) => (
                        <ListItem key={index} disablePadding sx={{ display: 'block' }}>
                            <ListItemButton
                                onClick={ (e) => {
                                    e.preventDefault()
                                    navigate(item.url)
                                }}
                                sx={{minHeight: 48, justifyContent: open ? 'initial' : 'center', px: 2.5}}>
                                <ListItemIcon sx={{minWidth: 0, mr: open ? 3 : 'auto', justifyContent: 'center'}}>
                                    {item.icon}
                                </ListItemIcon>
                                <ListItemText primary={item.actionName} sx={{ opacity: open ? 1 : 0 }} />
                            </ListItemButton>
                        </ListItem>
                    ))}
                </List>
                <Divider />
                <List>
                    {settingsMenuItems.map((item, index) => (
                        <ListItem key={index} disablePadding sx={{ display: 'block' }}>
                            <ListItemButton
                                onClick={ (e) => {
                                    e.preventDefault()
                                    navigate(item.url)
                                }}
                                sx={{minHeight: 48, justifyContent: open ? 'initial' : 'center', px: 2.5}}>
                                <ListItemIcon sx={{minWidth: 0, mr: open ? 3 : 'auto', justifyContent: 'center'}}>
                                    {item.icon}
                                </ListItemIcon>
                                <ListItemText primary={item.actionName} sx={{ opacity: open ? 1 : 0 }} />
                            </ListItemButton>
                        </ListItem>
                    ))}
                </List>
            </Drawer>

            <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
                <DrawerHeader />
                {children}
            </Box>
        </Box>
    );
}
