/* Interpreter Visitor */
import java.util.*;
import java.text.DecimalFormat;

public class LispParserEnvironmentVisitor implements LispParserVisitor
{
  private String sOldEnv = "(mtSub)";

     // *********************************************************************
  public Object visit(SimpleNode node, Object data) {
    return null;

  }

  // *********************************************************************
  public Object visit(ASTProgram node, Object data) {
    node.jjtGetChild(0).jjtAccept(this, data);    
    
    return sOldEnv;
    
  }

   // *********************************************************************
  public Object visit(ASTArithExpr node, Object data) {
    String sTemp = "";
  
    String sOp = node.getOp();
    
    if(sOp.equals("+")) {
      sTemp += "(+";
            
      for(int iChild=0;iChild < node.jjtGetNumChildren(); iChild++) {
      
        Node chile = node.jjtGetChild(iChild);        
        sTemp += " " + chile.jjtAccept(this, data);        
        
      }
      
      sTemp += ")";
      return sTemp;
    
    } else if(sOp.equals("-")) {
      sTemp += "(-";
      
      for(int iChild=0;iChild < node.jjtGetNumChildren(); iChild++) {
    		sTemp += " " + node.jjtGetChild(iChild).jjtAccept(this, data);      
        
      }
      
      sTemp += ")";
      return sTemp;
       
    } else if(sOp.equals("*")) {
      sTemp += "(*";    
    
      for(int iChild=0;iChild < node.jjtGetNumChildren(); iChild++) {
    	  sTemp += " " + node.jjtGetChild(iChild).jjtAccept(this, data);      
        
      }

      sTemp += ")";
      return sTemp;
          
    } else if(sOp.equals("/")) {
      sTemp += "(/";    
      
      for(int iChild=0;iChild < node.jjtGetNumChildren(); iChild++) {
    		sTemp += " " + node.jjtGetChild(iChild).jjtAccept(this, data);      
        
      }

      sTemp += sTemp + ")";
      return sTemp;
          
    } else {
      return "";
    
    }
    
  }

  // *********************************************************************
	public Object visit(ASTNum node, Object data) {
    // trim zeroes  
    Object n = node.getVal();
    DecimalFormat df = new DecimalFormat("#.#####");  
    n = df.format(n);     	
  	
    //since this is a num, just return value
    return n;
    
  }
  
  // *********************************************************************
	public Object visit(ASTIdentifier node, Object data) {
	  return node.getIdentifier();

  }

    
  // *********************************************************************
  	public Object visit(ASTNamedFunctionApp node, Object data) {
   
   
    
    return "(" + node.jjtGetChild(0).jjtAccept(this, data) + " " + node.jjtGetChild(1).jjtAccept(this, data) + ")";
    
  }

  // FOR CHANGING TO DYNAMIC SCOPING, YOU MAY JUST HAVE TO TRAVERSE THROUGH
  // THE ARRAYLIST "ENV" THE OTHER WAY
      
  // *********************************************************************
  public Object visit(ASTLambdaExpr node, Object data) {
    // lambda (x) (+ x 5)
    return "(closureV " + node.jjtGetChild(0).jjtAccept(this, data) + " " + node.jjtGetChild(1).jjtAccept(this, data) + " " + sOldEnv + ")";
    
  }
  
  // *********************************************************************
  
  // E.g. ((lambda (x) x) 3)
  
  // Child 0   = Lambda()                  = (lambda (x) x)
  // Child 0.0 = Identifier()              = x                ==> param
  // Child 0.1 = Anything() = Identifier() = x                ==> body
  
  // Child 1   = Anything() = Num()        = 3                ==> arg
  
  public Object visit(ASTFunctionExpr node, Object data) {  
    // get parameter of the lambda we are applying.
    ASTIdentifier param = (ASTIdentifier) node.jjtGetChild(0).jjtGetChild(0);
  
    Node body = node.jjtGetChild(0).jjtGetChild(1);
    
    
    // get argument.  argument may be a number or a lambda.
    Node arg = node.jjtGetChild(1);
  
    if ((arg instanceof ASTNum) || (arg instanceof ASTArithExpr) || (arg instanceof ASTIdentifier)) {
      String sNewEnv = "(aSub " + param.jjtAccept(this, null) + " " + arg.jjtAccept(this, null) + " " + sOldEnv + ")";
      sOldEnv = sNewEnv;
      
    } else if( (arg instanceof ASTLambdaExpr) || (arg instanceof ASTFunctionExpr) ) {
      String sNewEnv = "(aSub " + param.jjtAccept(this, null) + " " + arg.jjtAccept(this, null) + " " + sOldEnv + ")";
      sOldEnv = sNewEnv;
    
    }
    
    // be sure to recurs into body for more environment building.
    body.jjtAccept(this, null);
    return sOldEnv;
    
  }
     
}
