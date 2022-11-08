import {createContext, useContext, useReducer} from "react";
import {AuthContext} from "./AuthContext";

export const ChatContext = createContext()

export const ChatContextProvider = ({ children }) => {
    const {currentUser} = useContext(AuthContext)

    const INITIAL_STATE = {
        conversationId: "null",
        user: {}
    }

    const conversationReducer =(state, action) => {
        switch (action.type) {
            case "CHANGE_USER":
                return {
                    user: action.payload,
                    conversationId: currentUser.uid < action.payload.uid
                        ? currentUser.uid + "_" + action.payload.uid
                        : action.payload.uid + "_" + currentUser.uid
                }
            default:
                return state;
        }
    };

    const [state, dispatch] = useReducer(conversationReducer, INITIAL_STATE)

    return (
        <ChatContext.Provider value={{data:state, dispatch}}>
            {children}
        </ChatContext.Provider>
    );
};
