package core.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import core.board.dto.BoardDto;

@Mapper
public interface BoardMapper {
	List<BoardDto> selectBoardList() throws Exception;
	void insertBoard(BoardDto baord) throws Exception;
	BoardDto selectBoardDetail(int boardIdx) throws Exception;
}
