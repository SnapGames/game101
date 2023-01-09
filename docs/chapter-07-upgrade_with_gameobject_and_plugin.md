# Upgrade with GameObject and Plugin

## Goals

As we are going to enforce the entity model, we need to specialize the rendering pipeline to not let it become a Monster Gaz plant.
We also want to propose some specialized Entity to implement new graphical elements but also new behaviors and usages.

## Proposed evolution

To satisfy those goals, I need to move out from `Entity` to let it support the internals of our Renderer and entity's identification or basic attributes like position.
But every thing else will move to a new class named `GameObject`.

![The Renderer plugin class diagram implementation proposal](http://www.plantuml.com/plantuml/png/VOv1IWGn44NtTOfof-XSe95n5ehk56-GwNpjaYGTAjME8jxTHjFJI61MuQS-_xrTCQSsnK1dDY22St2IQ10FZh1XzEKlE3EL_MGer0TN2ZyZUIYqnhjaOfyrtBk8n_qRUcDys5mytgaCuu5lrHqU43BqAKolaeueIGPzSJsQoXVn9Vlvyrox1Ns5EdjzixtetaVnz5q9gq73Mc2DQzEixeqBdPPP5rUhfSMRpRRPw_BlK_wrwbgN9ID80jDklaun_000 "The Renderer plugin class diagram implementation proposal")

### The DrawHelperPlugin

Heart of our plugin implementation, this class define the draw helper API.

```java
interface DrawHelperPlugin<T extends Entity>{
    void draw(Renderer r, Graphics2D g, T object);
}
```