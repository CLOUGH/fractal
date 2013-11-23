/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fractal.semantics;

import java.util.*; 
import fractal.syntax.ASTDefine;
import fractal.syntax.ASTExpAdd;
import fractal.syntax.ASTExpDiv;
import fractal.syntax.ASTExpLit;
import fractal.syntax.ASTExpMod;
import fractal.syntax.ASTExpMul;
import fractal.syntax.ASTExpSub;
import fractal.syntax.ASTExpVar;
import fractal.syntax.ASTFracCompose;
import fractal.syntax.ASTFracSequence;
import fractal.syntax.ASTFracVar;
import fractal.syntax.ASTFractal;
import fractal.syntax.ASTFracExp;
import fractal.syntax.ASTRender;
import fractal.syntax.ASTRestoreStmt;
import fractal.syntax.ASTSaveStmt;
import fractal.syntax.ASTSelf;
import fractal.syntax.ASTSetLevel;
import fractal.syntax.ASTSetScale;
import fractal.syntax.ASTStmtSequence;
import fractal.syntax.ASTTCmdBack;
import fractal.syntax.ASTTCmdClear;
import fractal.syntax.ASTTCmdForward;
import fractal.syntax.ASTTCmdHome;
import fractal.syntax.ASTTCmdLeft;
import fractal.syntax.ASTTCmdPenDown;
import fractal.syntax.ASTTCmdPenUp;
import fractal.syntax.ASTTCmdRight;
import fractal.sys.FractalException;
import fractal.values.FractalValue;
import fractal.values.Fractal;
import fractal.syntax.ASTStatement;
import fractal.syntax.ASTExp;
/**
 *
 * @author newts
 */
public class FractalEvaluator extends AbstractFractalEvaluator {
	private static ASTFracExp fractalExp;

