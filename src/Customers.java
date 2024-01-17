import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Customers {
    int createCustomer(Connection connection, Scanner scanner) {
        int custId=-1;
        System.out.print("Enter Your name: ");
        String custName = scanner.next();
        scanner.nextLine();
        System.out.print("Enter Email Address: ");
        String email = scanner.next();
        String query = "INSERT INTO customer(custName, email) " +
                "VALUES ('" + custName + "','" + email + "')";
        try {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(query);
            if (rows > 0) {
                System.out.println("Customer Added Successfully");
            } else {
                System.out.println("Customer Not Added!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        String query2 = String.format("Select * from customer WHERE email='%s'",email);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query2);
            if (resultSet.next()) {
                custId=resultSet.getInt("custId");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return custId;
    }

    int existingCustomer(Connection connection, Scanner scanner) throws RuntimeException {
        System.out.println("Enter your Email Address:");
        String emailconfirm = scanner.next();
        String query = String.format("Select * from customer WHERE email='%s';",emailconfirm);
        int custId=-1;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                System.out.println("Customer Exists");
                custId=resultSet.getInt("custId");
            }
            else{
                System.out.println("Customer does not exists!");
                throw new RuntimeException("Please Enter Valid Email!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return custId;
    }
}
