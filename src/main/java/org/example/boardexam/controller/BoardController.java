package org.example.boardexam.controller;

import lombok.RequiredArgsConstructor;
import org.example.boardexam.domain.Board;
import org.example.boardexam.service.BoardService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        Page<Board> boards = boardService.findAllBoards(pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        return "list";
    }

    @GetMapping("/writeform")
    public String addBoard(Model model) {
        model.addAttribute("board", new Board());
        return "writeform";
    }

    @PostMapping("/writeform")
    public String addBoard(@ModelAttribute Board board, RedirectAttributes alert) {
        alert.addFlashAttribute("msg", "추가 성공");
        boardService.save(board);
        return "redirect:/list";
    }

    @GetMapping("/view/{id}")
    public String viewBoard(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "view";
    }

    @GetMapping("/updateform/{id}")
    public String updateBoard(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "updateform";
    }

    @PostMapping("/updateform/{id}")
    public String updateBoard(@PathVariable Long id,
                              @RequestParam String password,
                              @ModelAttribute Board board,
                              RedirectAttributes alert) {
        Board updateBoard = boardService.findById(id);
        if (updateBoard.getPassword().equals(password)) {
            alert.addFlashAttribute("msg", "수정 성공");
            updateBoard.setTitle(board.getTitle());
            updateBoard.setName(board.getName());
            updateBoard.setContent(board.getContent());
            updateBoard.setUpdated_at(LocalDateTime.now());
            boardService.save(updateBoard);

            return "redirect:/list";
        } else{
            alert.addFlashAttribute("msg", "수정 실패");
            return "redirect:/updateform/" + id;
        }
    }

    @GetMapping("/deleteform/{id}")
    public String deleteBoard(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "deleteform";
    }

    @PostMapping("/deleteform/{id}")
    public String deleteBoard(@PathVariable Long id,
                              @RequestParam String password,
                              @ModelAttribute Board board,
                              RedirectAttributes alert) {
        Board deleteBoard = boardService.findById(id);
        if (deleteBoard.getPassword().equals(password)) {
            boardService.delete(id);
            alert.addFlashAttribute("msg", "삭제 성공");
            return "redirect:/list";
        } else{
            alert.addFlashAttribute("msg", "삭제 실패");
            return "redirect:/deleteform/" + id;
        }
    }
}
