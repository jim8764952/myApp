package com.example.rw;

import engineering.foundation.retainingwall.earthPressure.ActivateEarthPressure;

import java.util.Vector;

import static java.lang.Math.*;

public class Analyze {
    enum ActivateEarthPressureMethod{Coulomb};
    enum PassiveEarthPressureMethod{CaquotKerisel}

    private double momentO;
    private double momentR;
    private double sumfv = -1;
    public double cross(double ax, double ay, double bx, double by){
        return ax * by - ay * bx;
    }
    public double getCrossSectionalArea(Vector<Point> points){
        double area = 0;

        System.out.println(points.toString());

        for (int i = 0; i < points.size(); i++){
            area += cross(points.get(i).x, points.get(i).y, points.get((i+1)%points.size()).x, points.get((i+1)%points.size()).y);
        }

        return abs(area)/2;
    }
    public Point getCentroid(Vector<Point> pts){
        double averX, averY;
        double tmpArea;
        double totalArea = 0;
        double sumCx = 0, sumCy = 0;

        for (int i = 0; i < pts.size()-3; i++) {
            double ax = pts.get(i+2).x - pts.get(0).x;
            double ay = pts.get(i+2).y - pts.get(0).y;
            double bx = pts.get(i+1).x - pts.get(0).x;
            double by = pts.get(i+1).y - pts.get(0).y;

            tmpArea = cross(ax,ay,bx,by)/2;
            averX = (pts.get(0).x + pts.get(i+1).x + pts.get(i+2).x)/3;
            averY = (pts.get(0).y + pts.get(i+1).y + pts.get(i+2).y)/3;

            totalArea += tmpArea;
            sumCx += averX * tmpArea;
            sumCy += averY * tmpArea;
        }

        return new Point(sumCx/totalArea, sumCy/totalArea);
    }
    public double getSumFv(CantileverRetainingWall crw){
        if (sumfv == -1) {
            return crw.getWrc() * getCrossSectionalArea(crw.getRwPoints())
                    + crw.getWb() * getCrossSectionalArea(crw.getBfPoints());
        }else {
            return sumfv;
        }
    }

    /* 牆體傾覆
    *  1. 長期載重狀況時應大於2.0
    **/
    public double overTurnCheck(CantileverRetainingWall crw){

        ActivateEarthPressure activateEarthPressure = new ActivateEarthPressure(crw);
        double Pa = activateEarthPressure.getPaF();
        double Pv = Pa * sin(toRadians(crw.getSurfaceSlope()));
        double Ph = Pa * cos(toRadians(crw.getSurfaceSlope()));

        //傾覆
        momentO = crw.getWallTotalHeight() / 3 * Ph; // 主動土壓力水平分力
        //抵抗 依設計規範牆體前被動土壓不計算
        momentR =
            crw.getPlateTotalBreath() * Pv // 主動土壓力垂直分力
            + getCrossSectionalArea(crw.getRwPoints()) * crw.getWrc() * getCentroid(crw.getRwPoints()).x // 牆身重
            + getCrossSectionalArea(crw.getBfPoints()) * crw.getWb() * getCentroid(crw.getBfPoints()).x; // 回填土力矩

        return momentR/momentO;
    }
    /* 牆體滑動
     * 1. 長期載重狀況應大於1.5
     * */
    public double slideCheck(CantileverRetainingWall crw){

        ActivateEarthPressure activateEarthPressure = new ActivateEarthPressure(crw);
        double Pa = activateEarthPressure.getPaF();
        double Ph = Pa * cos(toRadians(crw.getSurfaceSlope()));



        double resistingForces = getSumFv(crw) * tan(toRadians(crw.getWallWithSf()))
                + crw.getPlateTotalBreath() * crw.getCb();
//        TODO        + 前趾被動土壓力;


        return resistingForces/Ph;
    }
    /**
     * 基礎容許支承力
     * 長期載重狀況時應大於3.0
     * book qu 247
     * book ex 736
     * */
    public double breakCheck(CantileverRetainingWall crw){
        double B = crw.getPlateTotalBreath();
        double Df = crw.getToeSoilHeight();
        double L = Double.POSITIVE_INFINITY;
        double sf = crw.getFb();
        //N
        double Nq = pow((tan(toRadians(45 + sf/2))), 2) *exp(PI * tan(toRadians(sf)));
        double Nc = (Nq - 1) /tan(toRadians(sf));
        double Nr = 2 * (Nq + 1) *tan(sf);

        //Shape
        double fcs = 1 + (B/L) + (Nq/Nc);
        double fqs = 1 +(B/L) * tan(toRadians(sf));
        double frs = 1 - 0.4 * (B/L);

        //Depth
        double fcd = 0;
        double fqd = 0;
        double frd = 0;
        if (Df/B <= 1){
            if (sf == 0){
                fcd = 1 + 0.4 * (Df/B);
                fqd = 1;
                frd = 1;
            } else if (sf > 0)
            {
                fqd = 1 + 2 * tan(toRadians(sf)) * pow((1 - sin(toRadians(sf))), 2) * (Df/B);
                fcd = fqd - (1-fqd)/(Nc * tan(toRadians(sf)));
                frd = 1;
            }
        }else if (Df/B > 1){
            if (sf == 0){
                fcd = 1 + 0.4*atan(Df/B);
                fqd = 1;
                frd = 1;
            }else if (sf > 0){
                fqd = 1 + 2 * tan(toRadians(sf)) * pow((1 - sin(toRadians(sf))), 2) * atan(Df/B);
                fcd = fqd - (1-fqd)/(Nc * tan(toRadians(sf)));
                frd = 1;
            }
        }

        //Inclination
        double fci = pow((1-(B/90)), 2);
        double fqi = fci;
        double fri = pow((1-(B/sf)), 2);

        double averX = (momentR - momentO)/getSumFv(crw);
        double e = B/2 - averX;
        double q = crw.getWb() * Df; //前趾覆土單位重目前與回填土一樣

        double qu = crw.getCb() * Nc * fcs * fcd * fci
                + q * Nq * fqs * fqd * fqi
                + crw.getWb()/2 * B * Nr * frs * frd * fri;
        double qmax = sumfv/B * (1 + 6*e/B);


        return qu/qmax;
    }
}
