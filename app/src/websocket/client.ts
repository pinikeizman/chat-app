import {Message, User, WSClient} from "../types";

export function createWSClient(user: User, onMessage: (msg: Message) => void): WSClient {
    const socket = new WebSocket(`ws://localhost:8080/chat?name=${user.userName}`);
    socket.addEventListener('error', (e) => console.error(e));
    socket.addEventListener('close', (e) => console.info(e));
    socket.addEventListener('open', function (event) {
        console.log('connected');
    });

    socket.addEventListener('message', function (event) {
        console.log('Message from server ', event.data);
        const msg: Message = JSON.parse(event.data);
        onMessage(msg)
    });

    window.onbeforeunload = function () {
        socket.onclose = function () {
        }; // disable onclose handler first
        socket.close();
    };

    return {
        close: function () {
            console.log("closing connection")
            socket.onclose = function () {
            }; // disable onclose handler first
            socket.close();
        },
        send: function (msg: string) {
            socket.send(msg);
        }
    }
}
