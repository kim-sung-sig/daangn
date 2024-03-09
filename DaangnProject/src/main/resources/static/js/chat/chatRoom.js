$(function() {
	const chatRoomIdx = $("#chatRoomIdx").val();
    const sender = $("#sender").val();
	
    // WebSocket 및 Stomp 초기화
    var sock = new SockJS("/ws");
    var ws = Stomp.over(sock);
    
    var messages = [];
	
    /**
    * 메시지를 보낼때 실행되는 함수
    */
    function sendMessage() {
        const content = $('#message').val();
        ws.send("/pub/chat/message", {}, JSON.stringify({"typeRef": 2, "chatRoom": chatRoomIdx, "sender": sender, "content": content}));
        $('#message').val('');
        $("#send-message-btn").prop("disabled", true);
        $("#send-message-btn").css("background-color", "#F0F0F0");
		$("#send-message-btn").css("color", "#333");
    }
	
    /**
    * 메시지를 받을때 실행되는 함수
    */
    function recvMessage(recv) {
        const message = {"chatRoom": recv.chatRoom, "typeRef": recv.typeRef, "sender": recv.sender, "nickName": recv.nickName, "content": recv.content, "regDate": recv.regDate, "readed": recv.readed};
        if(message.typeRef == 1){
        	// ENTER
        	// 1. updateReadCount 를 실행한다. 지금 까지 readed =1 인것들 readed - 1
        	axios.put("/chat/readAll", {
        		"chatRoom": recv.chatRoom,
        		"sender": recv.sender,
        	})
			.then(res => {
				console.log('readAll 성공');
			})
			.catch(error => {
				console.error('readAll 실패',error);
			});
        	// 2. 현재 보여지는 창에서 .readed 제거
        	if(message.sender != sender){
	        	$(".ch2 .readed").remove();				
			} else {
				$(".ch1 .readed").remove();
			}
        } else if(message.typeRef == 2){
        	// TALK
        	messages.unshift(message);
	        updateMessagesUI(message);
        } else {
        	// RESERVE
        	
        }
    }

    // pub/sub 이벤트
    ws.connect({}, function(frame) {
        ws.subscribe("/sub/chat/room/" + chatRoomIdx, function(message) {
        	// 메시지를 받으면!
        	var recv = JSON.parse(message.body);
        	if( recv.typeRef != 1){
        		axios.put("/chat/read", {
	        		"idx" : recv.idx, // 이숫자로 chatMessage를 update시키자
	        	})
				.then(res => {
					console.log('read 성공');
				})
				.catch(error => {
					console.error('read 실패');
				});
        	}
        	setTimeout(() => {
        		if (recv.typeRef != 1){
        			axios.get("/chat/get?idx="+recv.idx)
	        		.then(res => {
						recvMessage(res.data);
						console.log(1);
					})
					.catch(error => {
						console.error('메시지받기 실패');
					});        			
        		} else {
        			recvMessage(recv);	
        		}
			}, 150);
        });
        ws.send("/pub/chat/message", {}, JSON.stringify({'typeRef': 1,'chatRoom': chatRoomIdx, 'sender': sender}));
        axios.post("/chat/findChatMessages", {
    		"chatRoom" : chatRoomIdx, // 이숫자로 chatMessage를 update시키자
    	})
		.then(res => {
			const data = res.data;
			reversedData = data.slice().reverse();
			console.log(reversedData);
			reversedData.forEach(recv => {
				recvMessage(recv);
			})
		})
		.catch(error => {
			console.error('read 실패');
		});
    }, function(error) {
        alert("error " + error);
    });
    
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
    
    
    function updateMessagesUI(message) {
        const messageList = $('#chatMessages');
        let ck = (sender == message.sender ? '2' : '1');
        const regDate = updateDate(message.regDate);
        content = `<div class="chat ch${ck}">`;
        if(ck==1){
        	content += `<div class="icon"><span>${message.nickName[0]}</span></div>`;
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
        const messageItem = $('<div>').html(content);
        messageList.append(messageItem);
        setTimeout(function(){
            messageList.scrollTop(messageList.prop('scrollHeight'));
        }, 50);
    }
	
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