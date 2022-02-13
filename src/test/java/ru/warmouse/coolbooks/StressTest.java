package ru.warmouse.coolbooks;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.loadtest4j.LoadTester;
import org.loadtest4j.Request;
import org.loadtest4j.Result;
import org.loadtest4j.drivers.jmeter.JMeterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class StressTest {
    private static final int BOOKS_AMOUNT = 100_000;
    //private static final LoadTester loadTester = LoadTesterFactory.getLoadTester();

    // @Autowired
    // private BookService bookService;

    @BeforeAll
    public static void clearDB(@Autowired BookService bookService) throws SQLException {
        bookService.clear();
    }

    @Test
    @Order(1)
    @RepeatedTest(3)
    @Rollback
    public void addBooksWarmup() {
        List<Request> requests = List.of(Request.post("/books/generate/1000")
                .withHeader("Accept", "application/json"));

        LoadTester loadTester = JMeterBuilder.withUrl("http", "localhost", 8082)
                .withNumThreads(10).withRampUp(1).build();

        Result result = loadTester.run(requests);

        printReport(result);
    }

    @RepeatedTest(3)
    @Order(10)
    @Transactional
    public void addBooks() {
        List<Request> requests = List.of(Request.post("/books/generate/100000")
                .withHeader("Accept", "application/json"));

        LoadTester loadTester = JMeterBuilder.withUrl("http", "localhost", 8082)
                .withNumThreads(1).withRampUp(1).build();

        Result result = loadTester.run(requests);

        printReport(result);

        assertThat(result.getResponseTime().getPercentile(90))
                .isLessThanOrEqualTo(Duration.ofSeconds(15));
    }

    @RepeatedTest(3)
    @Order(20)
    @Rollback
    public void test_findBooks_warmUp() {
        List<Request> requests = List.of(Request.get("/books/keywords/Title45600")
                .withHeader("Accept", "application/json"));

        LoadTester loadTester = JMeterBuilder.withUrl("http", "localhost", 8082)
                .withNumThreads(1000).withRampUp(1).build();

        Result result = loadTester.run(requests);

        printReport(result);
    }

    @RepeatedTest(3)
    @Order(21)
    @Rollback
    public void test_findByOneKeyword_within2sec() {
        Result result = findBooksTest("Title10045", 10000);

        printReport(result);

        assertThat(result.getResponseTime().getPercentile(90))
                .isLessThanOrEqualTo(Duration.ofMillis(30));
    }

    @RepeatedTest(3)
    @Order(22)
    @Rollback
    public void test_findByTwoKeyword_within2sec() {
        Result result = findBooksTest("Title10045%20AuthorSurname12500", 10000);

        printReport(result);

        assertThat(result.getResponseTime().getPercentile(90))
                .isLessThanOrEqualTo(Duration.ofMillis(30));
    }

    @Order(23)
    @RepeatedTest(3)
    @Rollback
    public void test_findByThreeKeyword_within2sec() {
        Result result = findBooksTest("AuthorName70210%20Title10045%20AuthorSurname12500", 10000);

        printReport(result);

        assertThat(result.getResponseTime().getPercentile(90))
                .isLessThanOrEqualTo(Duration.ofMillis(30));
    }

    public Result findBooksTest(String pattern, int threadsCount) {
        List<Request> requests = List.of(Request.get("/books/keywords/" + pattern)
                .withHeader("Accept", "application/json"));

        LoadTester loadTester = JMeterBuilder.withUrl("http", "localhost", 8082)
                .withNumThreads(threadsCount).withRampUp(1).build();

        return loadTester.run(requests);
    }

    private void printReport(Result result) {
        System.out.println("Median request time: "+
                result.getResponseTime().getMedian());
        System.out.println("Max request time: "+
                result.getResponseTime().getMax());
        System.out.println("OK %: "+
                result.getPercentOk());
        System.out.println("OK request number: "+
                result.getDiagnostics().getRequestCount().getOk());
        System.out.println("Total request number: "+
                result.getDiagnostics().getRequestCount().getTotal());
        System.out.println("Throughput (requests/sec): "+
                result.getDiagnostics().getRequestsPerSecond());
        System.out.println("Percentile 90: "+
                result.getResponseTime().getPercentile(90));
        System.out.println("Percentile 95: "+
                result.getResponseTime().getPercentile(95));
        System.out.println("Percentile 99: "+
                result.getResponseTime().getPercentile(99));
    }
}
