package pres;

import dao.DaoImpl;
import dao.IDao;
import metier.IMetierImpl;

public class Presentation {
    // instanciation static ( n'est pas bon )
    public static void main(String[] args){
        //IDao dao = new DaoImpl2(); // Using Sensor.
        IDao dao = new DaoImpl(); // Using Database.

        IMetierImpl metier = new IMetierImpl();
        metier.setDao(dao);
        System.out.println("Resumtats : "+metier.calcul());
    }
}
