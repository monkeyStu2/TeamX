package SMS;

import java.time.*;
import java.util.*;
import java.util.List;

/**
 * @author Tom Hui and Robert Garner
 */
public class Market {
    private List<Trader> traders;
    private Trader wAndGTrader;
    private List<Company> companies;
    private List<Company> bankruptCompanies;
    private LocalTime time;
    private LocalDate date;

    public Market(String file) {
        time = LocalTime.of(9, 0);
        date = LocalDate.of(2017, 1, 2);
        Object[] o = ExcelReader.readInitialData(file); // extract data from initialData.xlsx
        // Object[] contain a map of clients and their shares, and list of companies
        // clientsInfo contain a name of client, their shares and cash holding
        // shares and cash holding are stored in same list, cash holding are the last element in the list
        HashMap<String, ArrayList<Integer>> clientsInfo = (HashMap<String, ArrayList<Integer>>) o[0];
        companies = (ArrayList<Company>) o[1];
        bankruptCompanies = new ArrayList<>();
        ArrayList<Client> wAndGClients = new ArrayList<>();
        traders = new ArrayList<>();

        // iterate each client and add them to a trader
        for(Map.Entry<String, ArrayList<Integer>> entry : clientsInfo.entrySet()) {
            String key = entry.getKey(); // client name
            ArrayList<Integer> value = entry.getValue(); // client shares and cash holding

            ArrayList<ShareBundle> sbList = new ArrayList<>();
            int i = 0;
            for (Company company : companies) {
                sbList.add(new ShareBundle(company, value.get(i))); // create a share for a client shares list
                i++;
            }
            // our trader
            switch (key) {
                case "Justine Thyme":
                    wAndGClients.add(new Client(key, value.get(value.size() - 1), sbList));
                    break;
                case "Norbert DaVinci":
                    wAndGClients.add(new Client(key, value.get(value.size() - 1), sbList));
                    // random trader
                    break;
                default:
                    List<Client> c = new ArrayList<>();
                    c.add(new Client(key, value.get(value.size() - 1), sbList));
                    traders.add(new Trader(c, Trader.MODE.BALANCED));
                    break;
            }
        }
        wAndGTrader = new Trader(wAndGClients, Trader.MODE.BALANCED);
        traders.add(wAndGTrader);
    }

    public Market( String initialData, String eventData ){}

    /**for testing purpose*/
    public void cycle(String s) {
        Random r = new Random();
        for (Trader trader : traders) {
            for (Client client : trader.getClients()) {
                List<ShareBundle> sb = client.getShareBundles();
                sb.get(r.nextInt(sb.size()-1)).addShares((r.nextInt(20)-10));
            }
        }
        for (Company company : companies) {
            company.setSharePrice(company.getSharePrice()+r.nextFloat()*20-10);
        }
        updateTime();
    }

    /**actual simulation*/
    public void cycle() {
        List<Request> sr = getSellRequests(traders);
        List<Request> br = getBuyRequests(traders);
        updateMarket(sr, br);
        updateTime();
    }

    private void updateMarket(List<Request> srList, List<Request> brList) {
        List<Integer> supply = sumRequests(srList);
        List<Integer> demand = sumRequests(brList);
        for (int i = 0; i < companies.size(); i++) {
            if (supply.get(i) > demand.get(i)) {
                if(demand.get(i) != 0) {
                    fufillRequests(brList);
                    float checkSell = 0;
                    for (Request sRequest : srList) {
                        if (companies.get(i) == sRequest.getCompany()) {
                            int amount = Math.round((demand.get(i) * sRequest.getAmount()) / supply.get(i));
                            Trader trader = sRequest.getTrader();
                            trader.processOffer(new StockOffer(sRequest, amount));
                            checkSell += amount;
                        }
                    }
                }
            } else if (supply.get(i) < demand.get(i)) {
                if (supply.get(i) != 0) {
                    fufillRequests(srList);
                    float checkBuy = 0;
                    for (Request bRequest : brList) {
                        if (companies.get(i) == bRequest.getCompany()) {
                            int amount = Math.round((supply.get(i) * bRequest.getAmount()) / supply.get(i));
                            Trader trader = bRequest.getTrader();
                            trader.processOffer(new StockOffer(bRequest, amount));
                            checkBuy += amount;
                        }
                    }
                }
            } else {
                fufillRequests(srList);
                fufillRequests(brList);
            }
        }
        List<Integer> svd = new ArrayList(companies.size());//supply vs demand
        for (int i = 0; i < companies.size(); i++) {
            svd.add(i, 0);
        }
        for (int i = 0; i < svd.size(); i++) {
            svd.set(i, (demand.get(i) - supply.get(i)));
        }
        for (int i = 0; i < svd.size(); i++) {
            float s = svd.get(i);
            float cs = companies.get(i).getShares();
            float pIncrease = (s/cs);
            companies.get(i).pIncrease(pIncrease);
        }
        for (Iterator<Company> it = companies.iterator(); it.hasNext(); ) {
            Company c = it.next();
            if (c.getSharePrice() <= 0) {
                bankruptCompanies.add(c);
                it.remove();
            }
        }
    }

    private void fufillRequests(List<Request> rList) {
        for (Request request : rList) {
            for (Trader trader : traders) {
                if (trader == request.getTrader()) {
                    trader.processOffer(new StockOffer(request, request.getAmount()));
                }

            }
        }
    }

    private List<Request> getSellRequests(List<Trader> traders) {
        List<Request> sellRequestList = new ArrayList();
        for (Trader trader : traders) {
            sellRequestList.addAll(trader.makeSellReqest());
        }
        return sellRequestList;
    }

    private List<Request> getBuyRequests(List<Trader> traders) {
        List<Request> buyRequestList = new ArrayList();
        for (Trader trader : traders) {
            buyRequestList.addAll(trader.makeBuyRequest(companies));
        }
        return buyRequestList;
    }

    private List<Integer> sumRequests(List<Request> request) {
        List<Integer> accumulatedRequest = new ArrayList(companies.size());
        for(int i = 0; i < companies.size();i++) {
            accumulatedRequest.add(i, 0);
        }
        for (Request req : request) {
            Integer amt = 0;
            int n = companies.indexOf(req.getCompany());
            if (n>0) {
                amt = accumulatedRequest.get(n);
                accumulatedRequest.set(n, amt + req.getAmount());
            }
        }
        return accumulatedRequest;
    }

    public void updateTime() {
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        if (dateTime.getHour() != 16) {
            dateTime = dateTime.plusMinutes(15);
        } else {
            LocalDate date = LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
            LocalTime time = LocalTime.of(9, 0);
            dateTime = LocalDateTime.of(date, time);
            dateTime = dateTime.plusDays(1);
            if (dateTime.getDayOfWeek() == DayOfWeek.SATURDAY) {
                dateTime = dateTime.plusDays(2);
            }

            if (dateTime.toLocalDate().isEqual(LocalDate.of(2017, 4, 14))) {
                dateTime = dateTime.plusDays(4);  // skip Good Friday and Easter Monday
            } else if (dateTime.toLocalDate().isEqual(LocalDate.of(2017, 12, 25))) {
                dateTime = dateTime.plusDays(2); // skip Christmas Day and Boxing Day
            }

        }
        date = dateTime.toLocalDate();
        time = dateTime.toLocalTime();
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public List<Trader> getTrader() {
        return traders;
    }

    public Trader getWGTrader() {
        return wAndGTrader;
    }
}
