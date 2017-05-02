package SMS;

import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

public class MarketTest {

    private TimeSeriesCollection dataset;
    private LocalTime time;
    private LocalDate date;

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
            System.out.println(ldt);
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
