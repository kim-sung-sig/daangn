function detail(idx){
    window.open('/fleamarketDetail/' + idx, '_blank');
}

$(function(){
	let statusNum = $("#boardStatusNum").val();
	console.log(statusNum);
	// 카카오 지도 뿌리기
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	    mapOption = { 
	        center: new kakao.maps.LatLng($("#latitude").val(), $("#longitude").val()), // 지도의 중심좌표
	        level: 3 // 지도의 확대 레벨
	    };
	
	var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
	
	// 마커가 표시될 위치입니다 
	var markerPosition  = new kakao.maps.LatLng($("#latitude").val(), $("#longitude").val()); 
	
	// 마커를 생성합니다
	var marker = new kakao.maps.Marker({
	    position: markerPosition
	});
	
	// 마커가 지도 위에 표시되도록 설정합니다
	marker.setMap(map);
	
	
	// 좋아요 누르기!
	$("#likeHeart > a").click(function(e){
		e.preventDefault();
		if($("#likeHeart > a").hasClass("active")){
			axios.delete("/api/like",{
				data: {
			        boardIdx: $("#boardIdx").val()
			    }
			})
			.then(function (res) {
				data = res.data;
				console.log(data);
				if(data==1){
					$("#likeHeart > a").removeClass("active");
					$("#countLike").text('관심 ' + (Number($("#countLike").text().substring(3)) - 1));
				} else {
					alert("오류가 발생했습니다. 잠시후에 다시 시도해주세요.")
				}
			})
			.catch(function (err) {
				console.log(err);
			});
			
		} else {
			axios.post("/api/like",{
				'boardIdx': $("#boardIdx").val()
			})
			.then(function (res) {
				data = res.data;
				if(data==1){
					$("#likeHeart > a").addClass("active");
					$("#countLike").text('관심 ' + (Number($("#countLike").text().substring(3)) + 1));
				} else {
					alert("오류가 발생했습니다. 잠시후에 다시 시도해주세요.")
				}
			})
			.catch(function (err) {
				console.log(err);
			});
		}
	})
	
	// 시간 바꾸기!
	let times = document.querySelectorAll(".regDate")
	times.forEach(time => {
		let postedDateObj = new Date(time.innerHTML);
		let currentDateObj = new Date();
		let timeDiff = Math.abs(currentDateObj.getTime() - postedDateObj.getTime());
		let diffDays = Math.floor(timeDiff / (1000 * 3600 * 24));
		if (diffDays < 1) {
            let diffHours = Math.floor(timeDiff / (1000 * 3600));
            if (diffHours < 1) {
                let diffMinutes = Math.floor(timeDiff / (1000 * 60));
                console.log( diffMinutes + "분 전");
                time.innerHTML = diffMinutes + "분 전";
            } else {
				time.innerHTML = diffHours + "시간 전";
            }
        } else if (diffDays < 7) {
			time.innerHTML = diffDays + "일 전";
        } else {
			time.innerHTML = diffDays + "일 전"
		}
	})
	
	$("#chatBtn").submit(function(){
		axios.post("/chat/createChatRoom",{
			'boardIdx': $("#boardIdx").val()
		})
		.then(function (res) {
			data = res.data;
			console.log(data);
			if(data!=0){
				const url = `/chat/room/${data}`;
				const popupWidth = 400;
	            const popupHeight = 705;
	            const leftPosition = (window.screen.width - popupWidth) / 2;
	            const topPosition = (window.screen.height - popupHeight) / 2;
	            const popupWindow = window.open(url, "ChatRoomPopup", `width=${popupWidth}, height=${popupHeight}, left=${leftPosition}, top=${topPosition}, resizable=no`);
	            popupWindow.focus();
			} else {
				alert("로그인후 이용가능합니다.")
			}
		})
		.catch(function (err) {
			console.log(err);
		});
		
		return false;
	})
	
	$("#updateBtn").click(function(){
		const idx = $("#boardIdx").val();
		const form = $('<form>').attr({
		    action: '/fleamarketUpdate',
		    method: 'post'
		});
		const input = $('<input>').attr({
		    type: 'hidden',
		    name: 'idx',
		    value: idx
		});
		form.append(input);
		$('body').append(form);
		form.submit();
	})
	
	
	$("#statusChange").change(function(){
		const statusRef = $("#statusChange").val();
		const boardIdx = $("#boardIdx").val();
		const result = confirm('상품 상태를 변경하시겠습니까?');
		if(result) {
			const form = $('<form>');
		    form.attr('method', 'post');
		    form.attr('action', `/fleamarketStatusUpdate`);
		    form.append($('<input>').attr('type', 'hidden').attr('name', 'boardIdx').val(boardIdx));
		    form.append($('<input>').attr('type', 'hidden').attr('name', 'statusRef').val(statusRef));
		    $('body').append(form);
		    form.submit();
		}
	})
})


