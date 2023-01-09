package fr.snapgames.demo.core.entity;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class GridObject extends Entity<GridObject> {

    private double widthStep;

    private double heightStep;

    public GridObject(String name, double gridWidthStep, double gridHeightStep) {
        super(name);
        this.widthStep = gridWidthStep;
        this.heightStep = gridHeightStep;
    }

    /**
     * Prepare debug info to be displayed on debug mode.
     *
     * @return a list of string for debug info.
     */
    @Override
    public List<String> getDebugInfo() {
        List<String> infos = new ArrayList<>();
        infos.add(String.format("id:%04d", this.id));
        infos.add(String.format("name:%s", this.name));
        infos.add(String.format("pos:%4.2f,%4.2f", x, y));
        infos.add(String.format("size:%4.2f,%4.2f", width, height));

        return infos;
    }

    @Override
    public void updateBox() {
        this.box = new Rectangle2D.Double(x, y, width, height);
    }

    /**
     * @return the widthStep
     */
    public double getWidthStep() {
        return widthStep;
    }

    /**
     * @return the heightStep
     */
    public double getHeightStep() {
        return heightStep;
    }

}
