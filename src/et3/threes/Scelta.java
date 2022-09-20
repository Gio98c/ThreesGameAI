package et3.threes;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

@Id("scelta")
public class Scelta {

    @Param(0)
    String scelta;

    public Scelta() {

    }

    public Scelta(String scelta) {
        this.scelta = scelta;
    }

    @Override
    public String toString() {
        return "Scelta [scelta=" + scelta + "]";
    }

    public String getScelta() {
        return scelta;
    }

    public void setScelta(String scelta) {
        this.scelta = scelta;
    }




}
