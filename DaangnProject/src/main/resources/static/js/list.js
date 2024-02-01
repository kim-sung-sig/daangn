$(function(){
	// 지역 내비게이터
    $("#r1").change(function(){
        if($("#r1").val() == '0'){
            location.href = '/fleamarket'
            return ;
        }
        location.href = '/fleamarket/' + $("#r1").val()
    })
    $("#r2").change(function(){
        if($("#r2").val() == '0'){
            location.href = '/fleamarket/' + $("#r1").val()
            return ;
        }
        location.href = '/fleamarket/' + $("#r1").val() +"/" + $("#r2").val()
    })
    $("#r3").change(function(){
        if($("#r3").val() == '0'){
            location.href = '/fleamarket/' + $("#r1").val() +"/" + $("#r2").val()
            return ;
        }
        location.href = '/fleamarket/' + $("#r1").val() +"/" + $("#r2").val() + "/" + $("#r3").val()
    })
    
    $("#searchForm").submit(function(){
		let val = $("#searchText").val();
		if(val.trim().length == 0) {
			alert("검색어를 입력해주세요.")
			$("#searchText").val("");
			$("#searchText").focus();
			return false;
		}
		if (val.search(/[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi) >= 0){
			alert("특수문자를 사용할 수 없습니다.")
			$("#searchText").val("");
			$("#searchText").focus();
			return false;
		}
	})
})