package SMS;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tom
 */
public class Client {
    private String name;
    private List<ShareBundle> shareBundles = new ArrayList();
    private float cash;
    /**
     * Represents a client
     * @param cash      in pennies
     */
    public Client(String name, float cash, List<ShareBundle> shareBundles){
        this.name = name;
        this.shareBundles = shareBundles;
        this.cash = cash;
    }
    public float getAssetValue(){
        float total = 0;
        for (ShareBundle bundle:shareBundles){
            total += bundle.getAssetValue();
        }
        return total;
    }
    
    public void processOffer(StockOffer offer){
        for (int i = 0; i < shareBundles.size();i++){
            if(offer.getCompany() == shareBundles.get(i).getCompany()){
                if (offer.getType() == StockOffer.TYPE.BUY){
                    shareBundles.get(i).addShares(offer.getAmount());
                    cash -= offer.getAmount()*offer.getCompany().getSharePrice();
                }
                if(offer.getType() == StockOffer.TYPE.SELL){
                    shareBundles.get(i).addShares(-offer.getAmount());
                    cash += offer.getAmount()*offer.getCompany().getSharePrice();
                }
            }
        }
    }

    public List<ShareBundle> getShareBundles() {
        return shareBundles;
    }

    public float getCash() {
        return cash;
    }

    public String getName() {
        return name;
    }
    
}
