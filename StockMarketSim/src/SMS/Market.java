///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package SMS;
//
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;
//
///**
// *
// * @author Tom
// */
//public class Market {
//    List<Trader> traders;
//    List<Company> companies;
//    LocalTime time;
//    LocalDate date;
//
//    public Market( POIFSFileSystem initialData ) throws FileNotFoundException{
//        String fileName = "InitialData.xlsx";
//        try{InputStream stream = new FileInputStream(fileName);}
//        catch(FileNotFoundException e){
//            System.err.println("File "+fileName+" not found.");
//        }
//    }
//    //public Market( POIFSFileSystem initialData, POIFSFileSystem eventData ){}
//    public void cycle(){
//
//    }
//    public void updateMarket(){}
//    public void sellRequest(){}
//    public void buyRequest(){}
//    public void updateTime(){}
//}
