package symbol;

public class Symbol
{
    private String name;
    private String type;
    private Boolean init = false;
    private String value;

    public Symbol(String name, String type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
    }

    public Boolean getInit()
    {
        return init;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public void setInit(boolean init)
    {
        this.init = init;
    }
}