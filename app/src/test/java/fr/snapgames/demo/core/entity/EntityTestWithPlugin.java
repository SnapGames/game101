package fr.snapgames.demo.core.entity;

/**
 * A {@link EntityTestWithPlugin} extension to {@link GameObject} to test {@link fr.snapgames.demo.core.gfx.Renderer} Plugin architecture and usage.
 *
 * @author Frédéric Delorme
 * @since 0.0.9
 **/

public class EntityTestWithPlugin extends GameObject {

    private boolean drawnFlag;

    public EntityTestWithPlugin(String name) {
        super(name);
        drawnFlag = false;
    }

    public void setDrawnFlag(boolean drawnFlag) {
        this.drawnFlag = drawnFlag;
    }

    public boolean getDrawnFlag() {
        return drawnFlag;
    }
}
