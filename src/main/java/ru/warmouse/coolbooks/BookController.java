package ru.warmouse.coolbooks;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.warmouse.coolbooks.persistence.Book;

@RestController
@RequestMapping("books")
public class BookController {
    @Autowired
    private BookService service;

    @PostMapping("generate/{count}")
    public void generateBooks(@PathVariable("count") int count) {
        service.generateBooks(count);
    }

    @GetMapping
    public List<Book> generateBooks(@RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return service.getPage(page, size);
    }

    @GetMapping("keywords/{keys}")
    public List<Book> generateBooks(@PathVariable("keys") String keys) {
        String[] split = keys.split("\\+");
        return service.findByKeyWords(List.of(split));
    }
}
