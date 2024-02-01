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
})