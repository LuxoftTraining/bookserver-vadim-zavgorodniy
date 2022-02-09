package ru.warmouse.coolbooks;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.warmouse.coolbooks.persistence.Book;
import ru.warmouse.coolbooks.persistence.BookRepository;

@SpringBootApplication
public class CoolbooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoolbooksApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(BookRepository bookRepository)
	{
		return env ->
		{
			generateTestData(bookRepository);
			System.out.println("TEST DATA GENERATED");
		};
	}

	private void generateTestData(BookRepository bookRepository) {
		bookRepository.save(new Book("warmouse cool book 123"));
		bookRepository.save(new Book("cool book foo warmouse"));
		bookRepository.save(new Book("bar 13 book warmouse foo cool"));
		bookRepository.save(new Book("bazz cool qwe book rty warmouse  123qe"));
	}

}
