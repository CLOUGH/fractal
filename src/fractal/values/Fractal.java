/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.values;

import fractal.syntax.ASTStatement;
import fractal.syntax.ASTStmtSequence;
import fractal.semantics.FractalState;
import java.util.ArrayList;

/**
 *
 * @author newts
 */
public class Fractal extends FractalValue {
//    ArrayList<ASTStatement> body;
    FractalState state;
    ASTStmtSequence body;
    Fractal self;
    
    public Fractal(ArrayList<ASTStatement> b) {
        super(FractalTypes.FRACTAL);
        body = new ASTStmtSequence(b);
        // save state here.
        self = this;
    }
    
    @Override
    public boolean isFractal() {
        return true;
    }

    public ASTStmtSequence getBody() {
        return body;
    }
    
    public <T extends FractalValue> T render(Fractal self, int level, double scale) {
        // this method should be moved to the evaluator
        // newState = state.extend(self, level, scale);
        // return body.visit(this, newState)
        return null;
    } 
}
