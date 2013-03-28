clear
mv LispParserDumpVisitor.java LispParserDumpVisitor.keep
mv LispParserInterpreterVisitor.java LispParserInterpreterVisitor.keep
mv LispParserEnvironmentVisitor.java LispParserEnvironmentVisitor.keep
mv ASTNum.java ASTNum.keep
mv ASTArithExpr.java ASTArithExpr.keep
mv ASTIdentifier.java ASTIdentifier.keep
mv SimpleNode.java SimpleNode.keep

rm *.java
rm *.class
rm *.jj

mv LispParserDumpVisitor.keep LispParserDumpVisitor.java
mv LispParserInterpreterVisitor.keep LispParserInterpreterVisitor.java
mv LispParserEnvironmentVisitor.keep LispParserEnvironmentVisitor.java
mv ASTNum.keep ASTNum.java
mv ASTArithExpr.keep ASTArithExpr.java
mv ASTIdentifier.keep ASTIdentifier.java
mv SimpleNode.keep SimpleNode.java

jjtree LispParser.jjt
javacc LispParser.jj
javac *.java
