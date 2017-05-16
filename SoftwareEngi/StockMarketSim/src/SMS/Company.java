package SMS;
/**
 *@author Tom
 * 
 */
public class Company {
    String name;
    int shares;
    float sharePrice;
    StockType stockType;
    public enum StockType{
        Food, Hard, Hitech, Property
    }
    /**
     * Creates Company object
     * @param shares        the amount of shares issued by the company
     * @param sharePrice    the value of each share
     * @param stockType     the category of stock
     */
    public Company(String name, int shares, float sharePrice,StockType stockType){
        this.name = name;
        this.shares = shares;
        this.sharePrice = sharePrice;
        this.stockType = stockType;
    }
    public int getShares(){
        return shares;
    }
    
    public float getSharePrice(){
        return sharePrice;
    }
    
    public float getCapitalisation(){
        return shares*sharePrice;
    }

    public StockType getStockType() {
        return stockType;
    }

    public void pIncrease(float p){
        this.sharePrice += this.sharePrice*p;
    }

    public String getName(){
        return name;
    }
}
