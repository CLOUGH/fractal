/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.semantics;

import cs34q.turtle.TurtleDisplay;
import fractal.syntax.ASTProgram;
import fractal.syntax.ASTStmtSequence;
import fractal.sys.FractalException;
import fractal.values.FractalValue;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JFrame;


/**
 * This class provides a useful starting point for implementing a FRACTAL
 * interpreter.  It will set up a default window with a turtle display, and it
 * also provides an implementation of visitProgram that allows for its 
 * internally maintained state to be used across invocations of visitProgram,
 * so that the state may persist across those calls.  It can also be used so
 * that an externally prepared state is passed instead.  This gives this class
 * the flexibility of being used as a stand-alone interpreter that interacts 
 * with a command line, or a sub-system of a larger program that has the ability
 * to provide a prepared state (e.g. from resotring a previously saved state).
 * @author newts
 */
public abstract class AbstractFractalEvaluator implements 
        Visitor<FractalState, FractalValue> {

    private String interpID;
    private JFrame displayFrame;
    private FractalState persistentState;
    public static final int DEFAULT_WIDTH = 400;
    public static final int DEFAULT_HEIGHT = 400;
    
    public AbstractFractalEvaluator() {
        this("FRACTAL Display");
    }
    
    public AbstractFractalEvaluator(String name) {
        interpID = name;
        persistentState = new FractalState();
        TurtleDisplay display = persistentState.getDisplay();
        display.setForeground(Color.BLACK);
        display.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        _setFrame(_makeDefaultFrame(DEFAULT_WIDTH, DEFAULT_HEIGHT));
        
    }
    
    private void _setFrame(JFrame frame) {
        displayFrame = frame;
        TurtleDisplay display = persistentState.getDisplay();
        displayFrame.add(display);
        displayFrame.setVisible(true);
    }
    
    public void setFrame(JFrame frame) {
        _setFrame(frame);
    }
    
    private JFrame _makeDefaultFrame(int width, int height) {
        JFrame frame = new JFrame(interpID);
        frame.setSize(width, height);
        frame.getContentPane().setLayout(new GridLayout(1, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return frame;
    }
    
    public JFrame makeDefaultFrame(int width, int height) {
        return _makeDefaultFrame(width, height);
    }

    /**
     *
     * @return The persistent state associated with this interpreter.  It is 
     * initialised upon creation of this instance of the interpreter, and it is
     * used as the starting state for each call from the interpreter shell 
     * (REPL).
     */
    public FractalState getPersistentState() {
        return persistentState;
    }
    
    /**
     * Evaluate a Fractal program with respect to the given state.  If null is
     * passed as the state, then the interpreter's persistent state will be 
     * used.  This permits programs to be supplied incrementally.
     * 
     * @param form The program to be evaluated
     * @param state The top level context for that evaluation.  If null is 
     * passed, then the interpreter's internally maintained persistent state 
     * will be used.  If a non-null state is provided, then the evaluation of
     * the program will not affect the persistent state.
     * @return The result of evaluating the program.
     * @throws FractalException  if there is a problem in evaluating the program
     */
    @Override
    public FractalValue visitFractalProgram(ASTProgram form, FractalState state) throws FractalException {
        ASTStmtSequence body = form.getStatements();
        if (state == null)
            return body.visit(this, persistentState);
        else
            return body.visit(this, state);
    }

}
