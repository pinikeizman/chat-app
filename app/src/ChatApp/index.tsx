import * as React from 'react';
import './index.sass'
import Login from "./Login";
import {User} from "../types";
import Chat from "./Chat";

function ChatApp() {
    const [user, setUser] = React.useState<User>(null);

    return (
        <div className='app-container'>
            <h1>Aweosome chat app!</h1>
            {
                user ? <Chat user={user}/> : <Login onLogin={(user)=>setUser(user)}/>
            }
        </div>
    );
}

export default ChatApp;


