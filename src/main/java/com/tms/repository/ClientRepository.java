package com.tms.repository;

import com.tms.domain.Client;
import lombok.Getter;
import org.springframework.stereotype.Repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.Optional;

@Repository
@Getter
public class ClientRepository {
    private final Connection CONNECTION;

    {
        try {
            Class.forName("org.postgresql.Driver");
            CONNECTION = DriverManager.getConnection("jdbc:postgresql://localhost:5432/internet-banking", "postgres", "root");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean createClient(Client client){
        try {
            String sql = "INSERT INTO clients(id,first_name,last_name,phone_number,created)" +
                    "VALUES (DEFAULT,?,?,?,?)";
            PreparedStatement statement = CONNECTION.prepareStatement(sql);
            statement.setString(1, client.getFirstName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getPhoneNumber());
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Optional<Client> getClientById(Long id){
        try {
            String sql = "SELECT * FROM clients WHERE id = ?";
            PreparedStatement statement = CONNECTION.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return Optional.of(sqlParser(resultSet));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public Boolean updateClientFirstName(Client client){
        try {
            String sql = "UPDATE clients SET first_name=? WHERE id=?";
            PreparedStatement statement = CONNECTION.prepareStatement(sql);
            statement.setString(1, client.getFirstName());
            statement.setLong(2, client.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean updateClientLastName(Client client){
        try {
            String sql = "UPDATE clients SET last_name=? WHERE id=?";
            PreparedStatement statement = CONNECTION.prepareStatement(sql);
            statement.setString(1, client.getLastName());
            statement.setLong(2, client.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Boolean updateClientPhoneNumber(Client client){
        try {
            String sql = "UPDATE clients SET phone_number=? WHERE id=?";
            PreparedStatement statement = CONNECTION.prepareStatement(sql);
            statement.setString(1, client.getPhoneNumber());
            statement.setLong(2, client.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public Client sqlParser(ResultSet result) throws SQLException {
        Client client = new Client();
        client.setId(result.getLong("id"));
        client.setFirstName(result.getString("first_name"));
        client.setLastName(result.getString("last_name"));
        client.setPhoneNumber(result.getString("phone_number"));
        client.setCreated(result.getTimestamp("created"));
        return client;
    }
}
