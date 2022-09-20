package et3.threes;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("matriceIA")
public class MatriceIA {
    @Param(0)
    private String id;
    @Param(1)
    private int riga;
    @Param(2)
    private int colonna;
    @Param(3)
    private int valore;

    public MatriceIA() {}

    public MatriceIA(String id, int riga, int colonna, int valore) {
        this.id = id;
        this.riga = riga;
        this.colonna = colonna;
        this.valore = valore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRiga() {
        return riga;
    }

    public void setRiga(int riga) {
        this.riga = riga;
    }

    public int getColonna() {
        return colonna;
    }

    public void setColonna(int colonna) {
        this.colonna = colonna;
    }

    public int getValore() {
        return valore;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

    @Override
    public String toString() {
        return "InMatrice [id=" + id + ", riga=" + riga + ", colonna=" + colonna + ", valore=" + valore + "]";
    }
}
