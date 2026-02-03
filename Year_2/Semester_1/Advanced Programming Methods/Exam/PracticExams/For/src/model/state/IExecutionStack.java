package model.state;

import java.util.List;

public interface IExecutionStack<T>{
    void push(T element);
    T pop();
    boolean isEmpty();
    T peek();
    List<T> getReversed();

}
