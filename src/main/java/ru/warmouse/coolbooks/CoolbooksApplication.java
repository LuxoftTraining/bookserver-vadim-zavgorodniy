package ru.warmouse.coolbooks;

import java.sql.SQLException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoolbooksApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoolbooksApplication.class, args);
	}

	@Bean
	public CommandLineRunner init(BookService bookService)
	{
		return env ->
		{
			// generateTestData(bookService);
			// System.out.println("TEST DATA GENERATED");
		};
	}

	private void generateTestData(BookService bookService) throws SQLException {
		bookService.addBook("warmouse cool book 123");
		bookService.addBook("cool book foo warmouse");
		bookService.addBook("bar 13 book warmouse foo cool");
		bookService.addBook("bazz cool qwe book rty warmouse  123qe");
	}

}
