package com.tms.repository;

import lombok.Getter;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
@Getter
public class TransactionRepository {
    private static final Connection CONNECTION = new ClientRepository().getCONNECTION();

    public boolean transferMoneyBetweenTwoClients(String cardSender, String cardReceiver, BigDecimal amount){
        try{
            try{
                CONNECTION.setAutoCommit(false);
                PreparedStatement statementSender = CONNECTION.prepareStatement("UPDATE cards SET balance = balance - ? WHERE card_number = ?");
                statementSender.setBigDecimal(1, amount);
                statementSender.setString(2, cardSender);
                statementSender.executeUpdate();
                PreparedStatement statementReceiver = CONNECTION.prepareStatement("UPDATE cards SET balance = balance + ? WHERE card_number = ?");
                statementReceiver.setBigDecimal(1, amount);
                statementReceiver.setString(2, cardReceiver);
                statementReceiver.executeUpdate();
                CONNECTION.commit();
                return true;
            } catch (SQLException e){
                System.out.println(e.getMessage());
                CONNECTION.rollback();
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
