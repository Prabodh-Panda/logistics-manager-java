package inheritancetest;
/**
 * Created by Prabodh Kumar Panda on 5/2/2021.
 */
public class RightTriangle extends Shape{

    private double base,perp;

    public RightTriangle(double base, double perp){
        this.base = base;
        this.perp = perp;
    }

    @Override
    public double area(){
        return (base * perp) / 2;
    }
}
