# Add a resource manager

## Goal

As we are willing to create some fancy graphics, and maybe use some images as background and some new fonts to write
some text, we are going to have manage some resources.
And those resources maybe reused across multiple Scene. So to manage thinly the memory used by our game, we may propose
a tool to manage and store temporarily in the java memory some of those resources.

This is where a resource manager would help a lot.

It must provide a convenient way to request some resource, load and store it in a cache, and serve this resource when
required.

## The proposed Design

![The class ResourceManager and its API](http://www.plantuml.com/plantuml/png/NOwzJiKm38LtFuMv4mWwi1PqLJ6062eaU08tEKsAybCbpeHuTsXXe4oMl-_OEaDLATfYe0lrYE0ro9B81AcLNr5pAmQZ974e7yGT6p4U_IZh_PkM9RcRb-aTHi-R2rdivG_cLTHVtH5ViuC-Ht4ucFwXpJCAxAv-OuyvwJ6r4VeaUf88FjsUkElEs_nVhI_7dVnYmVkLwFV3gIdg7nYXFjjA0v9jCNm1)

The class `ResourceManager` provides some useful helpers to retrieve image or font:

- `getImage(String)` will load an image and return the corresponding `BufferedImage`, if it exists,
- `getFont(String)` will load a font for text rendering, using the Java API, and return a `Font` instance.

## Implementation

the main ResourceManager class would be:

```java
public class ResoucreManager {
    private static Map<String, Object> cache = new ConcurrentHashMap<>();

    public static BufferedImage getImage(String path) {
        if (!cache.contains(path) {
            load(path);
        }
        return (BufferedImage) cache.get(path);
    }

    public static Font getFont(String path) {
        if (!cache.contains(path) {
            load(path);
        }
        return (Font) cache.get(path);
    }

    private static void load(String path) {
        //...
    }
}
``` 

Here are clearly defined the *getImage* and *getFont*, but all the Intelligence remains in the *load* method. Based on
the file extension, we will define what can be done to load the corresponding resource with the right object type.

```java
public class ResourceManager {
    //...
    private static void load(String path) {
        if (!path.contains(".")) {
            return null;
        }
        switch (path.substring(path.findLast(".") + 1, path.length - (path.findLast(".") + 1)).toUppercase()) {
            case "PNG", "JPG", "GIF" -> {
                BufferedImage img = null;
                if (!cache.containsKey(path)) {
                    try {
                        img = ImageIO.read(ResourceManager.class.getResourceAsStream(path));
                        cache.put(path, img);
                    } catch (IOException e) {
                        System.err.printf("Game:Unable to read image %s: %s", path, e.getMessage());
                    }
                }
            }
            case "TTF" -> {
                // load a Font resource
                Font font = null;
                if (!cache.containsKey(path)) {
                    try {
                        InputStream stream = ResourceManager.class.getResourceAsStream(path);
                        font = Font.createFont(Font.TRUETYPE_FONT, stream);
                        if (font != null) {
                            cache.put(path, font);
                        }
                    } catch (FontFormatException | IOException e) {
                        System.err.printf("Unable to read font from %s%n", path);
                    }
                }
                if (Optional.ofNullable(font).isPresent()) {
                    cache.put(path, font);
                }
            }
            default -> {
                System.err.printf("File format unknown for %s%n", path);
            }
        }
    }
}
```
## Using the ResourceManager

In your own `Scene` implementation, you can now preload some resources in the `prepare()`
method:

```java
class MyScene extends AbstractScene{

    //...
    @Override
    public void prepare(Game g) {
        // load resources int cache
        ResourceManager.getImage("/images/backgrounds/forest.jpg");
        ResourceManager.getImage("/images/sprites01.png");
    }
    //...
}
```

And during the Scene creation using the `create()` method, you can get the preloaded resources :

```java
class MyScene extends AbstractScene {
    //...
    @Override
    public void create(Game g) {
        //...
        // Create the main player entity.
        var playerFrame1 = ResoureManager.getImage("/images/sprites01.png"); 
        var playerFrame1 = imagePlayer.getSubimage(0, 0, 32, 32);
        var player = (GameObject) new GameObject("player")
                .setImage(playerFrame1);
        //...
    }
    //...
}
```

So each time you re-activate this scene, resources are already in cache, so no wait to display the scene.

## Conclusion

We add hee avery useful service to load and cache some resources. Then, those resources can be used or reused by mulitple scene,
reducing the loading time.

Like in the 10 previous episodes, you can access the code from the GitHub repository you already know
now: https://github.com/SnapGames/game101 on
tag [create-resource-manager](https://github.com/SnapGames/game101/releases/tag/create-resource-manager).

That’s all falk!

McG.