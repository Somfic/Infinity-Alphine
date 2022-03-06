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
import alphine.logging.Logger;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The engine is actual management class of this framework. If accomplishes
 * several tasks:
 *
 * <ul>
 * <li>Management of (active) entities.</li>
 * <li>Service locator for all kind of alphine.systems (services) within the
 * application.</li>
 * <li>Central update mechanism of attached alphine.systems.</li>
 * </ul>
 *
 * <p>
 * In general there will be only one instance of an engine within an application
 * at a time. It is fine to create, initialize and destroy an engine for each
 * individual state of the application. The update method of the engine should
 * be called once each cycle of the main loop, passing the actual delta time as
 * parameter.
 * </p>
 *
 * <p><strong>Example</strong></p>
 * <pre>
 * public class PlayState extends GameState {
 *
 *     private Engine engine;
 *
 *     public void enterState() {
 *         engine = new Engine();
 *         engine.addSystem(new RenderSystem());
 *         engine.addSystem(new CollisionSystem());
 *         engine.addSystem(new PhysicsSystem());
 *         //...
 *     }
 *
 *     public void exitState() {
 *         engine.dispose();
 *         engine = null;
 *     }
 *
 *     public void update(double dt) {
 *         engine.update(dt);
 *     }
 * }
 * </pre>
 */
public final class World {

    /**
     * The list of entities added to this engine.
     */
    private List<Entity> entities = new ArrayList<>();

    /**
     * A map for all the different views of the entities.
     */
    private Map<EntityFilter, List<Entity>> views = new HashMap<>();

    /**
     * List of pending commands.
     */
    private List<Command> commands = new ArrayList<>();

    /**
     * List of added alphine.systems.
     */
    private List<System> systems = new ArrayList<>();

    /**
     * Registered entity listeners.
     */
    private List<EntityListener> listeners = new CopyOnWriteArrayList<EntityListener>();

    /**
     * Registered entity listeners listening only for certain entity families.
     */
    private Map<EntityFilter, List<EntityListener>> filteredListeners = new HashMap<>();

    /**
     * Indicates if an update cycle is currently in progress.
     */
    private boolean updating;


    /**
     * Adds the specified entity listener to this engine.
     *
     * @param l      the entity listener to be added
     * @param family the family of entities this listeners is interested in
     */
    public void addEntityListener(EntityListener l, EntityFilter family) {
        List<EntityListener> lst = filteredListeners.get(family);
        if (lst == null) {
            lst = new CopyOnWriteArrayList<>();
            filteredListeners.put(family, lst);
        }
        assert !lst.contains(l) : "listener already added " + l;
        lst.add(l);
    }

    /**
     * Removes the specified entity listeners from this engine.
     *
     * @param l      the entity listener to be removed
     * @param family the family of entities this listeners was interested in
     */
    public void removeEntityListener(EntityListener l, EntityFilter family) {
        List<EntityListener> lst = filteredListeners.get(family);
        if (lst != null) {
            lst.remove(l);
        }
    }

    /**
     * Adds the specified entity listener to this engine.
     *
     * @param l the entity listener to be added
     */
    public void addEntityListener(EntityListener l) {
        assert !listeners.contains(l) : "listener already added " + l;
        listeners.add(l);
    }

    /**
     * Removes the specified entity listeners from this engine.
     *
     * @param l the entity listener to be removed
     */
    public void removeEntityListener(EntityListener l) {
        listeners.remove(l);
    }

    /**
     * Adds the specified entity to this engine. If the entity is added during
     * an update cycle, the entity will be added when the update cycle is
     * complete.
     *
     * @param e the entity to be added
     */
    public void addEntity(Entity e) {
        if (updating) {
            commands.add(() -> {
                addEntityInternal(e);
            });
        } else {
            addEntityInternal(e);
        }
    }

    /**
     * Removes all entities. If this method is invoked during an update cycle,
     * the command queued and executed when the update cycle is complete.
     */
    public void removeAll() {
        if (updating) {
            commands.add(() -> {
                removeAllInternal();
            });
        } else {
            removeAllInternal();
        }
    }

    /**
     * Removes the specified entity to this engine. If the entity is removed
     * during an update cycle, the entity will be removed when the update cycle
     * is complete.
     *
     * @param e the entity to be removed
     */
    public void removeEntity(Entity e) {
        if (updating) {
            commands.add(() -> {
                removeEntityInternal(e);
            });
        } else {
            removeEntityInternal(e);
        }
    }

