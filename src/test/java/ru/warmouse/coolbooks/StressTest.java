package ru.warmouse.coolbooks;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.loadtest4j.LoadTester;
import org.loadtest4j.Request;
import org.loadtest4j.Result;
import org.loadtest4j.drivers.jmeter.JMeterBuilder;

public class StressTest {
    //private static final LoadTester loadTester = LoadTesterFactory.getLoadTester();

    @Test
    public void test_generate100k_within5sec() {
        List<Request> requests = List.of(Request.post("/books/generate/100000")
                .withHeader("Accept", "application/json"));

        LoadTester loadTester = JMeterBuilder.withUrl("http", "localhost", 8082)
                .withNumThreads(1).withRampUp(1).build();

        Result result = loadTester.run(requests);

        printReport(result);

        assertThat(result.getResponseTime().getPercentile(90))
                .isLessThanOrEqualTo(Duration.ofSeconds(5));
    }

    @Test
    public void test_findByKeyword_within20sec() {
        List<Request> requests = List.of(Request.get("/books/keywords/book")
                .withHeader("Accept", "application/json"));

        LoadTester loadTester = JMeterBuilder.withUrl("http", "localhost", 8082)
                .withNumThreads(5).withRampUp(1).build();

        Result result = loadTester.run(requests);

        printReport(result);

        assertThat(result.getResponseTime().getPercentile(90))
                .isLessThanOrEqualTo(Duration.ofSeconds(20));
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
