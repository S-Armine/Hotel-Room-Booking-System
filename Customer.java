import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Customer implements Serializable {
    private String name;
    private String email;
    private ArrayList<Booking> allBookings = new ArrayList<>();
    private ArrayList<Booking> currentBookings = new ArrayList<>();

    private int roomID;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
        BookingThread thread = new BookingThread(currentBookings);
        thread.setDaemon(true);
        thread.start();
    }

    public int getRoomID() {
        return roomID;
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

    public String getEmail() {
        return email;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    //This method is used when customer wants to make reservation
    public Booking makeReservation(Hotel hotel, RoomType type, LocalDate startDate, LocalDate endDate) {
        Room room = hotel.bookRoom(this, type, startDate, endDate);
        Booking booking = null;
        if (room != null) {
            booking = new Booking(this, room, startDate, endDate, hotel);
            allBookings.add(booking);
            currentBookings.add(booking);
        }
        return booking;
    }
}
