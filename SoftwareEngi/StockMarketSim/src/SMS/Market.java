/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SMS;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tom
 */
public class Market {
    List<Trader> traders;
    List<Company> companies;
    LocalTime time;
    LocalDate date;
    
    public Market( String initialData ) throws FileNotFoundException{
        String fileName = "InitialData.xlsx";
        try{InputStream stream = new FileInputStream(fileName);}
        catch(FileNotFoundException e){
            System.err.println("File "+fileName+" not found.");
        }
    }
    //public Market( POIFSFileSystem initialData, POIFSFileSystem eventData ){}
    public void cycle(){
        List<Request> srList = getSellRequests(traders);
        List<Request> brList = getBuyRequests(traders);
        updateMarket(srList, brList);
        List<Float> supply = sumRequests(srList);
        List<Float> demand = sumRequests(brList);
    }

    public void updateMarket(List<Request> srList,List<Request> brList){
        
    }

    private List<Request> getSellRequests(List<Trader> traders){
        List<Request> sellRequestList = new ArrayList();
        for (Trader trader:traders){
            sellRequestList.addAll(trader.makeSellReqest());
        }
        return sellRequestList;
    }
    private List<Request> getBuyRequests(List<Trader> traders){
        List<Request> buyRequestList = new ArrayList();
        for (Trader trader:traders){
            buyRequestList.addAll(trader.makeBuyRequest(companies));
        }
        return buyRequestList;
    }
    private List<Float> sumRequests(List<Request> request){
        List<Float> accumulatedRequest = new ArrayList(companies.size());
        for (Request req:request){
            accumulatedRequest.add(companies.indexOf(req),(float)req.getAmount()*req.getCompany().getSharePrices());
        }
        return accumulatedRequest;                
    }  

    public void updateTime(){}
}
