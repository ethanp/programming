/* Interpreter Visitor */
import java.util.*;
public class LispParserInterpreterVisitor implements LispParserVisitor
{
	private int indent = 0;



	private ArrayList<TreeMap <String, Object>> env = new ArrayList< TreeMap<String, Object>>();

	private String indentString() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < indent; ++i) {
			sb.append(' ');
		}
		return sb.toString();
	}

	public ArrayList<TreeMap<String, Object>> getEnv() {
		return env;
	}

	// *********************************************************************
	public Object visit(SimpleNode node, Object data) {
		//System.out.println(indentString() + node +
		//               ": acceptor not unimplemented in subclass?");
		++indent;
		data = node.childrenAccept(this, data);
		--indent;
		return data;
	}

	// *********************************************************************
	public Object visit(ASTProgram node, Object data) {
		//System.out.println(indentString() + node);
		/*  ++indent;
			data = node.childrenAccept(this, data);
			--indent; */
		//return data;

		return  node.jjtGetChild(0).jjtAccept(this, data);

	}

	// *********************************************************************
	public Object visit(ASTArithExpr node, Object data) {
		String sOp = node.getOp();

		if(sOp.equals("+")) {
			double sum = 0;

			for(int iChild=0;iChild < node.jjtGetNumChildren(); iChild++) {

				Node chile = node.jjtGetChild(iChild);

				Object result = chile.jjtAccept(this, data);
				//System.out.println("printing addition operand... " +  result);

				sum += (Double) result;

			}

			return sum;

		} else if(sOp.equals("-")) {
			// first child minus the rest
			double diff = (Double) node.jjtGetChild(0).jjtAccept(this, data);

			for(int iChild=1;iChild < node.jjtGetNumChildren(); iChild++) {
				Object result = node.jjtGetChild(iChild).jjtAccept(this, data);

				diff -= (Double) result;

			}

			return diff;


		} else if(sOp.equals("*")) {
			double prod = 1;

			for(int iChild=0;iChild < node.jjtGetNumChildren(); iChild++) {
				Object result = node.jjtGetChild(iChild).jjtAccept(this, data);

				prod *= (Double) result;

			}

			return prod;

		} else if(sOp.equals("/")) {
			double div = (Double) node.jjtGetChild(0).jjtAccept(this, data);

			for(int iChild=1;iChild < node.jjtGetNumChildren(); iChild++) {
				Object result = node.jjtGetChild(iChild).jjtAccept(this, data);

				div /= (Double) result;

			}

			return div;

		} else {
			return null;

		}

	}

	// *********************************************************************
	public Object visit(ASTNum node, Object data) {
		//since this is a num, just return value
		return node.getVal();
	}

	// *********************************************************************
	public Object visit(ASTNamedFunctionApp node, Object data) {
		//since this is a num, just return value

		Node app = new ASTFunctionExpr(0);
		/*String funcID = (String) node.jjtGetChild(0).jjtAccept(this, data);
			Node func=  null;
			for (TreeMap<String, Object> t : env) {
			  if (t.containsKey(funcID)){
				func = (Node) t.get(funcID);
			  }
			}*/
		//Node test = (Node) node.jjtGetChild(0).jjtAccept(this, data);
		//System.out.println( test.jjtGetChild(1).jjtGetChild(0));
		app.jjtAddChild((Node) node.jjtGetChild(0).jjtAccept(this, data), 0);
		app.jjtAddChild((Node) node.jjtGetChild(1), 1);
		Object toRet = app.jjtAccept(this, data);
		env.remove(env.size() - 1);
		return toRet;
	}

	// *********************************************************************
	public Object visit(ASTIdentifier node, Object data) {
		Object replacement = null;
		// LOOK FORWARDS IN THE ENVIRONMENT
		// which gets the most recent X
		for (TreeMap<String, Object> t : env) {
			if (t.containsKey(node.getIdentifier())){
				replacement = t.get(node.getIdentifier());
			}

		}

		if (replacement == null) throw new IllegalStateException("Lookup failed for " + node.getIdentifier());
		return replacement;

	}

	// *********************************************************************
	// (let ((y 5)) (let ((f (lambda (x) (+ x y)))) (let ((y 3)) (f 5))))
	public Object visit(ASTLambdaExpr node, Object data)
	{
		// if statically scoped, recurse through the lambda function and
		// look up all identifiers that are not the parameter.
		// store them as the
		// get param id of lambda expr - DO NOT EVAL\
		Boolean staticness = (Boolean) data;
		if (staticness == true)
		{
			ASTIdentifier param = (ASTIdentifier) node.jjtGetChild(0);

			// get body of lambda expr - DO NOT EVAL
			Node body = node.jjtGetChild(1);

			repIdent(body, param);
		}
		// just return self.
		return node;

	}

	// *********************************************************
	private void repIdent(Node node, ASTIdentifier param) {
		// if anything that has a body
/*
    if(node instance FunctionExpr || node instanceof NamedFunctionApp ||
       node instanceof LambdaExpr) { }
      // get parameter of function app
      Node param = node.jjtGetChild(1);

      if(child

    }
*/
		int numChildren = node.jjtGetNumChildren();
		for (int i = 0 ; i < numChildren ; i++) {
			Node child = node.jjtGetChild(i);

			if (child.jjtGetNumChildren() > 0) {
				repIdent(child, param);
			}
			else if (child instanceof ASTIdentifier) {
				ASTIdentifier childID = (ASTIdentifier)child;
				if (childID.getIdentifier().equals(param.getIdentifier()))
					continue;
				else {
					for (TreeMap<String,Object> t : env) {
						if (t.containsKey(childID.getIdentifier())) {
							Object storedValue = t.get(childID.getIdentifier());
							if (storedValue instanceof Double) {
								ASTNum storedNum = new ASTNum(913);
								storedNum.setVal((Double) storedValue);
								((SimpleNode)node).jjtSetChild(i, storedNum);
							}
						}
					}
				}
			}
		}
	}


	// *********************************************************************

	// E.g. ((lambda (x) x) 3)

	// Child 0   = Lambda()                  = (lambda (x) x)
	// Child 0.0 = Identifier()              = x                ==> param
	// Child 0.1 = Anything() = Identifier() = x                ==> body

	// Child 1   = Anything() = Num()        = 3                ==> arg

	public Object visit(ASTFunctionExpr node, Object data) {
		// get param id of lambda expr - DO NOT EVAL
		ASTIdentifier param = (ASTIdentifier) node.jjtGetChild(0).jjtGetChild(0);

		// get body of lambda expr - DO NOT EVAL
		Node body = node.jjtGetChild(0).jjtGetChild(1);

		// "evaluate" child argument. this would require an environment to build.
		Object arg = node.jjtGetChild(1).jjtAccept(this, data);

		// try adding a String, int map to the env arraylist
		TreeMap<String, Object> pair = new TreeMap<String, Object>();
		pair.put(param.getIdentifier(), arg);
//    System.out.println("Printing pair added to env: key is:  " + param.getIdentifier() + " and value is:  " + arg);
		env.add(pair);

		// just return self.
		return body.jjtAccept(this, data);

	}

}
