package SMS;

import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

public class MarketTest {

    private List<Trader> traders;
    private Trader wAndGTrader;
    private List<Company> companies;
    private TimeSeriesCollection dataset;
    private LocalTime time;
    private LocalDate date;

    public MarketTest(String file) {
        Object[] o = ExcelReader.readInitialData(file); // extract data from initialData.xlsx
        // Object[] contain a map of clients and their shares, and list of companies
        // clientsInfo contain a name of client, their shares and cash holding
        // shares and cash holding are stored in same list, cash holding are the last element in the list
        HashMap<String, ArrayList<Integer>> clientsInfo = (HashMap<String, ArrayList<Integer>>) o[0];
        companies = (ArrayList<Company>) o[1];
        ArrayList<Client> wAndGClients = new ArrayList<>();
        traders = new ArrayList<>();

        // iterate each client and add them to a trader
        for(Map.Entry<String, ArrayList<Integer>> entry : clientsInfo.entrySet()) {
            String key = entry.getKey(); // client name
            ArrayList<Integer> value = entry.getValue(); // client shares and cash holding

            ArrayList<ShareBundle> sbList = new ArrayList<>();
            int i = 0;
            for (Company company : companies) {
                sbList.add(new ShareBundle(company, i)); // create a share for a client shares list
                i++;
            }
            // our trader
            if (key.equals("Norbert DaVinci") || key.equals("Justine Thyme")) {
                wAndGClients.add(new Client(key, value.get(value.size()-1), sbList));
            // random trader
            } else {
                List<Client> c = new ArrayList<>();
                c.add(new Client(key, value.get(value.size()-1), sbList));
                traders.add(new Trader(c, Trader.MODE.BALANCED));
            }
        }
        wAndGTrader = new Trader(wAndGClients, Trader.MODE.BALANCED);
        traders.add(wAndGTrader);
//        traders.forEach(traders -> System.out.println(traders.getClientNames()));
    }

    public MarketTest() {
        dataset = new TimeSeriesCollection();
        time = LocalTime.of(9, 0);
        date = LocalDate.of(2017, 1, 2);
        LocalDateTime ldt = LocalDateTime.of(date, time);
        Date dateTime = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        TimeSeries data = new TimeSeries("Team-X Corp");
        data.add(new Minute(dateTime), 150);
        dataset.addSeries(data);
        data = new TimeSeries("Apple Burger");
        data.add(new Minute(dateTime), 146);
        dataset.addSeries(data);
        data = new TimeSeries("Nuka-Cola");
        data.add(new Minute(dateTime), 154);
        dataset.addSeries(data);
        cycle();
    }

    public MarketTest(String initialData, String externalEventData) {
    }

    public XYDataset getDataset() {
        return dataset;
    }

    public void addData() {
        Random r = new Random();
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            TimeSeries ser = dataset.getSeries(i);
            LocalDateTime ldt = LocalDateTime.of(date, time);
            Date dateTime = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            double value = (double) ser.getValue(ser.getItemCount() - 1) + r.nextDouble() * 2 - 1;
            ser.add(new Minute(dateTime), value);
        }
        cycle();
    }

    public void cycle() {
        if (time.getHour() != 16) {
            time = time.plusMinutes(15);
        } else {
            time = LocalTime.of(9, 0);
            date = date.plusDays(1);
        }
    }
}
