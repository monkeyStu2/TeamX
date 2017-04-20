package SMS;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.awt.*;

/**Task to do here
 * 1) Read an actual data from initialData
 * 2) Mouse-hover each point to read text*/
public class SimGraph extends JPanel {

    private ChartPanel cp;
    private enum PeriodMode {DAY, WEEK, MONTH, YEAR}
    private PeriodMode mode;

    private int month, day;

    public SimGraph() {
        JFreeChart lineGraph = ChartFactory.createTimeSeriesChart("Stock MarketTest", "Time", "Stock value", null,
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
        plot.setDomainPannable(false);
        plot.setRangePannable(false);
        plot.setRenderer(new XYAreaRenderer());

        //y-axis, stock values
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false); // range between min and max values, not 0 to max values
        rangeAxis.setAutoRange(true);
        rangeAxis.setTickLabelPaint(Color.WHITE);
        rangeAxis.setLabelPaint(Color.WHITE);

        //x-axis, time
        final DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
//        dateAxis.setDateFormatOverride(new SimpleDateFormat("DD-MMM HH:mm")); // display x-axis label in this format
//        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 60)); // 15 minutes interval
        dateAxis.setLabelPaint(Color.WHITE);
        dateAxis.setTimeline(SegmentedTimeline.newFifteenMinuteTimeline()); // remove closing trading hours
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2017, 1, 2), LocalTime.of(8, 55)); //2017-01-01, 08:55
        Date min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        ldt = LocalDateTime.of(LocalDate.of(2017, 1, 2), LocalTime.of(16, 5)); //2017-01-01, 08:55
        Date max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setRange(min, max);

        // Chart panel to fit on JPanel
        cp = new ChartPanel( lineGraph ); // panel to hold line graph
        cp.setRangeZoomable(false); // disable y-axis mouse-drag zoomable
        cp.setDomainZoomable(false); // disable x-axis mouse-drag zoomable
        cp.setPopupMenu(null);
        addMouseWheelListener(new ZoomClass());
        setVisible(true);
        setLayout(new BorderLayout());
        add(cp);
        mode = PeriodMode.DAY;
        month = 1;
        day = 2;
    }

    /**Return the graph plot to be easily accessed*/
    private XYPlot getPlot() {
        return cp.getChart().getXYPlot();
    }

    /**Zoom out the graph y-axis only*/
    public void zoomOut() {
        final NumberAxis rangeAxis = (NumberAxis) getPlot().getRangeAxis();
        double min = rangeAxis.getLowerBound();
        double max = rangeAxis.getUpperBound();
        rangeAxis.setRange(min - 2, max + 2);
    }

    /**Zoom in the graph y-axis only*/
    public void zoomIn() {
        final NumberAxis rangeAxis = (NumberAxis) getPlot().getRangeAxis();
        double min = rangeAxis.getLowerBound();
        double max = rangeAxis.getUpperBound();
        if (min + 2 < max - 2) { // avoiding zooming in where not possible
            rangeAxis.setRange(min + 2, max - 2);
        }
    }

    public void switch2Day() {
        mode = PeriodMode.DAY;
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("DD-MMM HH:mm")); // display x-axis label in this format
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MINUTE, 60)); // 60 minutes interval
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2017, 1, 2), LocalTime.of(8, 55)); //2017-01-01, 08:55
        Date dt = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setMinimumDate(dt);
        ldt = LocalDateTime.of(LocalDate.of(2017, 1, 2), LocalTime.of(16, 5)); //2017-01-01, 08:55
        dt = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setMaximumDate(dt);
    }

    public void switch2Week() {
        mode = PeriodMode.WEEK;
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("DD-MMM HH:mm")); // display x-axis label in this format
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1)); // 60 minutes interval
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2017, 1, 2), LocalTime.of(8, 55)); //2017-01-01, 08:55
        Date min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        ldt = LocalDateTime.of(LocalDate.of(2017, 1, 6), LocalTime.of(16, 5)); //2017-01-01, 08:55
        Date max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setRange(min, max);
        System.out.println(dateAxis.getTickMarkPosition());
