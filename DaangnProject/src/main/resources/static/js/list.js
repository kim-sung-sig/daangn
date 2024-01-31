$(function(){


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
})