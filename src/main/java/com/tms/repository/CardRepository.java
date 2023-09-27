package com.tms.repository;

import com.tms.domain.Card;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class CardRepository {
    private static final Connection CONNECTION = new ClientRepository().getCONNECTION();

    public Boolean createCard(Card card){
        try {
            String sql = "INSERT INTO cards(id, card_number, client_id, balance, money_currency, created)" +
                    "VALUES (DEFAULT,?,?,?,?,?)";
            PreparedStatement statement = CONNECTION.prepareStatement(sql);
            statement.setString(1, card.getCardNumber());
            statement.setLong(2, card.getClientId());
            statement.setBigDecimal(3, card.getBalance());
            statement.setString(4, card.getMoneyCurrency());
            statement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Card getCardById(Long id){
        try {
            String sql = "SELECT * FROM cards WHERE id = ?";
            PreparedStatement statement = CONNECTION.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return sqlParser(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return new Card();
    }

    public Boolean updateCardBalance(Card card){
        try {
            String sql = "UPDATE cards SET balance=? WHERE id=?";
            PreparedStatement statement = CONNECTION.prepareStatement(sql);
            statement.setBigDecimal(1, card.getBalance());
            statement.setLong(2, card.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean updateCardMoneyCurrency(Card card){
        try {
            String sql = "UPDATE cards SET money_currency=? WHERE id=?";
            PreparedStatement statement = CONNECTION.prepareStatement(sql);
            statement.setString(1, card.getMoneyCurrency());
            statement.setLong(2, card.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }



    public Card sqlParser(ResultSet result) throws SQLException {
        Card card = new Card();
        card.setId(result.getLong("id"));
        card.setCardNumber(result.getString("card_number"));
        card.setClientId(result.getLong("client_id"));
        card.setBalance(result.getBigDecimal("balance"));
        card.setMoneyCurrency(result.getString("money_currency"));
        card.setCreated(result.getTimestamp("created"));
        return card;
    }

}
