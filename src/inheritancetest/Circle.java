package inheritancetest;

/**
 * Created by Prabodh Kumar Panda on 5/2/2021.
 */
public class Circle extends Shape{
    private double rad;

    public Circle(double rad){
        this.rad = rad;
    }

    @Override
    public double area(){
        return Math.PI * rad * rad;
    }
}
