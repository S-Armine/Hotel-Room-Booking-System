import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class Booking implements Serializable {
    private Customer customer;
    private Room room;
    private LocalDate startDate;
    private LocalDate endDate;
    private Hotel hotel;
    private double tax;
    private double serviceFee;
    private double roomPrice;
    private double total;

    public Booking(Customer customer, Room room, LocalDate startDate, LocalDate endDate, Hotel hotel) {
        this.customer = customer;
        this.room = room;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hotel = hotel;
        this.roomPrice = room.getPrice();
        tax = 0.2 * roomPrice;
        serviceFee = 0.1 * roomPrice;
        this.total = getPrice();
    }

    public Customer getCustomer() {
        return customer;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public Customer  getCostumer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    //This method is used to get total price of a room for a period of booking
    public double getPrice() {
        return (roomPrice + tax + serviceFee) * ChronoUnit.DAYS.between(startDate, endDate);
    }

    //This method is used to get all information of booking in string representation
    @Override
    public String toString() {
        return "Hotel Name: " + hotel.getName() + "\n" +
                "Room ID: " + room.getId() + "\n" +
                "Room Type: " + room.getType() + "\n" +
                "Customer Name: " + customer.getName() + "\n" +
                "Customer Email: " + customer.getEmail() + "\n" +
                "From: " + startDate.toString() + "\n" +
                "To: " + endDate.toString() + "\n" +
                "Room Price(1 day): " + roomPrice + "$\n" +
                "Tax(1 day): " + tax + "$\n" +
                "Service Fee(1 day): " + serviceFee + "$\n" +
                "Total: " + this.total + "$\n" +
                "Items in room:" + Arrays.toString(room.getItems());
    }
}
