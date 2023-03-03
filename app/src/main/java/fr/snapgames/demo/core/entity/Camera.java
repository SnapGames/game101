package fr.snapgames.demo.core.entity;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Camera object to make the rendering viewport follow a defined {@link GameObject} `target`
 * according to a `tween` factor delay to smooth camera moves according to target moves.
 *
 * @author Frédéric Delorme
 * @since 2022
 **/
public class Camera extends GameObject {
    private GameObject target;
    private double tween;
    private Rectangle2D viewport;

    public Camera(String name) {
        super(name);
    }

    public Camera setViewport(Rectangle2D r) {
        this.viewport = r;
        return this;
    }

    public Camera setTween(double t) {
        this.tween = t;
        return this;
    }

    public Camera setTarget(GameObject go) {
        this.target = go;
        return this;
    }

    public void update(double elapsed) {
        this.position.x += Math
                .ceil((target.position.x + (target.size.x * 0.5) - ((viewport.getWidth()) * 0.5) - this.position.x)
                        * tween * Math.min(elapsed, 10));
        this.position.y += Math
                .ceil((target.position.y + (target.size.y * 0.5) - ((viewport.getHeight()) * 0.5) - this.position.y)
                        * tween * Math.min(elapsed, 10));


    }

    @Override
    public List<String> getDebugInfo() {
        List<String> info = new ArrayList<>();
        info.add(String.format("(1)id:%04d", this.getId()));
        info.add(String.format("(1)name:%s", this.getName()));
        info.add(String.format("(1)pos:%s", position));
        info.add(String.format("(2)tgt:%s", this.target.getName()));
        info.add(String.format("(2)twn:%f", this.tween));
        return info;
    }
}


