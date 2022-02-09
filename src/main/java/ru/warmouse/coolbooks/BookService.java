package ru.warmouse.coolbooks;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.warmouse.coolbooks.persistence.Book;
import ru.warmouse.coolbooks.persistence.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public void generateBooks(int count) {
        final NameGenerator generator = new NameGenerator();
        generator.prepare();

        List<Book> books = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            Book book = new Book();
            book.setName(generator.generate());
            books.add(book);
        }
        bookRepository.saveAll(books);
    }

    public List<Book> getPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return bookRepository.findAll(pageRequest).toList();
    }
}
