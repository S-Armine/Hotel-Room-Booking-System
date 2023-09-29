import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class HotelRoomBookingApplication {
    private static final String hotelFileName = "HotelDatabase.txt";
    private static final String customerFileName = "CustomersDatabase.txt";
    private static Scanner scanner = new Scanner(System.in);
    private static boolean shouldStart = true;
    private static ArrayList<Hotel> hotels = new ArrayList<>();
    static {
        try (FileInputStream fileInputStream = new FileInputStream(hotelFileName);
             ObjectInputStream ios = new ObjectInputStream(fileInputStream)) {
            if(fileInputStream.available() != 0) {
                hotels = (ArrayList<Hotel>) ios.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            shouldStart = false;
            System.out.println("Couldn't launch application");;
        }
    }

    private static ArrayList<Customer> customers = new ArrayList<>();
    static {
        try (
                FileInputStream fileInputStream = new FileInputStream(customerFileName);
                ObjectInputStream ios = new ObjectInputStream(fileInputStream))
        {
            if(fileInputStream.available() != 0) {
                customers = (ArrayList<Customer>) ios.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            shouldStart = false;
            System.out.println("Couldn't launch application");
        }

    }

    //This is start point of application where you can log in with hotel owner or customer permissions
    public static void main(String[] args) {
        if(shouldStart) {
            System.out.println("You have entered HotelRoomBookingApplication");
            while (true) {
                System.out.println("Press 1 if you want to continue as a hotel owner.");
                System.out.println("Press 2 if you want to continue as a customer.");
                System.out.println("Press 3 if you want to quit.");
                switch (scanner.nextLine()) {
                    case "1" -> {
                        hotelOwnerMode();
                        writeToFiles();
                        return;
                    }
                    case "2" -> {
                        customerMode();
                        writeToFiles();
                        return;
                    }
                    case "3" -> {
                        return;
                    }
                    default -> System.out.println("Something went wrong. Try again.");
                }
            }
        }
    }

    //This method is called when a user is hotel owner
    private static void hotelOwnerMode() {
        Hotel hotel = null;
        //This part of program is used so that user can choose to log in if the hotel is already registered or
        // register a new hotel
        while_loop: while (true) {
            System.out.println("Press 1 if you are already registered.");
            System.out.println("Press 2 if you are not registered.");
            System.out.println("Press 3 if you want to quit.");
            switch (scanner.nextLine()) {
                case "1" -> {
                    System.out.println("Enter hotel's name:");
                    String name;
                    do {
                        name = scanner.nextLine();
                        if (name.isEmpty()) System.out.println("Empty input. Try again");
                    } while (name.isEmpty());
                    if (!hotels.isEmpty()) {
                        for (Hotel hotel1 : hotels) {
                            if(hotel1.getName().equals(name)) hotel = hotel1;
                        }
                    }
                    if (hotel == null) {
                        System.out.println("Your hotel is not registered in application.");
                        System.out.println("Press 1 if you want to register new hotel");
                        System.out.println("Press 2 if you want to quit.");
                        switch (scanner.nextLine()) {
                            case "1" -> {
                                hotel = registrationOfNewHotel();
                                break while_loop;
                            }
                            case "2" -> {
                                return;
                            }
                            default -> System.out.println("Something went wrong. Try again.");
                        }
                    }
                    else {
                        break while_loop;
                    }
                }
                case "2" -> {
                    hotel = registrationOfNewHotel();
                    break while_loop;
                }
                case "3" -> {
                    return;
                }
                default -> System.out.println("Something went wrong. Try again.");
            }
        }
        //If the hotel owner has successfully loged in to account he can choose what operations he can do.
        System.out.println("You are the owner of " + hotel.getName() + " hotel. You can select operation from following list.");
        while (true) {
            System.out.println("Press");
            System.out.println("1 to view your profit.");
            System.out.println("2 to view your current bookings.");
            System.out.println("3 to view all of your bookings.");
            System.out.println("4 to add a room of specific type.");
            System.out.println("5 to see how many rooms of every type does the hotel have.");
            System.out.println("6 to quit.");
            switch (scanner.nextLine()) {
                case "1" -> {
                    System.out.println(hotel.getProfit());
                }
                case "2" -> {
                    if(hotel.getCurrentBookings().isEmpty()) System.out.println("Your hotel doesn't have any active booking.");
                    for (Booking currentBooking : hotel.getCurrentBookings()) {
                        System.out.println(currentBooking.toString());
                        System.out.println();
                    }
                }
                case "3" -> {
                    if(hotel.getAllBookings().isEmpty()) System.out.println("There have been no customers in this hotel since its registration.");
                    for (Booking allBooking : hotel.getAllBookings()) {
                        System.out.println(allBooking.toString());
                        System.out.println();
                    }
                }
                case "4" -> {
                    while (true) {
                        System.out.println("You can choose from one of these types. \n" + RoomType.stringRepresentation());
                        String typeString = scanner.nextLine();
                        RoomType type = typeVerification(typeString);
                        if (type == null) System.out.println("Something went wrong. Try again.");
                        else {
                            hotel.addRoom(type);
                            break;
                        }
                    }

                }
                case "5"-> {
                    int[] counts = hotel.getRoomCounts();
                    System.out.println("Single: " + counts[0]);
                    System.out.println("Double: " + counts[1]);
                    System.out.println("Deluxe: " + counts[2]);
                }
                case "6" -> {
                    boolean isPresent = false;
                    for (int i = 0; i < hotels.size(); i++) {
                        if(hotels.get(i).getName().equals(hotel.getName())) {
                            hotels.set(i, hotel);
                            isPresent = true;
                        }
                    }
                    if(!isPresent) hotels.add(hotel);
                    return;
                }
                default -> System.out.println("Something went wrong. Try again.");
            }
        }
    }

    //This method is called when a user is customer
    private static void customerMode() {
        Customer customer = null;
        boolean loop = true;
        //This part of program is used so that user can choose to log in if he is already registered or sign in
        while (loop) {
            System.out.println("Press 1 if you are already registered.");
            System.out.println("Press 2 if you are not registered.");
            System.out.println("Press 3 if you want to quit.");
            switch (scanner.nextLine()) {
                case "1" -> {
                    System.out.println("Enter your mail:");
                    String email;
                    do {
                        email = scanner.nextLine();
                        if (email.isEmpty()) System.out.println("Empty input. Try again");
                    } while (email.isEmpty());
                    if (!customers.isEmpty()) {
                        for (Customer customer1 : customers) {
                            if (customer1.getEmail().equals(email)) {
                                customer = customer1;
                                break;
                            }
                        }
                    }
                    if (customer == null) {
                        System.out.println("You are not registered in application.");
                        System.out.println("Press 1 if you want to register.");
                        System.out.println("Press 2 if you want to quit.");
                        switch (scanner.nextLine()) {
                            case "1" -> {
                                customer = registrationOfNewCustomer();
                                loop = false;
                            }
                            case "2" -> {
                                return;
                            }
                            default -> System.out.println("Something went wrong. Try again.");
                        }
                    }
                    else loop = false;
                }
                case "2" -> {
                    customer = registrationOfNewCustomer();
                    loop = false;
                }
                case "3" -> {
                    return;
                }
                default -> System.out.println("Something went wrong. Try again.");
            }
        }
        //If the customer has successfully loged in to account he can choose what operations he can do.
        System.out.println("Hello " + customer.getName());
        while_loop1: while (true) {
            System.out.println("Press");
            System.out.println("1 to view your current bookings.");
            System.out.println("2 to view all of your bookings.");
            System.out.println("3 to book a room of specific type.");
            System.out.println("4 to quit.");
            switch (scanner.nextLine()) {
                case "1" -> {
                    if (customer.getCurrentBookings().isEmpty()) System.out.println("You don't have any active booking.");
                    for (Booking currentBooking : customer.getCurrentBookings()) {
                        System.out.println(currentBooking.toString());
                        System.out.println();
                    }
                }
                case "2" -> {
                    if (customer.getAllBookings().isEmpty()) System.out.println("You haven't made any reservation since your registration.");
                    for (Booking allBooking : customer.getAllBookings()) {
                        System.out.println(allBooking.toString());
                        System.out.println();
                    }
                }
                case "3" -> {
                    //In this part of program user inputs are taken to determine booking parameters and checks whether they are valid inputs
                    while_loop: while (true) {
                        LocalDate toDate;
                        LocalDate fromDate;
                        System.out.println("You can choose from one of these types. \n" + RoomType.stringRepresentation());
                        String typeString = scanner.nextLine();
                        RoomType type = typeVerification(typeString);
                        if (type == null) System.out.println("Something went wrong. Try again.");
                        else {
                            while(true) {
                                try {
                                    System.out.println("Please enter the day that reservation should start in year-month-day format.");
                                    String from = scanner.nextLine();
                                    fromDate = LocalDate.parse(from);
                                    System.out.println("Please enter the day that reservation should end in year-month-day format.");
                                    String end = scanner.nextLine();
                                    toDate = LocalDate.parse(end);
                                    if (ChronoUnit.DAYS.between(fromDate, toDate) > 0
                                            && ChronoUnit.DAYS.between(LocalDate.now(), toDate) > 0) {
                                        break;
                                    } else {
                                        System.out.println("Invalid time period.");
                                    }
                                }catch (DateTimeParseException e) {
                                    System.out.println("Input was wrong, try again.");
                                }
                        }
                                    while(true) {
                                        System.out.println("Please enter a hotel from which you want to book a room.");
                                        System.out.println("You can choose from following hotels.");
                                        for (Hotel hotel : hotels) {
                                            System.out.println(hotel.getName());
                                        }
                                        String hotelName = scanner.nextLine();
                                        for (Hotel hotel : hotels) {
                                            if (hotel.getName().equals(hotelName)) {
                                                customer.makeReservation(hotel, type, fromDate, toDate);
                                                break while_loop;
                                            }
                                        }
                                        System.out.println("Something went wrong. Try again.");
                                    }
                                }
                            }

                        }
                case "4" -> {
                    boolean isPresent = false;
                    for (int i = 0; i < customers.size(); i++) {
                        if(customers.get(i).getEmail().equals(customer.getEmail())) {
                            customers.set(i, customer);
                            isPresent = true;
                        }
                    }
                    if(!isPresent) customers.add(customer);
                    return;
                }
                default -> System.out.println("Something went wrong. Try again.");
        }
            }
    }

    //This method is used for registration of new hotel
    public static Hotel registrationOfNewHotel() {
        System.out.println("Enter hotel's name: ");
        String name;
        do {
            name = scanner.nextLine();
            if (name.isEmpty()) System.out.println("Empty input. Try again");
        } while (name.isEmpty());
        boolean isInteger = true;
        int[] counts = new int[3];
        while (isInteger) {
        System.out.println("Enter numbers of single, double and deluxe rooms respectively.");
            try {
                for (int i = 0; i < 3; i++) {
                    counts[i] = Integer.parseInt(scanner.nextLine());
                }
                isInteger = false;
            } catch (NumberFormatException e) {
                System.out.println("Input must be integer.");
            }
        }
        return new Hotel(name, counts[0], counts[1], counts[2]);
    }

    //This method is used for registration of new customer
    public static Customer registrationOfNewCustomer() {
        System.out.println("Enter your name: ");
        String name;
        do {
            name = scanner.nextLine();
            if (name.isEmpty()) System.out.println("Empty input. Try again");
        } while (name.isEmpty());
        System.out.println("Enter your email: ");
        String email;
        do {
            email = scanner.nextLine();
            if (email.isEmpty()) System.out.println("Empty input. Try again");
        } while (email.isEmpty());
        return new Customer(name, email);
    }

    //This method is writing data to hotel and customer databases so after shutting down application the current state is saved
    public static void writeToFiles() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(customerFileName))) {
            oos.writeObject(customers);
        } catch (IOException e) {
            System.out.println("Couldn't write data in customer's database");
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(hotelFileName))) {
            oos.writeObject(hotels);
        } catch (IOException e) {
            System.out.println("Couldn't write data in hotel's database");
        }
    }

    //This method is used where a user should enter a type of room to check if the input is valid
    public static RoomType typeVerification(String type) {
        switch(type.toLowerCase()) {
            case "single" -> {
                return RoomType.SINGLE;
            }
            case "double" -> {
                return RoomType.DOUBLE;
            }
            case "deluxe" -> {
                return RoomType.DELUXE;
            }
            default -> {
                return null;
            }
        }
    }
}
