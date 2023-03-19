package metier;

import dao.IDao;

public class IMetierImpl implements IMetier {
    // Couplage Faible
    private IDao Dao;
    @Override
    public double calcul() {
        System.out.println("code Metier 2022 ");
        double tmp = Dao.getData();
        double res = tmp*60+3624/60; // ici example d'un calcul
        return res;
    }

    public void setDao(IDao dao) {
        Dao = dao;
    }
}
