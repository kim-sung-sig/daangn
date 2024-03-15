let lastItemIdx = 0;
const sizeOfPage = 8;
let categoryRef = undefined;
let search = undefined;
let region = undefined;
let gu = undefined;
let dong = undefined;

$(function(){
	init();
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
	
	$("#chatBtn").click(function(){
		const url = `http://localhost/chat/rooms`;
		const popupWidth = 400;
        const popupHeight = 705;
        const leftPosition = (window.screen.width - popupWidth) / 2;
        const topPosition = (window.screen.height - popupHeight) / 2;
        const popupWindow = window.open(url, "ChatRoomPopup", `width=${popupWidth}, height=${popupHeight}, left=${leftPosition}, top=${topPosition}, resizable=no`);
        popupWindow.focus();
	});
	
	// 스크롤 이벤트 처리
	window.addEventListener('scroll', handleScroll);
})


function detail(idx){
	location.href = "/fleamarketDetail/" + idx;
}

function init(){
	lastItemIdx = $("#lastItemIdx").val();
	region = $("#r1").val();
	gu = $("#r2").val();
	dong = $("#r3").val();
	search = $("#searchText").val();
	if(region == 0){
		region = undefined;
	}
	if(gu == 0) {
		gu = undefined;
	}
	if(dong == 0) {
		dong = undefined;
	}
	if(search.trim().length == 0){
		search = undefined;
	}
	console.log(lastItemIdx, region, gu, dong, search);
	getItem();
}

function findLastItemIdx(){
	let ukCards = document.querySelectorAll(".uk-card");
	let lastItem = ukCards[ukCards.length - 1];
	let value = lastItem.querySelector(".idx").value;
	lastItemIdx = value;
}

function getItem(){
	axios.post('/getfleamarketList',{
		'lastItemIdx' : lastItemIdx,
		'sizeOfPage' : sizeOfPage,
		'categoryRef' : categoryRef,
		'search' : search,
		'region' : region,
		'gu' : gu,
		'dong' : dong,
	})
	.then(res => {
		let data = res.data;
		console.log(data);
		content = "";
		if(data.length == 0) {
			window.removeEventListener('scroll', handleScroll);
			$("#viewBox").append("<div style='text-align:center;font-weight: bold;'>글이 더 이상 존재하지 않습니다.</div>");
			return ;
		}
		data.forEach(vo => {
			content += `
				<article class="uk-card" onclick="detail(${vo.idx})">
					<input type="hidden" class="idx" value="${vo.idx}">
					<div class="uk-card-media-top">
						<img src="/upload/${vo.boardFileList[0].saveFileName}" alt="" />
					</div>
					<div class="uk-card-body">
						<h4 class="uk-card-title">${vo.subject}</h4>
						<p class="price">${numberFormatter(vo.price)}</p>
						<span class="city">${vo.loc}</span>
						<p class="like"><span>관심 ${vo.countLike}</span>ㆍ<span>채팅 ${vo.chatRoomCount}</span>ㆍ<span>조회수 ${vo.readCount}</span></p>
						<p class="regDate">게시일 ${dateFormatter(vo.regDate)}</p>
					</div>
				</article>
			`;
		})
		$("#slide").append(content);
		findLastItemIdx();
	})
}

/** 수를 가격으로 바꿔주는 함수 */
function numberFormatter(number){
	let formattedNumber = new Intl.NumberFormat('ko-KR').format(number) + '원';
	return formattedNumber;
}

/** 날짜변경 */
function dateFormatter(dateStr){
	// Moment.js를 사용하여 상대적인 시간 표시 
	let momentRegDate = moment(dateStr);
	let now = moment();
	let diff = now.diff(momentRegDate, 'seconds'); // 차이 구하기
	var formattedDate;
	if (diff < 60) {
	    formattedDate = diff + '초 전';
	} else if (diff < 3600) {
	    formattedDate = Math.floor(diff / 60) + '분 전';
	} else if (diff < 86400) {
	    formattedDate = Math.floor(diff / 3600) + '시간 전';
	} else if (diff < 604800) {
	    formattedDate = Math.floor(diff / 86400) + '일 전';
	} else {
	    formattedDate = momentRegDate.format('YYYY.MM.DD');
	}
	return formattedDate;
}

/** 스크롤 이벤트 */
function handleScroll() {
    if (window.scrollY + window.innerHeight + 100 >= document.documentElement.scrollHeight) {
        getItem();
    }
}