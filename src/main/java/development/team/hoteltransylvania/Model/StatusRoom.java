package development.team.hoteltransylvania.Model;

import java.util.HashMap;
import java.util.Map;

public enum StatusRoom {
    libre(1, "libre"),
    ocupada(2, "ocupada"),
    en_mantenimiento(3, "en mantenimiento");

    private final int value;
    private final String name;

    private static final Map<Integer, StatusRoom> MAP_BY_ID = new HashMap<>();

    // Llenamos el mapa con los valores del enum
    static {
        for (StatusRoom sr : values()) {
            MAP_BY_ID.put(sr.value, sr);
        }
    }

    StatusRoom(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static StatusRoom fromId(int id) {
        return MAP_BY_ID.getOrDefault(id, null);
    }
}
