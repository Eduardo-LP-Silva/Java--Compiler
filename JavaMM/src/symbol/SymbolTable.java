package symbol;

import java.util.Hashtable;

public class SymbolTable
{
    private Hashtable<String, Symbol> table;
    private String returnType;

    public SymbolTable(Hashtable<String, Symbol> table)
    {
        this.table = table;
    }   

    public SymbolTable()
    {
        this.table = new Hashtable<String, Symbol>();
    }

    public boolean putSymbol(Symbol symbol)
    {
        String key = symbol.getName() + "-" + symbol.getValue();

        if(table.get(key) == null)
        {
            table.put(key, symbol);
            return true;
        }
        else
            return false;
            
    }

    public Hashtable<String, Symbol> getTable()
    {
        return table;
    }

    public void setReturnType(String returnType)
    {
        this.returnType = returnType;
    }

}