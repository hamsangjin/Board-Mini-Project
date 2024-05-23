package org.example.boardexam.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class Board {
    @Id
    private Long id;
    private String name;
    private String password;
    private String title;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Board() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }
}
