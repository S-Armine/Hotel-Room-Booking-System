import java.io.Serializable;

public enum RoomType implements Serializable {
    SINGLE,
    DOUBLE,
    DELUXE;


    public static String stringRepresentation() {
        return RoomType.SINGLE.name() + "\n" + RoomType.DOUBLE.name() + "\n" + RoomType.DELUXE.name();
    }
}