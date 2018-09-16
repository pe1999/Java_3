import java.util.ArrayList;

public class  Box<E extends Fruit> {
    private float weight;
    private ArrayList<E> box;

    public Box() {
        this.weight = 0;
        this.box = new ArrayList<>();
    }

    public float getWeight() {
        return weight;
    }

    public boolean add(E fruit) {
        return box.add(fruit);
    }

    public boolean compare(Box<?> o) {
        return true;
    }
}
