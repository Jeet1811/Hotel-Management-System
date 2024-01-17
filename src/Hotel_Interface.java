import java.sql.Connection;
import java.util.Scanner;

public interface Hotel_Interface {
    void createReservation(Connection connection, Scanner scanner);
    void viewReservation(Connection connection, Scanner scanner);
    void addRoom(Connection connection, Scanner scanner);
    void removeRoom(Connection connection, Scanner scanner);
    void checkout(Connection connection, Scanner scanner);
    void seeRoomDetails(Connection connection,Scanner scanner);
    void seeCustomerDetails(Connection connection,Scanner scanner);
}
