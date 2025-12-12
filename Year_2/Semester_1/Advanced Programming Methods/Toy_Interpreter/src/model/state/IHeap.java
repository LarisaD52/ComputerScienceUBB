package model.state;

import model.value.IValue;
import java.util.Map;

public interface IHeap {
    int allocate(IValue value);
    IValue read(int address);
    void write(int address, IValue value);
    boolean containsKey(int address);
    Map<Integer, IValue> getHeap();


    Map<Integer, IValue> getContent();
    void setContent(Map<Integer, IValue> map);
}
