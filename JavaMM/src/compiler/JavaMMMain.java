package compiler;

import ast.*;
import symbol.*;
import java.util.Hashtable;
import java.io.FileInputStream;
import java.util.Set;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;


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

        System.out.println("Syntax analisys done");

        if(buildSymbolTables(root))
        {
            System.out.println("Symbol tables built");
            //printSymbolTables();

            if(semanticAnalysis(root))
                System.out.println("Semantic analysis complete");
        }
            
    }

    public PrintWriter getJFile() 
    {
        try 
        {
            PrintWriter print_writer;

            String filename = "jasmin";
            File file = new File(filename);

            if(!file.exists())
                file.mkdirs();
            
            File jasmin_file = new File(filename + '/' + "moduleName" + ".j");

            if(!jasmin_file.exists()) 
                jasmin_file.createNewFile();
            
            print_writer = new PrintWriter(jasmin_file);

            return print_writer;
        } 
        catch(IOException e) 
        {
            System.err.println("File exception: " + e.toString());
            e.printStackTrace();

            return null;
        }
    }
    
    /*
     * public boolean toJVM(SimpleNode root) {
     * 
     * PrintWriter print_writer = getJFile();
     * 
     * if(root == null) return false;
     * 
     * /*if(root !instanceof SimpleNode) return null;
     * 
     * String s = ".super java/lang/Object \n";
     * 
     * print_writer.println(".class public " + this.moduleName); //substring(9) ?
     * print_writer.println(s); }
     */

  


  public void arithmeticExpressionToJVM(PrintWriter file, SymbolTable st, Node root, int loop, String op)
  {
    SimpleNode expr;

    /*
    if(expr.jjGetChild(0) instanceof SimpleNode || expr.jjGetChild(0) instanceof SimpleNode ) //ou Node ?
    {
      acessToJVM(file, stm, expr.jjGetChild(0), "Load");
      //rhsTojvm
    } */
  
    /*switch(op)
    {
      case "==":
          file.println("  if_icmpne loop" + loop + "_end" );
          break;
      case "!=":
          file.println("  if_icmpeq loop" + loop + "_end" );
          break;   
      case "<":
          file.println("  if_icmpge loop" + loop + "_end" );
          break;
      default:
          break;   
    }*/

    //functionTable.setMaxStack(2);
  
  }

    public void acessToJVM(PrintWriter file, SymbolTable st, Node n, String mode) 
    {
        SimpleNode access = (SimpleNode) n;

        /*
        int children = rightHandSide.jjtGetNumChildren();

        if (children == 0) 
        {
            if (mode.equals("Load")) 
                printFileLoadVariable(file, st, node, acess.getName());
            else if (mode.equals("Store")) 
                printFileStoreVariable(file, st, acess.getName());
            
        } */
    }

  public void functionToJVM(PrintWriter file, SymbolTable st, Node n)
  {
    //SymbolTable functionTable = this.symbolTables.get(function.name);

    //functionTable.setRegisters(function.name);

    SimpleNode function = (SimpleNode) n;
    SimpleNode args = null;

    int children = function.jjtGetNumChildren();

    for(int i = 0; i < children; i++)
    {

    }
  }


  public void rhsToJVM(PrintWriter file, SymbolTable st, Node n,  boolean isETChild)
  {
    // SimpleNode rightHandSide = (SimpleNode) n;

    // int children = rightHandSide.jjtGetNumChildren();

    // if(children == 1)
    // {
    //     if(rightHandSide.jjGetChild(0) instanceof SimpleNode) //verificar se é termo
    //     {
    //         if(!isETChild)
    //         termToJvm(file, functionTable, rhs.jjtGetChild(0), 1);
    //         else
    //         termToJvm(file, functionTable, rhs.jjtGetChild(0), 0);
    //     } 
    // }
    // else if(children == 2)
    // {
    //     SimpleNode t1 = (SimpleNode) rhs.jjtGetChild(0);
    //     SimpleNode t2 = (SimpleNode) rhs.jjtGetChild(1);

    //     termToJvm(file, st, term1, 0);
    //     termToJvm(file, st, term2, 1);
    
    //     String print;

    //     if(rhs.operator)
    //     {
    //         case "+":
    //             print = "  iadd";
    //             break;
    //         case "-":
    //             print = "  isub";
    //             break;
    //         case "*":
    //             print = "  imul";
    //             break;
    //         case "/":
    //             print = "  idiv";
    //             break;
    //         default:
    //             break;
    //     }

    //     file.println(print);
    // }

    //st.setMaxStack(2);  
  }

    public void termToJVM(PrintWriter file, SymbolTable st, Node node, int child_number) 
    {
      /*  SimpleNode term = (SimpleNode) node;

        int children = term.jjtGetNumChildren();
        String name;

        if (children == 0) 
        {
            // int
            if (term.Integer != "") 
            {
                name = term.Integer;

                //negative int
                if (term.op.equals("-")) 
                {
                    name = '-' + name;
                }

                printVariableLoad(file, functionTable, name, "Integer");
            }
        } 
        else if (children == 1) 
        {
            if (term.jjtGetChild(0) instanceof SimpleNode) 
            {
                if (child_number != 1) 
                    callToJvm(file, functionTable, term.jjGetChild(0), "int", 0);
                else 
                    callToJvm(file, functionTable, term.jjGetChild(0), "int", 1);
                
            }
        } */
    }

  public void callToJVM(PrintWriter file, SymbolTable st, Node node, String returnValue, int child_number)
  {
  
    /*
    SimpleNode call = (SimpleNode) node;
    SimpleNode argument_list;

    int call_children = call.jjtGetNumChildren();

    if(call.jjtGetNumChildren() == 0 && call.function.equals("main"))
    {
      file.println("  aconst_null");
      file.println("  invokestatic " + this.moduleName.substring(9) + "/main([Ljava/lang/String;)V");
    }
    
    
    if(call_children > 0 && call.jjGetChild(0) instanceof SimpleNode)
    {
      argument_list = call.jjGetChild(0);

      int arguments_children = argument_list.jjtGetNumChildren();

      for(int i = 0; i < arguments_children; i++)
      {

      }
    } */
  }

  public void printFileLoadVariable(PrintWriter file, SymbolTable st, String name, String type) 
  {
    /*
    int n = Integer.parseInt(name);
      
    if (type.equals("Integer")) 
    {
      if (n >= 0 && n <= 5)
        file.println("  iconst_" + n);
      else if(n >= -128 && n <= 127)
        file.println("  bipush " + n);
      else if(n >= -32768 && n <= 32767)
        file.println("  sipush " + n);
      if(n == -1)
          file.println("  iconst_m1");                
      else
        file.println("  ldc " + n);
    } 
    else if (type.equals("ID")) 
    {
      //Local Variables
      if (st != null && st.getFromAll(name) != null) 
      { 
        Symbol local_variable;
        local_variable = st.getFromAll(name);

        if(local_variable.getType().equals("int")) //ints
        { 
            if(local_variable.getRegister() >= 0 && local_variable.getRegister()<=3)
              file.println("  iload_" + local_variable.getRegister());
            else 
              file.println("  iload " + local_variable.getRegister());
        }
      } 

      //Global variable  
      else 
      {            
        Symbol global_variable;
        global_variable = symbolTables.get(this.moduleName).getFromAll(name);

        if(global_variable != null) 
        {
            String global_variable_type = global_variable.getType();
            file.println("  getstatic " + this.moduleName.substring(9) + "/" + global_variable.getName() + global_variable_type);
        }
      }
    } */
  }

  public void printFileStoreVariable(PrintWriter file, SymbolTable st, String name) 
  {
      /*
        if (functionTable != null && st.getFromAll(name) != null) 
        {
            Symbol local_variable;
            local_variable = functionTable.getFromAll(name);

            if (local_variable.getType().equals("int")) {
                if (local_variable.getRegister() >= 0 && local_variable.getRegister() <= 3)
                    file.println("  istore_" + local_variable.getRegister());
                else
                    file.println("  istore " + local_variable.getRegister());
            }
        } */
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
                        
                        return "int";
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
            newSymbol.setInit(true);
            
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
        funcTable.putArg(new Symbol(main.getType(), "String[]")); //Type in main = argument identifier
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