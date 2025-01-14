# Moving to Vector2D

We already had developed a fantastic `PhysicEngine` class, supporting some Newton's laws, and we rely on the `Entity`
attributes `(x,y)`, `(dx,dy)` and `(ax, ay)` for respectively position, velocity and acceleration, but also Material
attributes and the mass of this entity

We also used the `World` object as `PhysicEngine` world's limited context with the play area, the `gravity` and a clear
identified `Material` to define physic attributes of the play area where the `Entity` will move on.

As a kind reminder, please have a look at our class diagram,
It is now time to use some Math to compute things with a Vector2D class:

![The Application Game Engine class Diagram](https://www.plantuml.com/plantuml/png/TL71QiCm3BtxApnwpcM7GiqeXQxiiiAWi9rLHqq4ZbqiPQMC_VjSfw4EQoycUk_fwKLQiK2KldFgILhiK1yTa5SjrQWxb3RKF1GW4cdV2x8YBnWFO56_GUgOhTVh0jjPz5VGEc3M7-sPoMvyGnTyiLWC-23SWeS6Oubll92Sxn1V8RfQp-Rp-KIPaH-bTQMFWKaeUFE9La9yViteDxfWat2AWhNeCC9__I54YspUYULb99QPwO3PrA7VErI_Yjjm6ZCKwncoRK4m4hWnNQKzT6XsIUQRIrcZvzGvsYK4KwEGdOA7I3P7d8uTxSl1-HfLjPnirh3rsoRUoCbw3GvLAIZlPOv5eKecI_D5pEdogeblCMaOGSYgZ7NBQJYzYSrU6Hx_pNJNZw7paUV3BFgkrmfztNVk3m00 "The Application Game Engine class Diagram")

## The Vector2D

A `Vector2D` is math entity used to define a force on a 2D world. It basically contains 2 attributes: `x` and `y`.

![The Vector2D class diagram](http://www.plantuml.com/plantuml/png/TS-nJWCn30RWFKznR8VO63jrk3T0OlV5Lch9TahYL6qHxmw5eZQ8RB_ovK_iRNKetbJ2W-z8QTeBi8KeKElqIj5pULxUm_Gq7JUOsofqoQUx38ZpONEzYe-_QVasIq93ZSHomwL7v0CZpNZzwIzM1uiqRcVlx3OklJMGsk6Qin3OHOKi-Mw-BoWSMVbCi_uxYZPntijfm4O9dBe7BHS5uND_MjjQlK3qqKwUNxomfQ_MJmWVOKk6JUKJ "The Vector2D class diagram")

It allows all vector operation on a two-dimensional area: addition, subtraction, multiplication, get normal, cross
product and negate a vector.

```java
public class Vector2D {
    double x, y;

    public Vector2D() {
    }

    public Vector2D(double x, double y) {
    }
}
```

### Vector2D API

- create a vector
  `Vector2D v1 = new Vector2D(0.0,0.0);`
- normalize the resulting vector
  `double norm = v2.normalize();`
- compute length of the vector
  `double l = v2.length();`
- define distance between 2 vector
  `double distance = v1.distance(v2);`
- get the opposite vector
  `Vector2D v3 = v2.negate();`
- compute dot product between v1 and v2.
  `double d = v1.dot(v2);`

Some utilities are available to round result of the computation or add max and min threshold to the result:

- apply some ceiling and max operations:

```java
Vector2D v2 = v1.multiply(12.0)
        .ceil(0.001)
        .maximize(2000.0);
```

- apply ceil and max on each axis:

```java
Vector2D v2 = v1.multiply(12.0)
        .ceil(0.001, 0.1)
        .maximize(100.0, 50.0);
```

## Impact on the Entity class

As we've just discovered the `Vector2D` class, we can now use it to define previous position (x,y) and speed (dx,dy):

```java
public class Entity<T> {
    //...
    private Vector2D position;
    private Vector2D velocity;

    //...
    public Entity() {
        position = new Vector2D();
        velocity = new Vector2D();
    }

    public Entity(String name) {
        this();
        this.name = name;
    }

    //  Do not forget getters and setters !
}
```

As previously seen, we want to apply the Newton's laws on our Entity to let the physic magic operates, so we need a bit
more info:

```java
public class Entity<T> {
    //...
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;
    // a bunch of fluent API getters and setters
    //...
}
```

To proceed with physic computation, we need more information like material, mass, and a list of forces applied to our
Entity:

```java
public class Entity<T> {
    //...
    private List<Vector2D> forces;
    //...
    private Material material;
    private double mass = 1.0;
    //...
}
```

We can, now apply more Newton's low and in a more elegant way. Let's go and modify the PhysicEngine to use the brand new
offered capacities :

```java
public class PhysicEngine {
    //...
    private void updateEntity(Game game, Entity<?> entity, double elapsed) {

        // define default entity friction value
        double friction = entity.contact == 0 ? world.getMaterial().friction : entity.material.friction * world.getMaterial().friction;
        // define default entity density value
        double density = entity.getMaterial() != null ? entity.getMaterial().density : world.getMaterial().density;

        // compute acceleration and apply gravity (negate because of AWT/Swing display coordinates origin)
        entity.setAcceleration(entity.getAcceleration()
                .addAll(entity.forces)
                .add(world.getGravity().negate())
                .multiply(entity.getMass())
                .multiply(density)
                .ceil(minAcceleration)
                .maximize(world.maxAccX, world.maxAccY)
        );

        // compute velocity
        entity.setVelocity(entity.getVelocity()
                .add(entity.getAcceleration().multiply(elapsed))
                .multiply(friction)
                .ceil(minSpeed)
                .maximize(maxVelocity, maxSpeedY)
        );

        // compute position
        entity.setPosition(entity.getPosition().add(entity.velocity.multiply(elapsed)));

        // Update the bounding box accordingly to last position changes
        entity.updateBox();

        // clear forces for that entity.
        entity.forces.clear();
    }
    //...
}
```

you can see the elegance of the ceil and maximize utility method to lock resulting values.

## Conclusion

Since the 12's chapter, we use the [game101](https://github.com/SnapGames/game101) project on GitHub to support our
posts, so keeping this habit,
and you will find the [add-vector2d](https://github.com/SnapGames/game101/releases/tag/add-vector2d) tag
linked to this new chapter.

That’s all folks!

McG.