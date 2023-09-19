/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vmeccs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 *
 * @author Дмитрий
 */
public class VmeCCS {

    private static final String filesPath = "files\\";
    private static final int BOARD_BOOTER_ELF_MAX_SIZE = 0x4000000;
    private static final int SCHAR_MAX = 127;
    private static final int UCHAR_MAX = 255;
    private static final int INT_MAX = 2147483647;
    private static final int INT_MIN = (INT_MAX + 1) * (-1);
    private static final ArrayList<Integer> byteList = new ArrayList<>();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        ArrayList<String> filesList = new ArrayList<>();
        filesList.addAll(getFilesList());
        
        boolean count = false;
        //boolean count = true;
  
        
        for (String file : filesList) {
            byteList.add(countSum(file, count));
            System.out.println(String.format("HEX = 0x%08X  %-22s", countSum(file, count), file));        
        }
        
        System.out.println(String.format("Result HEX = 0x%08X", countFinalSum(byteList)));
    }   
    
    
    private static int countSum(String fileName, boolean count) throws IOException {
        
        byte[] array = Files.readAllBytes(Paths.get(filesPath + fileName));
        
        if (array.length > BOARD_BOOTER_ELF_MAX_SIZE)
            System.out.println("File is big");      
             
        byte signed_char;
        int overall_cs = 0;
        for (byte b : array) {
            signed_char = b;
            
            if (count) {
                if (signed_char < SCHAR_MAX) {
                    signed_char = (byte) (((UCHAR_MAX+1) - signed_char)*(-1));
                    System.out.println("signed_char = " + signed_char);
                    System.out.println(String.format("current sighned_char byte HEX = 0x%08X", signed_char));
                }
                overall_cs += b;

                if (overall_cs > INT_MAX)
                    overall_cs = (byte) ((INT_MIN - 1) + (overall_cs - INT_MAX));
                else if (overall_cs < INT_MIN)
                    overall_cs = (byte) ((INT_MAX + 1) - (INT_MIN - overall_cs));
            } else {
                overall_cs += b;
            }
        }
        return overall_cs;
    }
    
    private static ArrayList<String> getFilesList() {
        ArrayList<String> filesList = new ArrayList<>();
        File folder = new File(filesPath);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
               if (file.getName().equals("obtr202.bin")) filesList.add(file.getName());
               filesList.add(file.getName());
            }
        }
        return filesList;
    }
    
    private static int countFinalSum(ArrayList<Integer> list) {
        int result = 0;
        for (int sum : list) {
            result += sum;
        }
        return result;
    }
}
