package snowbober.ECS;

public class ComponentWithId implements Component {
    final int id;
    public ComponentWithId(int id) {
        this.id = id;
    }
    @Override
    public int getId() {
        return id;
    }
}
