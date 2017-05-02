package SMS;

/**
 *
 * @author Tom
 */
public class ShareBundle {
    private final Company company;
    private final int shares;
    public ShareBundle(Company company, int shares){
        this.company = company;
        this.shares = shares;
    }
    public float getAssetValue(){
        return company.sharePrice * shares; 
    }
    public Company getCompany() {
        return company;
    }
    public int getShares() {
        return shares;
    }
}
