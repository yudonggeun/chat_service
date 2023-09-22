const stompClient = new StompJs.Client({
    brokerURL: 'ws://' + `${domain}:${port}` + '/chatting'
});

function convertTime(time){
    const date = new Date(time);
    return `${date.getHours()}:${date.getMinutes()}`;
}

function createChatHandle(data){
    const sender = data.sender;
    const message = data.message;
    const roomId = data.roomId;
    const id = data.id;
    const createdAt = data.createdAt;
    showGreeting(roomId, id, sender, message, convertTime(createdAt));
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

function sendChat(roomId, sender) {
    console.log("sendChat", roomId, sender);
    const element = document.getElementById("message");
    const message = element.value;
    element.value = "";
    stompClient.publish({
        destination: "/app/chat/new",
        body: JSON.stringify({
        'message': message,
        'sender': sender,
        'roomId': roomId
        })
    });
}

function deleteFriend(friendName){
    fetch("/friend", {
      method: "DELETE",
      headers: {
          "Content-Type": "application/json",
        },
      body: JSON.stringify({
        friendNickname: friendName
      }),
    })
    .then((response) => response.json())
    .then((result) => {
        document.getElementById("friend-" + friendName).outerHTML = "";
        console.log("delete friend", result)
    });
}

function deleteChat(id, roomId, sender) {
    console.log(id, roomId, sender, nickname, sender !== nickname, sender != nickname);
    if(sender !== nickname) return;
    deleteChatElement(id);
    stompClient.publish({
        destination: "/app/chat/delete",
        body: JSON.stringify({
        'id': id,
        'roomId': roomId
        })
    });
}

function deleteChatElement(id){
    document.getElementById("chat-" + id).outerHTML = "";
}

function createRoom(){
    const element = document.getElementById("create-room-input");
    const roomTitle= element.value;
    element.value = "";

    fetch("/room", {
      method: "POST",
      headers: {
          "Content-Type": "application/json",
        },
      body: JSON.stringify({
        title: roomTitle,
        users: [
            nickname
        ]
      }),
    })
    .then((response) => response.json())
    .then((result) => {
        if(result.status === "success"){
            const data = result.data;
            addRoom(data.id, data.title);
        }
    });
}

function addRoom(roomId, title){
    const element = document.getElementById("chat-room-list");
    element.innerHTML += `
        <div id="room-item-${roomId}">
            <a href="/?roomId=${roomId}"
               onclick="disconnect()"
               class="flex items-center p-2 text-base font-normal text-gray-900 rounded-lg transition duration-75 hover:bg-gray-100 dark:hover:bg-gray-700 dark:text-white group">
               <span class="ml-3">${title}</span>
            </a>
        </div>`;
}

function deleteRoomUser(nickname){
    const element = document.getElementById("room-user-" +nickname);
    element.outerHTML = "";
}
function deleteRoom(roomId){
    const element = document.getElementById("room-item-" + roomId);
    element.outerHTML = "";
    stompClient.publish({
        destination: "/app/room/out",
        body: JSON.stringify({ 'id': roomId })
    });
    window.location.href = "/";
}
function inviteRequest(roomId){
    const element = document.getElementById("invite-text");
    const userName = element.value;
    element.value = "";
    stompClient.publish({
        destination: "/app/room/in",
        body: JSON.stringify({
        'roomId': roomId,
        'nickname': userName
        })
    });
}

function inviteRoomHandle(data){
    const element = document.getElementById("chat-room-list");
    const roomId = data.id;
    const title = data.title;
    element.innerHTML += `
        <div>
            <a href='/?roomId=${roomId}'
               onclick='disconnect()'
               class='flex items-center p-2 text-base font-normal text-gray-900 rounded-lg transition duration-75 hover:bg-gray-100 dark:hover:bg-gray-700 dark:text-white group'>
               <span class='ml-3'>${title}</span>
            </a>
        </div>
    `;
    console.log(element);
    console.log(element.innerHTML);
}

function inviteUserHandle(data){
    const target = document.getElementById("chat-users");
    target.innerHTML = "";

    const other = `
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-person-fill" viewBox="0 0 16 16">
            <path d="M3 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H3Zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z"/>
        </svg>
    `;
    const me = `
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-person" viewBox="0 0 16 16">
            <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6Zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0Zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4Zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10Z"/>
        </svg>
    `;
    data.users.forEach((value, index) => {
        target.innerHTML += `
                    <div
                        id="room-user-${value}"
                        class="flex items-center p-2 text-base font-normal text-gray-900 rounded-lg transition duration-75 hover:bg-gray-100 dark:hover:bg-gray-700 dark:text-white group">
                        ${value == nickname ? me : other}
                        <span class="ml-3">${value}</span>
                    </div>`;
    });
}

function showGreeting(roomId, id, user, message, time) {
    if(user === nickname){
        $("#room").append(
                    `<div id="chat-${id}">
                        <div class="flex justify-end w-full">
                            <div class="relative w-fit pb-4">
                                <div class="message mr-1 w-fit bg-gray-50 p-1 px-2 text border rounded-lg"
                                     onclick="deleteChat(${id}, ${roomId}, '${user}')">
                                    ${message}
                                </div>
                                <div class="text-sm absolute left-0">
                                    ${time}
                                </div>
                            </div>
                        </div>
                    </div>`
        );
    } else {
        $("#room").append(
                    `<div id="chat-${id}">
                        <div class="text-sm px-1">
                             ${user}
                        </div>
                        <div class="relative w-fit pb-4">
                            <div class="message w-fit bg-yellow-200 p-1 px-2 text border rounded-lg">
                                ${message}
                            </div>
                            <div class="text-sm absolute right-0">
                                ${time}
                            </div>
                        </div>
                     </div>`
        );
    }
    prepareScroll();
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
});