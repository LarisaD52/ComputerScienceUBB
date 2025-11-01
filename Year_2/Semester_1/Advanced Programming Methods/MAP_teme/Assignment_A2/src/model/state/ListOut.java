package model.state;

import java.util.ArrayList;
import java.util.List;
public class ListOut<T> implements IOut<T>{

    private final List<T> outputList;

    public ListOut(){
        outputList = new ArrayList<>();
    }

    @Override
    public void add(T element){
        outputList.add(element);
    }

    @Override
    public List<T> getAll(){
        return outputList;
    }
    @Override
    public String toString(){
        return outputList.toString();
    }

}
