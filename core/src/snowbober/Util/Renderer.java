package snowbober.Util;

import snowbober.Components.CmpId;
import snowbober.Components.Position;
import snowbober.Components.Visual;
import snowbober.ECS.Component;
import snowbober.ECS.World;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Renderer extends JPanel {
    public World world;

    public Renderer(World world) {
        this.world = world;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ArrayList<Component[]> entities = world.getEntitiesWithComponents(new int[]{
                CmpId.POSITION.ordinal(),
                CmpId.VISUAL.ordinal()});

        for (int entity = 0; entity < world.MAX_ENTITIES; entity++) {
            Position pos = (Position) entities.get(0)[entity];
            Visual vis = (Visual) entities.get(1)[entity];

            if (pos == null || vis == null) continue;

//            g.drawImage(
//                    vis.texture.img,
//                    pos.x,
//                    pos.y,
//                    vis.texture.imgWidth,
//                    vis.texture.imgHeight,
//                    this
//            );
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + Game.score, 10, 750);
        }
    }
}
