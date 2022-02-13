package ru.warmouse.coolbooks;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.warmouse.coolbooks.persistence.Book;
import ru.warmouse.coolbooks.persistence.BookRepository2;

@RestController
@RequestMapping("books")
public class BookController {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository2 bookRepository;

    // @Measure(value = "baseline", warmup = 2)
    // @GetMapping("keywords/{keywordsString}")
    // public List<Book> getBookByTitle(@PathVariable String keywordsString) {
    //     String[] keywords = keywordsString.split("\\+");
    //     if (keywords.length == 1) {
    //         return bookRepository.findAllByNameContaining(keywords[0]);
    //     } else if (keywords.length == 2) {
    //         return bookRepository.findAllByNameContainingAndNameContaining(
    //                 keywords[0], keywords[1]);
    //     } else if (keywords.length == 3) {
    //         return bookRepository.findAllByNameContainingAndNameContainingAndNameContaining(
    //                 keywords[0], keywords[1], keywords[2]);
    //     }
    //     return null;
    // }

    @PostMapping("generate/{count}")
    public void generateBooks(@PathVariable("count") int count) throws SQLException {
        bookService.generateBooks(count);
    }

    @PostMapping("generate")
    public void generateSomeBooks() throws SQLException {
        bookService.generateTestData();
    }

    @PostMapping("add/{name}")
    public void addBook(@PathVariable("name") String name) throws SQLException {
        bookService.addBook(name);
    }

    @GetMapping
    public List<Book> getBooks(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return bookService.getPage(page, size);
    }

    @GetMapping("keywords/{keys}")
    public List<Book> generateBooks(@PathVariable("keys") String keys) throws SQLException {
        String[] split = keys.split(" ");
        return bookService.findByKeyWords(List.of(split));
    }
}
