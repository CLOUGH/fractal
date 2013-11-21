/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.semantics;

import cs34q.turtle.Turtle;
import cs34q.turtle.TurtleDisplay;
import java.util.Stack;
import fractal.values.Fractal;

/**
 * The context for interpreting the language FRACTAL.  It contains an 
 * environment the turtle state.and any other information necessary for
 * correctly evaluating FRACTAL expressions and statements.
 * @author newts
 */
public class FractalState {
    public static final int INITIAL_DEFAULT_LEVEL = 8;
    public static final double INITIAL_DEFAULT_SCALE = 100.0;
    
    private Environment environment;   // The environment for variables
    private Turtle turtleState;        // The current state of the turtle
    private TurtleDisplay display;     // The display of turtle movements
    private Stack<Turtle> turtleStack; // The stack affected by SAVE and RESTORE
    private int defaultLevel;          // The most recent setting of LEVEL
    private double defaultScale;       // The most recent setting of SCALE
    
    public FractalState() {
        environment = Environment.makeGlobalEnv();
        turtleState = new Turtle(0, 0, 0, true);
        turtleStack = new Stack<>();
        display = new TurtleDisplay();
        turtleState.addListener(display);
        defaultLevel = INITIAL_DEFAULT_LEVEL;
        defaultScale = INITIAL_DEFAULT_SCALE;
    }
    
    /**
     * Create a new state with the specified level and scale, but with all other
     * properties exactly as they are in the given state.
     * 
     * @param state The state to be copied
     * @param level The level to set in the new state.
     * @param scale The scale to set in the new state.
     */
    public FractalState(FractalState state, int level, double scale) {
        environment = state.getEnvironment();
        turtleState = state.getTurtleState();
        turtleStack = state.getTurtleStack();
        display = state.getDisplay();
        defaultLevel = level;
        defaultScale = scale;
    }

    public Environment getEnvironment() {
        return environment;
    }
    
    /**
     * Retrieve the internal stack of turtle states used to support SAVE and 
     * RESTORE commands.
     * @return The stack of turtle states
     */
    protected Stack<Turtle> getTurtleStack() {
        return turtleStack;
    }

    public Turtle getTurtleState() {
        return turtleState;
    }

    public void setTurtleState(Turtle newState) {
        Turtle oldState = turtleState;
//        double sx = oldState.getX();
//        double sy = oldState.getY();
//        double dx = newState.getX();
//        double dy = newState.getY();
//        if (oldState.isPenDown() && (sx != dx || sy != dy)) {
//            display.drawSegment(sx, sy, dx, dy);
//        }
//        this.turtleState = newState;
//        updateDisplay();
    }
    
    protected void updateDisplay() {
        display.showTurtle(turtleState.getBearingInRads(), turtleState.getX(), 
                           turtleState.getY());
    }

    /**
     *
     * @return The display responsible for showing the trace of the turtle as 
     * it moves.
     */
    public TurtleDisplay getDisplay() {
        return display;
    }
    
    /**
     * Push a newly created clone of the current turtle state onto the internal
     * stack
     */
    public void pushTurtle() {
        turtleStack.push(turtleState.deriveDisplaced(0));
    }
    
    /**
     * Pop the turtle state that was last pushed from the stack and restore it
     * as the current state.
     */
    public void popTurtle() {
        turtleState = turtleStack.pop();
        updateDisplay();
    }

    /**
     *
     * @return The current level of approximation to which fractals will be drawn
     */
    public int getDefaultLevel() {
        return defaultLevel;
    }

    /**
     * Set the current value of the level of fractal to be drawn.
     * @param currentLevel The value to set the current level to
     */
    public void setDefaultLevel(int currentLevel) {
        this.defaultLevel = currentLevel;
    }

    /**
     *
     * @return The current scale for drawing fractals
     */
    public double getDefaultScale() {
        return defaultScale;
    }

    /**
     * Set the current value of the scale factor for drawing fractals
     * @param currentScale
     */
    public void setDefaultScale(double currentScale) {
        this.defaultScale = currentScale;
    }

    @Override
    public String toString() {
        return "[" + environment + ", " + turtleState  + ", " + turtleStack + 
                "]";
    }
}
