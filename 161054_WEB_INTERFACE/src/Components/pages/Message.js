import "../css/custom_css/input.css";


const Message = ({message}) => {
    return <div className={"message-item"}>
        <div className={"message-item-body"}>{message.emergencyMessageBody}</div>
        <a className={"message-item-coordinates"}>{message.emergencyLocation}</a>
        <span className={"message-item-timestamp"}>12/09/22 09:12</span>
    </div>
}

export default Message;