/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SMS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 *
 * @author Tom
 */
public class StockMarketSim {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fileName = "/resource/InitialData.xlsx";
        try{InputStream stream = new FileInputStream(fileName);}
        catch(FileNotFoundException e){
            System.err.println("File "+fileName+" not found.");
        }
    }
    
}
