package fr.snapgames.demo.core.gfx.plugin;

import fr.snapgames.demo.core.entity.Entity;
import fr.snapgames.demo.core.entity.EntityTestWithPlugin;
import fr.snapgames.demo.core.gfx.Renderer;
import fr.snapgames.demo.core.gfx.plugins.DrawHelperPlugin;

import java.awt.*;

/**
 * {@link DrawHelperPlugin} test implementation
 *
 * @author Frédéric Delorme
 * @since 0.0.9
 **/
public class EntityTestWithPluginDrawHelperPlugin implements DrawHelperPlugin<EntityTestWithPlugin> {


    @Override
    public Class<EntityTestWithPlugin> getEntityType() {
        return EntityTestWithPlugin.class;
    }

    @Override
    public void draw(Renderer r, Graphics2D g, Entity<?> e) {
        EntityTestWithPlugin etwp = (EntityTestWithPlugin) e;
        etwp.setDrawnFlag(true);
    }
}
