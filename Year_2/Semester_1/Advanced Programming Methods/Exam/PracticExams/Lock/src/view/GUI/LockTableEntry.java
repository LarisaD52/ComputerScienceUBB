package view.GUI;
public class LockTableEntry {
    private final Integer location;
    private final Integer value;

    public LockTableEntry(Integer location, Integer value) {
        this.location = location;
        this.value = value;
    }

    public Integer getLocation() {
        return location;
    }

    public Integer getValue() {
        return value;
    }
}
