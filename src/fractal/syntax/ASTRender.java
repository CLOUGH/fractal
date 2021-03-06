package fractal.syntax;

import fractal.semantics.Visitor;
import fractal.sys.FractalException;

/**
  Class ASTRender.
  Intermediate representation class autogenerated by CS34Q semantic generator.
  Created on Sat Oct 12 03:13:16 2013
*/
public class ASTRender extends ASTStatement {
  ASTExp level;
  ASTExp scale;
  ASTFracExp fractal;

  public ASTRender (ASTExp level,ASTExp scale,ASTFracExp fractal) {
    this.level = level;
    this.scale = scale;
    this.fractal = fractal;
  }
  
  public ASTRender (ASTFracExp fractal) {
      this(null, null, fractal);
  }

  public ASTExp getLevel() {
    return level;
  }

  public ASTExp getScale() {
    return scale;
  }

  public ASTFracExp getFractal() {
    return fractal;
  }

  @Override
  public <S, T> T visit(Visitor<S, T> v, S state) throws FractalException {
    return v.visitASTRender(this, state);
  }

}
