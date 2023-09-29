import java.io.Serializable;

public class Room implements Serializable {
    private static int ID_COUNT;
    private final int id;
    private String[] items;
    private int price;
    private boolean isAvailable = true;
    private RoomType type;

    public RoomType getType() {
        return type;
    }

    public Room (RoomType type) {
        this.type = type;
        id = ++ID_COUNT;
        switch (type)
        {
            case SINGLE -> {
                price = 20;
                items = new String[]{"single bed", "bathroom", "TV", "closet"};
            }
            case DOUBLE -> {
                price = 35;
                items = new String[]{"double bed", "bathroom", "TV", "closet"};
            }
            case DELUXE -> {
                price = 55;
                items = new String[]{"minibar", "bathtub", "king-size bed", "sitting area"};
            }
        }
    }
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public int getId() {
        return id;
    }

    public String[] getItems() {
        return items;
    }

    public int getPrice() {
        return price;
    }

    public static int getPriceForSpecificType(RoomType type) {
        if(type == RoomType.SINGLE) return 20;
        if(type == RoomType.DOUBLE) return 35;
        if(type == RoomType.DELUXE) return 55;
        return -1;
    }
}

