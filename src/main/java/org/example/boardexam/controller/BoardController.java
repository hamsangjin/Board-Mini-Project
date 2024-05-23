package org.example.boardexam.controller;

import lombok.RequiredArgsConstructor;
import org.example.boardexam.domain.Board;
import org.example.boardexam.service.BoardService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public String boards(Model model,
                          @RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Board> boards = boardService.findAllFriends(pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        return "/list";
    }

    @GetMapping("/writeform")
    public String addBoard(Model model) {
        model.addAttribute("board", new Board());
        return "/writeform";
    }

    @PostMapping("/writeform")
    public String addBoard(@ModelAttribute Board board) {
        boardService.save(board);
        return "redirect:/list";
    }

    @GetMapping("/view/{id}")
    public String viewBoard(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "/view";
    }

    @GetMapping("/updateform/{id}")
    public String updateBoard(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "/updateform";
    }

    @PostMapping("/updateform/{id}")
    public String updateBoard(@PathVariable Long id,
                              @RequestParam String password,
                              @ModelAttribute Board board) {
        Board updateBoard = boardService.findById(id);
        if (updateBoard.getPassword().equals(password)) {
            updateBoard.setTitle(board.getTitle());
            updateBoard.setName(board.getName());
            updateBoard.setContent(board.getContent());
            updateBoard.setUpdated_at(LocalDateTime.now());
            boardService.save(updateBoard);

            return "redirect:/list";
        } else return "redirect:/updateform/" + id;
    }

    @GetMapping("/deleteform/{id}")
    public String deleteBoard(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "/deleteform";
    }

    @PostMapping("/deleteform/{id}")
    public String deleteBoard(@PathVariable Long id,
                              @RequestParam String password,
                              @ModelAttribute Board board) {
        if (board.getPassword().equals(password)) {
            boardService.delete(id);
            return "redirect:/list";
        } else return "redirect:/deleteform/" + id;
    }
}
