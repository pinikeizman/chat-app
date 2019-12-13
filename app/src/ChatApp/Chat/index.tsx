import * as React from 'react';
import {Message, User, WSClient as WSClientType} from "../../types";
import * as WSClient from '../../websocket/client';
import './index.sass';
import {useEffect, useRef, useState} from "react";
import Input from "../Input";
import Button from "../Button";

export interface ChatProps {
    user: User
}

const Chat: React.FC<ChatProps> = (props: ChatProps) => {
    const {user} = props;
    const [wsClient, setWSClient] = useState<WebSocket>(null);
    const [messages, setMessages] = useState<Array<Message>>([]);
    const [message, setMessage] = useState<string>("");
    const messagesEleRef: React.Ref<HTMLDivElement> = useRef(null);

    useEffect(() => {
        const client = WSClient.createWSClient(user);
        setWSClient(client);
        return () => {
            client.close();
        }
    }, [true]);

    useEffect(() => {
        if (wsClient) {
            const cb = (event: MessageEvent) => {
                setMessages([...messages, JSON.parse(event.data)]);
                messagesEleRef.current.scrollTop = messagesEleRef.current.scrollHeight
            };
            wsClient.onmessage = cb;
        }
    }, [messages, wsClient]);

    const sendMsg = () => {
        setMessage('');
        wsClient.send(message)
    };

    return (
        <>
            <h4>Logged in as: {user.userName}</h4>
            <div className='app-chat-container__conversation'>
                <div className='app-chat-container__conversation__messages' ref={messagesEleRef}>
                    {
                        messages.map((msg: Message) =>
                            <div className='app-chat-container__conversation__messages__message'>
                                <div className='from'>{msg.from}:</div>
                                <div className='text'>{msg.message}</div>
                            </div>
                        )
                    }
                </div>
                <div className='app-chat-container__conversation__submit-form'>
                    <Input
                        value={message}
                        placeholder='Type a message'
                        onChange={(e) => setMessage(e.target.value)}
                        style={{flex: 1, paddingRight: 10}}
                        onKeyPress={(e) => e.key === "Enter" ? sendMsg() : null}
                    />
                    <Button
                        disabled={message.length === 0}
                        color='submit'
                        label='Send'
                        onClick={sendMsg}
                    />
                </div>
            </div>
        </>
    );
};

export default Chat;
