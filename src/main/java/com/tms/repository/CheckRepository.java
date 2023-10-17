package com.tms.repository;

import com.tms.exceptions.CheckException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Getter
@Repository
public class CheckRepository {
    public final Session session;

    @Autowired
    public CheckRepository(Session session) {
        this.session = session;
    }

    public void makeCheckForDepositAndWithdraw(String card, BigDecimal amount, String moneyCurrency, String type) throws CheckException {
        try {
            Integer checkNumber = new Random().nextInt(90000) + 10000;
            File file = makeUniqueFile(card, checkNumber);
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            writeCommonFields(bufferedWriter, checkNumber, type, "ProjectBank", "ProjectBank", card, card, amount, moneyCurrency);
        } catch (IOException e){
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
        } catch (IOException e) {
            throw new CheckException();
        }
    }

    public File makeUniqueFile(String card, Integer checkNumber){
        try {
            session.getTransaction().begin();
            Query query = session.createNativeQuery("SELECT id, first_name, last_name FROM clients" +
                    " WHERE id = (SELECT client_id FROM cards WHERE card_number = ?)");
            query.setParameter(1, card);
            List<Object[]> resultList = query.getResultList();
            Long id = null;
            String firstName = null;
            String lastName = null;
            if (!resultList.isEmpty()) {
                Object[] row = resultList.get(0);
                id = (Long) row[0];
                firstName = (String) row[1];
                lastName = (String) row[2];
            }
            session.getTransaction().commit();
            String dirName = id + "-" + firstName + "-" + lastName;
            File directory = new File("checks/" + dirName).getAbsoluteFile();
            if (!directory.exists()) {
                if (directory.mkdirs()){
                    throw new CheckException();
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
            session.getTransaction().rollback();
            log.warn("We have problem with file creation. The ex " + e);
        }
        return null;
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
        bufferedWriter.write("| Amount:" + String.format("%" + (52 - amount.toString().length() - moneyCurrency.length()) + "s%s", "", amount.doubleValue() + " " + moneyCurrency) + " |");
        bufferedWriter.newLine();
        bufferedWriter.write("|----------------------------------------------------------------|");
        bufferedWriter.close();
    }
}
