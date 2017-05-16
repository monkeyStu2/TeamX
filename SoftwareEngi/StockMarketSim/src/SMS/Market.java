/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SMS;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
//import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * @author Tom H and Robert G
 */
public class Market {
    private List<Trader> traders;
    private List<Company> companies;
    private List<Company> bankruptCompanies;
    private LocalDateTime dateTime;

    public Market() {
        companies = new ArrayList<>();
        companies.add(new Company("XY Corp", 50000, 150, Company.StockType.Hitech));
        companies.add(new Company("Evil Quack", 60000, 155, Company.StockType.Food));
        companies.add(new Company("Mac Burger", 55000, 140, Company.StockType.Hard));
        companies.add(new Company("Anti Machine Learning", 40000, 146, Company.StockType.Property));
        traders = new ArrayList<>();
        List<ShareBundle> sb1 = new ArrayList<>();
        sb1.add(new ShareBundle(companies.get(0), 20000));
        sb1.add(new ShareBundle(companies.get(1), 40000));
        sb1.add(new ShareBundle(companies.get(2), 25000));
        sb1.add(new ShareBundle(companies.get(3), 25000));
        List<ShareBundle> sb2 = new ArrayList<>();
        sb2.add(new ShareBundle(companies.get(0), 30000));
        sb2.add(new ShareBundle(companies.get(1), 20000));
        sb2.add(new ShareBundle(companies.get(2), 30000));
        sb2.add(new ShareBundle(companies.get(3), 15000));
        List<Client> clients1 = new ArrayList<>();
        clients1.add(new Client("Johnny Rock", 25000, sb1 ));
        traders.add(new Trader(clients1, Trader.MODE.BALANCED));

        List<Client> clients2 = new ArrayList<>();
        clients2.add(new Client("Dr Robotnik", 45000, sb2 ));
        traders.add(new Trader(clients2, Trader.MODE.BALANCED));

        bankruptCompanies = new ArrayList<>();
        dateTime = LocalDateTime.of(2017, 1, 2, 9, 0);
    }


    public Market( String initialData ) {}
    public Market( String initialData, String eventData ){}

    public void cycle() {
        List<Request> srList = getSellRequests(traders);
        List<Request> brList = getBuyRequests(traders);

        System.out.println("Sell List: " + srList);
        for (int i = 0; i < srList.size(); i++) {
            System.out.println(srList.get(i).getAmount());
            System.out.println(srList.get(i).getCompany().getName());
            System.out.println(srList.get(i).getOwner().getName());
            System.out.println(srList.get(i).getTrader());
            System.out.println(srList.get(i).getType());
            System.out.println();
        }
        System.out.println("Buy List: ");
        for (int i = 0; i < brList.size(); i++) {
            System.out.println(brList.get(i).getAmount());
            System.out.println(brList.get(i).getCompany().getName());
            System.out.println(brList.get(i).getOwner().getName());
            System.out.println(brList.get(i).getTrader());
            System.out.println(brList.get(i).getType());
            System.out.println();
        }

        updateMarket(srList, brList);
        updateTime();
    }

    private void updateMarket(List<Request> srList, List<Request> brList) {
        List<Integer> supply = sumRequests(srList);
        List<Integer> demand = sumRequests(brList);
        for (int i = 0; i < companies.size(); i++) {
            System.out.println("Test 0");
            System.out.println(supply + " " + demand);
            if (supply.get(i) > demand.get(i)) {
                System.out.println("Test 1");
                fufillRequests(brList);
                System.out.println("Test 2");
                for (Request request : srList) {
                    for (Trader trader : traders) {
                        if (trader == request.getTrader()) {
                            int amount = (int) (demand.get(i) * request.getAmount()) / supply.get(i);
                            trader.processOffer(new StockOffer(request, amount));
                            System.out.println("Test 3");
                        }
                    }
                }
            } else if (supply.get(i) < demand.get(i)) {
                System.out.println("Test 4");
                fufillRequests(srList);
                for (Request request : brList) {
                    for (Trader trader : traders) {
                        if (trader == request.getTrader()) {
                            int amount = (int) (supply.get(i) * request.getAmount()) / supply.get(i);
                            trader.processOffer(new StockOffer(request, amount));
                            System.out.println("Test 5");
                        }
                    }
                }
            } else {
                System.out.println("Test 6");
                fufillRequests(srList);
                fufillRequests(brList);
            }
        }
        System.out.println("Test 7");
        List<Integer> svd = new ArrayList(demand.size());//supply vs demand
        for (int i = 0; i < demand.size(); i++) {
            svd.add(i, (demand.get(i) - supply.get(i)));
        }
        for (int i = 0; i < svd.size(); i++) {
            companies.get(i).pIncrease(svd.get(i) / companies.get(i).getShares());
        }
        for (int i = 0; i < companies.size(); i++) {
            if (companies.get(i).getSharePrice() <= 0) {
                bankruptCompanies.add(companies.remove(i));
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
        for (Request req : request) {
            accumulatedRequest.add(companies.indexOf(req.getCompany()), (Integer) req.getAmount());
        }
        return accumulatedRequest;
    }

    public void updateTime() {
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
        System.out.println(dateTime);
    }
}
