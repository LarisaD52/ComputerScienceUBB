package view.GUI;

public class BarrierTableViewEntry {

    private final Integer index;
    private final Integer n;
    private final String waitingList;

    public BarrierTableViewEntry(Integer index, Integer n, String waitingList) {
        this.index = index;
        this.n = n;
        this.waitingList = waitingList;
    }

    public Integer getIndex() {
        return index;
    }

    public Integer getN() {
        return n;
    }

    public String getWaitingList() {
        return waitingList;
    }
}
