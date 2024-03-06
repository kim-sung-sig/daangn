
$(function() {
	// 아이디 중복 확인을 위한
	$("#username").keyup(function(){
		let username = $("#username").val();
		if(username.search(/[ㄱ-ㅎㅏ-ㅣ가-힣]/g) >= 0){
			$("#message").html("한글은 입력할 수 없습니다.").css('color', 'red');
			return ;
		}
		if(username.search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi) >= 0){
			$("#message").html("특수문자는 입력할 수 없습니다.").css('color', 'red');
			return ;
		}
		if(username.indexOf(" ") != -1){
			alert("공백은 포함할수 없어요")
			$("#username").val("")		
			return ;
		}
		if (8 <= username.length && username.length <= 20) {
			if (username.indexOf(" ") != -1) {
			} else {
				// Ajax를 호출하여 처리 한다.
				axios.post('/member/login/useridcheck',{
					'username': username
				})
				.then(function(response) {
					if (response.data * 1 == 0) {
						$("#message").html("사용가능한 아이디입니다.").css('color', 'blue');
					} else {
						$("#message").html("사용 불가능한 아이디입니다.").css('color', 'red');
					}
				})
				.catch(function(error) {
					console.log(error);
				})
			}
		} else {
			$("#message").html("").css('color', 'black');
		}
	})
	
	// 비밀번호 체크
	$("#password").blur(function(){
		let password = $("#password").val();
		if(password.indexOf(" ") != -1){
			$("#pwmessage").html("공백은 포함할 수 없습니다.").css('color', 'red');
			return ;			
		}
		if(password.search(/[ㄱ-ㅎㅏ-ㅣ가-힣]/g) >=0) {
			$("#pwmessage").html("한글은 사용할 수 없습니다.").css('color', 'red');
			return ;
		}
		if(password.search(/[0-9]/g) < 0){
			$("#pwmessage").html("숫자를 포함해주세요.").css('color', 'red');
			return ;
		}
		if(password.search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi) < 0){
			$("#pwmessage").html("특수문자를 포함해주세요.").css('color', 'red');
			return ;
		}
		if(password.length < 8 || 20 < password.length ){
			$("#pwmessage").html("8~20자 이내로 작성해주세요.").css('color', 'red');
			return ;
		}
		$("#pwmessage").html("사용가능한 비밀번호입니다.").css('color', 'blue');
	})
	// 비밀번호 이중 체크
	$("#password2").keyup(function(){
		let password = $("#password").val();
		let password2 = $("#password2").val();
		if(password != password2) {
			$("#pwmessage2").html("비밀번호가 일치하지 않습니다.").css('color', 'red');
			return ;
		}
		$("#pwmessage2").html("비밀번호가 일치합니다.").css('color', 'blue');
	})
	
	let emailOk = ""; // 인증번호를 보관할 변수
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
		// Ajax를 호출하여 처리 한다.
		axios.get('/member/send?to=' + email)
		.then(function(res) {
			if (res.data != "") {
				alert("메일 발송 성공")
				emailOk = res.data;
				$("#emailCheckBox").css('display','block');
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
	});
	
	$("#checkMail").click(function(){
		let check = $("#check").val();
		// alert(check);
		if(check == emailOk){
			alert("인증이 완료되었습니다.")
			$("#checkMail").val("인증 완료")
			$("#email").prop('disabled',true)
			$("#emailAddress").prop('disabled', true)
			$("#sendEmail").prop('disabled', true)
			$("#check").prop('disabled', true)
			$("#checkMail").prop('disabled', true)
		} else {
			alert("인증 번호를 확인해주세요.")
		}
	})
	
	$("#joinForm").submit(function(){
		if($("#message").html() != "사용가능한 아이디입니다."){
			alert('아이디를 확인해주세요.');
			$("#username").val("");
			$("#username").focus();
			return false;
		}
		if($("#pwmessage").html() != '사용가능한 비밀번호입니다.'){
			alert('비밀번호를 확인해주세요.');
			$("#password").val("");
			$("#password").focus();
			return false;			
		}
		if($("#pwmessage2").html() != '비밀번호가 일치합니다.'){
			alert('비밀번호 재확인을 확인해주세요.');
			$("#password2").val("");
			$("#password2").focus();
			return false;			
		}
		if($("#name").val().search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi) >= 0){
			alert('이름에는 특수문자를 사용할 수 없습니다.');
			$("#name").val("");
			$("#name").focus();
			return false;			
		}
		if($("#nickName").val().search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi) >= 0){
			alert('닉네임에는 특수문자를 사용할 수 없습니다.');
			$("#nickName").val("");
			$("#nickName").focus();
			return false;			
		}
		if($("#checkMail").val() != '인증 완료'){
			alert("메일인증을 해주세요.");
			$("#check").val("");
			$("#check").focus();
			return false;
		}
		if($("#addr1").val() == "" || $("#addr2").val() == "" || $("#addr2").val().search(/[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi) >= 0){
			alert("주소를 확인해주세요!");
			$("#addr2").val("");
			$("#daum").focus();
			return false;
		}
		alert("회원가입 성공")
		return true;
	})
})

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