    @Override
    public FractalValue visitASTStmtSequence(ASTStmtSequence seq, FractalState state) throws FractalException {
	System.out.print("->StmtSequence");
	state.updateDisplay();
	
	ASTStatement s;
	ArrayList alst = seq.getSeq();
	Iterator iter = alst.iterator();
	while(iter.hasNext()) {
		System.out.print("->Stmt");
		s = (ASTStatement)iter.next();
		s.visit(this, state);
		System.out.println("");
	}
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTSaveStmt(ASTSaveStmt form, FractalState state) throws FractalException {
        System.out.print("->SaveStmt");
	state.pushTurtle();
	return FractalValue.NO_VALUE;
	 
    }

    @Override
    public FractalValue visitASTRestoreStmt(ASTRestoreStmt form, FractalState state) throws FractalException {
	System.out.print("->RestoreStmt");
	state.popTurtle();
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTRender(ASTRender form, FractalState state) throws FractalException {
	System.out.print("->render");
	int level, prev_level;
	double scale, prev_scale;
	fractalExp = form.getFractal();

	if(form.getLevel()==null || form.getScale()==null) {
		level = 9;
		scale = 100;
	}
	else{
		FractalValue scaleValue =  form.getScale().visit(this,state);
		FractalValue levelValue = form.getLevel().visit(this, state);
		level = levelValue.intValue();
		scale = scaleValue.realValue();
	}	

	prev_level = state.getDefaultLevel();
	prev_scale = state.getDefaultScale();

	state.setDefaultLevel(level);
	state.setDefaultScale(scale);
	
	
	Fractal fractal = fractalExp.visit(this, state).fractalValue();
	System.out.println("\n\tscaleValue: "+scale+ " \n\tlevel: "+level);
	ArrayList<ASTStatement> fractal_body = fractal.getBody().getSeq();
	Iterator i = fractal_body.iterator();
	ASTStatement s;
	while(i.hasNext()) {
		System.out.print("\t->Stmt");
		s = (ASTStatement)i.next();
		s.visit(this, state);
		System.out.println("");
	} 
	
	state.setDefaultLevel(prev_level);
	state.setDefaultScale(prev_scale);

	
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTSetLevel(ASTSetLevel form, FractalState state) throws FractalException {
        System.out.print("->SetLevel");
	ASTExp levelExp = form.getLevel();
	FractalValue levelVal = levelExp.visit(this, state);
	int level = levelVal.intValue();
	state.setDefaultLevel(level);
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTSetScale(ASTSetScale form, FractalState state) throws FractalException {
        System.out.print("->SetScale");
	ASTExp scaleExp = form.getScale();
	FractalValue scaleVal = scaleExp.visit(this, state);
	double scale = scaleVal.realValue();
	state.setDefaultScale(scale);
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTDefine(ASTDefine form, FractalState state) throws FractalException {
	System.out.print("->defineStmt");
	
	Environment current_env = state.getEnvironment();
	String name = form.getVar() ;
	FractalValue value = form.getValueExp().visit(this, state);
	current_env.put(name,value);
	
        return FractalValue.NO_VALUE;	
    }

    @Override
    public FractalValue visitASTFracSequence(ASTFracSequence form, FractalState state) throws FractalException {
        throw new UnsupportedOperationException("Not supported yet.8"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFracCompose(ASTFracCompose form, FractalState state) throws FractalException {
        throw new UnsupportedOperationException("Not supported yet.9"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public FractalValue visitASTFracVar(ASTFracVar form, FractalState state) throws FractalException {
        System.out.print("->fracVar");
	Environment env = state.getEnvironment();
	
	return env.get(form.getVar());
    }

    @Override
    public FractalValue visitASTFractal(ASTFractal form, FractalState state) throws FractalException {
        System.out.print("->fractalStmt\n");
	ArrayList<ASTStatement> fractal_body = form.getBody();
	return new Fractal(fractal_body);
    }

    @Override
    public FractalValue visitASTSelf(ASTSelf form, FractalState state) throws FractalException {
       	System.out.print("->selfStmt");
	int level = state.getDefaultLevel();
	double scale = state.getDefaultScale();
	double length = form.getLength().visit(this, state).realValue();
	
	if(level == 0){
		ASTTCmdForward forward = new ASTTCmdForward(form.getLength());
		forward.visit(this, state);
	}
	else {
		ASTRender render = new ASTRender(
					new ASTExpLit(FractalValue.make(level-1)),
					new ASTExpLit(FractalValue.make(scale * length)),
					fractalExp);
		render.visit(this, state);
	}
	
	
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTTCmdLeft(ASTTCmdLeft form, FractalState state) throws FractalException {
       	System.out.print("->CmdLeft");
	ASTExp angleExp = form.getAngle();
	FractalValue angleVal = angleExp.visit(this, state);
	double angle = angleVal.realValue();
	state.getTurtleState().turn(angle);
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTTCmdRight(ASTTCmdRight form, FractalState state) throws FractalException {
        System.out.print("->CmdRight");
	ASTExp angleExp = form.getAngle();
	FractalValue angleVal = angleExp.visit(this, state);
	double angle = angleVal.realValue();
	state.getTurtleState().turn(-angle);
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTTCmdForward(ASTTCmdForward form, FractalState state) throws FractalException {
 	System.out.print("->CmdForward");
	ASTExp distExp = form.getLength();
	FractalValue distVal = distExp.visit(this, state);
	double distance  = distVal.realValue() * state.getDefaultScale();
	state.getTurtleState().displace(distance);
	return FractalValue.NO_VALUE;
	
    }

    @Override
    public FractalValue visitASTTCmdBack(ASTTCmdBack form, FractalState state) throws FractalException 	{	
        System.out.print("->CmdBack");
	ASTExp distExp = form.getLength();
	FractalValue distVal = distExp.visit(this, state);
	double distance  = distVal.realValue() * state.getDefaultScale();
	state.getTurtleState().displace(-distance);
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTTCmdPenDown(ASTTCmdPenDown form, FractalState state) throws FractalException {
        System.out.print("->CmdPenDown");
	state.getTurtleState().setPenDown(true);
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTTCmdPenUp(ASTTCmdPenUp form, FractalState state) throws FractalException {
	System.out.print("->CmdPenDown");
	state.getTurtleState().setPenDown(false);
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTTCmdClear(ASTTCmdClear form, FractalState state) throws FractalException {
	System.out.print("->Clear");
	state.getDisplay().clear();
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTTCmdHome(ASTTCmdHome form, FractalState state) throws FractalException {
	System.out.print("->CmdHome");
	state.getTurtleState().home();
	return FractalValue.NO_VALUE;
    }

    @Override
    public FractalValue visitASTExpAdd(ASTExpAdd form, FractalState state) throws FractalException {
	System.out.print("->ExpAdd");
        FractalValue v1 = form.getFirst().visit(this, state);
	FractalValue v2 = form.getSecond().visit(this, state);
	FractalValue result = v1.add(v2);
	return result;	
    }

    @Override
    public FractalValue visitASTExpSub(ASTExpSub form, FractalState state) throws FractalException {
        System.out.print("->ExpSub");
        FractalValue v1 = form.getFirst().visit(this, state);
	FractalValue v2 = form.getSecond().visit(this, state);
	FractalValue result = v1.sub(v2);
	return result;	
    }

    @Override
    public FractalValue visitASTExpMul(ASTExpMul form, FractalState state) throws FractalException {
        System.out.print("->ExpMul");
        FractalValue v1 = form.getFirst().visit(this, state);
	FractalValue v2 = form.getSecond().visit(this, state);
	FractalValue result = v1.mul(v2);
	return result;	
    }

    @Override
    public FractalValue visitASTExpDiv(ASTExpDiv form, FractalState state) throws FractalException {
        System.out.print("->ExpDiv");
        FractalValue v1 = form.getFirst().visit(this, state);
	FractalValue v2 = form.getSecond().visit(this, state);
	FractalValue result = v1.div(v2);
	return result;	
    }

    @Override
    public FractalValue visitASTExpMod(ASTExpMod form, FractalState state) throws FractalException {
        System.out.print("->ExpMod");
        FractalValue v1 = form.getFirst().visit(this, state);
	FractalValue v2 = form.getSecond().visit(this, state);
	FractalValue result = v1.mod(v2);
	return result;	
    }

    @Override
    public FractalValue visitASTExpLit(ASTExpLit form, FractalState state) throws FractalException {
	System.out.print("->ExpLit");
        return form.getValue();
	
    }

    @Override
    public FractalValue visitASTExpVar(ASTExpVar form, FractalState state) throws FractalException {
        throw new UnsupportedOperationException("Not supported yet.26"); //To change body of generated methods, choose Tools | Templates.
    }
    
}
