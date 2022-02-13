package ru.warmouse.coolbooks;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.warmouse.coolbooks.persistence.Book;
import ru.warmouse.coolbooks.persistence.BookByKeywordRepository;
import ru.warmouse.coolbooks.persistence.BookKeyword;
import ru.warmouse.coolbooks.persistence.BookRepository;

@Service
@Transactional
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookByKeywordRepository bookByKeywordRepository;

    private final NameGenerator generator = new NameGenerator();

    public void generateTestData() throws SQLException {
        addBook("warmouse cool book 123");
        addBook("cool book foo warmouse");
        addBook("bar 13 book warmouse foo cool");
        addBook("bazz cool qwe book rty warmouse  123qe");
    }

    public void generateBooks(int count) throws SQLException {
        for (int i = 0; i < count; i++) {
            addBook(generator.generate());
        }
    }

    public void addBook(String name) throws SQLException {
        Book book = new Book();
        book.setName(name);
        bookRepository.save(book);
        createKeywords(book);
    }

    private void createKeywords(Book book) throws SQLException {
        List<BookKeyword> tags = new ArrayList<>();
        for (String word : getKeyWords(book.getName())) {
            tags.add(new BookKeyword(book.getId(), word));
        }
        bookByKeywordRepository.saveAll(tags);
    }

    private Set<String> getKeyWords(String name) {
        return Stream.of(name.toLowerCase().split(" "))
                .map(String::trim)
                .filter(w -> !w.isBlank())
                .collect(Collectors.toUnmodifiableSet());
    }

    @Transactional(readOnly = true)
    public List<Book> getPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return bookRepository.findAll(pageRequest).toList();
    }

    @Transactional(readOnly = true)
    public List<Book> findByKeyWords(List<String> words) throws SQLException {
        List<String> lowWords = new ArrayList<>();
        for (String word : words) {
            if (!word.isBlank()) {
                lowWords.add(word.toLowerCase());
            }
        }
        return bookByKeywordRepository.findByWords(lowWords);
    }

    public void clear() throws SQLException {
        bookByKeywordRepository.deleteAll();
        bookRepository.deleteAll();
    }
}
