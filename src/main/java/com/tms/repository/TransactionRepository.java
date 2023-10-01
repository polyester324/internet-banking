package com.tms.repository;

import com.tms.domain.MoneyCurrency;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Objects;

@Repository
@Getter
public class TransactionRepository {
    private static final Connection CONNECTION = new ClientRepository().getCONNECTION();

    public boolean transferMoneyBetweenTwoClients(String cardSender, String cardReceiver, BigDecimal amount){
        try{
            try{
                CONNECTION.setAutoCommit(false);
                PreparedStatement statementCurrencySender = CONNECTION.prepareStatement("SELECT money_currency FROM cards WHERE card_number = ?");
                statementCurrencySender.setString(1, cardSender);
                ResultSet resultSet = statementCurrencySender.executeQuery();
                resultSet.next();
                String moneyCurrencySender = resultSet.getString(1);
                PreparedStatement statementCurrencyReceiver = CONNECTION.prepareStatement("SELECT money_currency FROM cards WHERE card_number = ?");
                statementCurrencyReceiver.setString(1, cardReceiver);
                resultSet = statementCurrencyReceiver.executeQuery();
                resultSet.next();
                String moneyCurrencyReceiver = resultSet.getString(1);
                PreparedStatement statementSender = CONNECTION.prepareStatement("UPDATE cards SET balance = balance - ? WHERE card_number = ?");
                statementSender.setBigDecimal(1, amount);
                statementSender.setString(2, cardSender);
                statementSender.executeUpdate();
                amount = amount.multiply(BigDecimal.valueOf(TransactionRepository.equalizationCoefficientToOneExchangeRate(moneyCurrencySender, moneyCurrencyReceiver)));
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

    public static double equalizationCoefficientToOneExchangeRate(String moneyCurrencyOne, String moneyCurrencyTwo){
        if (Objects.equals(moneyCurrencyOne, moneyCurrencyTwo)){
            return 1;
        } else {
            if (Objects.equals(moneyCurrencyOne, "BYN")) {
                if (Objects.equals(moneyCurrencyTwo, "USD")) {
                    return MoneyCurrency.USD.getVALUE();
                } else if (Objects.equals(moneyCurrencyTwo, "EUR")) {
                    return MoneyCurrency.EUR.getVALUE();
                } else if (Objects.equals(moneyCurrencyTwo, "RUS")) {
                    return MoneyCurrency.RUS.getVALUE();
                }
            }
            if (Objects.equals(moneyCurrencyOne, "RUS")){
                if (Objects.equals(moneyCurrencyTwo, "USD")){
                    return MoneyCurrency.USD.getVALUE() / (MoneyCurrency.RUS.getVALUE());
                } else if (Objects.equals(moneyCurrencyTwo, "BYN")) {
                    return 1 / MoneyCurrency.RUS.getVALUE();
                } else if (Objects.equals(moneyCurrencyTwo, "EUR")) {
                    return MoneyCurrency.EUR.getVALUE() / (MoneyCurrency.RUS.getVALUE());
                }
            }
            if (Objects.equals(moneyCurrencyOne, "USD")){
                if (Objects.equals(moneyCurrencyTwo, "BYN")){
                    return 1 / (MoneyCurrency.USD.getVALUE());
                } else if (Objects.equals(moneyCurrencyTwo, "RUS")) {
                    return MoneyCurrency.RUS.getVALUE() / (MoneyCurrency.USD.getVALUE());
                } else if (Objects.equals(moneyCurrencyTwo, "EUR")) {
                    return MoneyCurrency.EUR.getVALUE() / (MoneyCurrency.USD.getVALUE());
                }
            }
            if (Objects.equals(moneyCurrencyOne, "EUR")){
                if (Objects.equals(moneyCurrencyTwo, "BYN")){
                    return 1 / (MoneyCurrency.EUR.getVALUE());
                } else if (Objects.equals(moneyCurrencyTwo, "RUS")) {
                    return MoneyCurrency.RUS.getVALUE() / (MoneyCurrency.EUR.getVALUE());
                } else if (Objects.equals(moneyCurrencyTwo, "USD")) {
                    return MoneyCurrency.USD.getVALUE() / (MoneyCurrency.EUR.getVALUE());
                }
            }
        }
        return 1;
    }
}
