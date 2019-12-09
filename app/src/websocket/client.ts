const socket = new WebSocket(`ws://localhost:8080/greeter/${Date.now()}`);
window.socket = socket;
socket.addEventListener('open', function (event) {
    console.log("connected")
    socket.send('Hello Server!');
});

socket.addEventListener('message', function (event) {
    console.log('Message from server ', event.data);
});

