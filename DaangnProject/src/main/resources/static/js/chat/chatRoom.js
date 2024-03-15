$(function() {
	const chatRoomIdx = $("#chatRoomIdx").val();
	const sender = $("#sender").val();
	const sizeOfPage = 30;
	let lastItemIdx = $("#lastItemIdx").val();
	let isJoined = false;
    // WebSocket 및 Stomp 초기화
    var sock = new SockJS("/ws");
    var ws = Stomp.over(sock);
    
    var messages = [];
	
    /** 메시지를 보낼때 실행되는 함수 */
    function sendMessage() {
        const content = $('#message').val();
        ws.send("/pub/chat/message", {}, JSON.stringify({"typeRef": 2, "chatRoom": chatRoomIdx, "sender": sender, "content": content, "readed":2}));
        $('#message').val('');
        $("#send-message-btn").prop("disabled", true);
        $("#send-message-btn").css("background-color", "#F0F0F0");
		$("#send-message-btn").css("color", "#333");
    }
	
    /** 메시지를 받을때 실행되는 함수 */
    function recvMessage(recv) {
        let message = {"idx": recv.idx,"chatRoom": recv.chatRoom, "typeRef": recv.typeRef, "sender": recv.sender, "nickName": recv.nickName,"userProfileName": recv.userProfileName, "content": recv.content, "regDate": recv.regDate, "readed": recv.readed};
        console.log(message);
        if(isJoined) {
			message.readed = 0;
		} else {
			if(message.readed != 0) {
				message.readed = 1;				
			}
		}
        updateMessagesUI(message);
    }

    // pub/sub 이벤트
    ws.connect({}, function(frame) {
		// 입장 모두 읽음 처리
		axios.put("/chat/readAll", {
    		"chatRoom": chatRoomIdx,
    		"sender": sender,
    	})
		.then(res => console.log('readAll 성공'))
		.catch(error => console.error('readAll 실패',error));
		// 구독
        ws.subscribe("/sub/chat/room/" + chatRoomIdx, function(message) {
        	let recv = JSON.parse(message.body);
        	console.log(recv);
        	if(recv.typeRef == 1){ // 입장 메시지를 받으면!
				if(recv.sender != sender) {
					// 다른 유저가 접속 했다면!
					if(!isJoined){
						ws.send("/pub/chat/message", {}, JSON.stringify({'typeRef': 1,'chatRoom': chatRoomIdx, 'sender': sender}));
						console.log('상대방 입장');
						isJoined = true;
						$(".readed").remove();						
					}
				}
			} else if(recv.typeRef == 2) {
				axios.put("/chat/read", {'idx': recv.idx })
				.then(res => console.log('read 성공'))
				.catch(e => console.log('read 실패', e))
				
				recvMessage(recv);
			} else if (recv.typeRef == 3) {
				if(recv.sender != sender){
					isJoined = false
					console.log('상대방 떠남');					
				}
			}
        }); // subscribe - end
        ws.send("/pub/chat/message", {}, JSON.stringify({'typeRef': 1,'chatRoom': chatRoomIdx, 'sender': sender}));
        
        // 메시지 읽어오기
        readChatMessages();
    }, function(error) {
        alert("error " + error);
    });
    // 뒤집어야댄다!
    
    
    /** 메세지를 불러오기위한 함수 */
    function readChatMessages(){
		axios.post("/chat/findChatMessages", {
    		"chatRoomIdx" : chatRoomIdx,
    		"lastItemIdx" : lastItemIdx,
    		"sizeOfPage" : sizeOfPage,
    	})
		.then(res => {
			const data = res.data;
			if(data.length == 0){
				document.getElementById("chatMessages").removeEventListener('scroll', handleScroll);
				return ;
			}
			data.forEach(recv => {
				updateMessagesUIREAD(recv);
			})
			findLastItemIdx();
			if ($("#chatMessages").scrollTop() === 0) {
	            setTimeout(function() {
	                var messageList = $("#chatMessages");
	                messageList.scrollTop(messageList.prop('scrollHeight'));
	            }, 50);
	        }
		})
		.catch(e => console.error('이전메시지 불러오기 실패', e));
	}
	
	/** lastItemIdx 찾기 */
	function findLastItemIdx(){
		let chatMessages = document.querySelectorAll("#chatMessages .chat");
		let lastItem = chatMessages[0];
		let idx = lastItem.querySelector(".idx").value;
		lastItemIdx = idx;
		console.log('lastItemIdx1 =', lastItemIdx);
	}
    /**
    * Wed Feb 28 15:26:48 KST 2024 생긴걸
    * PM 3:26으로 바꿔주는 메서드
    */
    function updateDate(dateString){
    	let date = new Date(dateString);
    	let hours = date.getHours();
    	let minutes = date.getMinutes();
    	// 오전/오후 설정
    	let ampm = hours >= 12 ? 'PM' : 'AM';
    	hours = hours % 12;
    	minutes = minutes < 10 ? '0' + minutes : minutes;
    	let finalTime = ampm + ' ' + hours + ':' + minutes;
    	return finalTime;
    }
    
    /**받은 메시지를 뿌려주는 함수 */
    function updateMessagesUI(message) {
        const messageList = $('#chatMessages');
        let ck = (sender == message.sender ? '2' : '1');
        const regDate = updateDate(message.regDate);
        const userProfile = message.userProfileName ? "/upload/"+message.userProfileName : '/img/user.png';
        content = `<div class="chat ch${ck}"><input type="hidden" class="idx" value="${message.idx}">`;
        if(ck==1){
        	content += `<div class="icon"><img src="${userProfile}" alt="user"/></div>`;
        }
        content += `
	        	<div class="textbox">
		    	<p style="word-wrap: break-word;">${message.content}</p>
				<span class="chat-time">   
        `;
        if(message.readed != 0) {
        	content += `<span class="readed">1</span>`;
        }
        content += `${regDate}</span></div></div>`;
        messageList.append(content);
        setTimeout(function(){
            messageList.scrollTop(messageList.prop('scrollHeight'));
        }, 50);
    }
    
    /**읽은 메시지를 뿌려주는 함수 */
    function updateMessagesUIREAD(message) {
        const messageList = $('#chatMessages');
        let ck = (sender == message.sender ? '2' : '1');
        const regDate = updateDate(message.regDate);
        const userProfile = message.userProfileName ? "/upload/"+message.userProfileName : '/img/user.png';
        content = `<div class="chat ch${ck}"><input type="hidden" class="idx" value="${message.idx}">`;
        if(ck==1){
        	content += `<div class="icon"><img src="${userProfile}" alt="user"/></div>`;
        }
        content += `
	        	<div class="textbox">
		    	<p style="word-wrap: break-word;">${message.content}</p>
				<span class="chat-time">   
        `;
        if(message.readed != 0) {
        	content += `<span class="readed">1</span>`;
        }
        content += `${regDate}</span></div></div>`;
        messageList.prepend(content);
        /*
        setTimeout(function(){
            messageList.scrollTop(messageList.prop('scrollHeight'));
        }, 50);
        */
    }
    
    document.getElementById("chatMessages").addEventListener("scroll", handleScroll);
    /** 스크롤 이벤트 */
	function handleScroll() {
	    const chatMessagesDiv = document.getElementById("chatMessages");
		const scrollTop = chatMessagesDiv.scrollTop;
    	// console.log('스크롤위치 :', scrollTop);
	    // 만약 스크롤이 맨 위에 도달했다면
	    if (scrollTop < 50 ) {
	        readChatMessages();
	    }
	}
	
	window.addEventListener('beforeunload', function(event) {
	    event.preventDefault();
	    ws.send("/pub/chat/message", {}, JSON.stringify({'typeRef': 3,'chatRoom': chatRoomIdx, 'sender': sender}));
	});
	window.addEventListener('unload', function(event) {
		event.preventDefault();
        ws.send("/pub/chat/message", {}, JSON.stringify({'typeRef': 3,'chatRoom': chatRoomIdx, 'sender': sender}));
	});
	
	
	// 사용자가 확인 대화상자에 대답할 때 호출되는 함수
	function handleUserResponse() {
	    const response = confirm('채팅방을 나가시겠습니까?');
	    if (response) {
			ws.send("/pub/chat/message", {}, JSON.stringify({'typeRef': 3,'chatRoom': chatRoomIdx, 'sender': sender}));
	    } else {
			console.log('나 안떠낫어!!');
	    }
	};
	
	
	//======================================================================
	// 여기부터 보내는 영역
	
    function isSendOk(){
    	let result = true;
    	const textArea = document.querySelector("#message");
    	if(textArea.value.trim().length == 0){
    		result = false;
    	}
    	return result;
    }
    // 메시지 전송 버튼 클릭 이벤트 핸들러
    $('#send-message-btn').click(function() {
    	if(isSendOk()){
        	sendMessage();
    	}
    });
    $("#message").keyup(function(event) {
    	if(isSendOk()){
    		$("#send-message-btn").prop("disabled", false);
    		$("#send-message-btn").css("background-color", "#FF8A3D");
    		$("#send-message-btn").css("color", "#fff");
    	} else {
    		$("#send-message-btn").prop("disabled", true);
    		$("#send-message-btn").css("background-color", "#F0F0F0");
    		$("#send-message-btn").css("color", "#333");
    		
    		return ;
    	}
        // Enter 키의 keyCode 값은 13이며, Shift 키와 함께 눌렀을 때 keyCode 값이 10입니다.
        if (event.which === 13 && !event.shiftKey) {
    		if(isSendOk()){
	        	if(!$("#send-message-btn").prop("disabled")){
		        	sendMessage();
	        	}
        	}
        }
    });
    
    document.getElementById('message').addEventListener('wheel', function(e) {
        const element = e.target;
        const scrollTop = element.scrollTop;
        const scrollHeight = element.scrollHeight;
        const height = element.clientHeight;
        const delta = e.deltaY;
        const up = delta > 0 ? -1 : 1;

        // 스크롤바가 없는 상태에서 마우스 휠 동작 시 스크롤 이동
        if ((delta < 0 && scrollTop <= 0) || (delta > 0 && scrollTop + height >= scrollHeight)) {
            return;
        }

        e.preventDefault();
        element.scrollTo({
            top: scrollTop + delta * 0.3, // 스크롤 속도 조정 (0.3은 조절 가능)
            behavior: 'smooth'
        });
    });
});