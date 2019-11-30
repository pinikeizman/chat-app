import ChatApp from './ChatApp';
import ReactDOM from 'react-dom';
import React from 'react';
import './websocket/client'
const appEle = document.getElementById("chat-app");
ReactDOM.render(<ChatApp/>, appEle);
