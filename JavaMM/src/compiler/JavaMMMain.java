package compiler;

import ast.*;
import symbol.*;
import java.util.Hashtable;
import java.io.FileInputStream;
import java.util.Set;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class JavaMMMain
{
    private static Hashtable<String, SymbolTable> symbolTables;
    private static String className;
    private static PrintWriter jWriter;
    private static ArrayList<String> usedLabels; 
    public static void main(String[] args) throws Exception
    {
        if (args.length < 1)
        {
            System.out.println("Usage: JavaMMMain <filename>");
            System.exit(1);
        }

        symbolTables = new Hashtable<String, SymbolTable>();
        usedLabels = new ArrayList<String>();

        JavaMM parser = new JavaMM(new FileInputStream(args[0]));
        SimpleNode root = parser.Program();

        System.out.println("Syntax analisys done");

        if(buildSymbolTables(root))
        {
            System.out.println("Symbol tables built");
            //printSymbolTables();

            if(semanticAnalysis(root))
                System.out.println("Semantic analysis complete");
            else
                return;

            jWriter = getJFile();

            if(jWriter == null)
                return;

            toJVM(root);

            System.out.println("JVM file generated");
            jWriter.close();
        }

    }

    public static PrintWriter getJFile()
    {
        try
        {
            PrintWriter print_writer;

            String filename = "jasminFiles";
            File file = new File(filename);

            if(!file.exists())
                file.mkdirs();

            File jasmin_file = new File(filename + '/' + className + ".j");

            if(!jasmin_file.exists())
                jasmin_file.createNewFile();

            print_writer = new PrintWriter(jasmin_file);

            return print_writer;
        }
        catch(IOException e)
        {
            System.err.println("Couldn't open .j file");
            return null;
        }
    }

    public static void toJVM(SimpleNode root)
    {
        Node classNode = root.jjtGetChild(0);
        String extension;

        if(classNode.jjtGetNumChildren() > 0 && classNode.jjtGetChild(0).toString().equals("Extends"))
            extension = classNode.jjtGetChild(0).getName();
        else
            extension = ".super java/lang/Object\n";

        jWriter.println(".class public " + className);
        jWriter.println(extension);
        jWriter.println(".method public <init>()V");
        jWriter.println("\taload_0");
        jWriter.println("\tinvokenonvirtual java/lang/Object/<init>()V");
        jWriter.println("\treturn");
        jWriter.println(".end method\n");

        for (int i = 0; i < classNode.jjtGetNumChildren(); i++)
        {
            Node child = classNode.jjtGetChild(i);

            switch (child.toString())
            {
                case "Var":
                    //jWriter.println(child.getType() + " " + child.getName() + ";\n"); //Check if correct
                    break;

                case "Main":
                case "Method":
                    usedLabels.clear();
                    usedLabels.trimToSize();
                    functionToJVM(child);
                    break;

                default:
                    continue;
            }

        }
    }

    public static void functionToJVM(Node function)
    {
        int children = function.jjtGetNumChildren();

        String funcName = function.getName();

        int i = 0;

        jWriter.print(".method public ");

        if(funcName.equals("main"))
            jWriter.println("static main([Ljava/lang/String;)V");
        else
        {
            jWriter.print(funcName + "(");

            for(; i < children; i++)
            {
                Node arg = function.jjtGetChild(i);

                if(arg.toString().equals("Arg"))
                    jWriter.print(getJVMType(arg.getType()));
                else
                    break;

                if(i + 1 < children && function.jjtGetChild(i + 1).toString().equals("Arg"))
                    jWriter.print(" ");
            }

            jWriter.print(")" + getJVMType(function.getReturnType()) + "\n");
        }

        SymbolTable function_table = symbolTables.get(funcName);

        if(function_table != null)
        {
            int nLocals = function_table.getTable().size() + function_table.getArgs().size();

            jWriter.println("\t.limit locals " + nLocals + "\n");
        }

        for(; i < children; i++)
        {
            Node child = function.jjtGetChild(i);

            switch(child.toString())
            {
                case "Var":
                case "Arg":
                    break;

                case "Return":
                    returnToJVM(child, funcName);
                    break;

                default: //Statement
                    statementToJVM(child, funcName);
            }
        }

        switch (function_table.getReturnType()) 
        {
            case "int":
            case "boolean":
                jWriter.println("\tireturn");
                break;

            case "int[]":
                jWriter.println("\tareturn");
                break;

            case "void":
                jWriter.println("\treturn");
        }

        jWriter.println(".end method\n");
    }

    public static void returnToJVM(Node returnNode, String funcName)
    {
        expressionToJVM(returnNode.jjtGetChild(0), funcName);
    }

    public static void getJVMInt(int constant)
    {
        if(constant <= 5)
        {
            jWriter.println("\ticonst_" + constant);
            return;
        }

        if(constant < 128)
        {
            jWriter.println("\tbipush " + constant);
            return;
        }

        jWriter.println("\tsipush " + constant);
    }

    public static String getJVMType(String type)
    {
        switch(type)
        {
            case "int":
                return "I";

            case "boolean":
                return "Z";

            case "int[]":
                return "[I";

            default:
                return type;
        }
    }

    public static void statementToJVM(Node statement, String funcName)
    {
        switch (statement.toString())
        {
            case "If":
                //TODO Complete
                break;

            case "While":
                //TODO Complete
                break;

            case "TERM":
                termToJVM(statement, funcName, false);
                break;

            case "EQUALS":
                equalsToJVM(statement, funcName);
                break;

            case "Else":
            case "Then":
                //TODO Complete
                break;

            default:
                System.out.println("Unexpected statement type in JVM parsing: " + statement.toString());
        }
    }

    public static void termToJVM(Node term, String funcName, boolean store)
    {
        String termName = term.getName();
        String value = "";

        if(termName != null)
        {
            switch(termName)
            {
                case "true":
                    jWriter.println("\ticonst_1");
                    value = "boolean";
                    break;

                case "false":
                    jWriter.println("\ticonst_0");
                    value = "boolean";
                    break;

                case "this":
                    jWriter.println("\taload_0");
                    value = className;
                    break;

                default:

                    try
                    {
                        int i = Integer.parseInt(termName); //Integer

                        value = "int";
                        getJVMInt(i);
                    } //Variable
                    catch(NumberFormatException nfe)
                    {
                        value = identifierEvaluatesTo(term, funcName, true, false);
                        variableToJVM(term, funcName, store);
                    }
            }
        }

        if(term.jjtGetNumChildren() == 0)
            return;

        Node termSon = term.jjtGetChild(0);
        boolean noNewNorEnclosedExpr = false;

        switch(termSon.toString())
        {
            case "ENCLOSED_EXPR":
                if(termSon.jjtGetNumChildren() != 0)
                {
                    value = evaluatesTo(termSon.jjtGetChild(0), funcName);
                    expressionToJVM(termSon.jjtGetChild(0), funcName);
                    break;
                }

            case "NEW":
                if(termSon.jjtGetNumChildren() == 0) //New object
                {
                    value = termSon.getType();
                    jWriter.println("\tnew " + termSon.getType());
                    jWriter.print("\tinvokenonvirtual ");

                    if(termSon.getType().equals(className))
                        jWriter.println("<init>()V");
                    else
                        jWriter.println(termSon.getType() + "()V");

                }
                else
                {
                    value = "int[]";
                    //TODO Check for new array
                }

                break;

            default:
                noNewNorEnclosedExpr = true;
        }

        int childIndex;

        if(term.jjtGetNumChildren() == 1)
        {
            if(!noNewNorEnclosedExpr)
                return;
            else
                childIndex = 0;
        }
        else
        {
            if(noNewNorEnclosedExpr)
                return;
            else
                childIndex = 1;
        }

        Node termSecondSon = term.jjtGetChild(childIndex);

        switch(termSecondSon.toString())
        {
            case "ArrayAccs":
                //TODO Complete
                break;

            case "Member":

                boolean staticMember;

                if(value.equals("all"))
                {
                    value = term.getName();
                    staticMember = true;
                }
                else
                    staticMember = false;
                    

                functionCallToJVM(termSecondSon, funcName, value, staticMember);
                break;

            default:
                System.out.println("Unexpected term's second son in JVM term evaluation in function " + funcName
                    + ": " + termSecondSon.toString());
        }
    }

    public static void functionCallToJVM(Node member, String funcName, String caller, boolean staticMember)
    {
        String[] argTypes;
        boolean localFunc;
        String returnType = "V";

        if(caller.equals(className))
        {
            SymbolTable funcTable = symbolTables.get(member.getName());

            argTypes = funcTable.getArgsList();
            localFunc = true;
            returnType = getJVMType(funcTable.getReturnType());
        }
        else
        {
            argTypes = new String[member.jjtGetNumChildren()];
            localFunc = false;
        }

        for(int i = 0; i < member.jjtGetNumChildren(); i++)
        {
            if(!localFunc)
                argTypes[i] = evaluatesTo(member.jjtGetChild(i), funcName);

            expressionToJVM(member.jjtGetChild(i), funcName);
        }

        String cmd = "invoke";

        if(staticMember)
            cmd += "static ";
        else
            cmd += "virtual ";

        cmd += caller + "/" + member.getName() + "(";

        for(int i = 0; i < argTypes.length; i++)
        {
            cmd += getJVMType(argTypes[i]);

            if(i < argTypes.length - 1)
                cmd += " ";
        }
            

        jWriter.print("\t" + cmd + ")" + returnType + "\n");
    }

    public static void expressionToJVM(Node expression, String funcName)
    {
        String label1, label2;

        switch(expression.toString())
        {
            case "ADD":
                expressionToJVM(expression.jjtGetChild(0), funcName);
                expressionToJVM(expression.jjtGetChild(1), funcName);
                jWriter.println("\tiadd");
                break;

            case "SUB":
                expressionToJVM(expression.jjtGetChild(0), funcName);
                expressionToJVM(expression.jjtGetChild(1), funcName);
                jWriter.println("\tisub");
                break;

            case "DIV":
                expressionToJVM(expression.jjtGetChild(0), funcName);
                expressionToJVM(expression.jjtGetChild(1), funcName);
                jWriter.println("\tidiv");
                break;

            case "MUL":
                expressionToJVM(expression.jjtGetChild(0), funcName);
                expressionToJVM(expression.jjtGetChild(1), funcName);
                jWriter.println("\timul");
                break;

            case "AND":
                label1 = generateRandomLabel(funcName); 
                label2 = generateRandomLabel(funcName);
                
                expressionToJVM(expression.jjtGetChild(0), funcName);
                jWriter.println("\tifeq " + label1);
                expressionToJVM(expression.jjtGetChild(1), funcName);
                jWriter.println("\tifeq " + label1);
                jWriter.println("\ticonst_1");
                jWriter.println("\tgoto " + label2);
                jWriter.println("\n" + label1 + ":");
                jWriter.println("\ticonst_0");
                jWriter.println("\n" + label2 + ":");
                break;


            case "LOWER":
                label1 = generateRandomLabel(funcName); 
                label2 = generateRandomLabel(funcName);

                expressionToJVM(expression.jjtGetChild(0), funcName);
                expressionToJVM(expression.jjtGetChild(1), funcName);
                jWriter.println("\tif_icmpge " + label1);
                jWriter.println("\ticonst_1");
                jWriter.println("\tgoto " + label2);
                jWriter.println("\n" + label1 + ":");
                jWriter.println("\ticonst_0");
                jWriter.println("\n" + label2 + ":");
                break;

            case "NOT":
                label1 = generateRandomLabel(funcName); 
                label2 = generateRandomLabel(funcName);

                expressionToJVM(expression.jjtGetChild(0), funcName);
                jWriter.println("\tifne " + label1);
                jWriter.println("\ticonst_1");
                jWriter.println("\tgoto " + label2);
                jWriter.println("\n" + label1 + ":");
                jWriter.println("\ticonst_0");
                jWriter.println("\n" + label2 + ":");
                break;

            case "TERM":
                termToJVM(expression, funcName, false);
                break;

            default:
                System.out.println("Unexpected token to parse to JVM in function " + funcName + ": " + expression.toString());
                return;
        }
    }

    public static void equalsToJVM(Node equals, String funcName)
    {
        Node lhs = equals.jjtGetChild(0), expression = equals.jjtGetChild(1);
        
        expressionToJVM(expression, funcName);
        termToJVM(lhs, funcName, true);

        if(lhs.jjtGetNumChildren() > 0)
        {
            Node lhsChild = lhs.jjtGetChild(0);
            
            switch(lhsChild.toString())
            {
                case "ArrayAccs":
                    //TODO Complete;
                    break;

                case "Member":
                SymbolTable funcST = symbolTables.get(funcName);
                Symbol caller;

                caller = funcST.getTable().get(lhs.getName());

                if(caller == null)
                    caller = funcST.getArgs().get(lhs.getName());

                if(caller == null)
                {
                    funcST = symbolTables.get(className);

                    caller = funcST.getTable().get(lhs.getName());
                }

                if(caller != null)
                    functionCallToJVM(lhsChild, funcName, caller.getType(), false);
                else
                    functionCallToJVM(lhsChild, funcName, lhs.getName(), true);
            }
        }

        jWriter.print("\n");
    }

    public static String generateRandomLabel(String funcName)
    {
        String label;
        Random rand = new Random();

        do
        {
            label = funcName + Integer.toString(rand.nextInt(10000));
        }
        while(usedLabels.contains(label));

        usedLabels.add(label);

        return label;
    }

    public static void variableToJVM(Node identifier, String funcName, boolean store)
    {
        SymbolTable symbolTable = symbolTables.get(funcName);
        Symbol variable;
        String cmd;

        if(symbolTable != null)
        {
            variable = symbolTable.getTable().get(identifier.getName());

            if(variable == null)
            {
                variable = symbolTable.getArgs().get(identifier.getName());

                if(variable == null)
                {
                    symbolTable = symbolTables.get(className);

                    if(symbolTable != null)
                    {
                        variable = symbolTable.getTable().get(identifier.getName());

                        if(variable != null)
                        {
                            jWriter.println("\taload_0");

                            if(store)
                                jWriter.println("\tputfield " + className + "/" + variable.getName() 
                                    + " " + getJVMType(variable.getType()));
                            else
                                jWriter.println("\tgetfield " + className + "/" + variable.getName() 
                                    + " " + getJVMType(variable.getType()));
                        }
                    }

                    return;
                }
                else
                {
                    if(store)
                        cmd = "store_" + variable.getIndex();
                    else
                        cmd = "load_" + variable.getIndex();
                }
            }
            else
            {
                if(store)
                    cmd = "store " + variable.getIndex();
                else
                    cmd = "load " + variable.getIndex();
            }

            switch(variable.getType())
            {
                case "int":
                case "boolean":
                    cmd = "\ti" + cmd;
                    break;

                default:
                    cmd = "\ta" + cmd;
            }

            jWriter.println(cmd);
        }
    }

    public static boolean semanticAnalysis(SimpleNode root)
    {
        boolean continueAnalysis;
        Node classNode = root.jjtGetChild(0);

        for (int i = 0; i < classNode.jjtGetNumChildren(); i++)
        {
            Node child = classNode.jjtGetChild(i);

            switch (child.toString())
            {
                case "Main":
                case "Method":
                    continueAnalysis = analyseFunction(child);
                    break;

                default:
                    continue;
            }

            if (!continueAnalysis)
                return false;
        }

        return true;
    }

    public static boolean analyseFunction(Node func)
    {
        for(int i = 0; i < func.jjtGetNumChildren(); i++)
        {
            if(func.jjtGetChild(i).toString().equals("Var") || func.jjtGetChild(i).toString().equals("Arg"))
                continue;
            else
            {
                if(func.jjtGetChild(i).toString().equals("Return"))
                {
                    if(!isTheSameType(evaluatesTo(func.jjtGetChild(i).jjtGetChild(0), func.getName()), func.getReturnType()))
                    {
                        System.out.println("Return value in function " + func.getName() + " does not meet function prototype");
                        return false;
                    }
                    else
                        return true;

                }
                else
                {
                    if(!analyseStatement(func.jjtGetChild(i), func.getName()))
                    {
                        System.out.println("Error in function " + func.getName() + " statement(s)");
                        return false;
                    }
                }
            }

        }

        return true;
    }

    public static boolean analyseStatement(Node statement, String funcName)
    {
        boolean continueAnalysis;

        switch (statement.toString())
        {
            case "If":
                continueAnalysis = analyseIf(statement, funcName);
                break;

            case "While":
                continueAnalysis = analyseWhile(statement, funcName);
                break;

            case "TERM":
                continueAnalysis = !termEvaluatesTo(statement, funcName).equals("error");
                break;

            case "EQUALS":
                continueAnalysis = analyseEquals(statement, funcName);
                break;

            case "Else":
            case "Then":
                if(statement.jjtGetNumChildren() == 1)
                    continueAnalysis = analyseStatement(statement.jjtGetChild(0), funcName);
                else
                    continueAnalysis = true;

                break;

            // Standalone arithmetic and boolean expressions not valid (?)
            case "AND":
            case "LOWER":
            case "ADD":
            case "SUB":
            case "MUL":
            case "DIV":
                System.out.println("Standalone arithmetic or boolean expressions detected in " + funcName);
                return false;

            default:
                System.out.println("Unknown statement type " + statement.toString() + " in function " + funcName);
                return false;
        }

        if(!continueAnalysis)
            return false;

        return true;
    }

    public static boolean analyseIf(Node ifNode, String funcName)
    {
        if(ifNode.jjtGetNumChildren() < 3)
        {
            System.out.println("If in " + funcName + " doesn't have enough children");
            return false;
        }

        if(!isTheSameType(evaluatesTo(ifNode.jjtGetChild(0), funcName), "boolean"))
        {
            System.out.println("if condition in function " + funcName + " doesn't evaluate to a boolean");
            return false;
        }

        Node then = ifNode.jjtGetChild(1);

        for(int i = 0; i < then.jjtGetNumChildren(); i++)
        {
            if (!analyseStatement(then.jjtGetChild(i), funcName))
            {
                System.out.println("if 'then' statement is invalid in function " + funcName);
                return false;
            }
        }

        Node elseNode = ifNode.jjtGetChild(2);

        for(int i = 0; i < elseNode.jjtGetNumChildren(); i++)
        {
            if (!analyseStatement(elseNode.jjtGetChild(i), funcName))
            {
                System.out.println("if 'then' statement is invalid in function " + funcName);
                return false;
            }
        }


        return true;
    }

    public static boolean analyseWhile(Node whileNode, String funcName)
    {
        if(whileNode.jjtGetNumChildren() < 2)
        {
            System.out.println("While in " + funcName + " doesn't have enough children");
            return false;
        }

        if(!isTheSameType(evaluatesTo(whileNode.jjtGetChild(0), funcName), "boolean"))
        {
            System.out.println("while condition in function " + funcName + " doesn't evaluate to a boolean");
            return false;
        }

        Node then = whileNode.jjtGetChild(1);

        for (int i = 0; i < then.jjtGetNumChildren(); i++)
        {
            if (!analyseStatement(then.jjtGetChild(i), funcName))
            {
                System.out.println("while 'then' statement is invalid in function " + funcName);
                return false;
            }
        }

        return true;
    }

    public static boolean analyseEquals(Node equals, String funcName)
    {
        String op1Value = identifierEvaluatesTo(equals.jjtGetChild(0), funcName, false, false);
        String op2Value = evaluatesTo(equals.jjtGetChild(1), funcName);

        if(op1Value.equals("error") || op2Value.equals("error"))
        {
            System.out.println("Error in equals operand(s) in function " + funcName);
            return false;
        }

        if(isTheSameType(op1Value, op2Value))
            return true;
        else
        {
            System.out.println("Equals operator types don't match in function "
                + funcName + ": " + op1Value + " vs " + op2Value);

            return false;
        }
    }

    public static boolean isTheSameType(String type1, String type2)
    {
        return type1.equals(type2) || type1.equals("all") || type2.equals("all");
    }

    public static String evaluatesTo(Node expression, String funcName)
    {
        switch(expression.toString())
        {
            case "ADD":
            case "SUB":
            case "DIV":
            case "MUL":
                if(expression.jjtGetNumChildren() == 2
                    && isTheSameType(evaluatesTo(expression.jjtGetChild(0), funcName), "int")
                    && isTheSameType(evaluatesTo(expression.jjtGetChild(1), funcName), "int"))
                    return "int";
                else
                {
                    System.out.println("Operand(s) in expression of type " + expression.toString()
                        + " don't evaluate to integers in function " + funcName);
                    return "error";
                }


            case "AND":
                if(expression.jjtGetNumChildren() == 2
                    && isTheSameType(evaluatesTo(expression.jjtGetChild(0), funcName), "boolean")
                    && isTheSameType(evaluatesTo(expression.jjtGetChild(1), funcName), "boolean"))
                    return "boolean";
                else
                {
                    System.out.println("Operand(s) in expression of type AND don't evaluate to booleans in function "
                        + funcName);
                    return "error";
                }

            case "NOT":
                if(expression.jjtGetNumChildren() == 1 
                    && isTheSameType(evaluatesTo(expression.jjtGetChild(0), funcName), "boolean"))
                    return "boolean";
                else
                {
                    System.out.println("Operand in negated expression doesn't evaluate to boolean in function "
                        + funcName);
                    return "error";
                }


            case "LOWER":
                if(expression.jjtGetNumChildren() == 2
                    && isTheSameType(evaluatesTo(expression.jjtGetChild(0), funcName), "int")
                    && isTheSameType(evaluatesTo(expression.jjtGetChild(1), funcName), "int"))
                    return "boolean";
                else
                {
                    System.out.println("Operand(s) in expression of type LOWER don't evaluate to integers in function "
                        + funcName);
                    return "error";
                }


            case "TERM":
                return termEvaluatesTo(expression, funcName);

            default:
                System.out.println("Unexpected token to evaluate in function " + funcName + ": " + expression.toString());
                return "error";
        }
    }

    public static String termEvaluatesTo(Node term, String funcName)
    {
        String termName = term.getName();
        String value = "";

        if(termName != null)
        {
            switch(termName)
            {
                case "true":
                case "false":
                    value = "boolean";
                    break;

                case "this":
                    value = className;
                    break;

                default:

                    try
                    {
                        Integer.parseInt(termName); //Integer

                        return "int"; //TODO change to value =
                    } //Variable
                    catch(NumberFormatException nfe)
                    {
                        value = identifierEvaluatesTo(term, funcName, true, false);

                        if(value.equals("error"))
                        {
                            System.out.println("Unexpected term in term evaluation in function " + funcName + ": "
                                + termName);

                            return "error";
                        }
                    }
            }
        }

        if(term.jjtGetNumChildren() == 0)
        {
            if(termName == null)
            {
                System.out.println("Term has no name nor the expected number of children in function " + funcName
                    + ": " + term.toString());

                return "error";
            }

            return value;
        }

        Node termSon = term.jjtGetChild(0);
        boolean noNewNorEnclosedExpr = false;

        switch(termSon.toString())
        {
            case "ENCLOSED_EXPR":
                if(termSon.jjtGetNumChildren() != 0)
                {
                    value = evaluatesTo(termSon.jjtGetChild(0), funcName);
                    break;
                }
                else
                {
                    System.out.println("Enclosed expression is childless");
                    return  "error";
                }

            case "NEW":
                if(termSon.jjtGetNumChildren() == 0) //New object
                {
                    value = termSon.getType();
                    break;
                }
                else
                {
                    Node arrayAcces = termSon.jjtGetChild(0);

                    if(analyseArrayAccs(arrayAcces, funcName))
                    {
                        value = "int[]";
                        break;
                    }
                    else
                    {
                        System.out.println("Array initialization failed in function " + funcName);
                        return "error";
                    }
                }

            default:
                noNewNorEnclosedExpr = true;
        }

        int childIndex;

        if(term.jjtGetNumChildren() == 1)
        {
            if(!noNewNorEnclosedExpr)
                return value;
            else
                childIndex = 0;
        }
        else
        {
            if(noNewNorEnclosedExpr)
            {
                System.out.println("Unknown term child in term evaluation in function " + funcName + ": "
                    + termSon.toString());

                return "error";
            }
            else
                childIndex = 1;
        }

        Node termSecondSon = term.jjtGetChild(childIndex);

        switch(termSecondSon.toString())
        {
            case "ArrayAccs":
                if(identifierEvaluatesTo(termSecondSon.jjtGetParent(), funcName, true, true).equals("int[]")
                    && analyseArrayAccs(termSecondSon, funcName))
                    return "int";
                else
                {
                    System.out.println("Array access failed in function " + funcName + ": " + value);
                    return "error";
                }

            case "Member":
                return analyseFunctionCall(termSecondSon, funcName, value);

            default:
                System.out.println("Unexpected term's second son in term evaluation in function " + funcName
                    + ": " + termSecondSon.toString());

                return "error";
        }
    }

    public static String analyseFunctionCall(Node member, String funcName, String callerType)
    {
        boolean ownFunc = callerType.equals(className);
        SymbolTable funcST = symbolTables.get(member.getName());

        if(callerType.equals("int") || callerType.equals("boolean"))
        {
            System.out.println("int and boolean types don't have any members: function " + funcName);
            return "error";
        }

        if(callerType.equals("int[]") && member.getName().equals("length"))
            return "int";

        if(funcST == null)
        {
            if(ownFunc)
            {
                System.out.println("Couldn't find class function " + member.getName() + " in function " + funcName);
                return "error";
            }
            else
                return "all"; //Assume the result is what we wanted
        }

        String[] argProtos = funcST.getArgsList();

        if(argProtos.length != member.jjtGetNumChildren())
        {
            System.out.println("Number of arguments mismatch in member call in fucntion " + funcName);
            return "error";
        }

        for(int i = 0; i < argProtos.length && i <  member.jjtGetNumChildren(); i++)
        {
            if(!argProtos[i].equals(evaluatesTo(member.jjtGetChild(i), funcName)))
            {
                System.out.println("Call to function " + member.getName() + " in function " + funcName
                + " doesn't match function prototype");
                return "error";
            }
        }

        return funcST.getReturnType();
    }

    //Returns the type of variable
    public static String identifierEvaluatesTo(Node identifier, String funcName, boolean mustBeInit, boolean baseTerm)
    {
        SymbolTable symbolTable = symbolTables.get(funcName);
        Symbol variable;
        boolean arg = false;
        String key;

        if(symbolTable != null)
        {
            variable = symbolTable.getTable().get(identifier.getName());

            if(variable == null)
            {
                variable = symbolTable.getArgs().get(identifier.getName());
                arg = true;

                if(variable == null)
                {
                    arg = false;
                    symbolTable = symbolTables.get(className);
                    key = className;

                    if(symbolTable != null)
                    {
                        variable = symbolTable.getTable().get(identifier.getName());

                        if(variable == null)
                        {
                            if(identifier.jjtGetNumChildren() > 0
                            && identifier.jjtGetChild(0).toString().equals("Member"))
                                return "all";

                            System.out.println("Couldn't find variable " + identifier.getName()
                                + " in function " + funcName);

                            return "error";
                        }
                    }
                    else
                    {
                        System.out.println("Couldn't find class " + className + " symbol table in funtion" + funcName);
                        return "error";
                    }
                }
                else
                    key = funcName;
            }
            else
                key = funcName;

            if(!variable.getInit())
            {
                if(mustBeInit)
                {
                    System.out.println("Variable " + variable.getName() + " was not initialized in function " + funcName);
                    return "error";
                }
                else
                {
                    variable.setInit(true);

                    if(arg)
                        symbolTable.putArg(variable);
                    else
                        symbolTable.putSymbol(variable);

                    symbolTables.put(key, symbolTable);
                }

            }

            if(!baseTerm && identifier.jjtGetNumChildren() > 0 && identifier.jjtGetChild(0).toString().equals("ArrayAccs")
                    && analyseArrayAccs(identifier.jjtGetChild(0), funcName))
                return "int";
            else
                return variable.getType();
        }
        else
        {
            System.out.println("Couldn't find function " + funcName + " in variable analysis");
            return "error";
        }
    }

    public static boolean analyseArrayAccs(Node arrayAcs, String funcName)
    {
        if(arrayAcs.jjtGetNumChildren() != 1)
        {
            System.out.println("Array Access doesn't has expression associated");
            return false;
        }

        return evaluatesTo(arrayAcs.jjtGetChild(0), funcName).equals("int");
    }

    public static boolean buildSymbolTables(SimpleNode root)
    {
        Node classNode;

        if(root.toString().equals("Program"))
        {
            if(root.jjtGetNumChildren() != 0 && (classNode = root.jjtGetChild(0)).toString().equals("Class"))
            {
                if(classNode.toString().equals("Class"))
                {
                    boolean builtSymbolTable;

                    symbolTables.put(classNode.getName(), new SymbolTable());
                    className = classNode.getName();

                    for(int i = 0; i < classNode.jjtGetNumChildren(); i++)
                    {
                        Node child = classNode.jjtGetChild(i);

                        switch(child.toString())
                        {
                            case "Var":
                                builtSymbolTable = buildLocalSymbolTable(child, classNode, false, i);
                                break;

                            case "Main":
                                builtSymbolTable = buildMainSymbolTable(child, i);
                                break;

                            case "Method":
                                builtSymbolTable = buildFunctionSymbolTable(child);
                                break;

                            default:
                                continue;
                        }

                        if(!builtSymbolTable)
                            return false;
                    }

                }

            }
            else
            {
                System.out.println("Program doesn't have class");
                return false;
            }

        }
        else
        {
            System.out.println("Root node doesn't qualify as program: " + root.toString());
            return false;
        }

        return true;
    }

    public static boolean buildLocalSymbolTable(Node var, Node parentNode, boolean main, int index)
    {
        SymbolTable classST;

        if(!main)
            classST = symbolTables.get(parentNode.getName());
        else
            classST = symbolTables.get("main");

        Symbol newSymbol;

        newSymbol = new Symbol(var.getName(), var.getType(), index);

        if(var.toString().equals("Arg"))
        {
            if (!classST.putArg(newSymbol))
            {
                System.out.println("Duplicate argument " + var.getName() + " of type " + var.getType());
                return false;
            }
            else
            {
                if(main)
                    symbolTables.put("main", classST);
                else
                    symbolTables.put(parentNode.getName(), classST);

                return true;
            }
        }
        else
        {
            if (!classST.putSymbol(newSymbol))
            {
                System.out.println("Duplicate local variable " + var.getName() + " of type " + var.getType());
                return false;
            }
            else
            {
                if(main)
                    symbolTables.put("main", classST);
                else
                    symbolTables.put(parentNode.getName(), classST);

                return true;
            }
        }


    }

    public static boolean buildFunctionSymbolTable(Node func)
    {
        SymbolTable funcTable = symbolTables.get(func.getName());

        if(funcTable != null)
            return false;
        else
            funcTable = new SymbolTable();

        funcTable.setReturnType(func.getReturnType());
        symbolTables.put(func.getName(), funcTable);

        int index;

        if(func.jjtGetNumChildren() > 0 && 
            (func.jjtGetChild(0).toString().equals("Arg") || func.jjtGetChild(0).toString().equals("Var"))
            && !(func.jjtGetChild(0).getType().equals("int") ||  func.jjtGetChild(0).getType().equals("boolean")))
            index = 1;
        else
            index = 0;

        for(int i = 0; i < func.jjtGetNumChildren(); i++, index++)
        {
            if(func.jjtGetChild(i).toString().equals("Arg") || func.jjtGetChild(i).toString().equals("Var"))
                if(!buildLocalSymbolTable(func.jjtGetChild(i), func, false, index))
                    return false;
        }

        return true;
    }

    public static boolean buildMainSymbolTable(Node main, int index)
    {
        SymbolTable funcTable = symbolTables.get("main");

        if(funcTable != null)
            return false;
        else
            funcTable = new SymbolTable();

        funcTable.setReturnType("void");
        funcTable.putArg(new Symbol(main.getType(), "String[]", index)); //Type in main = argument identifier
        symbolTables.put("main", funcTable);

        for(int i = 0; i < main.jjtGetNumChildren(); i++)
        {
            if(main.jjtGetChild(i).toString().equals("Var"))
                if(!buildLocalSymbolTable(main.jjtGetChild(i), main, true, i + 1))
                    return false;
        }

        return true;
    }

    public static void printSymbolTables()
    {
        Set<String> keys = symbolTables.keySet();
        SymbolTable table;

        for(String key: keys)
        {
            System.out.println("--- " + key + " symbol table ---\n");
            table = symbolTables.get(key);
            table.printArgs();
            table.printTable();
            System.out.println("\n");
        }
    }
}
