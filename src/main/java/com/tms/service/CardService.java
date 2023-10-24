package com.tms.service;

import com.tms.domain.Card;
import com.tms.domain.Client;
import com.tms.domain.MoneyCurrency;
import com.tms.exceptions.CheckException;
import com.tms.exceptions.FileCreationException;
import com.tms.repository.CardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final ClientService clientService;

    public CardService(CardRepository cardRepository, ClientService clientService) {
        this.cardRepository = cardRepository;
        this.clientService = clientService;
    }

    /**
     * Method createCard adds client from json data to db
     * @return true if card was created and false otherwise
     */
    public Boolean createCard(Card card){
        card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        try {
            card.setCreated(Timestamp.valueOf(LocalDateTime.now()));
            cardRepository.save(card);
            log.info(String.format("card with card number %s was created", card.getCardNumber()));
        } catch (Exception e){
            log.warn(String.format("have problem creating card with card number %s have error %s", card.getCardNumber(), e));
            return false;
        }
        return true;
    }

    /**
     * Method getCardById shows json data of card with requested id
     * @return Optional<Card>
     */
    public Optional<Card> getCardById(Long id){
        return cardRepository.findById(id);
    }

    /**
     * Method getCardByNumber shows json data of card with requested card number
     * @return Optional<Card>
     */
    public Optional<Card> getCardByNumber(String cardNumber){
        return cardRepository.findCardByCardNumber(cardNumber);
    }

    /**
     * Method deleteCardById deletes card from db by id
     * @return true if card was deleted and false otherwise
     */
    public boolean deleteCardById(Long id){
        try {
            cardRepository.deleteById(id);
            log.info(String.format("card with id %s was deleted", id));
        } catch (Exception e){
            log.warn(String.format("have problem deleting card with id %s have error %s", id, e));
            return false;
        }
        return true;
    }

    /**
     * Method deposit replenishes the balance using the card number, specified amount and money currency
     * @return true if operation was successful and false otherwise
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean deposit(String cardNumber, BigDecimal amount, String moneyCurrency){
        try {
            String cardReceiverMoneyCurrency = cardRepository.findCardMoneyCurrencyByCardNumber(cardNumber);
            BigDecimal newAmount = amount.multiply(BigDecimal.valueOf(equalizationCoefficientToOneExchangeRate(moneyCurrency,
                    cardReceiverMoneyCurrency))).setScale(2, RoundingMode.HALF_UP);
            cardRepository.deposit(cardNumber, newAmount);
            makeCheckForDepositAndWithdraw(cardNumber, amount.setScale(2, RoundingMode.HALF_UP), moneyCurrency, "deposit");
            log.info(String.format("Money was deposited to %s", cardNumber));
        } catch (Exception e){
            log.info(String.format("Money was not deposited to %s. Error: %s", cardNumber, e));
            return false;
        }
        return true;
    }

    /**
     * Method withdraw withdraws the specified amount from the balance by card number and currency
     * @return true if operation was successful and false otherwise
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean withdraw(String cardNumber, BigDecimal amount, String moneyCurrency){
        try {
            String cardReceiverMoneyCurrency = cardRepository.findCardMoneyCurrencyByCardNumber(cardNumber);
            BigDecimal newAmount = amount.multiply(BigDecimal.valueOf(equalizationCoefficientToOneExchangeRate(moneyCurrency,
                    cardReceiverMoneyCurrency))).setScale(2, RoundingMode.HALF_UP);
            cardRepository.withdraw(cardNumber, newAmount);
            makeCheckForDepositAndWithdraw(cardNumber, amount.setScale(2, RoundingMode.HALF_UP), moneyCurrency, "withdraw");
            log.info(String.format("Money was withdraw from %s", cardNumber));
        } catch (Exception e){
            log.info(String.format("Money was not withdraw from %s. Error: %s", cardNumber, e));
            return false;
        }
        return true;
    }

    /**
     * Method transfer transfers money from one account to another using card numbers
     * @return true if operation was successful and false otherwise
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean transfer(String cardSenderNumber, String cardReceiverNumber, BigDecimal amount){
        try {
            String cardSenderMoneyCurrency = cardRepository.findCardMoneyCurrencyByCardNumber(cardSenderNumber);
            String cardReceiverMoneyCurrency = cardRepository.findCardMoneyCurrencyByCardNumber(cardReceiverNumber);

            cardRepository.withdraw(cardSenderNumber, amount);

            BigDecimal newAmount = amount.multiply(BigDecimal.valueOf(equalizationCoefficientToOneExchangeRate(cardSenderMoneyCurrency,
                    cardReceiverMoneyCurrency))).setScale(2, RoundingMode.HALF_UP);

            cardRepository.deposit(cardReceiverNumber, newAmount);
            makeCheckForTransfer(cardSenderNumber, cardReceiverNumber, amount.setScale(2, RoundingMode.HALF_UP), cardSenderMoneyCurrency, "transfer");
            log.info(String.format("Money was transferred from %s to %s", cardSenderNumber, cardReceiverNumber));
        } catch (Exception e){
            log.info(String.format("Money was not transferred from %s to %s. Error: %s", cardSenderNumber, cardReceiverNumber, e));
            return false;
        }
        return true;
    }

    /**
     * Method makeCheckForDepositAndWithdraw prints check for deposit and withdraw
     * throws CheckException if check was not printed
     */
    public void makeCheckForDepositAndWithdraw(String card, BigDecimal amount, String moneyCurrency, String type) throws CheckException {
        try {
            Integer checkNumber = new Random().nextInt(90000) + 10000;
            File file = makeUniqueFile(card, checkNumber);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            writeCheck(bufferedWriter, checkNumber, type, "ProjectBank", "ProjectBank", card, card, amount, moneyCurrency);
        } catch (IOException | FileCreationException e){
            throw new CheckException();
        }
    }

    /**
     * Method makeCheckForTransfer prints check for transfer
     * throws CheckException if check was not printed
     */
    public void makeCheckForTransfer(String cardSender, String cardReceiver, BigDecimal amount, String moneyCurrency, String type) throws CheckException {
        try {
            String bankNameSender = "ProjectBank";
            String bankNameReceiver = "AnotherBank";
            Integer checkNumber = new Random().nextInt(90000) + 10000;
            File file = makeUniqueFile(cardSender, checkNumber);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            writeCheck(bufferedWriter, checkNumber, type, bankNameSender, bankNameReceiver,  cardSender, cardReceiver, amount, moneyCurrency);
            fileWriter.close();
        } catch (IOException | FileCreationException e) {
            throw new CheckException();
        }
    }

    /**
     * Method makeUniqueFile creates directory for every client in the db as they make any operation
     * throws FileCreationException if directory was not created
     */
    public File makeUniqueFile(String cardNumber, Integer checkNumber) throws FileCreationException {
        try {
            Client client = clientService.getClientById(getCardByNumber(cardNumber).get().getClientId()).get();
            Long id = client.getId();
            String firstName = client.getFirstName();
            String lastName = client.getLastName();

            String dirName = id + "-" + firstName + "-" + lastName;
            File directory = new File("checks/" + dirName).getAbsoluteFile();
            if (!directory.exists()) {
                if (directory.mkdirs()){
                    log.info("created new directory");
                }
            }
            Date date = Date.valueOf(LocalDate.now());
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmm-ss").format(date);
            String uniqueFileName = "check_" + checkNumber + "_" + timestamp + "_" + UUID.randomUUID() + ".txt";

            File uniqueFile = new File(directory, uniqueFileName);
            try {
                if (uniqueFile.createNewFile()) {
                    return uniqueFile;
                } else {
                    System.out.println("Failed to create unique file");
                }
            } catch (IOException e) {
                System.out.println("Failed to create unique file");
            }

            return uniqueFile;
        } catch (Exception e){
            log.warn("We have problem with file creation. The ex " + e);
        }
        throw new FileCreationException();
    }

    /**
     * Method writeCheck is for check sample
     */
    private void writeCheck(BufferedWriter bufferedWriter, Integer checkNumber, String type, String bankNameSender, String bankNameReceiver, String cardSender, String cardReceiver, BigDecimal amount, String moneyCurrency) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        java.util.Date currentDate = new java.util.Date();

        bufferedWriter.write("|----------------------------------------------------------------|");
        bufferedWriter.newLine();
        bufferedWriter.write("|                           Bank check                           |");
        bufferedWriter.newLine();
        bufferedWriter.write("| Check:" + String.format("%" + (56 - String.valueOf(checkNumber).length()) + "s%s", "", checkNumber) + " |");
        bufferedWriter.newLine();
        bufferedWriter.write("| " + dateFormat.format(currentDate) + String.format("%" + (52 - timeFormat.format(currentDate).length()) + "s%s", "", timeFormat.format(currentDate)) + " |");
        bufferedWriter.newLine();
        bufferedWriter.write("| Transaction type:" + String.format("%" + (45 - type.length()) + "s%s", "", type) + " |");
        bufferedWriter.newLine();
        if (!type.equals("transfer")){
            bufferedWriter.write("| Bank:" + String.format("%" + (57 - bankNameReceiver.length()) + "s%s", "", bankNameReceiver) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("| Account:" + String.format("%" + (54 - cardReceiver.length()) + "s%s", "", cardReceiver) + " |");
            bufferedWriter.newLine();
        } else {
            bufferedWriter.write("| Bank sender:" + String.format("%" + (50 - bankNameSender.length()) + "s%s", "", bankNameSender) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("| Bank receiver:" + String.format("%" + (48 - bankNameReceiver.length()) + "s%s", "", bankNameReceiver) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("| Account sender:" + String.format("%" + (47 - cardSender.length()) + "s%s", "", cardSender) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("| Account receiver:" + String.format("%" + (45 - cardReceiver.length()) + "s%s", "", cardReceiver) + " |");
            bufferedWriter.newLine();
        }
        bufferedWriter.write("| Amount:" + String.format("%" + (54 - amount.toString().length() - moneyCurrency.length()) + "s%s", "", amount + " " + moneyCurrency) + " |");
        bufferedWriter.newLine();
        bufferedWriter.write("|----------------------------------------------------------------|");
        bufferedWriter.close();
    }

    /**
     * Method equalizationCoefficientToOneExchangeRate allows to find the coefficient
     * by which you need to multiply the amount of money to withdraw, deposit or transfer from one currency to another
     * @return double
     */
    double equalizationCoefficientToOneExchangeRate(String moneyCurrencyOne, String moneyCurrencyTwo){
        if (!Objects.equals(moneyCurrencyOne, moneyCurrencyTwo)) {
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
