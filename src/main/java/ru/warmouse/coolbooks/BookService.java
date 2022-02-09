package ru.warmouse.coolbooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.warmouse.coolbooks.persistence.Book;
import ru.warmouse.coolbooks.persistence.BookRepository;

@Service
public class BookService {
    public static final int PAGE_SIZE = 100;
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

    public List<Book> findByKeyWords(List<String> words) {
        final List<Book> res = new ArrayList<>();
        final Set<String> keys = words.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toUnmodifiableSet());
        Predicate<Book> predicate = createPredicate(keys);

        int pageNum = 0;
        Page<Book> page = bookRepository.findAll(PageRequest.of(pageNum++, PAGE_SIZE));
        while (!page.isEmpty()) {
            res.addAll(matchInPage(page, predicate));
            page = bookRepository.findAll(PageRequest.of(pageNum++, PAGE_SIZE));
        }

        return res;
    }

    private List<Book> matchInPage(Page<Book> page, Predicate<Book> predicate) {
        return page.stream()
                .filter(predicate)
                .collect(Collectors.toUnmodifiableList());
    }

    private Predicate<Book> createPredicate(Set<String> keys) {
        return book -> book.keyWords().containsAll(keys);
    }
}
