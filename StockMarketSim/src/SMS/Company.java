package SMS;
/**
 *@author Tom
 * 
 */
public class Company {
    int shares;
    float sharePrice;
    StockType stockType;
    public enum StockType{
        FOODCOM,HARDCOM,HITECH,PROPERTY
    }
    /**
     * Creates Company object
     * @param shares        the amount of shares released by the company
     * @param sharePrice    the value of each share
     * @param stockType     the category of stock
     */
    public Company(int shares, float sharePrice,StockType stockType){
        this.shares = shares;
        this.sharePrice = sharePrice;
        this.stockType = stockType;
    }
    public int getShares(){
        return shares;
    }
    
    public int getSharePrices(){
        return shares;
    }
    
    public float getCapitalisation(){
        return shares*sharePrice;
    }

    public void setSharePrices(int sharePrice){
        this.sharePrice = sharePrice;
    }
}
