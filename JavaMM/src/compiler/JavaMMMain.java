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

    public static Boolean semanticAnalysis(SimpleNode root)
    {
        Boolean continueAnalysis;
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

    public static Boolean analyseFunction(Node func)
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

    public static Boolean analyseStatement(Node statement, String funcName)
    {
        Boolean continueAnalysis;
        
        switch (statement.toString()) 
        {
            case "If":
                continueAnalysis = analyseIf(statement, funcName);
                break;

            case "While":
                continueAnalysis = analyseWhile(statement, funcName);
                break;

            case "TERM": // Verify standalone terms

                break;

            case "EQUALS":

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

        /*
         * if(!continueAnalysis) return false;
         */
        

        return true;
    }

    public static Boolean analyseIf(Node ifNode, String funcName)
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
            
        return true;
    }

    public static Boolean analyseWhile(Node whileNode, String funcName)
    {
        return true;
    }

    public static Boolean analyseStandAloneTerm(Node term, String funcName)
    {
        return true;
    }

    public static Boolean evaluatesToBoolean(Node expression, String funcName)
    {
        switch(expression.toString())
        {
            case "AND":
                return expression.jjtGetNumChildren() == 2 
                && evaluatesToBoolean(expression.jjtGetChild(0), funcName) 
                && evaluatesToBoolean(expression.jjtGetChild(1), funcName);

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

    public static Boolean evaluatesToInt(Node expression, String funcName)
    {
        return true;
    }

    public static Boolean booleanTerm(Node term, String funcName)
    {
        if(term.getName() != null)
        {
            switch(term.getName())
            {

            }
        }
        else
        {

        }
        
        return true;
    }

    public static Boolean intTerm(Node term, String funcName)
    {
        return true;
    }

    public static Boolean buildSymbolTables(SimpleNode root)
    {
        Node classNode;

        if(root.toString().equals("Program"))
        {
            if(root.jjtGetNumChildren() != 0 && (classNode = root.jjtGetChild(0)).toString().equals("Class"))
            {
                if(classNode.toString().equals("Class"))
                {
                    Boolean builtSymbolTable;

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

        if (!classST.putSymbol(newSymbol))
        {
            System.out.println("Duplicate local variable " + var.getName() + " of type " + var.getType());
            return false;
        }
        else
        {
            symbolTables.put(parentNode.getName(), classST);
            return true;
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
        funcTable.putSymbol(new Symbol(main.getName(), "String[]")); //Name in main = argument identifier
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
        Set<String> tableKeys;

        for(String key: keys)
        {
            System.out.println("--- " + key + " symbol table ---\n");

            table = symbolTables.get(key);
            tableKeys = table.getTable().keySet();

            for(String tableKey: tableKeys)
                System.out.println(table.getTable().get(tableKey).getType() + " " + table.getTable().get(tableKey).getName());

            System.out.println("\n");
        }
    }
}