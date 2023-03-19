package dao;


public class DaoImpl implements IDao{
    @Override
    public double getData(){
        System.out.println("IDo using Database ...");
        double tmp = Math.random()*40;
        return  tmp;
    }

}
