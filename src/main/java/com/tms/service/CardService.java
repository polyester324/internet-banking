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

    public Optional<Card> getCardById(Long id){
        return cardRepository.findById(id);
    }

    public Optional<Card> getCardByNumber(String cardNumber){
        return cardRepository.findCardByCardNumber(cardNumber);
    }


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

    public void makeCheckForDepositAndWithdraw(String card, BigDecimal amount, String moneyCurrency, String type) throws CheckException {
        try {
            Integer checkNumber = new Random().nextInt(90000) + 10000;
            File file = makeUniqueFile(card, checkNumber);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            writeCommonFields(bufferedWriter, checkNumber, type, "ProjectBank", "ProjectBank", card, card, amount, moneyCurrency);
        } catch (IOException | FileCreationException e){
            throw new CheckException();
        }
    }

    public void makeCheckForTransfer(String cardSender, String cardReceiver, BigDecimal amount, String moneyCurrency, String type) throws CheckException {
        try {
            String bankNameSender = "ProjectBank";
            String bankNameReceiver = "AnotherBank";
            Integer checkNumber = new Random().nextInt(90000) + 10000;
            File file = makeUniqueFile(cardSender, checkNumber);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            writeCommonFields(bufferedWriter, checkNumber, type, bankNameSender, bankNameReceiver,  cardSender, cardReceiver, amount, moneyCurrency);
            fileWriter.close();
        } catch (IOException | FileCreationException e) {
            throw new CheckException();
        }
    }

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

    private void writeCommonFields(BufferedWriter bufferedWriter, Integer checkNumber, String type, String bankNameSender, String bankNameReceiver, String cardSender, String cardReceiver, BigDecimal amount, String moneyCurrency) throws IOException {
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
