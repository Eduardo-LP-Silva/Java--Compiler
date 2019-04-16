import java.util.Hashtable;

class JavaMMMain
{
    private static Hashtable<String, SymbolTable> symbolTables;
    public static void main(String[] args) throws Exception 
    {
        if (args.length < 1) {
            System.out.println("Usage: JavaMM <filename>");
            System.exit(1);
        }

        symbolTables = new Hashtable<String, SymbolTable>();

        JavaMM parser = new JavaMM(new FileInputStream(args[0]));
        SimpleNode root = parser.Program();

        buildSymbolTables(root);

    }

    public static void buildSymbolTables(SimpleNode node)
    {
        switch(node.toString())
        {
            case "Class":
                break;
            
            case "Main":
            case "Method":
                buildFunctionSymbolTable(node);
                break;

            default:
                System.out.prinln("Skipped node of type: " + node.toString());
        }
    }

    public static void buildFunctionSymbolTable(SimpleNode func)
    {

    }


}