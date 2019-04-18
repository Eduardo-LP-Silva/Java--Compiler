package compiler;

import ast.*;
import symbol.*;
import java.util.Hashtable;
import java.io.FileInputStream;
import java.util.Set;


class JavaMMMain
{
    private static Hashtable<String, SymbolTable> symbolTables;
    private static String className;
    public static void main(String[] args) throws Exception 
    {
        if (args.length < 1) 
        {
            System.out.println("Usage: JavaMMMain <filename>");
            System.exit(1);
        }

        symbolTables = new Hashtable<String, SymbolTable>();

        JavaMM parser = new JavaMM(new FileInputStream(args[0]));
        SimpleNode root = parser.Program();

        if(buildSymbolTables(root))
            semanticAnalysis(root);
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
                if(!analyseStatement(func.jjtGetChild(i), func.getName()))
                {
                    System.out.println("Error in function " + func.getName() + " statement(s)");
                    return false;
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

            case "TERM": // Verify standalone terms
                continueAnalysis = analyseStandAloneTerm(statement, funcName);
                break;

            case "EQUALS":
                continueAnalysis = analyseEquals(statement, funcName);
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

        if(!evaluatesToBoolean(ifNode.jjtGetChild(0), funcName))
        {
            System.out.println("if condition in function " + funcName + " doesn't evaluate to a boolean");
            return false;
        }

        if(!analyseStatement(ifNode.jjtGetChild(1), funcName))
        {
            System.out.println("if 'then' statement is invalid in function " + funcName);
            return false;
        }

        if(!analyseStatement(ifNode.jjtGetChild(2), funcName))
        {
            System.out.println("if 'else' statement is invalid in function " + funcName);
            return false;
        }
            
        return true;
    }

    public static boolean analyseWhile(Node whileNode, String funcName)
    {
        return true;
    }

    public static boolean analyseStandAloneTerm(Node term, String funcName)
    {
        return true;
    }

    public static boolean analyseEquals(Node equals, String funcName)
    {
        return true;
    }

    public static boolean evaluatesToBoolean(Node expression, String funcName)
    {
        switch(expression.toString())
        {
            case "AND":
                return expression.jjtGetNumChildren() == 2 
                && evaluatesToBoolean(expression.jjtGetChild(0), funcName) 
                && evaluatesToBoolean(expression.jjtGetChild(1), funcName);

            case "NOT":
                return expression.jjtGetNumChildren() == 1 && evaluatesToBoolean(expression.jjtGetChild(0), funcName);

            case "LOWER":
                return expression.jjtGetNumChildren() == 2 
                && evaluatesToInt(expression.jjtGetChild(0), funcName) 
                && evaluatesToInt(expression.jjtGetChild(1), funcName);

            case "TERM":
                return booleanTerm(expression, funcName);

            case "ADD":
            case "SUB":
            case "MUL":
            case "DIV":
                System.out.println("Expression evaluates to arithmetic value instead of boolean in function " + funcName);
                return false;

            default:
                System.out.println("Unexpected token in " + funcName + "'s boolean condition evaluation: " + expression.toString());
                return false;
        }
    }

    public static boolean evaluatesToInt(Node expression, String funcName)
    {
        switch(expression.toString())
        {
            case "AND":
            case "LOWER":
                System.out.println("Expression evaluates to integer value insead of boolean in function " + funcName);
                return false;

            case "TERM":
                return intTerm(expression, funcName);

            case "ADD":
            case "SUB":
            case "MUL":
            case "DIV":
                return expression.jjtGetNumChildren() == 2 
                && evaluatesToInt(expression.jjtGetChild(0), funcName) 
                && evaluatesToInt(expression.jjtGetChild(1), funcName);

            default:
                System.out.println("Unexpected token in " + funcName + "'s integer condition evaluation: " + expression.toString());
                return false;
        }
    }

    public static boolean evaluatesToClass(Node expression, String funcName, String classType)
    {
        if(!expression.toString().equals("TERM"))
            return false;

        if(expression.jjtGetNumChildren() != 0) //Might be a function call or a new object
        {
            if(expression.jjtGetChild(0).toString().equals("NEW"))
            {
                if(!expression.jjtGetChild(0).getType().equals("int[]"))
                {   
                    if(expression.jjtGetChild(0).jjtGetNumChildren() == 0)
                        return expression.jjtGetChild(0).getType().equals(classType);
                    else
                    {
                        if(expression.jjtGetChild(0).jjtGetChild(0).toString().equals("Member"))
                        {
                            String type = analyseFunctionCall(expression.jjtGetChild(0).jjtGetChild(0), funcName, 
                                expression.jjtGetChild(0));

                            return type.equals(classType) || type.equals("all");
                        }
                        else
                            return false;
                    }
                }  
                else
                    return false;
            }
            else
                if(expression.jjtGetChild(0).toString().equals("Member"))
                {
                    String type = analyseFunctionCall(expression.jjtGetChild(0).jjtGetChild(0), funcName, 
                        expression.jjtGetChild(0)); 

                    return type.equals(classType) || type.equals("all");
                }
                else
                    return false;
        }
        else
        {
            String name = expression.getName();

            if(name == null)
                return false;

            switch (name) 
            {
                case "this":
                    return className.equals(classType);
                case "true":
                case "false":
                    return false;

                default:

                    try 
                    {
                        Integer.parseInt(name);
                        return false;
                    } 
                    catch (NumberFormatException e) 
                    {
                        String varType = analyseIdentifier(expression, funcName, true);

                        return varType.equals(classType) || varType.equals("all");
                    }
            }
        }
    } 

    public static boolean evaluatesToArray(Node expression, String funcName)
    {
        return true;
    }

    public static boolean booleanTerm(Node term, String funcName)
    {
        if(term.getName() != null)
        {
            switch(term.getName())
            {
                case "this":
                    if(term.jjtGetNumChildren() > 0 && term.jjtGetChild(0).toString().equals("Member"))
                    {
                        Node member = term.jjtGetChild(0);
                        String returnType = analyseFunctionCall(member, funcName, term);

                        if(returnType.equals("boolean") || returnType.equals("all"))
                            return true;
                    } 
                    else
                    {
                        System.out.println("Standalone this is not a boolean term in function " + funcName);
                        return false;
                    }

                case "true":
                case "false":
                    return true;
                        
                default:

                    try
                    {
                        Integer.parseInt(term.getName());
                        
                        System.out.println("Integer is not a boolean term: function + " + funcName);
                        return false;
                    }
                    catch(NumberFormatException e)
                    {
                        String varType = analyseIdentifier(term, funcName, true);

                        if(!varType.equals("boolean") && !varType.equals("all"))
                        {
                            System.out.println("Variable " + term.getName() + " isn't a boolean in function " + funcName);
                            return false;
                        }
                        else
                            if(term.jjtGetNumChildren() > 0)
                            {
                                String type =  analyseFollowUpTerm(term.jjtGetChild(0), funcName, term);

                                return type.equals("boolean") || type.equals("all");
                            }
                            else
                                return false;
                    }
            }
        }
        else
        {
            if(term.jjtGetNumChildren() != 0)
            {
                switch(term.jjtGetChild(0).toString())
                {
                    case "ENCLOSED_EXPR":
                        return evaluatesToBoolean(term.jjtGetChild(0), funcName);

                    case "NEW": //Mudar para ver casos new Class().someFunc()
                        String type = analyseFollowUpTerm(term.jjtGetChild(0), funcName, term);

                        if(type.equals("boolean") || type.equals("all"))
                            return true;
                        else
                        {
                            System.out.println("New token doesn't evaluate to boolean in function " + funcName);
                            return false;
                        }
                        
                    default:
                        System.out.println("Unexpected token in boolean term analysis: " + term.jjtGetChild(0).toString() + " in function " + funcName);
                        return false;
                }
            }
            else
            {
                System.out.println("Empty expression is not a boolean in function " + funcName);
                return false;
            }
        }
    }

    public static boolean intTerm(Node term, String funcName)
    {
        return true;
    }

    public static String analyseFollowUpTerm(Node followUpTerm, String funcName, Node parentNode)
    {
        switch(followUpTerm.toString())
        {
            case "ArrayAccs":
                return "int";

            case "Member":
                return analyseFunctionCall(followUpTerm, funcName, parentNode);

            default:
                System.out.println("Unexpected follow up term " + followUpTerm.toString() + " in function " + funcName);
                return "error";
        }
    }

    public static String analyseFunctionCall(Node member, String funcName, Node parentNode)
    {
        boolean ownFunc = isOwnClassVar(parentNode, funcName);
        SymbolTable funcST = symbolTables.get(member.getName());
        
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
        boolean isOfSameType;

        if(argProtos.length != member.jjtGetNumChildren())
        {
            System.out.println("Number of arguments mismatch in member call in fucntion " + funcName);
            return "error";
        }

        for(int i = 0; i < argProtos.length && i <  member.jjtGetNumChildren(); i++)
        {
            switch(argProtos[i])
            {
                case "int":
                    isOfSameType = evaluatesToInt(member.jjtGetChild(i), funcName);
                    break;

                case "int[]":
                    isOfSameType = evaluatesToArray(member.jjtGetChild(i), funcName);
                    break;

                case "boolean":
                    isOfSameType = evaluatesToBoolean(member.jjtGetChild(i), funcName);
                    break;

                default: //Class
                    isOfSameType = evaluatesToClass(member.jjtGetChild(i), funcName, argProtos[i]);
            }

            if(!isOfSameType)
            {
                System.out.println("Call to function " + member.getName() + " in function " + funcName 
                + " doesn't match function prototype");
                return "error";
            }
        }
        
        return funcST.getReturnType();
    }

    //Returns the type of variable
    public static String analyseIdentifier(Node identifier, String funcName, boolean mustBeInit)
    {
        SymbolTable funcST = symbolTables.get(funcName);
        Symbol variable;

        if(funcST != null)
        {
            variable = funcST.getTable().get(identifier.getName());

            if(variable == null)
            {
                variable = funcST.getArgs().get(identifier.getName());

                if(variable == null)
                {
                    SymbolTable classST = symbolTables.get(className);

                    if(classST != null)
                    {
                        variable = classST.getTable().get(identifier.getName());
                    }
                    else
                    {
                        System.out.println("Couldn't find class " + className + " symbol table in funtion" + funcName);
                        return "error";
                    }
                }
            }

            if(variable != null)
            {
                if(mustBeInit && !variable.getInit())
                {
                    System.out.println("Variable " + variable.getName() + " was not initialized in function " + funcName);
                    return "error";
                }
                else
                {
                    if(identifier.jjtGetNumChildren() == 1)
                    {
                        switch(identifier.jjtGetChild(0).toString())
                        {
                            case "Member":
                                return analyseFunctionCall(identifier.jjtGetChild(0), funcName, identifier); 

                            case "ArrayAccs":
                                if(analyseArrayAccs(identifier.jjtGetChild(0), funcName))
                                    return "int";
                                else
                                {
                                    System.out.println("Array access expression doesn't equal to integer value in function" + funcName);
                                    return "error";
                                }
                                    
                            default:
                                System.out.println("Unexpected Term child " + identifier.jjtGetChild(0) + " in function " + funcName);
                                return "error";
                        }  
                    }
                    else
                        return variable.getType();
                }
            }
            else
            {
                
                System.out.println("Couldn't find variable " + identifier.getName() + " in function " + funcName);
                return "error";
            }
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

        return evaluatesToInt(arrayAcs.jjtGetChild(0), funcName);
    }

    public static boolean isOwnClassVar(Node term, String funcName)
    {
        if(term.getName() != null)
        {
            if(term.getName().equals("this"))
                return true;
            else
            {
                String type = analyseIdentifier(term, funcName, true);

                return type.equals(className) || type.equals("all");
            }
        }
        else
            return false;
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
                                builtSymbolTable = buildLocalSymbolTable(child, classNode, false);
                                break;

                            case "Main":
                                builtSymbolTable = buildMainSymbolTable(child);
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

    public static boolean buildLocalSymbolTable(Node var, Node parentNode, boolean main)
    {
        SymbolTable classST; 
        
        if(!main)
            classST = symbolTables.get(parentNode.getName());
        else
            classST = symbolTables.get("main");
            
        Symbol newSymbol;

        newSymbol = new Symbol(var.getName(), var.getType());

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

        for(int i = 0; i < func.jjtGetNumChildren(); i++)
        {
            if(func.jjtGetChild(i).toString().equals("Arg") || func.jjtGetChild(i).toString().equals("Var"))
                if(!buildLocalSymbolTable(func.jjtGetChild(i), func, false))
                    return false;
        }

        return true;
    }

    public static boolean buildMainSymbolTable(Node main)
    {
        SymbolTable funcTable = symbolTables.get("main");

        if(funcTable != null)
            return false;
        else
            funcTable = new SymbolTable();

        funcTable.setReturnType("void");
        funcTable.putArg(new Symbol(main.getName(), "String[]")); //Name in main = argument identifier
        symbolTables.put("main", funcTable);

        for(int i = 0; i < main.jjtGetNumChildren(); i++)
        {
            if(main.jjtGetChild(i).toString().equals("Var"))
                if(!buildLocalSymbolTable(main.jjtGetChild(i), main, true))
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