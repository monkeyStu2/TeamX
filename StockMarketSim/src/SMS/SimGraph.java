package SMS;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**Task to do here
 * 1) Scaling - make sure x-axis labels fit while zooming out/in
 * 2) Limit the graph between 9am to 4pm per day
 * 3) Disable the right-click menu (for now)*/
public class SimGraph extends JPanel {

    private XYDataset dataset;

    public SimGraph() {
        dataset = createDataset(); // default Stock Market data
        JFreeChart lineGraph = ChartFactory.createTimeSeriesChart("Shock Market", "Time", "Stock value", dataset,
                true, false, false); // create a line graph to plot the data

        //Background, border and title colour
        lineGraph.setBackgroundPaint(Color.BLACK);
        lineGraph.setBorderPaint(Color.WHITE);
        lineGraph.getTitle().setPaint(Color.WHITE);

        final XYPlot plot = lineGraph.getXYPlot();

        //Graph background, grids colour
        plot.setBackgroundPaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setDomainPannable(true); // ctrl+mouse_drag to scroll the graph horizontal

        //y-axis, stock values
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());  // auto calculate tick units
        rangeAxis.setAutoRangeIncludesZero(false); // range between min and max values, not 0 to max values
        rangeAxis.setTickLabelPaint(Color.WHITE);
        rangeAxis.setLabelPaint(Color.WHITE);

        //x-axis, time
        final DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("DD-MMM hh:mm")); // display x-axis label in this format
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 15)); // 15 minutes interval
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2017, 1, 1), LocalTime.of(8, 55)); //2017-01-01, 08:55
        Date minDateTime = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()); // convert LocalDateTime to Date
        ldt = LocalDateTime.of(LocalDate.of(2017, 1, 1), LocalTime.of(10, 05)); //2017-01-01, 10:05
        Date maxDateTime = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()); // convert LocalDateTime to Date
        dateAxis.setRange(minDateTime, maxDateTime); // initial range when launch the program
        dateAxis.setTickLabelPaint(Color.WHITE);
        dateAxis.setLabelPaint(Color.WHITE);

        //Line and Shape
        final XYLineAndShapeRenderer renderer =
                (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true); // display a shape in each point in the line

        ChartPanel cp = new ChartPanel( lineGraph ); // panel to hold line graph
        cp.setRangeZoomable(false);
        cp.setDomainZoomable(false); // disable mouse zoomable
        setLayout(new BorderLayout());
        add(cp, BorderLayout.CENTER);
        setVisible(true);
    }

    private XYDataset createDataset() {
        final TimeSeriesCollection d = new TimeSeriesCollection();
        final TimeSeries ser1 = new TimeSeries("XYZ Corp");
        final TimeSeries ser2 = new TimeSeries("Mac Apple Burger");
        final TimeSeries ser3 = new TimeSeries("Zaggo King");

        double s1 = 145;
        double s2 = 123;
        double s3 = 154;

        Random r = new Random();

        for (int j = 9; j < 16; j++) {
            for (int i = 0; i < 60; i += 15) {
                LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2017, 1, 1), LocalTime.of(j, i));
                Date dateTime = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                ser1.add(new Minute(dateTime), s1);
                ser2.add(new Minute(dateTime), s2);
                ser3.add(new Minute(dateTime), s3);
                s1 += r.nextDouble() * 2 - 1;
                s2 += r.nextDouble() * 2 - 6;
                s3 += r.nextDouble() * 2 + 1;
            }
        }

        d.addSeries(ser1);
        d.addSeries(ser2);
        d.addSeries(ser3);

        return d;
    }

    public void addData() {
        final TimeSeriesCollection d = (TimeSeriesCollection) dataset;

        final TimeSeries ser1 = new TimeSeries("Nox");
        final TimeSeries ser2 = new TimeSeries("P0tat0 Farm");
        final TimeSeries ser3 = new TimeSeries("Team-X Ltd");

        double s1 = 176;
        double s2 = 198;
        double s3 = 110;
        Random r = new Random();

        for (int j = 9; j < 16; j++) {
            for (int i = 0; i < 60; i += 15) {
                LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2017, 1, 1), LocalTime.of(j, i));
                Date dateTime = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                ser1.add(new Minute(dateTime), s1);
                ser2.add(new Minute(dateTime), s2);
                ser3.add(new Minute(dateTime), s3);
                s1 += r.nextDouble() * 2 - 1;
                s2 += r.nextDouble() * 2 + 1;
                s3 += r.nextDouble() * 2 - 4;
            }
        }

        d.addSeries(ser1);
        d.addSeries(ser2);
        d.addSeries(ser3);

        dataset = d;
    }

}
