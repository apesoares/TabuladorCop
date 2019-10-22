/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouvidoria;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author N0026925
 */
public class chart_class {

    public JFreeChart createChart(final CategoryDataset dataset, String chart_title, String xtitle, String ytitle) {

        // create the chart...
        final JFreeChart chart = ChartFactory.createStackedBarChart(
                chart_title, // chart title
                xtitle, // domain axis label
                ytitle, // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        chart.setBorderVisible(true);

        LegendTitle lg = chart.getLegend();
        lg.setVisible(false);

        Font font_title = new Font("Dialog", Font.PLAIN, 18);
        chart.getTitle().setFont(font_title);
        chart.getTitle().setMargin(5, 5, 5, 5);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setUpperMargin(0.5);

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
                0.0f, 0.0f, global.get_color(0, 0, 153),
                0.0f, 0.0f, global.get_color(0, 0, 153));

        renderer.setSeriesPaint(0, gp0);

        renderer.setItemMargin(0.02);

        //renderer.setSeriesPaint(0, Color.blue);
        renderer.setBaseItemLabelsVisible(Boolean.TRUE);

        renderer.setBaseItemLabelPaint(Color.white);
        Font font = renderer.getBaseItemLabelFont();
        renderer.setBaseItemLabelFont(new Font(font.getFontName(), Font.BOLD, 12));

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.UP_45
        );

        domainAxis.setCategoryMargin(0.3);

        // OPTIONAL CUSTOMISATION COMPLETED.
        BarRenderer barrenderer = (BarRenderer) plot.getRenderer();
        barrenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        barrenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));

        return chart;

    }

    public static Vector headers(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<Object> columnNames = new Vector<Object>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.addElement(metaData.getColumnName(column));
        }

        return columnNames;

    }

    public static Vector data(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        int columnCount = metaData.getColumnCount();

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.addElement(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return data;

    }

    public CategoryDataset create_dual_Dataset(ResultSet rs, String serie_title1, String serie_title2) {

        Vector<Object> colunas = null;
        Vector<Vector<Object>> linhas = null;
        try {
            linhas = data(rs);
        } catch (SQLException ex) {
            Logger.getLogger(chart_class.class.getName()).log(Level.SEVERE, null, ex);
        }

        // row keys...
        final String series1 = serie_title1;
        final String series2 = serie_title2;

        // column keys...
        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Vector<Object> item : linhas) {

            Object obj = item.get(1);

            if (obj != null) {

                String str = obj.toString();

                double final_value = Double.parseDouble(str);

                dataset.addValue(final_value, series1, (Comparable) item.get(0));

            }

        }

        return dataset;

    }

    public JFreeChart create_dual_axis_chart(final CategoryDataset[] datasets,
            String chart_title, String xtitle, String ytitle1, String ytitle2) {

        // create the first renderer...
        //      final CategoryLabelGenerator generator = new StandardCategoryLabelGenerator();
        final BarRenderer renderer = new BarRenderer();
        //    renderer.setLabelGenerator(generator);
        //renderer.setBaseItemLabelsVisible(Boolean.TRUE);

        final CategoryPlot plot = new CategoryPlot();
        plot.setDataset(datasets[0]);
        plot.setRenderer(renderer);

        final GradientPaint gp0 = new GradientPaint(
                0.0f, 0.0f, global.get_color(0, 0, 153),
                0.0f, 0.0f, global.get_color(0, 0, 153));

        renderer.setSeriesPaint(0, gp0);

        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());

        CategoryAxis axis = new CategoryAxis(xtitle);

        plot.setDomainAxis(axis);

        axis.setCategoryMargin(0.3);

        NumberAxis rangeAxis1 = new NumberAxis(ytitle1);
        plot.setRangeAxis(rangeAxis1);

        rangeAxis1.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis1.setUpperMargin(0.5);

        renderer.setBaseItemLabelsVisible(Boolean.TRUE);
        renderer.setDrawBarOutline(false);

        renderer.setItemMargin(0.02);

        renderer.setBaseItemLabelPaint(Color.white);
        Font font = renderer.getBaseItemLabelFont();
        renderer.setBaseItemLabelFont(new Font(font.getFontName(), Font.BOLD, 12));

        BarRenderer barrenderer = (BarRenderer) plot.getRenderer();
        barrenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        barrenderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        final CategoryItemRenderer renderer2 = new LineAndShapeRenderer();

        renderer2.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());

        LineAndShapeRenderer lsr = (LineAndShapeRenderer) renderer2;

        lsr.setSeriesPaint(0, Color.red);

        lsr.setSeriesStroke(
                0, new BasicStroke(
                        1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                        1.0f, new float[]{5.0f, 3.0f}, 0.0f
                )
        );
        lsr.setBaseItemLabelsVisible(Boolean.TRUE);

        lsr.setBaseItemLabelPaint(Color.white);
        Font font2 = lsr.getBaseItemLabelFont();
        lsr.setBaseItemLabelFont(new Font(font.getFontName(), Font.BOLD, 12));

        lsr.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        lsr.setBasePositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER));

        plot.setDataset(1, datasets[1]);
        plot.setRenderer(1, renderer2);

        final ValueAxis rangeAxis2 = new NumberAxis(ytitle2);
        plot.setRangeAxis(1, rangeAxis2);

        rangeAxis2.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis2.setUpperMargin(0.5);

        plot.mapDatasetToRangeAxis(1, 1);

        // change the rendering order so the primary dataset appears "behind" the 
        // other datasets...
        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

        plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        final JFreeChart chart = new JFreeChart(plot);
        chart.setTitle(chart_title);

        chart.setBackgroundPaint(Color.white);

        Font font_title = new Font("Dialog", Font.PLAIN, 18);
        chart.getTitle().setFont(font_title);
        chart.getTitle().setMargin(5, 5, 5, 5);

        chart.setBorderVisible(true);

        return chart;

    }

    public CategoryDataset[] get_data_sets(ResultSet rs, String serie_title1, String serie_title2) {

        Vector<Object> colunas = null;
        Vector<Vector<Object>> linhas = null;
        try {
            linhas = data(rs);
        } catch (SQLException ex) {
            Logger.getLogger(chart_class.class.getName()).log(Level.SEVERE, null, ex);
        }

        // row keys...
        final String series1 = serie_title1;
        final String series2 = serie_title2;

        // column keys...
        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        final DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

        for (Vector<Object> item : linhas) {

            Object obj = item.get(1);

            if (obj != null) {

                String str = obj.toString();

                double final_value = Double.parseDouble(str);

                dataset.addValue(final_value, series1, (Comparable) item.get(0));

            }

        }

        for (Vector<Object> item : linhas) {

            Object obj = item.get(2);

            if (obj != null) {

                String str = obj.toString();

                double final_value = Double.parseDouble(str);

                dataset2.addValue(final_value, series2, (Comparable) item.get(0));

            }

        }

        CategoryDataset[] array_data_set = new CategoryDataset[2];

        array_data_set[0] = dataset;
        array_data_set[1] = dataset2;

        return array_data_set;

    }

}
