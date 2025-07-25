import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class hotel_reservation {
	//here we should first declare the url , username and passward for the sql connection
	private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
	private static final String username = "root";
	private static final String passward = "Atharv@123";
	
	public static void main(String [] args) throws ClassNotFoundException, SQLException {
		//Lets get all the required drivers for the further connection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		//Lets now create a new connection by using the same driver we created!! above after establishing the connection we should just start execution of the program
		// like start the menu of the application we are going to use for the reservation.
		
		try {
			Connection connection = DriverManager.getConnection(url,username,passward);
			
			while(true) {
				System.out.println("!!HOTEL RESERVATION SYSTEM!!");
				Scanner scanner = new Scanner(System.in);
				System.out.println("1. Reserve a new guest ");
				System.out.println("2. View All Guests");
				System.out.println("3. Delete any Guest");
				System.out.println("4. Update Guest Info");
				System.out.println("5. Exit");
				System.out.println("Enter your choice!!: ");
				int choice = scanner.nextInt();
				switch(choice) {
					case 1:
						reserveRoom(connection, scanner);
						break;
						
					case 2:
						viewreservation(connection);
						break;
						
					case 5:
//						exit();
						scanner.close();
						
					case 3:
						DeleteGuest(connection, scanner );
						break;
						
					case 4:
						UpdateGuest(connection, scanner);
						break;
						
					default:
						System.out.println("Invalid Choice, Try Again Later!!");
						
				}
			}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void reserveRoom(Connection connection, Scanner scanner) throws SQLException{
		System.out.println("Enter the name of the guest:");
		String name = scanner.next();
		System.out.println("Enter Room Number: ");
		int rnum = scanner.nextInt();
		System.out.println("Enter contact number: ");
		String cnum = scanner.next();
		
		String sql = "INSERT INTO reservations(guest_name,room_number,contact_number)"
				+ "VALUES('" + name+"', " + rnum +", '" + cnum+ "')";
		
		Statement smst = connection.createStatement();
		
		try(smst){
			int rows_affected = smst.executeUpdate(sql);
			
			if(rows_affected > 0) {
				System.out.println("Room  is Successfully Reserved!!");
			}
			else {
				System.out.println("Room Reservation failed!!");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void viewreservation(Connection connection ) throws SQLException {
		String sql = "SELECT reservation_id, guest_name, room_number,contact_number,reservation_date"
				+ " FROM reservations;";
		try {
			Statement smst = connection.createStatement();
			ResultSet result = smst.executeQuery(sql);
			
			System.out.println("--------------------------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------------------");
			System.out.println("Res_id------Guest_name-----Room_no-----Con_Num-----Reservation_date--------------");
			
			while(result.next()) {
				int id = result.getInt("reservation_id");
				String name = result.getString("guest_name");
				int r_no = result.getInt("room_number");
				String c_no = result.getString("contact_number");
				String r_date = result.getTimestamp("reservation_date").toString();
				
				System.out.printf("%d------%s-------%d-----------%s--------%s---------------\n",id,name,r_no,c_no,r_date);
			}
			System.out.println("--------------------------------------------------------------------------------");
			System.out.println("--------------------------------------------------------------------------------");
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void DeleteGuest(Connection connection, Scanner scanner) throws SQLException {
		System.out.println("Enter the Guest ID to be Deleted");
		int id = scanner.nextInt();
		String sql = "DELETE FROM reservations WHERE reservation_id = "+id;
		try(Statement smst = connection.createStatement()){
			int affected_row = smst.executeUpdate(sql);
			
			if(affected_row > 0) {
				System.out.println("Guest is Deleted Sucessfully");
			}
			else {
				System.out.println("Guest Deletion Failed!!");
			}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void UpdateGuest(Connection connection, Scanner scanner) throws SQLException{
		
		System.out.println("Enter the Guest's ID want to update:");
		int id = scanner.nextInt();
		System.out.println("Enter updated name:");
		String name = scanner.next();
		System.out.println("Enter updated contact number:");
		String contact = scanner.next();
		System.out.println("Enter updated Room_no:");
		int room = scanner.nextInt();
		
		String sql = "UPDATE reservations SET "
				+ "guest_name = "+ name + ",room_number = " + room + ",contact_numer = " + contact +"WHERE reservation_id = " + id + ";";
		
		try(Statement smst = connection.createStatement()){
			int rowaffected = smst.executeUpdate(sql);
			
			if(rowaffected > 0) {
				System.out.println("!! Updated Succesfully !!");
			}
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
