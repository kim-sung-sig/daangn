$(function(){


    $("#r1").change(function(){
        if($("#r1").val() == '지역을 선택하세요.'){
            location.href = '/fleamarket'
        }
        location.href = '/fleamarket/' + $("#r1").val()
    })
    $("#r2").change(function(){
        if($("#r2").val() == '지역을 선택하세요.'){
            location.href = '/fleamarket/' + $("#r1").val()
        }
        location.href = '/fleamarket/' + $("#r1").val() +"/" + $("#r2").val()
    })
})