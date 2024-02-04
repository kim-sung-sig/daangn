$(function() {
	// 지도!!
	// 위도 경도를 구하는 함수
	navigator.geolocation.getCurrentPosition((position) => {
		let latitude = position.coords.latitude;
		let longitude = position.coords.longitude;
		// 카카오 맵 api 사용
		var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
			mapOption = {
				center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
				level: 3 // 지도의 확대 레벨
			};
		var map = new kakao.maps.Map(mapContainer, mapOption);
		// 컨트롤러 설정!
		var mapTypeControl = new kakao.maps.MapTypeControl();
		map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);
		var zoomControl = new kakao.maps.ZoomControl();
		map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

		// 주소-좌표 변환 객체를 생성합니다
		var geocoder = new kakao.maps.services.Geocoder();

		// 마커를 생성합니다
		function addMarker(position) {
			var marker = new kakao.maps.Marker({
				position: position
			});
			// 마커가 지도 위에 표시되도록 설정합니다
			marker.setMap(map);
			// 생성된 마커를 배열에 추가합니다
			markers.push(marker);
		}
		// 마커 모두 지우는 함수
		function setMarkers(map) {
			for (var i = 0; i < markers.length; i++) {
				markers[i].setMap(null);
			}
		}
		function searchAddrFromCoords(coords, callback) {
			// 좌표로 행정동 주소 정보를 요청합니다
			geocoder.coord2RegionCode(coords.getLng(), coords.getLat(), callback);
		}
		function inputText(result, status) {
			if (status === kakao.maps.services.Status.OK) {
				for (var i = 0; i < result.length; i++) {
					// 행정동의 region_type 값은 'H' 이므로
					if (result[i].region_type === 'H') {
						let content =
							`${result[i].address_name}에 올릴 게시글 내용을 작성해 주세요.
(판매 금지 물품은 게시가 제한될 수 있어요!)

신뢰할 수 있는 거래를 위해 자세히 적어주세요.
과학기술정보통신부, 한국 인터넷진흥원과 함께해요.`;
						$("#content").attr("placeholder", content);
						$("#loc").val(result[i].address_name);
						break;
					}
				}
			}
		}
		// **초기화 영역**
		// 마커를 담은 배열
		var markers = [];
		// 마커 하나를 지도위에 표시합니다 
		addMarker(new kakao.maps.LatLng(latitude, longitude));

		searchAddrFromCoords(map.getCenter(), inputText)
		$("#latitude").val(latitude)
		$("#longitude").val(longitude)
		// 그후 지도가 움직일때 실행될 영역
		// 마우스 드래그로 지도 이동이 완료되었을 때 마지막 파라미터로 넘어온 함수를 호출하도록 이벤트를 등록합니다
		kakao.maps.event.addListener(map, 'center_changed', function() {
			// 마커 모두 지우기
			setMarkers(map)
			// 지도 중심좌표를 얻어옵니다 
			var latlng = map.getCenter();
			// 마커찍기
			addMarker(new kakao.maps.LatLng(latlng.getLat(), latlng.getLng()));
			searchAddrFromCoords(map.getCenter(), inputText)
			$("#latitude").val(latlng.getLat())
			$("#longitude").val(latlng.getLng())
		});

	});
	
	
	// 사진 업로드 하기!
    document.getElementById('fileInput').addEventListener('change', function (event) {
        // 선택된 파일 가져오기
        var files = event.target.files;

        // 파일 미리보기를 표시할 엘리먼트
        var previewElement = document.getElementById('preview');

        // 파일마다 미리보기를 생성
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            var reader = new FileReader();

            // 파일의 내용을 읽어 데이터 URL로 변환
            reader.readAsDataURL(file);

            // 읽기가 완료된 후 실행되는 콜백 함수
            reader.onload = function () {
                // 미리보기 이미지를 생성하여 추가
                var img = document.createElement('img');
                img.src = reader.result;
                img.alt = 'Preview';
                img.className = 'uk-responsive-width uk-margin-small-right';
                previewElement.appendChild(img);
            };
        }
    });
    
    
    
    // 서브밋 될때
    $("#uploadForm").submit(function(){
		let value = $("#subject").val();
		if(value.trim().length == 0){
			alert('제목을 입력해주세요.');
			$("#subject").val("");
			$("#subject").focus();
			return false;
		}
		
		if(value.search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi) >= 0){
			alert('제목에는 특수문자는 사용할 수 없습니다.');
			$("#subject").val("");
			$("#subject").focus();
			return false;
		}
		
		value = $("#content").val();
		if(value.trim().length == 0){
			alert('내용을 입력해주세요.');
			$("#content").val("");
			$("#content").focus();
			return false;
		}
		
		if(value.search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi) >= 0){
			alert('내용에는 특수문자는 사용할 수 없습니다.');
			$("#content").val("");
			$("#content").focus();
			return false;
		}
		
		value = $("#detailAddress").val();
		if(value.trim().length == 0){
			alert('상세주소를 입력해주세요.');
			$("#detailAddress").val("");
			$("#detailAddress").focus();
			return false;
		}
		
		if(value.search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi) >= 0){
			alert('상세주소에는 특수문자는 사용할 수 없습니다.');
			$("#detailAddress").val("");
			$("#detailAddress").focus();
			return false;
		}
		alert('상품을 등록했습니다.')
		return true;
	})
});