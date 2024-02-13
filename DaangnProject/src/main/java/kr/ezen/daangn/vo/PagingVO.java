package kr.ezen.daangn.vo;
// 페이징을 하는 클래스를 만들어 보자
// 어떤 데이터든 페이징이 가능하도록 Generic클래스로 만들자

import java.util.List;

public class PagingVO<T> {
	// 1페이지 분량의 데이터를 저장할 리스트
	List<T> list;
	
	// 페이지 계산을 위한 변수들....
	private int totalCount; 	// 전체 개수
	private int currentPage; 	// 현재 페이지 수
	private int sizeOfPage;     // 1페이지당 개수
	private int sizeOfBlock;    // 하단에 표시할 페이지 개수
	
	private int startNo; 		// 시작 글번호
	private int endNo;          // 끝 글번호
	private int startPage;      // 시작 페이지 번호
	private int endPage;        // 끝페이지 번호
	private int totalPage;      // 전체 페이지 수
	
	// 상단의 4개 변수는 넘겨준다. 나머지 5개는 계산을 한다.
	// 4개는 생성자로 받아서 처리해 보자
	public PagingVO(int totalCount, int currentPage, int sizeOfPage, int sizeOfBlock) {
		super();
		this.totalCount = totalCount;
		this.currentPage = currentPage;
		this.sizeOfPage = sizeOfPage;
		this.sizeOfBlock = sizeOfBlock;
		calc();
	}

	private void calc() {
		// 넘겨받은 값들의 유효성을 검사한다.
		// 현재페이지 번호가 1보다 적을 수 없다.
		if(currentPage<=0) currentPage = 1;
		// 페이지당 글수는 최소 2보다 적을 수 없다.
		if(sizeOfPage<2) sizeOfPage = 10;
		// 하단의 페이지수는 최소 2개 이상이어야 한다.
		if(sizeOfBlock<2) sizeOfBlock = 10;
		
		// 글이 존재할때만 나머지를 계산해주자!!
		if(totalCount>0) {
			// 나머지 변수들을 계산해준다.
			// 전체 페이지수를 계산 = (전체개수-1)/페이지당글수 + 1
			totalPage = (totalCount-1)/sizeOfPage + 1;
			
			// 현재페이지수가 천체 페이지수를 넘을수 없다.
			if(currentPage>totalPage) currentPage = 1;
			
			// 시작글번호 = (현재페이지-1)*페이지당 글수  + 1
			startNo = (currentPage-1)*sizeOfPage + 1;
			// 끝 글번호 = 시작글번호+페이지당글수-1
			endNo = startNo+sizeOfPage-1;
			// 마지막번호는 전체개수를 넘을 수 없다.
			if(endNo>totalCount) endNo = totalCount;
			// 시작페이지번호 = (현재페이지-1)/표시할페이지개수 * 표시할페이지개수 + 1
			startPage = (currentPage-1)/sizeOfBlock * sizeOfBlock + 1;
			// 끝페이지번호 = 시작페이지번호 +  표시할페이지개수 -1
			endPage = startPage + sizeOfBlock -1;
			// 끝 페이지 번호는 전체 페이지 수를 넘을 수 없다.
			if(endPage> totalPage) endPage = totalPage;
		}
	}

	// Getter & Setter를 만든다.
	// Setter는 List만 만들어주고 나머지는 모두 Getter만 만든다.
	public List<T> getList() {
		return list;
	}
	
