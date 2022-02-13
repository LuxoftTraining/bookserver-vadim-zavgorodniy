package ru.warmouse.coolbooks.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "book_keyword", indexes = {
        @Index(name = "idx_book_keyword_name", columnList = "name")
})
@Getter
@Setter
public class BookKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private long bookId;

    public BookKeyword() {
    }

    public BookKeyword(long bookId, String name) {
        this.bookId = bookId;
        this.name = name;
    }
}
