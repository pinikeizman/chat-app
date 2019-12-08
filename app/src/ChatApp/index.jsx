import React, {useState} from 'react';
import './index.sass'
import Login from "./Login";

function ChatApp() {
    // Declare a new state variable, which we'll call "count"
    const [count, setCount] = useState(0);

    return (
        <div className='app-container'>
            <h1>Aweosome chat app!</h1>
            <Login/>
        </div>
    );
}

export default ChatApp;


