const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/chatting'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/chat/new', (greeting) => {
        const response = JSON.parse(greeting.body);
        const sender = response.sender;
        const message = response.message;
        const roomId = response.roomId;
        const id = response.id;
        console.log(response);
        showGreeting("NEW room : " + roomId + ", from : " + sender + "=(" + id + ": " + message + ")");
    });
    stompClient.subscribe('/topic/chat/delete', (greeting) => {
        const response = JSON.parse(greeting.body);
        const status = response.status;
        const id = response.id;
        const roomId = response.roomId;
        showGreeting("DELETE " + id + " at " + roomId);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.publish({
        destination: "/app/chat/new",
        body: JSON.stringify({
        'message': $("#name").val(),
        'sender': "test1",
        'roomId': 10
        })
    });
}

function deleteChat() {
    stompClient.publish({
        destination: "/app/chat/delete",
        body: JSON.stringify({
        'id': $("#id").val(),
        'roomId': 10
        })
    });
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
    $( "#deleteId" ).click(() => deleteChat());
});