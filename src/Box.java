import java.util.ArrayList;

public class  Box<E extends Fruit> {
    private ArrayList<E> content;

    public Box() {
        this.content = new ArrayList<>();
    }

    public float getWeight() {
        float weight = 0f;
        for (E fruit: content) {
            weight += fruit.getWeight();
        }
        return weight;
    }

    public boolean add(E fruit) {
        return content.add(fruit);
    }

    public boolean compare(Box<?> box) {
        return Math.abs(getWeight() - box.getWeight()) < Lesson1.WEIGHT_DELTA;
    }

    public void reloadTo(Box<E> box) {
        while (!this.content.isEmpty()) {
            box.add(this.content.remove(0));
        }
    }
}
