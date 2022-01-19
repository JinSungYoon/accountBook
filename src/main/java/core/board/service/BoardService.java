package core.board.service;

import java.util.List;

import core.board.dto.BoardDto;

public interface BoardService {
	List<BoardDto> selectBoardList() throws Exception;
	void insertBoard(BoardDto board) throws Exception;
}
