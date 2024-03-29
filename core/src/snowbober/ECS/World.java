package snowbober.ECS;

import java.util.ArrayList;
import java.util.List;

public class World {
    public final int MAX_ENTITIES = 20;
    public final int MAX_COMPONENTS = 12;

    List<System> systems = new ArrayList<>();
    List<System> renderSystems = new ArrayList<>();
    Component[][] components = new Component[MAX_COMPONENTS][MAX_ENTITIES];

    public void resetWorld() {
        removeAllSystems();
        killAllEntities();
    }

    public void addSystem(System system) {
        systems.add(system);
    }

    public void addRenderSystem(System system) {
        renderSystems.add(system);
    }

    public void removeAllSystems() {
        systems.clear();
        renderSystems.clear();
    }

    public void updateSystems(long gameFrame, float delta) throws InterruptedException {
        for (System s : systems) {
            s.update(gameFrame, delta, this);
        }
    }

    public void updateRenderSystems(long gameFrame, float delta) throws InterruptedException {
        for (System s : renderSystems) {
            s.update(gameFrame, delta, this);
        }
    }

    public void killAllEntities() {
        for (int j = 0; j < MAX_ENTITIES; j++) {
            for (int i = 0; i < MAX_COMPONENTS; i++) {
                components[i][j] = null;
            }
        }
    }

    public void killEntity(int entityId) {
        for (int i = 0; i < components.length; i++) {
            components[i][entityId] = null;
        }
    }

    public void addComponentToEntity(int entityId, Component cmp) {
        components[cmp.getId()][entityId] = cmp;
    }

    public void removeComponentFromEntity(int entityId, Component cmp) {
        components[cmp.getId()][entityId] = null;
    }

    public void removeComponentFromEntity(int entityId, int cmpId) {
        components[cmpId][entityId] = null;
    }

    public ArrayList<Component[]> getEntitiesWithComponents(int[] cmpId) {
        ArrayList<Component[]> result = new ArrayList<>();
        for (int id : cmpId) {
            result.add(components[id]);
        }
        return result;
    }

    public ArrayList<Component> getComponentOfEntity(int entityId) {
        ArrayList<Component> result = new ArrayList<>();
        for (int i = 0; i < MAX_COMPONENTS; i++) {
            if (components[i][entityId] != null) {
                result.add(components[i][entityId]);
            }
        }
        return result;
    }

    public Component getComponent(int entityId, int cmpId) {
        return components[cmpId][entityId];
    }

    public static boolean isEntityOk(int entityId, ArrayList<Component[]> entities) {
        for (Component[] components : entities) {
            if (components[entityId] == null) {
                return false;
            }
        }
        return true;
    }
}