	public void setList(List<T> list) {
		this.list = list;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getSizeOfPage() {
		return sizeOfPage;
	}

	public int getSizeOfBlock() {
		return sizeOfBlock;
	}

	public int getStartNo() {
		return startNo;
	}

	public int getEndNo() {
		return endNo;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	@Override
	public String toString() {
		return "PagingVO [list=" + list + ", totalCount=" + totalCount + ", currentPage=" + currentPage
				+ ", sizeOfPage=" + sizeOfPage + ", sizeOfBlock=" + sizeOfBlock + ", startNo=" + startNo + ", endNo="
				+ endNo + ", startPage=" + startPage + ", endPage=" + endPage + ", totalPage=" + totalPage + "]";
	}
	
	// 메서드 2개를 추가하자
	// 1. 페이지 상단에 전체 ?개(?/?페이지)를 출력해주는 메서드를 만들자.
	public String getPageInfo() {
		return totalCount==0 ? "전체 : 0개" : "전체 : " + totalCount + "개(" + currentPage + "/" + totalPage + "Page)"; 
	}
	
	// 2. 하단에 이동하는 메서드를 만들자.
	public String getPageList() {
		StringBuffer sb = new StringBuffer();
		if(totalCount>0) {
			sb.append("<nav aria-label='Page navigation example'>");
			sb.append("<ul class='pagination pagination-sm justify-content-center'>");
			
			// 이전 : 시작페이지가 1보다 클때만 이전이 있다.
			if(startPage>1) {
				sb.append("<li class='page-item'>");
				sb.append("<a class='page-link' href='?p=" + (startPage-1) + "&s=" + sizeOfPage + "&b=" +sizeOfBlock + "' aria-label='Previous'>");
				sb.append("<span aria-hidden='true'>&laquo;</span>");
				sb.append("</a>");
			}
			// 페이지 이동
			for(int i=startPage;i<=endPage;i++) {
				if(i==currentPage) { // 현재 페이지는 링크를 걸지 않는다.
					sb.append("<li class='page-item active'  aria-current='page'><a class='page-link'>" + i + "</a></li>");
				}else {
					sb.append("<li class='page-item'><a class='page-link' href='?p=" + i + "&s=" + sizeOfPage + "&b=" + sizeOfBlock +"'>" + i + "</a></li>");
				}
			}
			// 다음 : 끝페이지가 전체페이지 수 보다 적을때만 다음이 있다.
			if(endPage<totalPage) {
				sb.append("<li class='page-item'>");
				sb.append("<a class='page-link' href='?p=" + (endPage+1) + "&s=" + sizeOfPage + "&b=" +sizeOfBlock + "' aria-label='Next'>");
				sb.append("<span aria-hidden='true'>&raquo;</span>");
				sb.append("</a>");
				sb.append("</li>");
			}
			
			sb.append("</ul>");
			sb.append("</nav>");
		}
		return sb.toString();
	}

	public String getPageList2() {
		StringBuffer sb = new StringBuffer();
		if(totalCount>0) {
			sb.append("<nav aria-label='Page navigation example'>");
			sb.append("<ul class='pagination pagination-sm justify-content-center'>");
			
			// 이전 : 시작페이지가 1보다 클때만 이전이 있다.
			if(startPage>1) {
				sb.append("<li class='page-item'>");
				sb.append("<a class='page-link' href='javascript:sendPost(\"?\",{\"p\":"+(startPage-1)+",\"s\":"+sizeOfPage+",\"b\":"+sizeOfBlock+"})' aria-label='Previous'>");
				sb.append("<span aria-hidden='true'>&laquo;</span>");
				sb.append("</a>");
			}
			// 페이지 이동
			for(int i=startPage;i<=endPage;i++) {
				if(i==currentPage) { // 현재 페이지는 링크를 걸지 않는다.
					sb.append("<li class='page-item active'  aria-current='page'><a class='page-link'>" + i + "</a></li>");
				}else {
					sb.append("<li class='page-item'><a class='page-link' href='javascript:sendPost(\"?\",{\"p\":"+i+",\"s\":"+sizeOfPage+",\"b\":"+sizeOfBlock+"})'>" + i + "</a></li>");
				}
			}
			// 다음 : 끝페이지가 전체페이지 수 보다 적을때만 다음이 있다.
			if(endPage<totalPage) {
				sb.append("<li class='page-item'>");
				sb.append("<a class='page-link' href='javascript:sendPost(\"?\",{\"p\":"+(endPage+1)+",\"s\":"+sizeOfPage+",\"b\":"+sizeOfBlock+"})' aria-label='Next'>");
				sb.append("<span aria-hidden='true'>&raquo;</span>");
				sb.append("</a>");
				sb.append("</li>");
			}
			
			sb.append("</ul>");
			sb.append("</nav>");
		}
		return sb.toString();
	}
	
	
	
	
	
	
	
}
