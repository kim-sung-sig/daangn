$(function() {
	let emailOk = "";
	// 메일 인증하는 함수!
	$("#sendEmail").click(function() {
		let email = $("#email").val();
		if (email.trim().length == 0) {
			alert("이메일을 입력해주세요.")
			$("#email").val("");
			$("#email").focus();
			return;
		}
		if (email.indexOf(" ") != -1) {
			alert("공백은 포함할 수 없어요.");
			$("#email").val("");
			$("#email").focus();
			return;
		}
		let emailAddress = $("#emailAddress").val();
		
		if (emailAddress == 0) {
			alert("도메인을 선택해주세요.");
			$("#emailAddress").focus();
			return;
		}
		email = email + '@' + emailAddress;
		alert(email);
		// Ajax를 호출하여 처리 한다.
		axios.get('/member/send?to=' + email)
		.then(function(res) {
			if (res.data != "") {
				alert(res.data);
				alert("메일 발송 성공")
				emailOk = res.data;
				console.log(emailOk);
				$("#emailCheckBox").css('display', 'flex')
			} else {
				alert("메일 발송 실패")
			}
		})
		.catch(function(error) {
			console.log(error);
		})
		.finally(function() {
			// 항상 실행되는 영역
		});
	})
})

// 중복확인하는 함수
function useridCheck() {
	let username = $("#username").val();
	if(username.indexOf(" ") != -1){
		alert("공백은 포함할수 없어요")
		$("#username").val("")		
		return ;
	}
	if (username.length >= 4) {
		if (username.indexOf(" ") != -1) {
		} else {
			// Ajax를 호출하여 처리 한다.
			axios.get('/member/login/userIdCheck?username=' + username)
				.then(function(response) {
					console.log(username);
					console.log(response.data);
					// 성공 핸들링
					if (response.data * 1 == 0) {
						$("#message").html("사용가능한 아이디입니다.").css('color', 'blue');
					} else {
						$("#message").html("사용 불가능한 아이디입니다.").css('color', 'red');
					}
				})
				.catch(function(error) {
					// 에러 핸들링
					console.log(error);
				})
				.finally(function() {
					// 항상 실행되는 영역
				});
		}
	} else {
		$("#message").html("").css('color', 'black');
	}
}

// 다음 우편번호 API를 이용한 우편번호 검색 함수
function daumPostcode() {
	new daum.Postcode({
		oncomplete: function(data) {
			// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

			// 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
			// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
			var roadAddr = data.roadAddress; // 도로명 주소 변수

			// 우편번호와 주소 정보를 해당 필드에 넣는다.
			document.getElementById('postcode').value = data.zonecode;
			document.getElementById("addr1").value = roadAddr;
		}
	}).open();
	document.getElementById("addr2").focus();
}