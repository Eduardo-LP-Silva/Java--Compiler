package compiler;

import ast.*;
import symbol.*;
import java.util.Hashtable;
import java.io.FileInputStream;
import java.util.Set;


class JavaMMMain
{
    private static Hashtable<String, SymbolTable> symbolTables;
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

        buildSymbolTables(root);
        printSymbolTables();

    }

    public static void buildSymbolTables(SimpleNode root)
    {
        if(root.toString().equals("Program"))
        {
            if(root.jjtGetNumChildren() != 0)
            {
                Node classNode = root.jjtGetChild(0);

                if(classNode.toString().equals("Class"))
                {
                    Boolean builtSymbolTable;

                    symbolTables.put(classNode.getName(), new SymbolTable());

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
                            return;
                    }
                        
                }
                
            }
            else
            {
                System.out.println("Program doesn't have class");
                return;
            }
            
        }
        else
            System.out.println("Root node doesn't qualify as program: " + root.toString());
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