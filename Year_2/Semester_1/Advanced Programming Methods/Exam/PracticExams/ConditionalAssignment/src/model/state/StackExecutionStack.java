package model.state;

import exceptions.StackEmptyException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class StackExecutionStack<T> implements IExecutionStack<T> {

    private final Stack<T> stack;

    public StackExecutionStack() {
        this.stack = new Stack<>();
    }

    @Override
    public void push(T element) {
        stack.push(element);

    }

    @Override
    public T pop() throws StackEmptyException {
        if (stack.isEmpty()) {
            throw new StackEmptyException("Execution stack is empty!");
        }
        return stack.pop();
    }

    @Override
    public T peek() throws StackEmptyException {
        if (stack.isEmpty()) {
            throw new StackEmptyException("Execution stack is empty!");
        }
        return stack.peek();
    }


    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    public String toString() {
        return stack.toString();
    }
    public List<T> getReversed() {
        List<T> list = new ArrayList<>(stack); // presupunând că variabila ta se numește 'stack'
        Collections.reverse(list);
        return list;
    }
}
