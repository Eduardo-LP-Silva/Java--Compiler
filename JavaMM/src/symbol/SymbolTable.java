package symbol;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.Set;
import ast.*;

public class SymbolTable
{
    private Hashtable<String, Symbol> table;
    private LinkedHashMap<String, Symbol> args;
    private String returnType;

    public SymbolTable(Hashtable<String, Symbol> table, LinkedHashMap<String, Symbol> args)
    {
        this.table = table;
        this.args = args;
    }   

    public SymbolTable()
    {
        this.table = new Hashtable<String, Symbol>();
        this.args = new LinkedHashMap<String, Symbol>();
    }

    public boolean putSymbol(Symbol symbol)
    {
        String key = symbol.getName();

        if(table.get(key) == null)
        {
            table.put(key, symbol);
            return true;
        }
        else
            return false;
            
    }

    public boolean putArg(Symbol symbol)
    {
        String key = symbol.getName();

        if(args.get(key) == null)
        {
            args.put(key, symbol);
            return true;
        }
        else
            return false;
    }

    public boolean checkArgs(Node member)
    {
        return true;
    }

    public void printTable()
    {
        Set<String> tableKeys;

        System.out.println("\n\tLocal Variables\n");

        tableKeys = table.keySet();

        for(String tableKey: tableKeys)
            System.out.println("* " + table.get(tableKey).getType() + " " + table.get(tableKey).getName());
    }

    public void printArgs()
    {
        Set<Entry<String, Symbol>> argsSet;
        Iterator<Entry<String, Symbol>> argsIterator;
        Entry<String, Symbol> arg;


        System.out.println("\tArguments\n");

        argsSet = args.entrySet();
        argsIterator = argsSet.iterator();

        while(argsIterator.hasNext())
        {
            arg = argsIterator.next();
            System.out.println("* " + arg.getValue().getType() + " " + arg.getValue().getName());
        }
            
    }

    public Hashtable<String, Symbol> getTable()
    {
        return table;
    }

    public LinkedHashMap<String, Symbol> getArgs()
    {
        return args;
    }

    public void setReturnType(String returnType)
    {
        this.returnType = returnType;
    }

}