package fr.snapgames.demo.core.gfx.plugins;

import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.entity.GridObject;
import fr.snapgames.demo.core.gfx.Renderer;

import java.awt.*;

/**
 * Add the GridObject draw helper.
 *
 * @author : Frédéric Delorme
 * @since 0.0.9
 **/
public class GridObjectDrawHelperPlugin implements DrawHelperPlugin<GridObject> {
    @Override
    public Class<GridObject> getEntityType() {
        return GridObject.class;
    }

    @Override
    public void draw(Renderer r, Graphics2D g, Entity e) {
        GridObject go = (GridObject) e;
        g.setColor(go.borderColor);
        Stroke backPreviousStrokeValue = null;
        if (go.getStroke() != null) {
            backPreviousStrokeValue = g.getStroke();
            g.setStroke(go.getStroke());
        }
        for (double x = 0; x < go.width; x += go.getStepX()) {
            g.drawRect((int) x, 0, (int) go.getStepX(), (int) go.height);
        }
        for (double y = 0; y < go.height; y += go.getStepY()) {
            g.drawRect(0, (int) y, (int) go.width, (int) go.getStepY());
        }
        if (go.getStroke() != null && backPreviousStrokeValue != null) {
            g.setStroke(backPreviousStrokeValue);
        }
    }
}
