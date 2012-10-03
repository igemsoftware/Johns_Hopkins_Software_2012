/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autogene.core.bio.controllers;

import org.autogene.core.bio.managers.PlasmidManager;
import java.util.List;
import org.autogene.core.bio.entities.Plasmid;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author giovanni
 */
public class PlasmidManagerTest {
    
    public PlasmidManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testPersist(){
        
    }
    
    /**
     * Test of findAll method, of class PlasmidManager.
     */
    @Test
    public void testFindAll() {
        System.out.println("findAll");
        PlasmidManager instance = new PlasmidManager();
        List result = instance.findAll();
        assertNotNull(result);
    }

    /**
     * Test of findByName method, of class PlasmidManager.
     */
    @Test
    public void testFindByName() {
        System.out.println("findByName");
        String name = "pGEM-T";
        PlasmidManager instance = new PlasmidManager();
        Plasmid result = instance.findByName(name);
        assertNotNull(result);
    }
    
    @Test
    public void testRemove(){
        PlasmidManager pc = new PlasmidManager();
        Plasmid p = pc.findByName("pGEM-T");
        pc.remove(p);
    }
}
