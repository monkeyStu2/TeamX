package SMS;

/**
 * @author Tom Hui
 */
public class StockOffer {
    private final Company company;
    private final Integer amount;//number of shares
    private final Client client;
    private final TYPE type;
    private Request request;
    public enum TYPE{
        BUY, SELL
    }
    /**
     * This class represents the offer made by the market 
     * @param request   this is the request this offer is repsonding to
     * @param amount    the amount of shares offered
     */
    public StockOffer(Request request,int amount){
        this.request = request;
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

    public Request getRequest(){return request;}
    
}
