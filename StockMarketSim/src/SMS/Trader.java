///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package SMS;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// * @author Tom
// */
//public class Trader {
//    List<Client> clientList;
//    MODE tradingMode;
//    public enum MODE{
//        PURCHASER, BALANCED,SELLER
//    }
//    public Trader(List<Client> clientList, Mode tradingMode){
//        this.clientList = clientList;
//        this.tradingMode = tradingMode;
//    }
//    public List<Request> makeReqest(){
//        List<SellRequest> sellRequests = new ArrayList();
//        for (Client client :clientList){
//            sellRequests.add(sellOffer(client));
//        }
//    }
//    private SellRequest sellOffer(Client client){
//        if (tradingMode == MODE.BALANCED){
//            return sell(client, 1);
//        }else if (tradingMode == MODE.SELLER){
//            return sell(client, 2);
//        }else{
//            return null;
//        }
//    }
//    private BuyRequest BuyOffer(Client client){
//        if (tradingMode == MODE.BALANCED){
//            return buy(client, 1);
//        }if (tradingMode == MODE.SELLER){
//            return buy(client, 2);
//        }else return null;
//    }
//    private SellRequest sell(Client client, int pAssets){
//        float sellAmount = client.getAssetValue()*pAssets;
//
//    }
//    private BuyRequest buy(Client client, int pAssets){
//
//    }
//
//}
