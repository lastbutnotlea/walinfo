import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Wahljahr {
    private Date jahr;

    public Date getJahr() {
        return jahr;
    }

    public void setJahr(Date jahr) {
        this.jahr = jahr;
    }

    public Wahljahr(Date jahr) {

        this.jahr = jahr;
    }

    private SimpleDateFormat format = new SimpleDateFormat("yy");

    String getKurzschreibweise() {
        return format.format(jahr);
    }
}
