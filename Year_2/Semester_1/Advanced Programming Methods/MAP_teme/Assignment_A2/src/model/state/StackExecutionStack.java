package model.state;

import exceptions.StackEmptyException;

import java.util.Stack;

public class StackExecutionStack<T> implements IExecutionStack<T> {

    private final Stack<T> stack;

    public StackExecutionStack() {
        this.stack = new Stack<>();
    }

    @Override
    public void push(T element) {
        stack.push(element);
        // Optional: dacă ai capacitate fixă, aruncă StackFullException
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
}
