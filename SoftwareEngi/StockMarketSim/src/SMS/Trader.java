package SMS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 *
 * @author Tom Hui
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
        for (Client client : clientList){
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
            }
            case PURCHASER:{
                if(randn<0.7){
                    tradingMode = MODE.BALANCED;
                }
            }
            case BALANCED:{
                if(randn<0.1){
                    tradingMode = MODE.PURCHASER;
                }else if(randn>0.9){
                    tradingMode = MODE.SELLER;
                }
            }
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
//        System.out.println("sellamt:"+sellAmount);
//        System.out.println("client:"+client.getShareBundles().get(1).getShares());
        boolean notMetSellAmount = true;
        List<Integer> bagOIndex = genBagOIndex(client.getShareBundles().size());
        List<Request> sr = new ArrayList();
        while ((notMetSellAmount)||(!(bagOIndex.isEmpty()))){
            int randn = bagOIndex.remove((int)(rn.nextFloat()*bagOIndex.size()));
            ShareBundle sb = client.getShareBundles().get(randn);
            if (sb.getAssetValue() > sellAmount){
                sr.add(new Request(sb.getCompany(), (int)(sellAmount/sb.getCompany().getSharePrice()), client,  this,Request.TYPE.SELL));
                notMetSellAmount = false;
            }else{
                sr.add(new Request(sb.getCompany(), sb.getShares(), client, this,Request.TYPE.SELL));
                sellAmount -= sb.getAssetValue();
            }
        }
        //System.out.println("Sell request amt"+sr.get(1).getAmount());
        return sr;
    }
    private List<Request> buy(Client client, float pAssets,List<Company> companyList){
        Random rn = new Random();
        float spendAmount = client.getAssetValue()*pAssets;
        if(client.getAssetValue()>=0) {
            List<Request> br = new ArrayList();
            float randn = 0;
            List<Float> proportion = new ArrayList();
            List<Integer> companies = new ArrayList();
            float total = 0;
            for (int i = 0; i < 4; i++) {
                randn = rn.nextFloat();
                companies.add((int) (randn * companyList.size()));
                randn = rn.nextFloat();
                proportion.add(randn);
                total += randn;
            }
            for (int i = 0; i < companies.size(); i++) {
                float floaty = (spendAmount * (proportion.get(i) / total)) / companyList.get(companies.get(i)).getSharePrice();
                int shareAmount = (int) floaty;
                br.add(new Request(companyList.get(companies.get(i)), shareAmount, client, this, Request.TYPE.BUY));
            }
            return br;
        }else return null;

    }
    private List<Integer> genBagOIndex(int size){
        List<Integer> bag = new ArrayList();
        for (int i = 0; i<size;i++){
            bag.add(i);
        }
        return bag;
    }

    public List<String> getClientNames() {
        return clientList.stream().map(Client::getName).collect(Collectors.toList());
    }

    public List<Client> getClients() {
        return clientList.stream().collect(Collectors.toList());
    }
}
