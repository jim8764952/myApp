package engineering.foundation.retainingwall.earthPressure;

import com.example.rw.CantileverRetainingWall;

import static java.lang.Math.*;

/**
 * 不規則載重、傾斜的牆面、不規則形狀的地表面、多層土（岩）體不適用 應採用土楔法
 *
 */
public class ActivateEarthPressure {
    private double Sw; //土壤單位重(tf/m^3）
    private double Sc; //土壤凝聚力（tf/m^2）
    private double wallH; //牆之垂直高度（m）
    private double pointPD; //牆頂地表面至欲求土壓力點之垂直深度（ｍ）
    private double Sf; //牆背土壤之內摩擦角（度）通常採用 牆體若為木材、鋼材或預鑄鋼筋混凝土，則因牆面較平滑，最大牆面摩擦角可採用牆體若為場鑄鋼筋混凝土，其牆面較粗糙，最大牆面摩擦角可採用
    private double wallWithSf; //牆背面與土壤間之摩擦角（度）
    private double backSDeg; //牆背地表面與水平面之交角（度）
    private double backDeg; //牆背面與垂直面交角（度），以逆時針方向為正，順時針方向為負

    public ActivateEarthPressure(double sw, double sc, double wallH, double pointPD, double sf, double wallWithSf, double backSDeg, double backDeg) {
        this.Sw = sw;
        this.Sc = sc;
        this.wallH = wallH;
        this.pointPD = pointPD;
        this.Sf = sf;
        this.wallWithSf = wallWithSf;
        this.backSDeg = backSDeg;
        this.backDeg = backDeg;
    }
    public ActivateEarthPressure(CantileverRetainingWall crw){
        this.Sw = crw.getWb();
        this.Sc = crw.getCb();
        this.wallH = crw.getWallTotalHeight();
//        this.pointPD = ;
        this.Sf = crw.getFb();
//        this.wallWithSf = ;
        this.backSDeg = crw.getSurfaceSlope();
//        this.backDeg = 0;
    }

    /**
     * 計算Coulomb 主動土壓力係數
     * V : Vertical
     * P : Pressure
     * D : Depth
     * W : Weight
     * F : Force
     * A : Activate
     * C : Cohesion
     * @return double Ka
     */
    public double CoulombActivateEarthPressureInNormalCond(){
        double KaNumerator = pow(cos(toRadians(Sf-backDeg)),2);
        double KaDenominatorLeft = pow(cos(toRadians(backDeg)),2)*cos(toRadians(backDeg+wallWithSf));
        double KaDenominatorRight = pow(1+sqrt((sin(toRadians(Sf+wallWithSf))*sin(toRadians(Sf-backSDeg)))/(cos(toRadians(wallWithSf+backDeg))*cos(toRadians(backDeg-backSDeg)))),2);

        return KaNumerator/(KaDenominatorLeft*KaDenominatorRight);
    }

    /**
     * 計算單位面積主動土壓力
     */
    public double getUnitAreaPa(){
        if (Sc > 0){
            pointPD = pointPD - 2*Sc/Sw * tan(toRadians(45+wallWithSf/2));
            if (pointPD <= 0){
                pointPD = 0;
            }
        }
        return CoulombActivateEarthPressureInNormalCond()*Sw*pointPD;
    }

    /**
     * 計算主動土壓力合力（tf/m）
     */
    public double getPaF(){
        if (Sc > 0){
            wallH = wallH - 2*Sc/Sw * tan(toRadians(45+wallWithSf/2));
            if (wallH <= 0){
                System.out.println("H<=0 應考慮長期效應所造成之土壓力");
            }
        }
        return 0.5*CoulombActivateEarthPressureInNormalCond()*Sw*wallH;
    }

}
