package org.example.boardexam.service;

import lombok.RequiredArgsConstructor;
import org.example.boardexam.domain.Board;
import org.example.boardexam.repository.BoardRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> findAllBoards(Pageable pageable) {
        Pageable sortedByDescId = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
        );
        return boardRepository.findAll(sortedByDescId);
    }

    public Board findById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    public Board save(Board board) {
        return boardRepository.save(board);
    }

    public void delete(Long id) {
        boardRepository.delete(findById(id));
    }
}
