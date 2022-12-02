import "../css/custom_css/input.css";
import 'react-chat-widget/lib/styles.css';


export const MessageSender = ({message}) => {
    return <div className={"message-wrapper-sender"}>
        <p className={"sender-bubble"}>{message.emergencyMessageBody}</p>
    </div>
}

export const MessageReceiver = ({message}) => {
    // https://www.google.com/mas/place/-26.48,31.31
    const url = "https://maps.google.com/?q=" + message.emergencyLocation;

    return <div className={"message-wrapper-receiver"}>
        <p className={"receiver-bubble"}>{message.emergencyMessageBody}</p>
        <a className={"receiver-bubble"} href={url} target={"_blank"}>{message.emergencyLocation}</a>
        {message.timeStamp && <p className={"receiver-bubble"}>{message.timeStamp.toDate().toDateString()}</p>}
    </div>
}


/*{
    "emergencyType": "11",
    "senderUid": "cmTdAYrJUANbodUkXmQOEdvg9qf1",
    "emergencyLocation": "-26.4856268,31.3087212",
    "emergencyMessageBody": "Lindelwa Sfiso Dlamini -- (-26.4856268, 31.3087212)",
    "timeStamp": {
        "seconds": 1667077829,
        "nanoseconds": 17000000
    }

<a className={"sender-bubble"}>{message.emergencyLocation}</a>
}*/
