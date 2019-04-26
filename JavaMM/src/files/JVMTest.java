package files;

class JVMTest 
{
    int x;
    boolean b;
    public static void main(String args[]) 
    {
        int v1 = 1, v2 = 2;
        boolean v3;

        v3 = v1 < v2;

    }

    public boolean someFunc(int arg1)
    {
        JVMTest t = new JVMTest();

        t.someOtherFunc();

        return b;
    }

    public int someOtherFunc()
    {
        return 1;
    }
}