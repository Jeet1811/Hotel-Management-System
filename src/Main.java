import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DbConnector hotel = new DbConnector();
        try {
            hotel.dbConnector("Hotel");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage());
        }
        while (true) {
            try{
            System.out.println("------------------------------Welcome To Booking Portal!------------------------------");
            System.out.println("Please Select The Following Services");
            System.out.println("1. Room Booking System");
            System.out.println("2. Exit");
            Scanner scanner = new Scanner(System.in);
            int choice1 = scanner.nextInt();
            if (choice1 == 1) {
                while (true) {
                    System.out.println("-----------------------------------------");
                    System.out.println("HOTEL MANAGEMENT SYSTEM");
                    System.out.println("-----------------------------------------");
                    System.out.println("1. View Available Rooms");
                    System.out.println("2. Book a room");
                    System.out.println("3. Add a Room");
                    System.out.println("4. Delete a Room");
                    System.out.println("5. Checkout");
                    System.out.println("6. See the Room Details");
                    System.out.println("7. See the Customer Details");
                    System.out.println("8. Return to Main Menu"); // Option to return to the main menu.
                    System.out.print("Choose an option: ");
                    int choice=scanner.nextInt();
                    if (choice == 8) {
                        System.out.println("Returning to the main menu...");
                        break;
                    }

                    Hotel_Reservation h = new Hotel_Reservation();
                    switch (choice) {
                        case 1 -> h.viewReservation(hotel.connection, scanner);
                        case 2 -> h.createReservation(hotel.connection, scanner);
                        case 3 -> h.addRoom(hotel.connection, scanner);
                        case 4 -> h.removeRoom(hotel.connection, scanner);
                        case 5 -> h.checkout(hotel.connection, scanner);
                        case 6 -> h.seeRoomDetails(hotel.connection, scanner);
                        case 7 -> h.seeCustomerDetails(hotel.connection, scanner);
                        default -> System.out.println("Invalid choice. Try again.");
                    }
                }
            }
            else if (choice1 == 2) {
                System.out.println("Exiting...");
                scanner.close();
                return;
            }

            else {
                System.out.println("Choose Valid Options!");
            }
            }
            catch (InputMismatchException e){
                System.out.println("Input is Not Valid");
            }
        }
    }
}
