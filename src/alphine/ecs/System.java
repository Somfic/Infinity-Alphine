/*******************************************************************************
 * Copyright (c) 2016 Roman Divotkey, Univ. of Applied Sciences Upper Austria. 
 * All rights reserved.
 *  
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE', which is part of this source code package.
 *  
 * THIS CODE IS PROVIDED AS EDUCATIONAL MATERIAL AND NOT INTENDED TO ADDRESS
 * ALL REAL WORLD PROBLEMS AND ISSUES IN DETAIL.
 *******************************************************************************/
package alphine.ecs;

import javafx.scene.canvas.GraphicsContext;

import java.util.List;

/**
 * A engine system maintains a certain aspect of the engine.
 * <p>
 * The most common case is to process a certain family of entities (e.g.
 * position update). However, due to the fact that engine alphine.systems are accessible
 * by all other alphine.systems and get updated each cycle of the game loop, engine
 * system can be used to realize other tasks that do not directly process
 * entities (e.g. camera management or centralized input processing).
 * </p>
 */
public abstract class System {

	/** Whether this system is enabled or disabled. */
	private boolean enabled = true;
	
    /** A reference to the engine this system belongs to. */
    private World world;
    
    /**
     * Sets the reference to the engine this system belongs to.
     * 
     * @param e
     *            the engine
     */
    final void setEngine(World e) {
        world = e;
    }
    
	/**
	 * Enables or disables this system. Disabled alphine.systems will not get updated by
	 * the engine.
	 * 
	 * @param b
	 *            {@code true} is this system should be enabled, {@code false}
	 *            otherwise
	 */
    public final void setEnabled(boolean b) {
    	if (enabled == b) return;
    	enabled = b;
    	enabledStateChanged();
    }

	/**
	 * Returns the enables state of this system.
	 * 
	 * @return {@code true} if this system is enabled
	 */
    public final boolean isEnabled() {
    	return enabled;
    }
    
    /**
    /* Returns the engine this system belongs to.
     * 
     * @return the engine of this system
     */
    public final World getWorld() {
        return world;
    }

    public final List<Entity> getEntities(EntityFilter filter) {
    	return world.getEntities(filter);
    }

    public final void addEntity(Entity entity) {
        world.addEntity(entity);
    }


    /**
     * Invoked when this system is added to an engine. This method can be
     * overwritten by derived classes to provide initialization code.
     * 
     * <p>
     * The engine provided as parameter is identical to the engine that will be
     * returned by the {@link #getWorld} method.
     * </p>
     *            the engine this system has been added to
     */
    public void onStart() {}
    
    /**
     * Invoked when this system is removed from an engine. This method can be
     * overwritten by derived classes to provide clean up code. This method 
     * will also be called when the engine gets disposed.
     * 
     * <p>
     * The engine provided as parameter is identical to the engine that will be
     * returned by the {@link #getWorld} method.
     * </p>
     * 
     * @param e
     *            the engine this system has been added to
     */
    public void onEnd() {}
    
    /**
     * Called by the engine when the engines update method is invoked. In
     * general this method will be called once each game loop cycle.
     * 
     * @param dt
     *            the delta time in seconds
     */
    public void onUpdate(double dt) { }

    /**
     * Called by the engine when the engines fixed update method is invoked. In
     * general this method will be called once each game loop cycle.
     * @param dt
     *            the delta time in seconds
     */
    public void onFixedUpdate(double dt) { }

    /**
     * Called by the engine when the engines render method is invoked.
     * This method will be called once each frame.
     */
    public void onRender(GraphicsContext graphics) { }
    
    /** 
     * Called when the enabled state of this system has changed.
     */
    protected void enabledStateChanged() {}

    
}
