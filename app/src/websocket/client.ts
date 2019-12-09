const socket = new WebSocket(`ws://localhost:8080/greeter/${Date.now()}`);
socket.addEventListener('open', function (event) {
    console.log("connected")
    socket.send('Hello Server!');
});

socket.addEventListener('message', function (event) {
    console.log('Message from server ', event.data);
});

window.onbeforeunload = function() {
    socket.onclose = function () {}; // disable onclose handler first
    socket.close();
};

