package fr.snapgames.demo.core.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The EntityManager is the class under test.
 *
 * @author : Frédéric Delorme
 * @since 0.0.9
 **/
public class EntityManagerTest {

    @Test
    public void testEntityManagerIsEmptyAtStart() {
        EntityManager em = new EntityManager();
        Assertions.assertEquals(0, em.getEntities().size());
    }

    @Test
    public void testEntityManagerAddAnEntity() {
        EntityManager em = new EntityManager();
        Entity<String> test01 = new Entity<>("test01");
        em.add(test01);
        Assertions.assertEquals(1, em.getEntities().size());
        Assertions.assertTrue(em.getEntities().contains(test01));
        Assertions.assertTrue(em.getEntityMap().containsKey("test01"));
    }

    @Test
    public void testEntityManagerHostsManyEntity() {
        EntityManager em = new EntityManager();
        for (int i = 0; i < 20; i++) {
            Entity<String> test = new Entity<>("test_" + i);
            em.add(test);
        }
        Assertions.assertEquals(20, em.getEntities().size());
        Assertions.assertTrue(em.getEntityMap().containsKey("test_0"));
        Assertions.assertTrue(em.getEntityMap().containsKey("test_2"));
        Assertions.assertTrue(em.getEntityMap().containsKey("test_3"));
        Assertions.assertTrue(em.getEntityMap().containsKey("test_10"));
        Assertions.assertTrue(em.getEntityMap().containsKey("test_19"));
    }
}
