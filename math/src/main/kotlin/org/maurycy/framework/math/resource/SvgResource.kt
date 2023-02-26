package org.maurycy.framework.math.resource

import io.quarkus.logging.Log
import java.awt.Rectangle
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtils
import org.jfree.chart.JFreeChart
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.renderer.category.StatisticalBarRenderer
import org.jfree.data.category.CategoryDataset
import org.jfree.data.statistics.DefaultStatisticalCategoryDataset
import org.jfree.graphics2d.svg.SVGGraphics2D


@Path("svg")
class SvgResource(

) {

    @GET
    @Produces(MediaType.APPLICATION_SVG_XML)
    fun get(): String {
        val chart = createChart(createDataset())
        val g2 = SVGGraphics2D(800, 600)
        val r = Rectangle(0, 0, 800, 600)
        chart.draw(g2, r)
        return g2.svgElement
    }



    @GET
    @Path("t")
    @Produces(MediaType.TEXT_PLAIN)
    fun t(): String {
        Log.info("TESTing ")
        return "testsssssss"
    }

    private fun createDataset(): CategoryDataset {
        val dataset = DefaultStatisticalCategoryDataset()
        dataset.add(30.0, null, "Row Key 1", "Column Key 1")
        dataset.add(50.0, null, "Row Key 2", "Column Key 2")
        dataset.add(40.0, null, "Row Key 3", "Column Key 3")
        dataset.add(80.0, null, "Row Key 4", "Column Key 4")
        dataset.add(60.0, null, "Row Key 5", "Column Key 5")
        return dataset
    }

    private fun createChart(dataset: CategoryDataset): JFreeChart {
        val chart: JFreeChart = ChartFactory.createLineChart(
            "Title", "Category Axis Label",
            "Value Axis Label", dataset
        )
        val plot: CategoryPlot = chart.plot as CategoryPlot
        val renderer = StatisticalBarRenderer()
        plot.renderer = renderer
        ChartUtils.applyCurrentTheme(chart)
        renderer.defaultItemLabelGenerator = StandardCategoryItemLabelGenerator()
        renderer.defaultItemLabelsVisible = true
        return chart
    }
}