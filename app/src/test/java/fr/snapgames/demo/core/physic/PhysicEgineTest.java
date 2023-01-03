package fr.snapgames.demo.core.physic;

import fr.snapgames.demo.core.entity.EntityManager;
import fr.snapgames.demo.core.entity.Entity;

public class PhysicEgineTest {
    
    Game game;
    PhysicEngine pe;

    @BeforeEach
    public void setup(){
        game = new Game("./config-physicengine.properties"); 
        pe = new PhysicEngine(game);
    }

    @Test
    public void testPhysicEngineIsInitialized(){
        Assertions.assertEquals(new Point2D(0.0,0.000981), pe.getWorld().getGravity(), "World's gravity");
    }
}
