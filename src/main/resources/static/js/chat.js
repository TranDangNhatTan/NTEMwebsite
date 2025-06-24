document.addEventListener('DOMContentLoaded', (event) => {
    const chatToggleButton = document.getElementById('chat-toggle-button');
    const chatPopup = document.getElementById('chat-popup');

    const currentUserId = document.body.dataset.userid;
    const currentUserName = document.body.dataset.username;

    if (!currentUserId) return;

    const userListPanel = document.getElementById('user-list-panel');
    const conversationPanel = document.getElementById('conversation-panel');
    const userSearchInput = document.getElementById('user-search-input');
    const userListUL = document.getElementById('user-list');

    const backToUsersButton = document.getElementById('back-to-users');
    const conversationHeader = document.getElementById('conversation-header-name');
    const messageArea = document.getElementById('message-area');
    const messageForm = document.getElementById('message-form');
    const messageInput = document.getElementById('message-input');

    let stompClient = null;
    let selectedUserId = null;

    chatToggleButton.addEventListener('click', () => {
        const isShown = chatPopup.classList.toggle('show');
        if (isShown && !stompClient) {
            connect();
        }
    });

    backToUsersButton.addEventListener('click', () => {
        conversationPanel.style.display = 'none';
        userListPanel.style.display = 'flex';
        selectedUserId = null;
    });

    messageForm.addEventListener('submit', (event) => {
        event.preventDefault();
        sendMessage();
    });

    userSearchInput.addEventListener('keyup', () => {
        fetchUsers(userSearchInput.value);
    });

    function connect() {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    }

    function onConnected() {
        stompClient.subscribe(`/user/${currentUserId}/queue/messages`, onMessageReceived);
        fetchUsers('');
    }

    function onError(error) {
        console.error('Could not connect to WebSocket server.');
    }

    async function fetchUsers(searchTerm) {
        const response = await fetch(`/api/users?search=${searchTerm}`);
        const users = await response.json();
        userListUL.innerHTML = '';
        users.forEach(user => {
            if (user.id.toString() !== currentUserId) {
                const li = document.createElement('li');
                li.textContent = `${user.fullName} (${user.role})`;
                li.dataset.id = user.id;
                li.dataset.name = user.fullName;
                li.addEventListener('click', () => {
                    selectedUserId = user.id;
                    conversationHeader.textContent = user.fullName;
                    userListPanel.style.display = 'none';
                    conversationPanel.style.display = 'flex';
                    messageArea.innerHTML = '';
                });
                userListUL.appendChild(li);
            }
        });
    }

    function sendMessage() {
        const messageContent = messageInput.value.trim();
        if (messageContent && stompClient && selectedUserId) {
            const chatMessage = {
                senderId: currentUserId,
                receiverId: selectedUserId, // <-- Sửa cho khớp với Entity
                content: messageContent,
            };
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            displayMessage(chatMessage, true);
            messageInput.value = '';
        }
    }

    function onMessageReceived(payload) {
        const message = JSON.parse(payload.body);
        if (selectedUserId && selectedUserId.toString() === message.senderId.toString()) {
            displayMessage(message, false);
        } else {
            alert(`New message from user ID: ${message.senderId}`);
        }
    }

    function displayMessage(message, isSender) {
        const messageElement = document.createElement('div');
        messageElement.classList.add('chat-message');
        messageElement.classList.add(isSender ? 'sender' : 'receiver');
        const textElement = document.createElement('p');
        textElement.textContent = message.content;
        messageElement.appendChild(textElement);
        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }
});