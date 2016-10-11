package ch.ethz.coss.nervousnetgen.virtual.plot;

import android.graphics.Color;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

import ch.ethz.coss.nervousnetgen.virtual.virtual_sensor.VirtualSensor;

/**
 * Created by ales on 19/07/16.
 */
public class GraphPlot {
    public GraphView graph;

    public GraphPlot(GraphView graph){
        this.graph = graph;
    }

    /**
     * The function takes points and clusters and plots them. It considers only first two coordinates
     * which means only 2 dimensions as we can easily plot in 2D plane. If the dimension is 1, then
     * the function automatically sets the second coordinate to a constant value 1 and plots it.
     * @param pointsInit    points

     */
    public void plot(ArrayList<VirtualSensor> pointsInit) {

        if (pointsInit == null){
            Log.d("GRAPH", "Empty list");
            return;
        }

        // Convert data of Points into DataPoint
        //graph.removeAllSeries();

        DataPoint[] data = new DataPoint[pointsInit.size()];
        //DataPoint[] clusters = new DataPoint[clustersInit.size()];

        double minX = 0;
        double maxX = 1;
        double minY = 0;
        double maxY = 2;

        for(int i = 0; i < pointsInit.size(); i++) {
            double[] coord = pointsInit.get(i).getCoordinates();
            if (coord.length >= 2) {
                double x = coord[0];
                double y = coord[1];
                data[i] = new DataPoint(x, y);
                if (x < minX) minX = x;
                if (y < minY) minY = y;
                if (x > maxX) maxX = x;
                if (y > maxY) maxY = y;
            }
            else if (coord.length == 1) {
                double x = coord[0];
                data[i] = new DataPoint(x, 1);
                if (x > maxX) maxX = x;
                if (x < minX) minX = x;

            }
            else {
                // ignore this point as it has wrong number of coordinates
                Log.d("GraphPlot", "Wrong coordinate type, so the point hasn't been plotted");
            }
        }


        // Plot Points
        PointsGraphSeries<DataPoint> point_series = new PointsGraphSeries<DataPoint>(data);
        point_series.setShape(PointsGraphSeries.Shape.POINT);
        point_series.setColor(Color.BLACK);
        point_series.setSize(5);

        // Add points to graph
        graph.addSeries(point_series);

        // Additional format
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(minX);
        graph.getViewport().setMaxX(maxX * 1.1);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(minY);
        graph.getViewport().setMaxY(maxY* 1.1);

        graph.getViewport().setScrollable(true);
        Log.d("GRAPH", "Done");
    }

    public void clear(){
        this.graph.removeAllSeries();
    }
}
