import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ApartmentManagement {
    


    private static final String URL = "jdbc:mysql://localhost:3306/data";
    private static final String USER = "root";  
    private static final String PASSWORD = "12345";  

  
    public static Connection getConnection() {
        Connection conn = null;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");


            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established successfully!");

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error connecting to the database: " + e.getMessage());
        }
        return conn;
    }


    public static int addResident(String name, String apartmentNo, String contact, String email, String leaseStart, String leaseEnd) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int residentId = -1;

        try {
            conn = getConnection();
            if (conn != null) {

                String query = "INSERT INTO Residents (name, apartment_no, contact_number, email, lease_start, lease_end) VALUES (?, ?, ?, ?, ?, ?)";
                ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                ps.setString(2, apartmentNo);
                ps.setString(3, contact);
                ps.setString(4, email);
                ps.setString(5, leaseStart);
                ps.setString(6, leaseEnd);


                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        residentId = rs.getInt(1); 
                        System.out.println("Resident added successfully! Resident ID: " + residentId);
                    }
                } else {
                    System.out.println("Failed to add resident.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting resident: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
        return residentId;
    }

    public static void addPayment(int residentId, double amount) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            if (conn != null) {
                String query = "INSERT INTO FinancialTransactions (resident_id, amount) VALUES (?, ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, residentId);
                ps.setDouble(2, amount);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Payment added successfully for Resident ID: " + residentId);
                } else {
                    System.out.println("Failed to add payment.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding payment: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static void addParking(int residentId, boolean parkingRequired, String parkingSpace) {
        if (!parkingRequired) {
            System.out.println("No parking required for Resident ID: " + residentId);
            return;  
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            if (conn != null) {
                String query = "INSERT INTO Parking (resident_id, parking_space) VALUES (?, ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, residentId);
                ps.setString(2, parkingSpace);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Parking added successfully for Resident ID: " + residentId);
                } else {
                    System.out.println("Failed to add parking.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding parking: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static void addMaintenance(int residentId, double amount) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();
            if (conn != null) {
                String query = "INSERT INTO Maintenance (resident_id, maintenance_date, amount) VALUES (?, NOW(), ?)";
                ps = conn.prepareStatement(query);
                ps.setInt(1, residentId);
                ps.setDouble(2, amount);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Maintenance added successfully for Resident ID: " + residentId);
                } else {
                    System.out.println("Failed to add maintenance.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding maintenance: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

public static void displayAllData() {
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    try {
        conn = getConnection();
        if (conn != null) {
            stmt = conn.createStatement();

            // Display Residents
            System.out.println("\nResidents:");
            String query1 = "SELECT * FROM Residents";
            rs = stmt.executeQuery(query1);
            if (!rs.isBeforeFirst()) {
                System.out.println("No residents data available.");
            } else {
                while (rs.next()) {
                    System.out.println("Resident ID: " + rs.getInt("resident_id") +
                            ", Name: " + rs.getString("name") +
                            ", Apartment No: " + rs.getString("apartment_no"));
                }
            }

            System.out.println("\nPayments:");
            String query2 = "SELECT * FROM FinancialTransactions";
            rs = stmt.executeQuery(query2);
            if (!rs.isBeforeFirst()) {
                System.out.println("No payment data available.");
            } else {
                while (rs.next()) {
                    System.out.println("Resident ID: " + rs.getInt("resident_id") +
                            ", Amount: " + rs.getDouble("amount"));
                }
            }

            System.out.println("\nParking:");
            String query3 = "SELECT * FROM Parking";
            rs = stmt.executeQuery(query3);
            if (!rs.isBeforeFirst()) {
                System.out.println("No parking data available.");
            } else {
                while (rs.next()) {
                    System.out.println("Parking ID: " + rs.getInt("parking_id") +
                            ", Resident ID: " + rs.getInt("resident_id") +
                            ", Parking Space: " + rs.getString("parking_space"));
                }
            }

            System.out.println("\nMaintenance:");
            String query4 = "SELECT * FROM Maintenance";
            rs = stmt.executeQuery(query4);
            if (!rs.isBeforeFirst()) {
                System.out.println("No maintenance data available.");
            } else {
                while (rs.next()) {
                    System.out.println("Maintenance ID: " + rs.getInt("maintenance_id") +
                            ", Resident ID: " + rs.getInt("resident_id") +
                            //", Maintenance Date: " + rs.getDate("maintenance_date") +
                            ", Amount: " + rs.getDouble("amount"));
                }
            }
        }
    } catch (SQLException e) {
        System.err.println("Error displaying data: " + e.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Apartment Management System ---");
            System.out.println("1. Add Resident");
            System.out.println("2. Add Payment");
            System.out.println("3. Add Parking");
            System.out.println("4. Add Maintenance");
            System.out.println("5. Display All Data");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter Resident Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Apartment No: ");
                    String apartmentNo = scanner.nextLine();
                    System.out.print("Enter Contact Number: ");
                    String contact = scanner.nextLine();
                    System.out.print("Enter Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter Lease Start Date (yyyy-mm-dd): ");
                    String leaseStart = scanner.nextLine();
                    System.out.print("Enter Lease End Date (yyyy-mm-dd): ");
                    String leaseEnd = scanner.nextLine();


                    int residentId = addResident(name, apartmentNo, contact, email, leaseStart, leaseEnd);
                    break;

                case 2:

                    System.out.print("Enter Resident ID for Payment: ");
                    int paymentResidentId = scanner.nextInt();
                    System.out.print("Enter Payment Amount: ");
                    double amount = scanner.nextDouble();


                    addPayment(paymentResidentId, amount);
                    break;

                case 3:

                    System.out.print("Enter Resident ID for Parking: ");
                    int parkingResidentId = scanner.nextInt();
                    System.out.print("Does this resident require parking? (true/false): ");
                    boolean parkingRequired = scanner.nextBoolean();

                    if (parkingRequired) {
                        scanner.nextLine(); 
                        System.out.print("Enter Parking Space: ");
                        String parkingSpace = scanner.nextLine();

                        addParking(parkingResidentId, parkingRequired, parkingSpace);
                    } else {

                        addParking(parkingResidentId, parkingRequired, null);
                    }
                    break;

                case 4:

                    System.out.print("Enter Resident ID for Maintenance: ");
                    int maintenanceResidentId = scanner.nextInt();
                    System.out.print("Enter Maintenance Amount: ");
                    double maintenanceAmount = scanner.nextDouble();

                    addMaintenance(maintenanceResidentId, maintenanceAmount);
                    break;

                case 5:

                    displayAllData();
                    break;

                case 0:
                    System.out.println("Exiting the system.");
                    break;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 0);

        scanner.close();
    }
}
