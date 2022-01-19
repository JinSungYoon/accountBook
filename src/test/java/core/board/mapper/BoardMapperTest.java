package core.board.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import core.board.dto.BoardDto;
import core.excel.mapper.ExcelMapper;
import core.statistics.dto.StatisticsCondDto;
import core.statistics.dto.StatisticsDto;
import core.statistics.mapper.StatisticsMapper;


@SpringBootTest
@PropertySource("classpath:/application.properties")
public class BoardMapperTest {

	@Autowired
	public BoardMapper boardMapper;

	
	
//	@Test
//	public void insertBoard() throws Exception{
//		BoardDto board = new BoardDto();
//		board.setTitle("Test Title");
//		board.setContents("Test Contents");
//		boardMapper.insertBoard(board);
//	}
	
//	@Test
//	public void searchBoardDetail() throws Exception{
//		int boardIdx = 1;
//		BoardDto board = boardMapper.selectBoardDetail(boardIdx);
//		System.out.println("result = "+board.toString());
//	}
	
}
