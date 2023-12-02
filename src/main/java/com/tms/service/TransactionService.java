package com.tms.service;

import com.tms.domain.Client;
import com.tms.domain.MoneyCurrency;
import com.tms.exceptions.CheckException;
import com.tms.exceptions.ClientFromDatabaseNotFound;
import com.tms.exceptions.FileCreationException;
import com.tms.exceptions.NoAccessByIdException;
import com.tms.repository.BankRepository;
import com.tms.repository.CardRepository;
import com.tms.repository.TransactionRepository;
import com.tms.security.service.SecurityService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Data
public class TransactionService {
    private final ClientService clientService;
    private final SecurityService securityService;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final BankRepository bankRepository;

    /**
     * Method deposit replenishes the balance using the card number, specified amount and money currency
     * @return String if operation was successful and throws Exception otherwise
     */
    @Transactional(rollbackFor = Exception.class)
    public List<String> deposit(String cardNumber, BigDecimal amount, String moneyCurrency) {
        String cardReceiverMoneyCurrency = cardRepository.findCardMoneyCurrencyByCardNumber(cardNumber);
        String bank = cardRepository.findCardTypeByCardNumber(cardNumber);
        BigDecimal newAmount = amount.multiply(BigDecimal.valueOf(equalizationCoefficientToOneExchangeRate(moneyCurrency,
                cardReceiverMoneyCurrency))).setScale(2, RoundingMode.HALF_UP);
        transactionRepository.deposit(cardNumber, newAmount);
        log.info(String.format("Money was deposited to %s", cardNumber));
        return makeCheckForDepositAndWithdraw(cardNumber, amount.setScale(2, RoundingMode.HALF_UP), moneyCurrency, bank, "deposit");
    }

    /**
     * Method withdraw withdraws the specified amount from the balance by card number and currency
     * @return String if operation was successful and throws Exception otherwise
     */
    @Transactional(rollbackFor = Exception.class)
    public List<String> withdraw(String cardNumber, BigDecimal amount, String moneyCurrency) {
        String cardReceiverMoneyCurrency = cardRepository.findCardMoneyCurrencyByCardNumber(cardNumber);
        String bank = cardRepository.findCardTypeByCardNumber(cardNumber);
        BigDecimal newAmount = amount.multiply(BigDecimal.valueOf(equalizationCoefficientToOneExchangeRate(moneyCurrency,
                cardReceiverMoneyCurrency))).setScale(2, RoundingMode.HALF_UP);
        transactionRepository.withdraw(cardNumber, newAmount);
        log.info(String.format("Money was withdraw from %s", cardNumber));
        return makeCheckForDepositAndWithdraw(cardNumber, amount.setScale(2, RoundingMode.HALF_UP), moneyCurrency, bank, "withdraw");
    }

    /**
     * Method transfer transfers money from one account to another using card numbers
     * @return String if operation was successful and throws Exception otherwise
     */
    @Transactional(rollbackFor = Exception.class)
    public List<String> transfer(String cardSenderNumber, String cardReceiverNumber, BigDecimal amount, String MoneyCurrency) {
        if (securityService.checkAccessById(cardRepository.findCardByCardNumber(cardSenderNumber).getClientId())) {
            String cardSenderMoneyCurrency = cardRepository.findCardMoneyCurrencyByCardNumber(cardSenderNumber);
            String bankSender = cardRepository.findCardTypeByCardNumber(cardSenderNumber);
            String bankReceiver = cardRepository.findCardTypeByCardNumber(cardReceiverNumber);

            BigDecimal commission = bankRepository.findBankCommissionByBankName(bankReceiver);

            BigDecimal newAmountForSender = amount.multiply(BigDecimal.valueOf(equalizationCoefficientToOneExchangeRate(MoneyCurrency,
                    cardSenderMoneyCurrency))).setScale(2, RoundingMode.HALF_UP);
            BigDecimal newAmountForSenderWithCommission = newAmountForSender.add(newAmountForSender.multiply(commission));
            transactionRepository.withdraw(cardSenderNumber, newAmountForSenderWithCommission);
            deposit(cardReceiverNumber, amount, MoneyCurrency);
            log.info(String.format("Money was transferred from %s to %s", cardSenderNumber, cardReceiverNumber));
            return makeCheckForTransfer(cardSenderNumber, cardReceiverNumber, amount.setScale(2, RoundingMode.HALF_UP), MoneyCurrency, bankSender, bankReceiver, "transfer", newAmountForSender.multiply(commission).setScale(2, RoundingMode.HALF_UP));
        }
        throw new NoAccessByIdException(cardRepository.findCardByCardNumber(cardSenderNumber).getClientId(), SecurityContextHolder.getContext().getAuthentication().getName());
    }

