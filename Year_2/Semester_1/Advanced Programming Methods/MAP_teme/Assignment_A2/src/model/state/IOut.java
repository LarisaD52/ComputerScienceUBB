package model.state;
import java.util.List;

public interface IOut<T> {
    void add(T value);
    List<T> getAll();
}
