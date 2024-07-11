package nl.hu.bep2.casino.chips.data;

import nl.hu.bep2.casino.chips.domain.Chips;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

//Omdat JpaChipsRepository de @Primary annotatie heeft wordt deze repository niet gebruikt.
//Deze class is puur als voorbeeld om het verschil tussen JPA/JDBC versies wat concreter te maken.
//Je kunt 'm zonder problemen verwijderen. (of experimenteel: @Primary verplaatsen om juist deze te gaan gebruiken)
@Component
public class SqlChipsRepository implements ChipsRepository {

    private final DataSource dataSource;

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public SqlChipsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Chips> findByUsername(String username) {
        String sql = "select * from Chips where username=?";
        try (PreparedStatement statement = this.getConnection().prepareStatement(sql)) {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Chips found = Chips.fromDatabaseRow(
                            resultSet.getLong("id"),
                            resultSet.getString("username"),
                            resultSet.getLong("amount"),
                            resultSet.getDate("creation_date"),
                            resultSet.getDate("last_update")
                    );
                    return Optional.of(found);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Chips chips) {
        Date now = Date.valueOf(LocalDate.now());

        try(Connection connection = this.getConnection()) {
            if (chips.getId() != null) {
                PreparedStatement update = connection.prepareStatement("update chips set amount=?, last_update=? where username = ?");

                update.setLong(1, chips.getAmount());
                update.setDate(2, now);
                update.setString(3, chips.getUsername());
                update.executeUpdate();
                chips.updatedInDb(now);
            } else {
                connection.setAutoCommit(false);
                PreparedStatement selectNewId = connection.prepareStatement("select nextval('chips_seq')");
                ResultSet result = selectNewId.executeQuery();
                result.next();
                long newId = result.getLong(1);

                PreparedStatement insert = connection.prepareStatement("insert into chips(id, username, amount, creation_date, last_update) values(?,?,?,?,?)");
                insert.setLong(1, newId);
                insert.setString(2, chips.getUsername());
                insert.setLong(3, chips.getAmount());

                insert.setDate(4, now);
                insert.setDate(5, now);

                insert.executeUpdate();
                chips.savedInDb(newId, now);

                connection.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
