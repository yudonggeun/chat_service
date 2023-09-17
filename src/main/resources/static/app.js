const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/chatting'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/chat-1', (res) => {
        const response = JSON.parse(res.body);
        const type = response.type;
        const data = response.data;
        if(type === "deleteChat"){
            showGreeting(deleteChatHandle(data));
        } else if(type === "createChat"){
            showGreeting(createChatHandle(data));
        }
    });
};

function createChatHandle(data){
    const sender = data.sender;
    const message = data.message;
    const roomId = data.roomId;
    const id = data.id;
    return "NEW room : " + roomId + ", from : " + sender + "=(" + id + ": " + message + ")";
}

function deleteChatHandle(data){
    const status = data.status;
    const id = data.id;
    const roomId = data.roomId;
    return "DELETE " + id + " at " + roomId;
}

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
        'roomId': 1
        })
    });
}

function deleteChat() {
    stompClient.publish({
        destination: "/app/chat/delete",
        body: JSON.stringify({
        'id': $("#id").val(),
        'roomId': 1
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