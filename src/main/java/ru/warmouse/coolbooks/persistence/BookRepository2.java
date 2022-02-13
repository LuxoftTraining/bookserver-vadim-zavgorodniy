package ru.warmouse.coolbooks.persistence;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository2 extends JpaRepository<Book, Integer> {
    List<Book> findAllByNameContaining(String keyword);
    List<Book> findAllByNameContainingAndNameContaining(String keyword1, String keyword2);
    List<Book> findAllByNameContainingAndNameContainingAndNameContaining(
        String keyword1, String keyword2, String keyword3);

}
