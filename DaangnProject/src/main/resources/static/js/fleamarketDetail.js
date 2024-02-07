$(function(){
	
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
			axios.post("/api/unlike",{
				'boardIdx': $("#boardIdx").val()
			})
			.then(function (res) {
				data = res.data;
				if(data==1){
					$("#likeHeart > a").removeClass("active");
				} else {
					alert("오류가 발생했습니다.")
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
				} else {
					alert("로그인후 이용가능합니다.")
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
})


