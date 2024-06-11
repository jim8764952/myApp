package com.example.rw;
import lombok.Getter;

import java.util.Vector;

import static java.lang.Math.*;


public class CantileverRetainingWall implements RetainingWall,Backfill {
    public double getWallTotalHeight() {
        return wallTotalHeight;
    }

    public double getToeSoilHeight() {
        return toeSoilHeight;
    }

    public double getPlateSlopeHeight() {
        return plateSlopeHeight;
    }

    public double getPlateEndpointHeight() {
        return plateEndpointHeight;
    }

    public double getWallTopBreath() {
        return wallTopBreath;
    }

    public double getToeBreadth() {
        return toeBreadth;
    }

    public double getBottomBreadth() {
        return bottomBreadth;
    }

    public double getHeelBreath() {
        return heelBreath;
    }

    public double getPlateTotalBreath() {
        return plateTotalBreath;
    }

    public double getWallFrontSlope() {
        return wallFrontSlope;
    }

    public double getCb() {
        return cb;
    }

    public double getFb() {
        return fb;
    }

    public double getWb() {
        return wb;
    }

    public double getSurfaceSlope() {
        return surfaceSlope;
    }

    public double getWallWithSf() {
        return wallWithSf;
    }

    public double getWrc() {
        return wrc;
    }

    public Vector<Point> getRwPoints() {
        return rwPoints;
    }

    public Vector<Point> getBfPoints() {
        return bfPoints;
    }

    //擋土牆尺寸
    private double wallTotalHeight; // (m)
    private double toeSoilHeight; // (m) 板底至覆土表面高
    private double plateSlopeHeight; // (m)
    private double plateEndpointHeight; // (m)
    private double wallTopBreath; // (m)
    private double toeBreadth; // (m)
    private double bottomBreadth; // (m)
    private double heelBreath; // (m)
    private double plateTotalBreath; // (m)
    private double wallFrontSlope;
    //回填土性質
    private double cb; // (tf/m2)
    private double fb; // (deg.)
    private double wb; // (tf/m3)
    private double surfaceSlope; // (deg.)
    private double wallWithSf; // (deg.)
    //常用參數
    private double wrc = 2.4; // (tf/m3)
    //剖面頂點
    private Vector<Point> rwPoints = new Vector<>();
    private Vector<Point> bfPoints = new Vector<>();

    public CantileverRetainingWall(){}

    public CantileverRetainingWall(
            double wallTotalHeight, double toeSoilHeight, double plateSlopeHeight,
            double plateEndpointHeight, double wallTopBreath, double toeBreadth,
            double heelBreath, double plateTotalBreath, double wallFrontSlope,
            double cb, double fb, double wb,
            double surfaceSlope)
    {
        this.wallTotalHeight = wallTotalHeight;
        this.toeSoilHeight = toeSoilHeight;
        this.plateSlopeHeight = plateSlopeHeight;
        this.plateEndpointHeight = plateEndpointHeight;
        this.wallTopBreath = wallTopBreath;
        this.toeBreadth = toeBreadth;
        this.bottomBreadth = plateTotalBreath - toeBreadth - heelBreath;
        this.heelBreath = heelBreath;
        this.plateTotalBreath = plateTotalBreath;
        this.wallFrontSlope = wallFrontSlope;
        this.cb = cb;
        this.fb = fb;
        this.wb = wb;
        this.surfaceSlope = surfaceSlope;

        rwPoints.add(new Point(0.,0.));
        rwPoints.add(new Point(0.,plateEndpointHeight));
        rwPoints.add(new Point(toeBreadth,plateSlopeHeight+plateEndpointHeight));
        rwPoints.add(new Point(toeBreadth+bottomBreadth-toeBreadth,wallTotalHeight));
        rwPoints.add(new Point(toeBreadth+bottomBreadth,wallTotalHeight));
        rwPoints.add(new Point(toeBreadth+bottomBreadth,plateSlopeHeight+plateEndpointHeight));
        rwPoints.add(new Point(plateTotalBreath,plateEndpointHeight));
        rwPoints.add(new Point(plateTotalBreath,0.));

        bfPoints.add(new Point(toeBreadth+bottomBreadth,plateSlopeHeight+plateEndpointHeight));
        bfPoints.add(new Point(toeBreadth+bottomBreadth,wallTotalHeight));
        bfPoints.add(new Point(plateTotalBreath,heelBreath*tan(toRadians(surfaceSlope))+wallTotalHeight));
        bfPoints.add(new Point(plateTotalBreath,plateEndpointHeight));

    }

}
