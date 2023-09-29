import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

//This thread is made to check if bookings are still active
class BookingThread extends Thread implements Serializable {
    private ArrayList<Booking> bookings;
    public BookingThread(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if(!bookings.isEmpty()) {
                    for (Booking booking : bookings) {
                        if (ChronoUnit.DAYS.between(LocalDate.now(), booking.getEndDate()) < 0) {
                            booking.getRoom().setAvailable(true);
                            bookings.remove(booking);
                        }
                    }
                    Thread.sleep(1000 * 60 * 60 * 24);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}