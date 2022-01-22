package snowbober.ECS;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static final int MAX_ENTITIES = 16;
    public static final int MAX_COMPONENTS = 8;

    List<System> systems = new ArrayList<>();
    Component[][] components = new Component[MAX_COMPONENTS][MAX_ENTITIES];


    // SYSTEM PART

//  Podejście Data Oriented Programming:
//    componentsA [];
//    componentsB [];
//    compoentsC [];
//
//    system1, system2;
//
//    if system 1:
//        for ( int i):
//            componentsA[i];
//            componentsB[i];
//   if system 1:
//        for ( int i):
//            componentsB[i];
//            componentsC[i];
//
//


    public void addSystem(System system) {
        systems.add(system);
    }

    public void removeAllSystems() {
        systems.clear();
    }

    public void update(long gameFrame) throws InterruptedException {
        for (System s : systems) {
            s.update(gameFrame, this);
        }
    }

    // COMPONENT PART

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
        for (int cmp = 0; cmp < entities.size(); cmp++) {
            if (entities.get(cmp)[entityId] == null) {
                return false;
            }
        }
        return true;
    }
}
