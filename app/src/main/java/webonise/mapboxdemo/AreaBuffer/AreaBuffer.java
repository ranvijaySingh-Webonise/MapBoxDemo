package webonise.mapboxdemo.areabuffer;

import java.util.ArrayList;
import java.util.List;

public class AreaBuffer {
    /**
     * Function to get the buffer points from a given set of lat lng
     *
     * @param pointList List<Points> A list of INCLOSED points
     * @return List<Point> list of buffered points
     * @throws Exception
     */
    public List<Point> buffer(List<Point> pointList) throws Exception {
        EquationHandler equationHandler = new EquationHandler();
        List<LineEquation> bufferedLineEquationList = new ArrayList<>();
        /**
         * Find the buffered line Equation.
         * If number of points in polygon are 5 ie. 0,1,2,3,4,0
         * then run loop for points (0,1),(1,2),(2,3),(3,4),(4,0)
         */
        for (int i = 0; i < pointList.size() - 1; i++) {
            Point firstPoint = pointList.get(i);
            Point secondPoint = pointList.get(i + 1);
            /**
             * Step 1: Get equation of line from two points
             */
            LineEquation actualLineEquation = equationHandler.getLineEquation(firstPoint, secondPoint);
            /**
             * Step 2: Get center point of two Points
             */
            Point centerPoint = new Point();
            centerPoint = centerPoint.getCenterPoint(firstPoint, secondPoint);
            /**
             * Step 3: Get perpendicular line though center point.
             */
            LineEquation perpendicularLine = equationHandler.getPerpendicularLineEquation
                    (actualLineEquation, centerPoint);
            /**
             * Step 4: Get point on the line a given distance
             */
            Point[] polygon = pointList.toArray(new Point[pointList.size() - 1]);
            Point bufferPoint = equationHandler.getPointOnLineAtDistance(perpendicularLine,
                    centerPoint, 0.0001, polygon);
            /**
             * Step 5: Get equation of line parallel to original line passing though buffer point
             */
            LineEquation bufferLineEquation = equationHandler.getParallelLineEquation
                    (actualLineEquation, bufferPoint);
            bufferedLineEquationList.add(bufferLineEquation);
        }
        /**
         * Once you have line equation of all the buffered line.
         * Find the list of intersection points of all buffered lines.
         */
        List<Point> bufferedPoints = new ArrayList<>();
        for (int i = 0; i < bufferedLineEquationList.size(); i++) {
            int nextIndex = (i + 1) % bufferedLineEquationList.size();
            Point intersectionPoint = equationHandler.getIntersectionPoint
                    (bufferedLineEquationList.get(i), bufferedLineEquationList.get(nextIndex));
            bufferedPoints.add(intersectionPoint);
        }
        bufferedPoints.add(bufferedPoints.get(0));
        return bufferedPoints;
    }
}
