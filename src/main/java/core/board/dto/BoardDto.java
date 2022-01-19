package core.board.dto;

import java.time.LocalDateTime;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class BoardDto {
	
	private int boardIdx;
	private String title;
	private String contents;
	private int hitCnt;
	private String creUsrId;
	private LocalDateTime creDt;
	private String updUsrId;
	private LocalDateTime updDt;
}
