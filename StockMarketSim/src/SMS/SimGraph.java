package SMS;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import org.jfree.chart.annotations.*;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.StandardXYSeriesLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYSeriesLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Minute;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.awt.*;

/**Task to do here
 * 1) Read an actual data from initialData
 * 2) Mouse-hover each point to read text
 * 3) Get Switch2Day to set to first day of month that is Monday*/
public class SimGraph extends JPanel {

    private ChartPanel cp;
    private enum PeriodMode {DAY, WEEK, MONTH, YEAR}
    private PeriodMode mode;

    public SimGraph() {
        JFreeChart lineGraph = ChartFactory.createTimeSeriesChart("Stock Market", "Time", "Stock value", null,
                true, true, false); // create a line graph to plot the data
        //Background, border and title colour
        lineGraph.setBackgroundPaint(Color.BLACK);
        lineGraph.setBorderPaint(Color.WHITE);
        lineGraph.getTitle().setPaint(Color.WHITE);

        final XYPlot plot = lineGraph.getXYPlot();

        //Graph background, grids colour
        plot.setBackgroundPaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setDomainGridlinesVisible(false);
        plot.setDomainPannable(false);
        plot.setRangePannable(false);
        plot.setRenderer(new XYAreaRenderer());
        plot.getRenderer().setBaseToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());

