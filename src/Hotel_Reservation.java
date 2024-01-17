import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Hotel_Reservation extends DbConnector implements Hotel_Interface {
    @Override
    public void createReservation(Connection connection, Scanner scanner) {
//        Checking For Existing Customer or Not
        Customers c = new Customers();
        System.out.println("Select Any One Option:");
        System.out.println("1.New Customer");
        System.out.println("2.Existing Customer");
        int choice = scanner.nextInt();
        int custId = -1;
        if (choice == 1) {
            custId = c.createCustomer(connection, scanner);
        } else if (choice == 2) {
            try {
                custId = c.existingCustomer(connection, scanner);
            } catch (RuntimeException e) {
                return;
            }
        } else {
            System.out.println("Invalid Option");
            return;
        }
        System.out.println("Customer Id:" + custId);
//       Finding Customer Name & Id:
        String custNameFind = String.format("Select * from customer WHERE custId=%d;", custId);
        String custName = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(custNameFind);
            if (resultSet.next()) {
                custName = resultSet.getString("custName");
            } else {
                System.out.println("No such Name exisits");
            }
            System.out.println("Customer Name: " + custName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
//        Viewing Available Cars
        viewReservation(connection, scanner);
//        Entering The details of the car
        System.out.println("Enter the Room Number:");
        int roomName = scanner.nextInt();
        System.out.println("Enter the No. of Days You Want to Rent:");
        int days = scanner.nextInt();
        String query = String.format("Select * from hotel_booking WHERE roomName=%d;", roomName);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int pricePerDay = resultSet.getInt("room_price");
                int price = pricePerDay * days;
                int roomId = resultSet.getInt("roomId");
                String query1 = String.format("UPDATE hotel_booking SET isReserved=%d,customerId=%d,cust_name ='%s',days=%d ,reserved_time=current_time WHERE roomName=%d;", 1, custId, custName, days, roomName);
                String query2 = String.format("UPDATE customer SET cust_room_id=%d ,total_price=%d, room_name=%d ,hotel_days=%d WHERE custId=%d;", roomId, price, roomName, days, custId);
                try {
                    int result = statement.executeUpdate(query1);
                    statement.executeUpdate(query2);
                    if (result > 0) {
                        System.out.println("Booked Successfully");
                        System.out.println("You Need To Pay :" + price + " Rs");
                    } else {
                        System.out.println("Booking Failed!");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Room Does Not Exists!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void viewReservation(Connection connection, Scanner scanner) {
        String carAvailable = "Select * from hotel_booking WHERE isReserved=0;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(carAvailable);
            System.out.println("Available Rooms :");
            System.out.println("+----------------+-----------------+---------------+--------------------------+");
            System.out.println("|        ID       | Room Number    | isReserved    | Price Per Day            |");
            System.out.println("+----------------+-----------------+---------------+--------------------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("roomId");
                int roomName = resultSet.getInt("roomName");
                int isReserved = resultSet.getInt("isReserved");
                int ppd = resultSet.getInt("room_price");
                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15d | %-13d | %-24s |\n",
                        id, roomName, isReserved, ppd);
            }

            System.out.println("+----------------+-----------------+---------------+--------------------------+");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addRoom(Connection connection, Scanner scanner) {
        System.out.println("Enter Room Number");
        int roomName = scanner.nextInt();
        System.out.println("Enter The Price of Room Per Day:");
        int roomPrice = scanner.nextInt();
        String query = String.format("INSERT INTO hotel_booking (roomName,room_price) VALUES(%d,%d)", roomName, roomPrice);
        try {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(query);
            if (rows > 0) {
                System.out.println("Room Added Successfully");
            } else {
                System.out.println("Failed To Add Car!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void removeRoom(Connection connection, Scanner scanner) {
        System.out.println("Enter The Room Number");
        int roomName = scanner.nextInt();
        String query = String.format("DELETE FROM hotel_booking WHERE roomName=%d", roomName);
        try {
            Statement statement = connection.createStatement();
            int rows = statement.executeUpdate(query);
            if (rows > 0) {
                System.out.println("Room Deleted Successfully");
            } else {
                System.out.println("Room Does Not Exists!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void checkout(Connection connection, Scanner scanner) {
        Customers c = new Customers();
        int custId = -1;
        try {
            custId = c.existingCustomer(connection, scanner);
            System.out.println("Enter the Room Number:");
            int roomName = scanner.nextInt();
            String query = String.format("Select * from hotel_booking WHERE roomName=%d;", roomName);
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    int customersId = resultSet.getInt("customerId");
                    if (customersId == custId) {
                        String query1 = String.format("UPDATE hotel_booking SET isReserved=%d,customerId=%d ,cust_name=NULL,reserved_time=NULL, days=%d WHERE roomName=%d;", 0, -1, 0, roomName);
                        String query2 = String.format("UPDATE customer SET cust_room_id=%d ,room_name=NULL ,total_price=%d ,hotel_days=%d WHERE custId=%d;", -1, 0, 0, custId);
                        try {
                            int result = statement.executeUpdate(query1);
                            statement.executeUpdate(query2);
                            if (result > 0) {
                                System.out.println("Checkout Successfully");
                            } else {
                                System.out.println("Checkout Failed!");
                            }
                        } catch (SQLException e) {
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Customer Does Not Have " + roomName);
                    }
                } else {
                    System.out.println("Room Does Not Exists!");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (RuntimeException e) {
            return;
        }

    }

    @Override
    public void seeRoomDetails(Connection connection, Scanner scanner) {
        String roomAvailable = "select * from hotel_booking;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(roomAvailable);
            System.out.println("Room Details :");
            System.out.println("+----------------+-----------------+----------------+-------------------------+----------------------+--------+------------------+");
            System.out.println("|        ID      | Room Number    |  isReserved    | Reservation Date        | Customer Name        | Days   | Price Per Day    |");
            System.out.println("+----------------+-----------------+----------------+-------------------------+----------------------+--------+------------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("roomId");
                int roomName = resultSet.getInt("roomName");
                int isReserved = resultSet.getInt("isReserved");
                String reservationDate;
                if(resultSet.getTimestamp("reserved_time")==null){
                    reservationDate ="N/A";
                }
                else{
                reservationDate = resultSet.getTimestamp("reserved_time").toString();
                }
                String custName = resultSet.getString("cust_name");
                int ppd = resultSet.getInt("room_price");
                int days = resultSet.getInt("days");
                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15d | %-13d | %-24s | %-20s | %-6d | %-16d |\n",
                        id, roomName, isReserved, reservationDate, custName, days, ppd);
            }

            System.out.println("+----------------+-----------------+----------------+-------------------------+----------------------+--------+------------------+");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void seeCustomerDetails(Connection connection, Scanner scanner) {
        String roomAvailable = "Select * from customer;";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(roomAvailable);
            System.out.println("Customer Details :");
            System.out.println("+----------------+-----------------+--------------------------+----------------+---------------------+--------+");
            System.out.println("|        ID      | Customer Name   | Email                     | Room Number   | Total Price         | Days   |");
            System.out.println("+----------------+-----------------+--------------------------+----------------+---------------------+--------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("custId");
                String cust_Name = resultSet.getString("custName");
                String email = resultSet.getString("email");
                int roomName = resultSet.getInt("room_name");
                int totalPrice = resultSet.getInt("total_price");
                int days = resultSet.getInt("hotel_days");
                // Format and display the reservation data in a table-like format
                System.out.printf("| %-14d | %-15s | %-24s | %-14d | %-19d | %-6d |\n",
                        id, cust_Name, email, roomName, totalPrice, days);
            }
            System.out.println("+----------------+-----------------+--------------------------+----------------+---------------------+--------+");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}