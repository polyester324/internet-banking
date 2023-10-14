package com.tms.repository;

import org.springframework.stereotype.Repository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Repository
public class CheckRepository {
    //private static final Connection CONNECTION = new ClientRepository().getCONNECTION();

    public static boolean makeCheckForDepositAndWithdraw(String card, BigDecimal amount, String moneyCurrency, String type){
        /*try {
            Integer checkNumber = new Random().nextInt(90000) + 10000;
            File file = makeUniqueFile(card, checkNumber);
            FileWriter fileWriter = new FileWriter(file);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            java.util.Date currentDate = new java.util.Date();
            String bankName = "ProjectBank";
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
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
            bufferedWriter.write("| Bank:" + String.format("%" + (57 - bankName.length()) + "s%s", "", bankName) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("| Account:" + String.format("%" + (54 - card.length()) + "s%s", "", card) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("| Amount:" + String.format("%" + (52 - amount.toString().length() - moneyCurrency.length()) + "s%s", "", amount.doubleValue() + " " + moneyCurrency) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("|----------------------------------------------------------------|");

            bufferedWriter.close();
            fileWriter.close();
            return true;
        } catch (IOException e){
            System.out.println(e.getMessage());
        }*/
        return false;
    }

    public static boolean makeCheckForTransfer(String cardSender, String cardReceiver, BigDecimal amount, String moneyCurrency, String type){
        /*try {
            Integer checkNumber = new Random().nextInt(90000) + 10000;
            File file = makeUniqueFile(cardSender, checkNumber);
            FileWriter fileWriter = new FileWriter(file);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            java.util.Date currentDate = new java.util.Date();
            String bankNameSender = "ProjectBank";
            String bankNameReceiver = "AnotherBank";
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
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
            bufferedWriter.write("| Bank sender:" + String.format("%" + (50 - bankNameSender.length()) + "s%s", "", bankNameSender) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("| Bank receiver:" + String.format("%" + (48 - bankNameReceiver.length()) + "s%s", "", bankNameReceiver) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("| Account sender:" + String.format("%" + (47 - cardSender.length()) + "s%s", "", cardSender) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("| Account receiver:" + String.format("%" + (45 - cardReceiver.length()) + "s%s", "", cardReceiver) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("| Amount:" + String.format("%" + (52 - amount.toString().length() - moneyCurrency.length()) + "s%s", "", amount.doubleValue() + " " + moneyCurrency) + " |");
            bufferedWriter.newLine();
            bufferedWriter.write("|----------------------------------------------------------------|");

            bufferedWriter.close();
            fileWriter.close();
            return true;
        } catch (IOException e){
            System.out.println(e.getMessage());
        }*/
        return false;
    }

    public static File makeUniqueFile(String card, Integer checkNumber){
        /*try {
            PreparedStatement statement = CONNECTION.prepareStatement("SELECT id, first_name, last_name FROM clients " +
                    "WHERE id = (SELECT client_id FROM cards WHERE card_number = ?)");
            statement.setString(1, card);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String dirName = id + "-" + firstName + "-" + lastName;
            File directory = new File("checks/" + dirName).getAbsoluteFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            Date date = Date.valueOf(LocalDate.now());
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
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
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }*/
        return null;
    }
}
