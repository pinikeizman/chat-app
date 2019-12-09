import ChatApp from './ChatApp';
import * as ReactDOM from 'react-dom';
import * as React from 'react';
import './websocket/client'

const appEle = document.getElementById("chat-app");
ReactDOM.render(<ChatApp/>, appEle);
