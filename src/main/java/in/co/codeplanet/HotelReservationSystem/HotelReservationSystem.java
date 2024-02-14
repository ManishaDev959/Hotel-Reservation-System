package in.co.codeplanet.HotelReservationSystem;

import javax.xml.stream.events.StartElement;
import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem", "root", "root");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println();
                System.out.println("Hotel Management System");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.println("Enter your choice");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        reserveRoom(connection,scanner);
                        break;
                    case 2:
                        viewReservation(connection);
                        break;
                    case 3:
                        getRoomNumber(connection,scanner);
                        break;
                    case 4:
                        updateReservation(connection,scanner);
                        break;
                    case 5:
                        deleteReservation(connection,scanner);
                        break;
                    case 0:
                        exit();
                    default:
                        System.out.println("Enter a Valid Choice");
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    public static void reserveRoom(Connection connection, Scanner scanner) {
        System.out.println("Enter guest name");
        String guest_name = scanner.next();
        scanner.nextLine();
        System.out.println("Enter room number");
        int roomNumber = scanner.nextInt();
        System.out.println("Enter contact number");
        String contactNumber = scanner.next();
        String query = "insert into reservations(guest_name, room_number, contact_number) values (?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,guest_name);
            preparedStatement.setInt(2,roomNumber);
            preparedStatement.setString(3,contactNumber);
            int affectedRows=  preparedStatement.executeUpdate();
            if(affectedRows>0)
            {
                System.out.println("Reseration Successful");
            }
            else{
                System.out.println("Reservation failed");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void viewReservation(Connection connection)
    {
        String query="select * from reservations";
        try{
            Statement statement= connection.createStatement();
            ResultSet resultSet=statement.executeQuery(query);
            System.out.println("Current Reservations");
            System.out.println("+----------------+------------+-------------+----------------+-----------------+");
            System.out.println("| Reservation Id | Guest_name | Room Number | Contact Number | Reservation Time |");
            System.out.println("+----------------+------------+-------------+----------------+-----------------+");

            while(resultSet.next())
            {
                int reservation_id=resultSet.getInt(1);
                String guest_name=resultSet.getString(2);
                int room_number=resultSet.getInt(3);
                String contact_number=resultSet.getString(4);
                String reservationDate=resultSet.getTimestamp(5).toString();

                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s | \n ",
                        reservation_id, guest_name, room_number, contact_number, reservationDate);
            }

        }
        catch(SQLException e)
        {

        }

    }

     private static void getRoomNumber(Connection connection, Scanner scanner)
     {
         System.out.println("Enter your Reservation Id");
         int reservation_id=scanner.nextInt();
         System.out.println("Enter Your Name");
         String guest_name=scanner.next();
         String query="select room_number from reservations where reservation_id=? and guest_name=?";
         try{
             PreparedStatement preparedStatement=connection.prepareStatement(query);
             preparedStatement.setInt(1,reservation_id);
             preparedStatement.setString(2,guest_name);
             ResultSet resultSet= preparedStatement.executeQuery();
             if(resultSet.next())
             {
                 int room_number=resultSet.getInt(1);
                 System.out.println("Room Number for Reservation Id" + reservation_id + "is" + room_number);
             }
             else {
                 System.out.println("Reservation not found for the given Id and name");
             }
         }
         catch(SQLException e)
         {
             e.printStackTrace();
         }
     }

   private static void updateReservation(Connection connection, Scanner scanner)
   {
       System.out.println("Enter your Reservation id");
       int reservation_id=scanner.nextInt();
       scanner.nextLine();

       if(!reservation_exists(connection, reservation_id))
       {
           System.out.println("Reservation not found for the given id");
           return;
       }

       System.out.println("Enter new guest_name");
       String newGuestName=scanner.nextLine();
       System.out.println("Enter new room number");
       int newRoomNumber=scanner.nextInt();
       System.out.println("Enter new contact number");
       String newPhoneNumber=scanner.next();

       String query="update reservations set guest_name=?, room_number=?, contact_number=?";
       try{
           PreparedStatement preparedStatement= connection.prepareStatement(query);
           preparedStatement.setString(1,newGuestName);
           preparedStatement.setInt(2,newRoomNumber);
           preparedStatement.setString(3,newPhoneNumber);
           int affectedRows=   preparedStatement.executeUpdate();
           if(affectedRows>0)
           {
               System.out.println("Reservation Updated Successfully");
           }
           else{
               System.out.println("Reservation Updation Failed");
           }
       }
       catch(SQLException e)
       {
           e.printStackTrace();
       }


   }
   public static boolean reservation_exists(Connection connection, int reservation_id)
   {
       String query="select * from reservations where reservation_id=?";
       try{
           PreparedStatement preparedStatement=connection.prepareStatement(query);
           preparedStatement.setInt(1,reservation_id);
           ResultSet resultSet=preparedStatement.executeQuery();
           if(resultSet.next()) {
               return true;
           }
           else{
               return false;
           }
       }
       catch(SQLException e)
       {
           e.printStackTrace();

       }
       return false;
   }
    private static void deleteReservation(Connection connection,Scanner scanner)
    {
        try{
            System.out.println("Enter the reservation id to be deleted");
            int reservation_id=scanner.nextInt();

            if(!reservation_exists(connection,reservation_id))
            {
                System.out.println("Reservation for the given id not found");
                return ;
            }

            String query="delete from reservations where reservation_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,reservation_id);
            int affectedRows= preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Reservation for the given has been deleted successfully");
            }
            else{
                System.out.println("Deletion unsuccessful");
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    public static void exit() throws InterruptedException
    {
        System.out.print("Existing System");
       for(int i=1;i<=5;i++)
       {
           System.out.println(".");
           Thread.sleep(500);
       }
        System.out.println();
        System.out.println("Thankyou for Using Hotel Reservation System");
    }
}
