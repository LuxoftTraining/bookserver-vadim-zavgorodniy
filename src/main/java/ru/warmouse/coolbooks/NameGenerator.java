package ru.warmouse.coolbooks;

public class NameGenerator {
    public final int BOOKS_AMOUNT = 100_000;

    private final java.util.Random rand = new java.util.Random();

    public String generate() {
        return String.format("Title%d AuthorName%d AuthorSurname%d",
                rand.nextInt(BOOKS_AMOUNT), rand.nextInt(BOOKS_AMOUNT), rand.nextInt(BOOKS_AMOUNT));
    }
}
