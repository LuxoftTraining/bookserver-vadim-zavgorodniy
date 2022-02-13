package ru.warmouse.coolbooks.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookByKeywordRepository {
    @Autowired
    private DataSource dataSource;

    public List<Book> findByWords(List<String> words) throws SQLException {
        if (words.isEmpty()) {
            throw new RuntimeException("Word list is empty");
        }

        List<Book> res = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(prepareQuery(words));
                try (ResultSet rs = statement.getResultSet()) {
                    while (rs.next()) {
                        Book book = new Book();
                        book.setId(rs.getLong("id"));
                        book.setName(rs.getString("name"));
                        res.add(book);
                    }
                }
            }
        }
        return res;
    }

    /**
     * Prepare SQL query text.
     * select * from book where
     *   id in (select book_id from book_tag where name = 'word1')
     *   and id in (select book_id from book_tag where name = 'word2') ...
     *
     * @param words key word list
     * @return formatted query
     */
    private String prepareQuery(List<String> words) {
        StringBuilder sb = new StringBuilder("select * from book where ");
        for (int i = 0; i < words.size(); ++i) {
            if (i != 0) {
                sb.append("\n  and ");
            }
            sb.append("id in (select book_id from book_keyword where name = \'")
                    .append(words.get(i)).append("\') ");
        }
        return sb.toString();
    }

    public void saveAll(List<BookKeyword> tags) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String query = "insert into book_keyword(book_id, name) values(?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (BookKeyword tag : tags) {
                    statement.setLong(1, tag.getBookId());
                    statement.setString(2, tag.getName());
                    statement.execute();
                }
            }
        }
    }

    public void deleteAll() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String query = "delete from book_keyword";
            try (Statement statement = connection.createStatement()) {
                statement.execute(query);
            }
        }
    }
}
