package com.tms.repository;

import com.tms.domain.Client;
import com.tms.domain.MoneyCurrency;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Objects;

@Slf4j
@Repository
@Getter
public class TransactionRepository {
    public final Session session;

    @Autowired
    public TransactionRepository(Session session) {
        this.session = session;
    }

    public boolean transferMoneyBetweenTwoClients(String cardSender, String cardReceiver, BigDecimal amount){
        try {
            session.getTransaction().begin();
            Query<Client> queryFirst = session.createNativeQuery("UPDATE cards SET balance = balance - ? WHERE card_number = ?");
            String moneyCurrency = findMoneyCurrencyOfTheCard(cardSender);
            queryFirst.setParameter(1, amount);
            queryFirst.setParameter(2, cardSender);
            queryFirst.executeUpdate();
            BigDecimal newAmount = amount.multiply(BigDecimal.valueOf(equalizationCoefficientToOneExchangeRate(moneyCurrency, findMoneyCurrencyOfTheCard(cardReceiver))));
            Query<Client> querySecond = session.createNativeQuery("UPDATE cards SET balance = balance + ? WHERE card_number = ?");
            querySecond.setParameter(1, newAmount);
            querySecond.setParameter(2, cardReceiver);
            querySecond.executeUpdate();
            session.getTransaction().commit();
            new CheckRepository(session).makeCheckForTransfer(cardSender, cardReceiver, amount, moneyCurrency, "transfer");
            return true;
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with transaction transfer. The ex " + e);
        }
        return false;
    }

    public boolean putMoneyIntoTheAccount(String card, BigDecimal amount, String moneyCurrency){
        try {
            session.getTransaction().begin();
            BigDecimal newAmount = amount.multiply(BigDecimal.valueOf(equalizationCoefficientToOneExchangeRate(moneyCurrency, findMoneyCurrencyOfTheCard(card))));
            Query<Client> query = session.createNativeQuery("UPDATE cards SET balance = balance + ? WHERE card_number = ?");
            query.setParameter(1, newAmount);
            query.setParameter(2, card);
            query.executeUpdate();
            session.getTransaction().commit();
            new CheckRepository(session).makeCheckForDepositAndWithdraw(card, amount, moneyCurrency, "deposit");
            return true;
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with transaction deposit. The ex " + e);
        }
        return false;
    }

    public boolean withdrawMoneyFromTheAccount(String card, BigDecimal amount, String moneyCurrency){
        try {
            session.getTransaction().begin();
            BigDecimal newAmount = amount.multiply(BigDecimal.valueOf(equalizationCoefficientToOneExchangeRate(moneyCurrency, findMoneyCurrencyOfTheCard(card))));
            Query<Client> query = session.createNativeQuery("UPDATE cards SET balance = balance - ? WHERE card_number = ?");
            query.setParameter(1, newAmount);
            query.setParameter(2, card);
            query.executeUpdate();
            session.getTransaction().commit();
            new CheckRepository(session).makeCheckForDepositAndWithdraw(card, amount, moneyCurrency, "withdraw");
            return true;
        } catch (Exception e){
            session.getTransaction().rollback();
            log.warn("We have problem with transaction withdraw. The ex " + e);
        }
        return false;
    }

    public String findMoneyCurrencyOfTheCard(String card){
        try {
            Query<String> query = session.createNativeQuery("SELECT money_currency FROM cards WHERE card_number = ?");
            query.setParameter(1,card);
            return query.getSingleResult();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public double equalizationCoefficientToOneExchangeRate(String moneyCurrencyOne, String moneyCurrencyTwo){
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
