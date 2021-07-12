package com.example.app16.utils;

import android.content.Context;
import android.app.Activity;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.HashMap; 
import java.util.StringTokenizer;

import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;

public class FileAccessor
{ Context myContext;

  public FileAccessor(Context context)
  { myContext = context; }

  public void createFile(String filename)
  { try 
    { File newFile = new File(myContext.getFilesDir(), filename); }
    catch (Exception _e) { _e.printStackTrace(); }
  }

   public ArrayList<String> readFile(String filename)
   { ArrayList<String> result = new ArrayList<String>();

     try {
           InputStream inStrm = myContext.openFileInput(filename);
           if (inStrm != null) {
               InputStreamReader inStrmRdr = new InputStreamReader(inStrm);
               BufferedReader buffRdr = new BufferedReader(inStrmRdr);
               String fileContent;

               while ((fileContent = buffRdr.readLine()) != null)
               { result.add(fileContent); }
               inStrm.close();
           }
       } catch (Exception _e) { _e.printStackTrace(); }
     return result;
   }

   public void writeFile(String filename, ArrayList<String> contents)
   { try {
       OutputStreamWriter outStrm =
               new OutputStreamWriter(myContext.openFileOutput(filename, Context.MODE_PRIVATE));
       try {
         for (int i = 0; i < contents.size(); i++)
         { outStrm.write(contents.get(i) + "\n"); }
       }
       catch (IOException _ix) { }
       outStrm.close();
     }
     catch (Exception e) { e.printStackTrace(); }
   }

}
