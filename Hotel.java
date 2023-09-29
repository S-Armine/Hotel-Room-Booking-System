import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Hotel implements Serializable {
    private String name;
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Booking> allBookings = new ArrayList<>();
    private ArrayList<Booking> currentBookings = new ArrayList<>();
    private int[] roomCounts;
    private ArrayList<Room> singleRooms = new ArrayList<>();
    private ArrayList<Room> doubleRooms = new ArrayList<>();
    private ArrayList<Room> deluxeRooms = new ArrayList<>();
    private double profit;


    public Hotel(String name, int singleRoomCount, int doubleRoomCount, int deluxeRoomCount) {
        this.name = name;
        for (int i = 0; i < singleRoomCount; i++) {
            singleRooms.add(new Room(RoomType.SINGLE));
        }
        for (int i = 0; i < doubleRoomCount; i++) {
            doubleRooms.add(new Room(RoomType.DOUBLE));
        }
        for (int i = 0; i < deluxeRoomCount; i++) {
            deluxeRooms.add(new Room(RoomType.DELUXE));
        }
        roomCounts = new int[]{singleRoomCount, doubleRoomCount, deluxeRoomCount};
        BookingThread thread = new BookingThread(currentBookings);
        thread.setDaemon(true);
        thread.start();
    }

    public double getProfit() {
        return profit;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Booking> getAllBookings() {
        return allBookings;
    }

    public ArrayList<Booking> getCurrentBookings() {
        return currentBookings;
    }

    public int[] getRoomCounts() {
        return roomCounts;
    }

    //This method adds a room of specific type to hotel
    public void addRoom(RoomType type) {
        Room room = new Room(type);
        if(type == RoomType.SINGLE) {
            singleRooms.add(room);
            roomCounts[0]++;
        }
        else if (type == RoomType.DOUBLE) {
            doubleRooms.add(room);
            roomCounts[1]++;
        }
        else {
            deluxeRooms.add(room);
            roomCounts[2]++;
        }

    }

    //This method checks if there is a room of specific type that can be booked and returns a room if available null otherwise
    private Room getRoom(RoomType type) {
        Room room = null;
        switch (type)
        {
            case SINGLE -> {
                for (Room singleRoom : singleRooms) {
                    if(singleRoom.getIsAvailable()) {
                        singleRoom.setAvailable(false);
                        room = singleRoom;
                        break;
                    }
                }
            }
            case DOUBLE -> {
                for (Room doubleRoom : doubleRooms) {
                    if(doubleRoom.getIsAvailable()) {
                        doubleRoom.setAvailable(false);
                        room = doubleRoom;
                        break;
                    }
                }
            }
            case DELUXE -> {
                for (Room deluxeRoom : deluxeRooms) {
                    if(deluxeRoom.getIsAvailable()) {
                        deluxeRoom.setAvailable(false);
                        room = deluxeRoom;
                        break;
                    }
                }
            }
        }
        return room;
    }

    //This method is used for booking a room, if there isn't any available room it outputs corresponding message and returns null
    public Room bookRoom(Customer customer, RoomType type, LocalDate startDate, LocalDate endDate) {
        Room room = getRoom(type);
        if(room != null) {
            Booking booking = new Booking(customer, room, startDate, endDate, this);
            allBookings.add(booking);
            currentBookings.add(booking);
            customers.add(customer);
            profit += booking.getPrice();
            System.out.println("Room reservation was successfully made.");
        } else {
            System.out.println("Room reservation failed.");
        }
        return room;
    }
}


