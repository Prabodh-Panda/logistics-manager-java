package inheritancetest;

/**
 * Created by Prabodh Kumar Panda on 5/2/2021.
 */
public class TestShape {
    public static void main(String[] args) {
        Circle c1 = new Circle(12);
        System.out.println(c1.area());

        RightTriangle t1 = new RightTriangle(12,34);
        System.out.println(t1.area());
    }
}
