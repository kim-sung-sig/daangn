$(function(){
	moveLeftArea()
	$(window).on('resize',function(){
		moveLeftArea();
	})
})
function moveLeftArea(){
	const rightAreaPosition = $('#rightArea').offset();
    $("#leftArea").css('top',rightAreaPosition.top).css('left',rightAreaPosition.left-250);
}