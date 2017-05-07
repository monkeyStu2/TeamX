//package SMS;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// * @author Tom
// */
//public class Client {
//    private Trader trader;
//    private List<ShareBundle> shareBundle = new ArrayList();
//    private float cash;
//    /**
//     * Represents a client
//     * @param trader    the intermediary between the market and the client
//     * @param cash      in pennies
//     */
//    public Client(Trader trader, float cash){
//        this.trader = trader;
//        this.cash = cash;
//    }
//    public float getAssetValue(){
//        float total = 0;
//        for (ShareBundle bundle:shareBundle){
//            total += bundle.getAssetValue();
//        }
//        return total + cash;
//    }
//}