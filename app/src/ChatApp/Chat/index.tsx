import * as React from 'react';
import './index.sass';
import {User} from "../../types";

export interface ChatProps {
    user: User
}

const Chat: React.FC<ChatProps> = (props: ChatProps) => {
    const {user} = props;

    return (
        <div className='app-chat-container'>
            <h4>Logged in as: {user.userName}</h4>
            <div className='app-chat-container__conversation'>asd</div>
        </div>
    );
};

export default Chat;
