package com.harlan.smonitor.monitor.common;

import java.io.*;

public class EncryptUtil
{
  private static final String READ_FILE = Constants.ECHN_TOOL_HOME;
  private static final String WRITE_FILE = Constants.ECHN_TOOL_HOME;

  public static void encryptFile(String fileName) {
    BufferedReader reader = null;
    PrintWriter out = null;
    try {
      File file = new File(READ_FILE + fileName);
      if (file.canRead()) {
        DESDecryptNew des = new DESDecryptNew();
        StringBuilder content = new StringBuilder();
        reader = new BufferedReader(new FileReader(file));
        String tempString = null;
        out = new PrintWriter(
          new OutputStreamWriter(new FileOutputStream(WRITE_FILE + fileName + "_e", false), "GBK"));

        while ((tempString = reader.readLine()) != null)
          if (!"".equals(tempString.trim())) {
            String encryptText = des.encrypt(tempString.trim());
            content.append(encryptText);
            content.append("\n");
          }
        out.write(content.toString());
        out.flush();
        out.close();
        reader.close();
      }
    } catch (Exception e) {
      e.printStackTrace();

      if (reader != null)
        try {
          out.close();
          reader.close();
        }
        catch (Exception localException1) {
        }
      if (reader != null)
        try {
          out.close();
          reader.close();
        }
        catch (Exception localException2)
        {
        }
    }
    finally
    {
      if (reader != null)
        try {
          out.close();
          reader.close();
        } catch (Exception localException3) {
        }
    }
  }
  
  public static void main(String[] args) {
		if (args.length == 1) {
			encryptFile(args[0]);
		} else if (args.length > 1) {
			System.err.println("only need a file name");
		} else {
			System.err.println("need a file name");
		}
	}
  
  private EncryptUtil(){}
}