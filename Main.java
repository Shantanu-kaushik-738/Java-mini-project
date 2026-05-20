import java.util.*;

interface Service {
    double calculateBill(int days, double rate);
}

class Room {
    private int roomNumber;
    private String roomType;
    private double pricePerDay;
    private boolean isAvailable;
    private int bookedDays;

    public Room(int roomNumber, String roomType, double pricePerDay) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerDay = pricePerDay;
        this.isAvailable = true;
        this.bookedDays = 0;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean avl) {
        this.isAvailable = avl;
    }

    public int getBookedDays() {
        return bookedDays;
    }

    public void setBookedDays(int days) {
        this.bookedDays = days;
    }
}

class Hotel implements Service {
    private Room[][] rooms;
    private final int floors = 3;
    private final int Rooms_PF = 5;

    public Hotel() {
        rooms = new Room[floors][Rooms_PF];

        for (int i = 0; i < floors; i++) {
            for (int j = 0; j < Rooms_PF; j++) {
                int roomNo = (i + 1) * 100 + (j + 1);

                String type;
                double price;

                if (i == 0) {
                    type = "Deluxe";
                    price = 4500.00;
                } else if (i == 1) {
                    type = "Double";
                    price = 2500.00;
                } else {
                    type = "Single";
                    price = 1500.00;
                }
                rooms[i][j] = new Room(roomNo, type, price);
            }
        }
    }

    public void bookRoom(int typeChoice, int roomIndex, int days) {
        int floor = typeChoice;
        if (floor < 0 || floor >= floors || roomIndex < 0 || roomIndex >= Rooms_PF) {
            System.out.println("Invalid room type or room selected.");
            return;
        }

        Room room = rooms[floor][roomIndex];
        if (room.isAvailable()) {
            room.setAvailable(false);
            room.setBookedDays(days);
            System.out.println("Room booked successfully for " + days + " days.");
        } else {
            System.out.println("Room already occupied. Only the booked room can be canceled.");
        }
    }

    public void cancelBooking(int typeChoice, int roomIndex, int cancelDays) {
        int floor = typeChoice;
        if (floor < 0 || floor >= floors || roomIndex < 0 || roomIndex >= Rooms_PF) {
            System.out.println("Invalid room type or room selected.");
            return;
        }

        Room room = rooms[floor][roomIndex];
        if (room.isAvailable()) {
            System.out.println("This room is not booked, so it cannot be canceled.");
            return;
        }

        int bookedDays = room.getBookedDays();
        if (cancelDays > bookedDays) {
            System.out.println("Cannot cancel for more days than were booked. Booked days: " + bookedDays);
            return;
        }

        room.setAvailable(true);
        room.setBookedDays(0);
        double penalty = calculateBill(cancelDays, room.getPricePerDay()) * 0.20;

        System.out.println("Booking canceled successfully.");
        System.out.println("Cancellation penalty (20% of " + cancelDays + " days): " + penalty);
    }

    @Override
    public double calculateBill(int days, double rate) {
        return days * rate;
    }

    public void displayRoomStatus() {
        System.out.println("\nRoom Status:");

        for (int i = 0; i < floors; i++) {
            for (int j = 0; j < Rooms_PF; j++) {
                Room room = rooms[i][j];
                String status = (room.isAvailable() ? "Available" : "Occupied");
                String info = "Room " + room.getRoomNumber() + " - " + status + " (" + room.getRoomType() + ")";
                if (!room.isAvailable()) {
                    info += " - Booked days: " + room.getBookedDays();
                }
                System.out.println(info);
            }
        }
    }

    public Room getRoom(int floor, int roomNo) {
        if ((floor >= 0 && floor < floors) && (roomNo >= 0 && roomNo < Rooms_PF)) {
            return rooms[floor][roomNo];
        }
        return null;
    }
}

public class Main {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        Scanner sc = new Scanner(System.in);
        boolean active = true;

        System.out.println("\n *** Hotel Room Reservation System *** ");

        while (active) {
            System.out.println("\nMenu Options:");

            System.out.println("1. View Room Status");
            System.out.println("2. Book a Room");
            System.out.println("3. Cancel a Booking");
            System.out.println("4. Exit");

            System.out.print("\nEnter choice (1-4): ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    hotel.displayRoomStatus();
                    break;

                case 2:
                    System.out.println("Select room type: 0 for Deluxe, 1 for Double, 2 for Single");
                    System.out.print("Enter room code (0-2): ");
                    int typeBook = sc.nextInt();

                    System.out.print("Enter Room number (0-4): ");
                    int rBook = sc.nextInt();

                    System.out.print("Enter Number of Days: ");
                    int bookDays = sc.nextInt();

                    hotel.bookRoom(typeBook, rBook, bookDays);

                    if (typeBook >= 0 && typeBook <= 2 && rBook >= 0 && rBook <= 4) {
                        Room targetRoom = hotel.getRoom(typeBook, rBook);
                        if (targetRoom != null && !targetRoom.isAvailable()) {
                            double billAmount = hotel.calculateBill(bookDays, targetRoom.getPricePerDay());
                            System.out.println("\nTotal Bill for " + bookDays + " days: " + billAmount);
                        }
                    }
                    break;

                case 3:
                    System.out.println("Select room type to cancel: 0 for Deluxe, 1 for Double, 2 for Single");
                    System.out.print("Enter room type code (0-2): ");
                    int typeCancel = sc.nextInt();

                    System.out.print("Enter Room number (0-4): ");
                    int rCancel = sc.nextInt();

                    System.out.print("Enter Number of Days to cancel (cannot exceed booked days): ");
                    int cancelDays = sc.nextInt();

                    hotel.cancelBooking(typeCancel, rCancel, cancelDays);

                    break;

                case 4:
                    System.out.println("Thank you for visiting!");
                    active = false;
                    break;

                default:
                    System.out.println("Invalid option selection. Try again.");
            }
        }
    }
}