    /**
     * The method that actually adds an entity.
     *
     * @param e the entity to be added
     */
    private void addEntityInternal(Entity e) {
        if (e.getEngine() != null) {
            Logger.warn("Entity of type " + e.getClass().getName() + " is already added to the world");
            throw new IllegalArgumentException(
                    "entity already added to an engine");
        }
        assert e.getEngine() == null;
        assert !e.isActivated();
        assert !entities.contains(e);

        entities.add(e);
        e.setEngine(this);
        e.activate();

        addEntityToViews(e);

        // inform listeners
        for (EntityListener l : listeners) {
            l.entityAdded(e);
        }

        for (Entry<EntityFilter, List<EntityListener>> entry : filteredListeners.entrySet()) {
            if (entry.getKey().isMember(e)) {
                for (EntityListener l : entry.getValue()) {
                    l.entityAdded(e);
                }
            }
        }
    }

    /**
     * Adds the specified entity to the corresponding views.
     *
     * @param e the entity to be added
     */
    private void addEntityToViews(Entity e) {
        for (EntityFilter family : views.keySet()) {
            if (family.isMember(e)) {
                views.get(family).add(e);
            }
        }
    }

    /**
     * The method that actually removes the entity.
     *
     * @param e the entity to be removed
     */
    private void removeEntityInternal(Entity e) {
        if (e.getEngine() != this) {
            // silently ignore this event (best practice)
            return;
        }
        assert e.getEngine() == this;
        assert e.isActivated();
        assert entities.contains(e);

        // inform listeners as long as the entity is still active
        for (EntityListener l : listeners) {
            l.entityRemoved(e);
        }
        for (Entry<EntityFilter, List<EntityListener>> entry : filteredListeners.entrySet()) {
            if (entry.getKey().isMember(e)) {
                for (EntityListener l : entry.getValue()) {
                    l.entityRemoved(e);
                }
            }
        }

        // actually remove entity
        e.deactivate();
        e.setEngine(null);
        entities.remove(e);

        removeEntityFromViews(e);
    }

    /**
     * The method that actually removes all entities.
     */
    private void removeAllInternal() {
        while (!entities.isEmpty()) {
            removeEntityInternal(entities.get(0));
        }
    }

    /**
     * Removes the specified entity from the corresponding views.
     *
     * @param e the entity to be removed
     */
    private void removeEntityFromViews(Entity e) {
        for (List<Entity> view : views.values()) {
            view.remove(e);
        }
    }

    public void start() {
        assert !updating;
        updating = true;

        // update alphine.systems
        for (System s : systems) {
            if (s.isEnabled()) {
                s.onStart();
            }
        }

        // execute pending commands
        for (Command cmd : commands) {
            cmd.execute();
        }
        commands.clear();

        updating = false;
    }

    /**
     * Updates this engine and its attached alphine.systems. This method should be
     * called once each main cycle.
     *
     * @param dt the elapsed time in seconds
     */
    public void update(double dt) {
        assert !updating;
        updating = true;

        // update alphine.systems
        for (System s : systems) {
            if (s.isEnabled()) {
                s.onUpdate(dt);
            }
        }

        // execute pending commands
        for (Command cmd : commands) {
            cmd.execute();
        }
        commands.clear();

        updating = false;
    }

    public void render(GraphicsContext graphics) {
        assert !updating;
        updating = true;

        // render alphine.systems
        for (System s : systems) {
            if (s.isEnabled()) {
                s.onRender(graphics);
            }
        }

        // execute pending commands
        for (Command cmd : commands) {
            cmd.execute();
        }
        commands.clear();

        updating = false;
    }

    private double accumulator = 0.0;
    public void fixedUpdate(double dt, double targetDt) {
        assert !updating;
        updating = true;

        accumulator += dt;
        while(accumulator >= targetDt) {
            for (System s : systems) {
                if (s.isEnabled()) {
                    s.onFixedUpdate(targetDt);
                }
            }
            accumulator -= targetDt;
        }

        // execute pending commands
        for (Command cmd : commands) {
            cmd.execute();
        }
        commands.clear();

        updating = false;
    }

    /**
     * Returns a list that will contain only entities which are members of the
     * specified family. It is safe to keep a reference of the returned list.
     * The list will be updated each update cycle and show reflect the current
     * state.
     *
     * <p>
     * The returned list cannot be modified. Any attempt to do so will result in
     * an {@code UnsupportedOperationException}.
     * </p>
     *
     * @param family the entity family this requested list should show
     * @return the list of entities
     */
    public List<Entity> getEntities(EntityFilter family) {
        List<Entity> view = views.get(family);
        if (view == null) {
            view = new ArrayList<>();
            views.put(family, view);
            initView(family, view);
        }
        return Collections.unmodifiableList(view);
    }

    /**
     * Initializes the specified view.
     *
     * @param family the entity family the new view should contain
     * @param view   the view that should be initialized
     */
    private void initView(EntityFilter family, List<Entity> view) {
        assert view.isEmpty();
        for (Entity e : entities) {
            if (family.isMember(e)) {
                view.add(e);
            }
        }
    }

