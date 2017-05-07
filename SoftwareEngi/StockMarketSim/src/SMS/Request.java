package SMS;

/**
 *
 * @author Tom
 */
public class Request {
    private final Company company;
    private final Integer amount;//number of shares
    private final Client owner;
    private final Trader trader;
    private TYPE type;
    public enum TYPE{
        BUY, SELL
    }/**
     * 
     * @param company   the company that owns the shares in question
     * @param amount    the amount of shares requested
     * @param client    the client that the trader is request on behalf of
     * @param trader    the trader that made the request
     * @param type  the type of request. (Buy|Sell)
     */
    public Request(Company company, int amount, Client client, Trader trader, TYPE type){
        this.company = company;
        this.amount = amount;
        this.owner = client;
        this.trader = trader;    
        this.type = type;
    }
    public Company getCompany() {
        return company;
    }
    public int getAmount() {
        return amount;
    } 

    public Client getOwner() {
        return owner;
    }

    public Trader getTrader() {
        return trader;
    }
}
