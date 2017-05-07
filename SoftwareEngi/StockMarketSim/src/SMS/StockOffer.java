package SMS;

/**
 *
 * @author Tom
 */
public class StockOffer {
    private final Company company;
    private final Integer amount;//number of shares
    private final Client client;
    private final TYPE type;
    public enum TYPE{
        BUY, SELL
    }
    /**
     * This class represents the offer made by the market 
     * @param company   the company the stock belongs to
     * @param amount    the amount of shares offered
     * @param client    the client the trader is trading on behalf of
     * @param type      the offer type (Buy / Sell) in the Traders' perspective
     */
    public StockOffer(Company company, Integer amount, Client client, TYPE type){
        this.company = company;
        this.amount = amount;//number of shares
        this.client = client;
        this.type = type;     
    }
    public StockOffer(Request request,int amount){
        this.company = request.getCompany();
        this.amount = amount;//number of shares
        this.client = request.getOwner();
        if(request.getType() == Request.TYPE.BUY){
            this.type = StockOffer.TYPE.BUY;
        }else{
            this.type = StockOffer.TYPE.SELL;
        }
    }

    public Company getCompany() {
        return company;
    }

    public Integer getAmount() {
        return amount;
    }

    public Client getClient() {
        return client;
    }

    public TYPE getType() {
        return type;
    }
    
}