    /**
     * Method makeRandomCheckNumber makes random number fromm 10_000 to 99_999
     * @return Integer
     */
    public Integer makeRandomCheckNumber(){
        return new Random().nextInt((90_000) + 10_000);
    }

    /**
     * Method makeCheckForDepositAndWithdraw prints check for deposit and withdraw
     * throws CheckException if check was not printed
     */
    public List<String> makeCheckForDepositAndWithdraw(String card, BigDecimal amount, String moneyCurrency, String bank, String type) {
        try {
            Integer checkNumber = makeRandomCheckNumber();
            File file = makeUniqueFile(card, checkNumber);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            BigDecimal commissionForDepositAndWithdraw = BigDecimal.valueOf(0);
            writeCheck(bufferedWriter, checkNumber, type, bank, bank, card, card, amount, moneyCurrency, commissionForDepositAndWithdraw);
            ArrayList<String> list = new ArrayList<>();
            list.add(file.getParentFile().getName());
            list.add(file.getName());
            return list;
        } catch (IOException | FileCreationException e){
            log.info("Check creation failed");
        }
        throw new CheckException();
    }

    /**
     * Method makeCheckForTransfer prints check for transfer
     * throws CheckException if check was not printed
     */
    public List<String> makeCheckForTransfer(String cardSender, String cardReceiver, BigDecimal amount, String moneyCurrency, String bankSender, String bankReceiver, String type, BigDecimal commission) {
        try {
            Integer checkNumber = makeRandomCheckNumber();
            File file = makeUniqueFile(cardSender, checkNumber);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            writeCheck(bufferedWriter, checkNumber, type, bankSender, bankReceiver,  cardSender, cardReceiver, amount, moneyCurrency, commission);
            fileWriter.close();
            ArrayList<String> list = new ArrayList<>();
            list.add(file.getParentFile().getName());
            list.add(file.getName());
            return list;
        } catch (IOException | FileCreationException e) {
            log.info("Check creation failed");
        }
        throw new CheckException();
    }

    /**
     * Method makeUniqueFile creates directory for every client in the db as they make any operation
     * throws FileCreationException if directory was not created
     */
    public File makeUniqueFile(String cardNumber, Integer checkNumber) {
        try {
            Client client = clientService.getClientById(cardRepository.findCardByCardNumber(cardNumber).getClientId()).orElseThrow(ClientFromDatabaseNotFound::new);

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
                    log.info("Failed to create unique file");
                }
            } catch (IOException e) {
                log.info("Failed to create unique file");
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
    public void writeCheck(BufferedWriter bufferedWriter, Integer checkNumber, String type, String bankNameSender, String bankNameReceiver, String cardSender, String cardReceiver, BigDecimal amount, String moneyCurrency, BigDecimal commission) throws IOException {
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
        bufferedWriter.write("| Commission:" + String.format("%" + (50 - commission.toString().length() - moneyCurrency.length()) + "s%s", "", commission + " " + moneyCurrency) + " |");
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
        return MoneyCurrency.valueOf(moneyCurrencyTwo).getVALUE()/MoneyCurrency.valueOf(moneyCurrencyOne).getVALUE();
    }
}