//        dateAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
    }

    public void switch2Month() {
        mode = PeriodMode.MONTH;
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("DD-MMM")); // display x-axis label in this format
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 7)); // 60 minutes interval
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2016, 12, 31), LocalTime.of(16, 5)); //2017-01-01, 08:55
        Date dt = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setMinimumDate(dt);
        ldt = LocalDateTime.of(LocalDate.of(2017, 2, 1), LocalTime.of(8, 55)); //2017-01-01, 08:55
        dt = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setMaximumDate(dt);
    }

    public void switch2Year() {
        mode = PeriodMode.YEAR;
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM")); // display x-axis label in this format
        dateAxis.setTickUnit(new DateTickUnit(DateTickUnitType.MONTH, 1)); // 60 minutes interval
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2016, 12, 31), LocalTime.of(16, 5)); //2017-01-01, 08:55
        Date dt = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setMinimumDate(dt);
        ldt = LocalDateTime.of(LocalDate.of(2018, 1, 1), LocalTime.of(8, 55)); //2017-01-01, 08:55
        dt = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setMaximumDate(dt);
    }

    public void nextPeriod() {
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        final NumberAxis rangeAxis = (NumberAxis) getPlot().getRangeAxis();
        LocalDateTime ldt;
        Date min, max;
        ldt = LocalDateTime.of(LocalDate.of(2017, 1, 1), LocalTime.of(8,55));
        min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        ldt = LocalDateTime.of(LocalDate.of(2017, 1, 1), LocalTime.of(16, 5)); //2017-01-01, 08:55
        max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        switch (mode) {
            case DAY:
                day += 1;
                ldt = LocalDateTime.of(LocalDate.of(2017, 1, day), LocalTime.of(8,55));
                min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                ldt = LocalDateTime.of(LocalDate.of(2017, 1, day), LocalTime.of(16, 5)); //2017-01-01, 08:55
                max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                break;
            case WEEK:
                day += 7;
                ldt = LocalDateTime.of(LocalDate.of(2017, 1, day), LocalTime.of(8,55));
                min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                ldt = LocalDateTime.of(LocalDate.of(2017, 1, day), LocalTime.of(16, 5)); //2017-01-01, 08:55
                max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                break;
            case MONTH:
                month += 1;
                ldt = LocalDateTime.of(LocalDate.of(2017, month, 1), LocalTime.of(8,55));
                min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                ldt = LocalDateTime.of(LocalDate.of(2017, month+1, 1), LocalTime.of(16, 5)); //2017-01-01, 08:55
                max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                break;
            case YEAR:
                break;
        }
        dateAxis.setRange(min, max);
        rangeAxis.setAutoRange(true);
    }

    public void prevPeriod() {
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        final NumberAxis rangeAxis = (NumberAxis) getPlot().getRangeAxis();
        LocalDateTime ldt;
        Date min, max;
        ldt = LocalDateTime.of(LocalDate.of(2017, 1, 1), LocalTime.of(8,55));
        min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        ldt = LocalDateTime.of(LocalDate.of(2017, 1, 1), LocalTime.of(16, 5)); //2017-01-01, 08:55
        max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        switch (mode) {
            case DAY:
                day -= 1;
                ldt = LocalDateTime.of(LocalDate.of(2017, 1, day), LocalTime.of(8,55));
                min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                ldt = LocalDateTime.of(LocalDate.of(2017, 1, day), LocalTime.of(16, 5)); //2017-01-01, 08:55
                max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                break;
            case WEEK:
                day -= 7;
                ldt = LocalDateTime.of(LocalDate.of(2017, 1, day), LocalTime.of(8,55));
                min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                ldt = LocalDateTime.of(LocalDate.of(2017, 1, day), LocalTime.of(16, 5)); //2017-01-01, 08:55
                max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                break;
            case MONTH:
                month -= 1;
                ldt = LocalDateTime.of(LocalDate.of(2017, month, 1), LocalTime.of(8,55));
                min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                ldt = LocalDateTime.of(LocalDate.of(2017, month+1, 1), LocalTime.of(16, 5)); //2017-01-01, 08:55
                max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
                break;
            case YEAR:
                break;
        }
        dateAxis.setRange(min, max);
        rangeAxis.setAutoRange(true);
    }

    public void setDataset( XYDataset d ) {
        getPlot().setDataset(d);
        cp.getChart().fireChartChanged();
        getPlot().getRangeAxis().setAutoRange(true);
    }

    public void switchGraph() {
        if (getPlot().getRenderer() instanceof XYAreaRenderer) {
            getPlot().setRenderer(new XYLineAndShapeRenderer());
            getPlot().getRenderer().setSeriesShape(0, new Rectangle(2,2));
        } else {
            getPlot().setRenderer(new XYAreaRenderer());
        }
        cp.getChart().fireChartChanged();
    }

    private class ZoomClass implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if (e.getScrollType() != MouseWheelEvent.WHEEL_UNIT_SCROLL) return;
            if (e.getWheelRotation()< 0) {
                System.out.println("Scroll Up");
                zoomIn();
            } else {
                System.out.println("Scroll Down");
                zoomOut();
            }
        }
    }
}
