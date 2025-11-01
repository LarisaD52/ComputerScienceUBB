package model.state;

public interface IExecutionStack<T>{
    void push(T element);
    T pop();
    boolean isEmpty();
    T peek();
}
