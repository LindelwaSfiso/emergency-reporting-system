import React, {useContext, useEffect, useState} from "react";
import Typography from "@mui/material/Typography";

import "../css/custom_css/Main.css"
import Drawer from "../home/Drawer";
import Divider from "@mui/material/Divider";
import {
    Avatar, FormControl,
    InputAdornment,
    InputLabel,
    ListItemAvatar,
    ListItemText,
    OutlinedInput
} from "@mui/material";
import List from "@mui/material/List";
import Grid from "@mui/material/Grid";
import {Search} from "@mui/icons-material";
import ListItemButton from "@mui/material/ListItemButton";
import {useNavigate} from "react-router-dom";
import IconButton from "@mui/material/IconButton";
import {ChatContext} from "../../context/ChatContext";
import {collection, doc, getDocs, getDoc, setDoc, serverTimestamp, updateDoc, onSnapshot} from "firebase/firestore";
import {db} from "../../config/firebaseConfig";
import {AuthContext} from "../../context/AuthContext";
import {forEach} from "react-bootstrap/ElementChildren";

function stringToColor(string) {
    let hash = 0;
    let i;

    /* eslint-disable no-bitwise */
    for (i = 0; i < string.length; i += 1) {
        hash = string.charCodeAt(i) + ((hash << 5) - hash);
    }

    let color = '#';

    for (i = 0; i < 3; i += 1) {
        const value = (hash >> (i * 8)) & 0xff;
        color += `00${value.toString(16)}`.substr(-2);
    }
    /* eslint-enable no-bitwise */

    return color;
}

function stringAvatar(name) {
    return {
        sx: {
            bgcolor: stringToColor(name),
        },
        children: `${name.split(' ')[0][0]}${name.split(' ')[1][0]}`,
    };
}

export default function NotificationsPage() {
    const [values, setValues] = useState({
        searchText: "",
        users: [],
        errors: "",
        dashBoardMessages: []
    })
    const {dispatch} = useContext(ChatContext);
    const {currentUser} = useContext(AuthContext)
    const navigation = useNavigate();

    const handleChange = (prop) => (event) => {
        setValues({...values, [prop]: event.target.value});
    };

    const handleClickSearch = async (e) => {
        e.preventDefault()
        console.log(values)

        // for now, get all users
        const users = await getDocs(collection(db, "USERS"));
        let a = []
        users.forEach((doc) => {
            a.push(doc.data())
        })
        setValues({...values, users: a})
    }

    const searchForEmergency = async (e) => {
        if (e.key === "Enter") {
            await handleClickSearch(e)
        }
    }

    useEffect(() => {
        const getDashBoard = () => {
            const unSub = onSnapshot(doc(db, "USER_DASHBOARD", currentUser.uid), (doc) => {
                const data = doc.data()
                if (data !== undefined) {
                    if (data.hasOwnProperty("created")) {delete data.created}
                    setValues({...values, dashBoardMessages: data})
                } else {

                }
            })
            return () => {
                unSub();
            }
        }
        currentUser.uid && getDashBoard()
    }, [currentUser.uid])

    const navigateToChat = async (e, info) => {
        e.preventDefault()

        const userInfo = info[1]
        console.log(userInfo, userInfo.uid, currentUser.uid)

        const combinedId = currentUser.uid < userInfo.uid
            ? currentUser.uid + "_" + userInfo.uid
            : userInfo.uid + "_" + currentUser.uid

            const conversation = await getDoc(doc(db, "LOGGED_EMERGENCIES", combinedId))
            if (!conversation.exists()) {
                await setDoc(doc(db, "LOGGED_EMERGENCIES", combinedId), {
                    messages: [],
                    created: "true"
                })
            }
           /* await updateDoc(doc(db, "USER_DASHBOARD", currentUser.uid), {
                [combinedId + ".uid"]: userInfo.uid,
                [combinedId + ".displayName"]: userInfo.fullName,
                [combinedId + ".timeStamp"]: serverTimestamp(),
                [combinedId + ".lastMessage"]:""
            })
            await updateDoc(doc(db, "USER_DASHBOARD", userInfo.uid), {
                [combinedId + ".uid"]: currentUser.uid,
                [combinedId + ".displayName"]: currentUser.displayName,
                [combinedId + ".timeStamp"]: serverTimestamp(),
                [combinedId + ".lastMessage"]: ""
            })
*/
        setValues({users: [], searchText: ""})

        dispatch({type:"CHANGE_USER", payload: userInfo})
        navigation("/notification-details")
    }

    return (
        <React.Fragment>
            <Drawer>
                <Grid container>
                    <Grid item xs>
                        <Typography variant="h5" color="primary" component={"h5"}>
                            Logged emergencies.
                        </Typography>
                        <Typography color="gray" component={"h6"} mt={1} style={{fontSize: "0.8em"}}>
                            Due to high traffic, use search button to query specific emergency.
                        </Typography>
                    </Grid>
                    <Grid item xs ml={"auto"} alignItems={"center"} display={"flex"}>
                        <FormControl variant="outlined" sx={{ml: 'auto'}}>
                            <InputLabel htmlFor="search-label">Search with ID</InputLabel>
                            <OutlinedInput
                                id="search-label"
                                type={'text'}
                                value={values.searchText}
                                onChange={handleChange('searchText')}
                                onKeyDown={searchForEmergency}
                                endAdornment={
                                    <InputAdornment position="end">
                                        <IconButton
                                            aria-label="toggle password visibility"
                                            onClick={handleClickSearch}
                                            edge="end">
                                            <Search/>
                                        </IconButton>
                                    </InputAdornment>
                                }
                                label="Search with ID"
                            />
                        </FormControl>
                    </Grid>
                </Grid>
                <Divider/>
                <List sx={{width: '100%', bgcolor: 'background.paper'}}>
                    {values.dashBoardMessages && Object.entries(values.dashBoardMessages)?.map((message, index) => (
                        <div key={index}>
                            <ListItemButton onClick={(e) => navigateToChat(e, message)}>
                                <ListItemAvatar>
                                    <Avatar {...stringAvatar(message[1].displayName)} />
                                </ListItemAvatar>
                                <ListItemText primary={message[1].displayName} secondary={message[1].lastMessage}/>
                            </ListItemButton><Divider variant="inset" component="li"/>
                        </div>
                    ))}

                    {values.users && values.users.map((data, index) => (
                        <div key={index}>
                            <ListItemButton onClick={(e) => navigateToChat(e, data)}>
                                <ListItemAvatar>
                                    <Avatar {...stringAvatar(data.fullName)} />
                                </ListItemAvatar>
                                <ListItemText primary={data.fullName} secondary={data.email}/>
                            </ListItemButton><Divider variant="inset" component="li"/>
                        </div>
                    ))}
                </List>
            </Drawer>
        </React.Fragment>
    );
}

