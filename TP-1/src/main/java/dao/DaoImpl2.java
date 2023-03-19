package dao;


public class DaoImpl2 implements IDao{
    @Override
    public double getData(){
        System.out.println("IDo using Sensors ...");
        double tmp = Math.random()*40;
        return  tmp;
    }

}
