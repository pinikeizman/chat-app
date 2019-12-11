import * as React from 'react';
import {Message, User, WSClient as WSClientType} from "../../types";
import * as WSClient from '../../websocket/client';
import './index.sass';
import {useEffect, useState} from "react";
import Input from "../Input";
import Button from "../Button";

export interface ChatProps {
    user: User
}

const Chat: React.FC<ChatProps> = (props: ChatProps) => {
    const {user} = props;
    const [wsClient, setWSClient] = useState<WSClientType>(null);
    const [messages, setMessages] = useState<Array<Message>>([]);
    const [message, setMessage] = useState<string>("");

    useEffect(() => {
        const client = WSClient.createWSClient(
            user,
            (msg) => {
                setMessages([...messages, msg])
            }
        );
        setWSClient(client);
        return () => {
            client.close();
        }
    }, [true]);
    console.log(wsClient, messages, message);

    return (
        <div className='app-chat-container'>
            <h4>Logged in as: {user.userName}</h4>
            <div className='app-chat-container__conversation'>
                <div className='app-chat-container__conversation__messages'></div>
                <div className='app-chat-container__conversation__submit-form'>
                    <Input
                        value={message}
                        placeholder='Type a message'
                        onChange={(e) => setMessage(e.target.value)}
                        style={{flex: 1, paddingRight: 10}}
                    />
                    <Button color='submit' label='Send' onClick={() => {
                        setMessage('')
                        setTimeout(() => wsClient.send(message), 0)
                    }}></Button>
                </div>
            </div>
        </div>
    );
};

export default Chat;
