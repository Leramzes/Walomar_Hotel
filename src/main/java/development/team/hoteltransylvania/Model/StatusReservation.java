package development.team.hoteltransylvania.Model;

import java.util.HashMap;
import java.util.Map;

public enum StatusReservation {
    pendiente (1, "Pendiente"),
    confirmada (2, "Confirmada"),
    cancelada (3, "Cancelada"),
    ocupada (4, "Ocupada"),
    finalizada (5, "Finalizada");

    private final int value;
    private final String name;

    private static final Map<Integer, StatusReservation> MAP_BY_ID = new HashMap<>();
    static {
        for (StatusReservation sr : values()) {
            MAP_BY_ID.put(sr.value, sr);
        }
    }
    StatusReservation(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static StatusReservation fromId(int id) {
        return MAP_BY_ID.getOrDefault(id, null);
    }
}
