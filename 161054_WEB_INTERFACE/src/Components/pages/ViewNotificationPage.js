import React, {useContext, useEffect, useState} from "react";
import Typography from "@mui/material/Typography";

import "../css/custom_css/Main.css"
import Drawer from "../home/Drawer";
import Divider from "@mui/material/Divider";
import {ArrowBack, Send} from "@mui/icons-material";
import IconButton from "@mui/material/IconButton";
import Grid from "@mui/material/Grid";
import {Link} from "react-router-dom";
import Box from "@mui/material/Box";
import {ChatContext} from "../../context/ChatContext";
import {doc, onSnapshot, updateDoc, arrayUnion, Timestamp, serverTimestamp} from "firebase/firestore";
import {db} from "../../config/firebaseConfig";
import { MessageReceiver, MessageSender } from "./Message";
import "../css/custom_css/input.css";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import {AuthContext} from "../../context/AuthContext";
import {v4 as uuid} from "uuid";
import {Alert, Snackbar} from "@mui/material";

export default function ViewNotificationPage() {
    const {data} = useContext(ChatContext);
    const {currentUser} = useContext(AuthContext);
    const [messages, setMessages] = useState([])
    const [reply, setReply] = useState("")
    const [open, setOpen] = useState(false)
    const [notification, setNotification] = useState("")

    const conversationId = data.conversationId
    const victim = data.user

    useEffect(() => {
        const unSub = onSnapshot(doc(db, "LOGGED_EMERGENCIES", data.conversationId), (document1) => {
            console.log(document1.exists(), document1.data())
            document1.exists() && setMessages(document1.data().messages)
            const box = document.getElementById("message-box")
            box.scrollTop = box.scrollHeight;
        })

        return () => {
            unSub()
        }
    }, [conversationId, setMessages])

    const onInputChange = (e) => {
        setReply(e.target.value)
        console.log(reply)
    }

    const onCloseSnackBar = () => {
        setOpen(false)
    }

    const sendReply = async () => {
        if (reply === "") {
            console.log("Can not send empty message!")
            return
        }
        try {
            await updateDoc(doc(db, "LOGGED_EMERGENCIES", data.conversationId), {
                messages: arrayUnion ({
                    id: uuid(),
                    emergencyType: "",
                    emergencyMessageBody: reply,
                    emergencyLocation: "null",
                    timeStamp: Timestamp.now(),
                    senderUid: currentUser.uid
                })
            })
            await updateDoc(doc(db, "USER_DASHBOARD", victim.uid), {
                [conversationId+".lastMessage"]: reply,
                [conversationId+".timeStamp"]: serverTimestamp(),
                [conversationId+".senderUid"]:currentUser.uid,
                [conversationId+".uid"]:currentUser.uid
            })

            await updateDoc(doc(db, "USER_DASHBOARD", currentUser.uid), {
                [conversationId+".lastMessage"]: reply,
                [conversationId+".timeStamp"]: serverTimestamp(),
                [conversationId+".senderUid"]:currentUser.uid,
                [conversationId+".uid"]:victim.uid
            })
            setReply("")
        }catch (e) {
            console.log("ERROR", e)
        }
    }

    return (
        <React.Fragment>
            <Snackbar
                anchorOrigin={{vertical: "bottom", horizontal:"right"}}
                open={open} autoHideDuration={600}
                onClose={onCloseSnackBar}>
                <Alert severity="success" sx={{ width: '100%' }}>
                    {notification}
                </Alert>
            </Snackbar>
            <Drawer>
                <Grid container mb={1}>
                    <Grid item mr={2}>
                        <IconButton style={{background: '#e8dede'}} to={"/notifications"} component={Link}>
                            <ArrowBack/>
                        </IconButton>
                    </Grid>
                    <Grid item xs mr={4}>
                        <Typography color={"primary"} component={"h5"} mt={1}>
                            From: {victim.displayName}
                        </Typography>
                    </Grid>
                </Grid>
                <Divider/>

                <div className={"chat-feed"} id="message-box">
                    {messages && messages.map((message, index) => {
                        return message.senderUid === currentUser.uid ? ( <MessageSender message={message} key={index}/>)
                            : (<MessageReceiver message={message} key={index}/>)
                    } )}
                </div>

                <Box component={"footer"}
                     sx={{marginTop: 'calc(10% + 60px)', position: 'fixed', bottom: 10, width: 'calc(100% - 100px)'}}>
                    <Grid container>
                        <Grid item container xs={10}>
                            <TextField
                                variant="filled"
                                label="Reply"
                                type={"text"}
                                value={reply}
                                onChange={onInputChange}
                                fullWidth={true}/>
                        </Grid>
                        <Grid item container xs={2} ml={"auto"} display={"flex"}>
                            <Button type={"submit"} variant="contained"
                                    endIcon={<Send/>} color={"success"}
                                    sx={{marginLeft: "auto"}}
                            onClick={sendReply}>
                                Send
                            </Button>
                        </Grid>
                    </Grid>
                </Box>

            </Drawer>
        </React.Fragment>
    );
}

// https://eswatiniemergencyservices.netlify.app/