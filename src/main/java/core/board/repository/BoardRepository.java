package core.board.repository;

import java.util.List;
import java.util.Optional;

import core.board.dto.BoardDto;



public interface BoardRepository {
	BoardDto save(BoardDto member);
	Optional<BoardDto> findById(Long id);
	Optional<BoardDto> fidByName(String name);
	List<BoardDto> findAll();
}