    /**
     * Adds the specified system to this engine. Systems must not be added
     * during an update cycle. This method will call the {@code addedToEngine}
     * method of the specified system.
     *
     * @param s the system to be added
     * @throws IllegalStateException    if an update cycle is currently in progress
     * @throws IllegalArgumentException if the specified system has already been added
     */
    public void addSystem(System s) throws IllegalStateException,
            IllegalArgumentException {
        if (updating) {
            Logger.warn("Cannot add system during update cycle");
            throw new IllegalStateException("cannot add system while updating");
        }

        if (systems.contains(s)) {
            Logger.warn("System of type " + s.getClass().getName() + " is already added to the world");
            throw new IllegalArgumentException("system already added");
        }

        s.setEngine(this);
        systems.add(s);
    }

    /***
     * Removes the specified system from this engine. Systems must not be
     * removed during an update cycle. This method will call the
     * {@code removedFromEngine} method of the specified system.
     *
     * @param s
     *            the system to be added
     * @throws IllegalStateException
     *             if an update cycle is currently in progress
     * @throws IllegalArgumentException
     *             if the specified system in unknown
     */
    public void removeSystem(System s) throws IllegalStateException,
            IllegalArgumentException {
        if (updating) {
            Logger.warn("Cannot remove system during update cycle");
            throw new IllegalStateException(
                    "cannot remove system while updating");
        }

        if (!systems.contains(s)) {
            Logger.warn("System of type " + s.getClass().getName() + " is not added to the world");
            throw new IllegalArgumentException("system is unknown");
        }

        s.onEnd();

        systems.remove(s);
        s.setEngine(null);
    }

    /**
     * Returns {@code true} if there exists at least one system that matches the
     * specified interface of class.
     *
     * @param clazz the interface or class the requested system must implement
     * @return {@code true} if this engine as matching system, {@code false}
     * otherwise
     */
    public boolean hasSystem(Class<?> clazz) {
        for (System s : systems) {
            if (clazz.isInstance(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the first system that matches the specified interface of class.
     *
     * @param <T>   type parameter used to avoid casts, irrelevant when calling
     *              this method
     * @param clazz the interface or class the requested system must implement
     * @return the system that matches the requirements
     * @throws IllegalArgumentException in case no suitable system could be found
     */
    public <T> T getSystem(Class<T> clazz) throws IllegalArgumentException {
        for (System s : systems) {
            if (clazz.isInstance(s)) {
                return clazz.cast(s);
            }
        }

        Logger.warn("No system of type " + clazz.getName() + " could be found");
        throw new IllegalArgumentException("system not found "
                + clazz.getName());
    }

    /**
     * Disposes this engine. All acquired resources will be released, all
     * entities will be deactivated and destroyed, all attached alphine.systems will be
     * detached and removed.
     *
     * <p>
     * Note: The attached alphine.systems will be removed in reverse order to resolve
     * dependencies without conflicts.
     * </p>
     *
     * @throws IllegalStateException in case this method is called during an update cycle
     */
    public void dispose() throws IllegalStateException {
        if (updating) {
            Logger.warn("Cannot dispose during an update cycle");
            throw new IllegalStateException("dispose not allowed during update");
        }

        // dispose entities
        for (Entity e : entities) {
            if (e.isActivated()) {
                e.deactivate();
                e.setEngine(null);
            }
        }
        entities.clear();
        views.clear();

        // dispose alphine.systems
        for (int i = systems.size() - 1; i >= 0; --i) {
            System s = systems.get(i);
            s.setEnabled(false);
            s.onEnd();
            s.setEngine(null);
        }
        systems.clear();
    }

    /**
     * Returns the number of system registered at this engine.
     *
     * @return the number of alphine.systems
     */
    public int getNumOfSystems() {
        return systems.size();
    }

    /**
     * Returns the system with the specified index.
     *
     * @param idx the index of the system to be returned
     * @return the requested system
     * @throws IndexOutOfBoundsException in case the specified index is {@code<} 0 or {@code>=} number
     *                                   of registered alphine.systems
     */
    public System getSystem(int idx) throws IndexOutOfBoundsException {
        return systems.get(idx);
    }

    /**
     * Returns the number of active entities within this engine.
     *
     * @return the number of entities
     */
    public int getNumOfEntities() {
        return entities.size();
    }

    private boolean debug;

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean isDebug() {
        return this.debug;
    }


    /////////////////////////////////////////////////
    /////// Inner classes and interfaces
    /////////////////////////////////////////////////

    /**
     * Interface for the command pattern.
     */
    private interface Command {
        void execute();
    }

}
