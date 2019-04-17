package compiler;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * DISCLAIMER: As funçoes desta classe vão ser passadas posteriormente para a JavaMMMain, mas para não originar conflitos
 * está aqui por agora.
 */
public class JasminMain
{
  public static PrintWriter pw;

  public boolean getFile()
  {
    try
    {
      String filename = "jasmin";
      File file = new File(filename);
  
      if(!file.exists())
      {
        file.mkdirs();
      }
  
      File jasmin_file = new File(filename + "moduleName" + ".j");
  
      pw = new PrintWriter(jasmin_file);
  
      return true;
    }
    catch(IOException e)
    {
      System.err.println("File exception: " + e.toString());
      e.printStackTrace();
      return false;
    }
  
  }
}