        //y-axis, stock values
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false); // range between min and max values, not 0 to max values
        rangeAxis.setAutoRange(true);
        rangeAxis.setTickLabelPaint(Color.WHITE);
        rangeAxis.setLabelPaint(Color.WHITE);
        rangeAxis.setRange(-5, 5);

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
        dateAxis.setTickLabelsVisible(false);

        // Chart panel to fit on JPanel
        cp = new ChartPanel( lineGraph ); // panel to hold line graph
        cp.setRangeZoomable(false); // disable y-axis mouse-drag zoomable
        cp.setDomainZoomable(false); // disable x-axis mouse-drag zoomable
        cp.setPopupMenu(null);
        cp.setHorizontalAxisTrace(true);
        cp.setInitialDelay(0);
        cp.addMouseMotionListener(new MoveClass());
        addMouseWheelListener(new ZoomClass());
        setVisible(true);
        setLayout(new BorderLayout());
        add(cp);
        mode = PeriodMode.DAY;
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

    /**Get this to set to first day of month that is Monday*/
    public void switch2Day() {
        mode = PeriodMode.DAY;
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        LocalDateTime date = LocalDateTime.ofInstant(dateAxis.getMinimumDate().toInstant(), ZoneId.systemDefault());
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2017, date.getMonth(), 2), LocalTime.of(8, 55)); //2017-01-01, 08:55
        Date min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        ldt = LocalDateTime.of(LocalDate.of(2017, date.getMonth(), 2), LocalTime.of(16, 5)); //2017-01-01, 08:55
        Date max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setRange(min, max);
        System.out.println(min);
        System.out.println(max);
    }

    public void switch2Week() {
        mode = PeriodMode.WEEK;
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        LocalDateTime date = LocalDateTime.ofInstant(dateAxis.getMinimumDate().toInstant(), ZoneId.systemDefault());
        LocalDateTime monday = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY));
        LocalDateTime friday = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY)).plusDays(4);

        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2017, monday.getMonth(), monday.getDayOfMonth()), LocalTime.of(8, 55)); //First monday of month
        Date min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        ldt = LocalDateTime.of(LocalDate.of(2017, friday.getMonth(), friday.getDayOfMonth()), LocalTime.of(16, 5)); //First friday of month
        Date max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setRange(min, max);
        System.out.println(min);
        System.out.println(max);
    }

    public void switch2Month() {
        mode = PeriodMode.MONTH;
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        LocalDateTime date = LocalDateTime.ofInstant(dateAxis.getMinimumDate().toInstant(), ZoneId.systemDefault());
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2017, date.getMonth(), 1), LocalTime.of(8, 55)); //2017-01-01, 08:55
        Date min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        ldt = LocalDateTime.of(LocalDate.of(2017, date.getMonth(), date.plusMonths(1).withDayOfMonth(1).minusDays(1).getDayOfMonth()), LocalTime.of(16, 5)); //2017-01-01, 08:55
        Date max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setRange(min, max);
        System.out.println(min);
        System.out.println(max);
    }

    public void switch2Year() {
        mode = PeriodMode.YEAR;
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        LocalDateTime ldt = LocalDateTime.of(LocalDate.of(2017, 1, 1), LocalTime.of(8, 55)); //2017-01-01, 08:55
        Date min = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        ldt = LocalDateTime.of(LocalDate.of(2017, 12, 31), LocalTime.of(16, 55)); //2017-01-01, 08:55
        Date max = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setRange(min, max);
        System.out.println(min);
        System.out.println(max);
    }

    public void nextPeriod() {
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        final NumberAxis rangeAxis = (NumberAxis) getPlot().getRangeAxis();
        LocalDateTime minLdt = LocalDateTime.ofInstant(dateAxis.getMinimumDate().toInstant(), ZoneId.systemDefault());
        LocalDateTime maxLdt = LocalDateTime.ofInstant(dateAxis.getMaximumDate().toInstant(), ZoneId.systemDefault());
        Date min, max;
        switch (mode) {
            case DAY:
                if (!maxLdt.plusDays(1).isAfter(LocalDateTime.of(2017, 12, 31, maxLdt.getHour(), maxLdt.getMinute()))) {
                    minLdt = minLdt.plusDays(1);
                    maxLdt = maxLdt.plusDays(1);
                }
                break;
            case WEEK:
                if (!maxLdt.plusWeeks(1).isAfter(LocalDateTime.of(2017, 12, 31, maxLdt.getHour(), maxLdt.getMinute()))) {
                    minLdt = minLdt.plusWeeks(1);
                    maxLdt = maxLdt.plusWeeks(1);
                }
                break;
            case MONTH:
                if (!maxLdt.plusMonths(1).isAfter(LocalDateTime.of(2017, 12, 31, maxLdt.getHour(), maxLdt.getMinute()))) {
                    minLdt = minLdt.plusMonths(1);
                    maxLdt = maxLdt.plusMonths(1);
                }
                break;
        }
        min = Date.from(minLdt.atZone(ZoneId.systemDefault()).toInstant());
        max = Date.from(maxLdt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setRange(min, max);
        rangeAxis.setAutoRange(true);
        System.out.println(max);
    }

    public void prevPeriod() {
        final DateAxis dateAxis = (DateAxis) getPlot().getDomainAxis();
        final NumberAxis rangeAxis = (NumberAxis) getPlot().getRangeAxis();
        LocalDateTime minLdt = LocalDateTime.ofInstant(dateAxis.getMinimumDate().toInstant(), ZoneId.systemDefault());
        LocalDateTime maxLdt = LocalDateTime.ofInstant(dateAxis.getMaximumDate().toInstant(), ZoneId.systemDefault());
        Date min, max;
        switch (mode) {
            case DAY:
                if (!minLdt.minusDays(1).isBefore(LocalDateTime.of(2017, 1, 1, minLdt.getHour(), minLdt.getMinute()))) {
                    minLdt = minLdt.minusDays(1);
                    maxLdt = maxLdt.minusDays(1);
                }
                break;
            case WEEK:
                if (!minLdt.minusWeeks(1).isBefore(LocalDateTime.of(2017, 1, 1, minLdt.getHour(), minLdt.getMinute()))) {
                    minLdt = minLdt.minusWeeks(1);
                    maxLdt = maxLdt.minusWeeks(1);
                }
                break;
            case MONTH:
                if (!minLdt.minusMonths(1).isBefore(LocalDateTime.of(2017, 1, 1, minLdt.getHour(), minLdt.getMinute()))) {
                    minLdt = minLdt.minusMonths(1);
                    maxLdt = maxLdt.minusMonths(1);
                }
                break;
        }
        min = Date.from(minLdt.atZone(ZoneId.systemDefault()).toInstant());
        max = Date.from(maxLdt.atZone(ZoneId.systemDefault()).toInstant());
        dateAxis.setRange(min, max);
        rangeAxis.setAutoRange(true);
        System.out.println(min);
    }

    public void setDataset( XYDataset d ) {
        getPlot().setDataset(d);
        cp.getChart().fireChartChanged();
        getPlot().getRangeAxis().setAutoRange(true);
    }

    public void switchGraph() {
        if (getPlot().getRenderer() instanceof XYAreaRenderer) {
            getPlot().setRenderer(new XYLineAndShapeRenderer());
            getPlot().getRenderer().setSeriesShape(0, new Rectangle(4,4));
            getPlot().getRenderer().setBaseToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
        } else {
            getPlot().setRenderer(new XYAreaRenderer());
            getPlot().getRenderer().setBaseToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
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

    private class MoveClass implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            cp.getChart().fireChartChanged();
        }
    }
}
