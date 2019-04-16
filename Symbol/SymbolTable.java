import java.util.Hashtable;

public class SymbolTable
{
    private Hashtable<String, Symbol> table;

    public SymBolTable(Hashtable<String, Symbol> table)
    {
        this.table = table;
    }   

    public SymbolTable()
    {
        this.table = new Hashtable<String, Symbol>();
    }


    
}