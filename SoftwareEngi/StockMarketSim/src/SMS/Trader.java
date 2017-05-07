/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SMS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 *
 * @author Tom
 */
public class Trader {
    private List<Client> clientList;
    private MODE tradingMode;
    public enum MODE{
        PURCHASER, BALANCED,SELLER 
    }
    public Trader(List<Client> clientList, MODE tradingMode){
        this.clientList = clientList;
        this.tradingMode = tradingMode;
    }
    /**
     * 
     * @return 
     */
    public List<Request> makeSellReqest(){
        List<Request> sellRequests = new ArrayList();
        for (Client client :clientList){
            sellRequests.addAll(sellOffer(client));
        }
        return sellRequests;
    }
    public void newMode(){
        Random rn = new Random();
        float randn = rn.nextFloat();
        switch (tradingMode) {
            case SELLER:{
                if(randn<0.6){
                    tradingMode = MODE.BALANCED;
                }
            };
            case PURCHASER:{
                if(randn<0.7){
                    tradingMode = MODE.BALANCED;
                }
            };
            case BALANCED:{
                if(randn<0.1){
                    tradingMode = MODE.PURCHASER;
                }else if(randn>0.9){
                    tradingMode = MODE.SELLER;
                }
            }; 
        }
    }
    /**
     * 
     * @param companyList a list of the current Companies on the market
     * @return Returns a list of buying request from all the clients of this trader 
     */
    public List<Request> makeBuyRequest(List<Company> companyList){
        List<Request> buyRequests = new ArrayList();
        for (Client client :clientList){
            buyRequests.addAll(buyOffer(client,companyList));
        }
        return buyRequests;
    }
    public void processOffer(StockOffer stockOffer){
        int clientPos = clientList.indexOf(stockOffer.getClient());
        clientList.get(clientPos).processOffer(stockOffer);
    }
    private List<Request> sellOffer(Client client){
        Random rn = new Random();
        switch (tradingMode) {
            case SELLER:
                return sell(client, (float) 0.02*rn.nextFloat());
            case PURCHASER:
                return sell(client, (float)0.005*rn.nextFloat());
            case BALANCED:
                return sell(client, (float)0.01*rn.nextFloat());  
            default:
                return null;
        }
    }
    private List<Request> buyOffer(Client client, List<Company> companyList){
        Random rn = new Random();
        switch (tradingMode) {
            case SELLER:
                return buy(client, (float)0.005*rn.nextFloat(),companyList);
            case PURCHASER:
                return buy(client, (float)0.02*rn.nextFloat(),companyList);
            case BALANCED:
                return buy(client, (float)0.01*rn.nextFloat(),companyList);  
            default:
                return null;
        }
    }
    private List<Request> sell(Client client, float pAssets){
        Random rn = new Random();
        float sellAmount = client.getAssetValue()*pAssets;
        boolean notMetSellAmount = false;
        List<Integer> bagOIndex = genBagOIndex(client.getShareBundles().size());
        List<Request> sr = new ArrayList(); 
        while ((notMetSellAmount)||(bagOIndex.isEmpty())){
            int randn = bagOIndex.remove((int)(rn.nextFloat()*bagOIndex.size())); 
            ShareBundle sb = client.getShareBundles().get(randn);
            if (sb.getAssetValue() > sellAmount){
                sr.add(new Request(sb.getCompany(), (int)(sellAmount/sb.getCompany().getSharePrice()), client,  this,Request.TYPE.SELL));
                notMetSellAmount = true;
            }else{
                sr.add(new Request(sb.getCompany(), sb.getShares(), client, this,Request.TYPE.SELL));
                sellAmount -= sb.getAssetValue();
            }
        }
        return sr;
    }
    private List<Request> buy(Client client, float pAssets,List<Company> companyList){
        Random rn = new Random();
        float spendAmount = (client.getCash())*pAssets;
        List<Request> br = new ArrayList(); 
        List<Float> proportion = new ArrayList();
        float total = 0;
        for(int i = 0; i < companyList.size();i++){
            float randn = rn.nextFloat();
            proportion.add(i, randn);
            total += randn;
        }
        for(int i = 0; i < companyList.size();i++){
            float floaty = (spendAmount*(proportion.get(i)/total))/companyList.get(i).getSharePrice();
            int shareAmount = (int)floaty;
            br.add(new Request(companyList.get(i), shareAmount, client, this, Request.TYPE.BUY));
        }
        return br;
    }
    private List<Integer> genBagOIndex(int size){
        List<Integer> bag = new ArrayList();
        for (int i = 0; i<size;i++){
            bag.add(i);
        }
        return bag;
    }

    public List<String> getClientNames() {
        List<String> names = new ArrayList<>();
        for (Client client : clientList) {
            names.add(client.getName());
        }
        return names;
    }
}
