package ru.warmouse.coolbooks;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import ru.warmouse.coolbooks.persistence.Book;
import ru.warmouse.coolbooks.persistence.BookRepository2;

@SpringBootTest
@Commit
class BookServerApplicationTests {

    public final int BOOKS_AMOUNT = 100_000;

    @Autowired
    BookRepository2 bookRepository;

    @Autowired
    BookService bookService;

    @Test
    void contextLoads() {
    }

    //@Test
    public void removeAllBooks() throws SQLException {
        bookService.clear();
    }

    @Test
    @Transactional
    public void addBooks() throws SQLException {
        Random random = new Random();
        for (int i=0; i<BOOKS_AMOUNT; i++) {
            String title = "Book"+random.nextInt(BOOKS_AMOUNT);
            String authorName = "AuthorName"+random.nextInt(BOOKS_AMOUNT)+" ";
            String authorSurname = "AuthorSurname"+random.nextInt(BOOKS_AMOUNT);
            bookService.addBook(title+" by "+authorName+authorSurname);
        }
    }

    @Test
    public void showRandomBooks() {
        Random random = new Random();
        List<Book> all = bookRepository.findAll();
        System.out.println("Found "+all.size()+" books");
        for (int i=0;i<10;i++) {
            int index = random.nextInt(BOOKS_AMOUNT);
            System.out.println(all.get(index));
        }
    }

}
