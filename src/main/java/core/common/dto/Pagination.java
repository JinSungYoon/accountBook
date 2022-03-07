package core.common.dto;

import lombok.Data;

@Data
public class Pagination {

	private int listSize = 10;                //초기값으로 목록개수를 10으로 셋팅
	private int rangeSize = 10;            //초기값으로 페이지범위를 10으로 셋팅
	private int page;
	private int range;
	private int listCnt;
	private int pageCnt;
	private int startPage;
	private int startList;
	private int endPage;
	private boolean prev;
	private boolean next;
	
	public void pageInfo(int page, int range, int rangeSize, int listCnt) {
		this.page = page;
		this.range = range;
		this.rangeSize = rangeSize;
		this.listCnt = listCnt;

		//전체 페이지수 
		//두 개의 정수를 서로 나누는 작업을 수행하면 항상 내림 된 정수가 된다.
		//따라서 우리가 알고있는 올림을 하기 위해서는 나눠지는값을 나누는값으로 나눈 나머지가 0이면 0을 더하고 아니면 1을 더하는 방식으로 계산해야한다.
		this.pageCnt = (int) listCnt / listSize + ((listCnt % listSize == 0) ? 0 : 1);

		//시작 페이지
		this.startPage = (range - 1) * rangeSize + 1 ;
		
		//끝 페이지
		this.endPage = range * rangeSize;

		//게시판 시작번호
		this.startList = (page - 1) * listSize;

		//이전 버튼 상태
		this.prev = range == 1 ? false : true;

		//다음 버튼 상태
		this.next = endPage > pageCnt ? false : true;
		if (this.endPage > this.pageCnt) {
			this.endPage = this.pageCnt;
			this.next = false;
		}
	}
	
}
