import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.text.ParseException;

public class Batasan {
    String [] bat;
    boolean hc1;
    boolean hc2;
    double hc3;
    boolean hc4;
    LocalTime hc51a;
    LocalTime hc51b;
    LocalTime hc52a;
    LocalTime hc52b;
    LocalTime hc53a;
    LocalTime hc53b;
    int hc6;
    double hc7;
    DateFormat waktu = new SimpleDateFormat("hh:mm");
    int sc1a;
    int sc1b;
    int sc1c;
    int sc2;
    int sc3a;
    int sc3b;
    int sc3c;
    int sc4;
    int sc5aMax;
    int sc5aMin;
    int sc5bMax;
    int sc5bMin;
    int sc5cMax;
    int sc5cMin;
    boolean sc6;
    boolean sc7;
    int sc8;
    int sc9;

    public Batasan(String [] bts)
    {
        bat = bts;
    }

    public void setHc1() {
        if(bat[0].equals("Yes"))
            hc1 = true;
        if(bat[0].equals("N/A"))
            hc1 = false;
    }

    public void setHc2() {
        if(bat[1].equals("Yes"))
            hc2 = true;
        if(bat[1].equals("N/A"))
            hc2 = false;
    }

    public void setHc3() {
        String x = bat[2].substring(1, 2);
        hc3 = (Double.parseDouble(x))/100;
    }

    public void setHc4() {
        if(bat[3].equals("Yes"))
            hc4 = true;
        if(bat[3].equals("N/A"))
            hc4 = false;
    }

    public void setHc51a() {
        if(bat[4].length()==1)
            hc51a = LocalTime.parse("0" + bat[4]+":00");
        else
            hc51a = LocalTime.parse(bat[4]+":00");
    }

    public void setHc51b() {
        if(bat[5].length()==1)
            hc51b = LocalTime.parse("0" + bat[5]+":00");
        else
            hc51b = LocalTime.parse(bat[5]+":00");
    }

    public void setHc52a() {
        if(bat[6].length()==1)
            hc52a = LocalTime.parse("0" + bat[6]+":00");
        else
            hc52a = LocalTime.parse(bat[6]+":00");
    }

    public void setHc52b() {
        if(bat[7].length()==1)
            hc52b = LocalTime.parse("0" + bat[7]+":00");
        else
            hc52b = LocalTime.parse(bat[7]+":00");
    }

    public void setHc53a() {
        if(bat[8].length()==1)
            hc53a = LocalTime.parse("0" + bat[8]+":00");
        else
            hc53a = LocalTime.parse(bat[8]+":00");
    }

    public void setHc53b() {
        if(bat[9].length()==1)
            hc53b = LocalTime.parse("0" + bat[9]+":00");
        else
            hc53b = LocalTime.parse(bat[9]+":00");
    }

    public void setHc6() {
        hc6 = Integer.parseInt(bat[10]);
    }

    public void setHc7() {
        hc7 = Double.parseDouble(bat[11]);
    }

    public void setSc1a()
    {
        if(bat[12].equals("N/A")) sc1a = 0;
        else sc1a = Integer.parseInt(bat[12]);
    }

    public void setSc1b()
    {
        if(bat[13].equals("N/A")) sc1b = 0;
        else sc1b = Integer.parseInt(bat[13]);
    }

    public void setSc1c()
    {
        if(bat[14].equals("N/A")) sc1c = 0;
        else sc1c = Integer.parseInt(bat[14]);
    }

    public void setSc2()
    {
        if(bat[15].equals("N/A")) sc2 = 0;
        else sc2 = Integer.parseInt(bat[15]);
    }

    public void setSc3a()
    {
        if(bat[16].equals("N/A")) sc3a = 0;
        else sc3a = Integer.parseInt(bat[16]);
    }

    public void setSc3b()
    {
        if(bat[17].equals("N/A")) sc3b = 0;
        else sc3b = Integer.parseInt(bat[17]);
    }

    public void setSc3c()
    {
        if(bat[18].equals("N/A")) sc3c = 0;
        else sc3c = Integer.parseInt(bat[18]);
    }

    public void setSc4()
    {
        if(bat[19].equals("N/A")) sc4 = 0;
        else sc4 = Integer.parseInt(bat[19]);
    }

    public void setSc5aMax()
    {
        if(bat[20].equals("N/A")) sc5aMax = 0;
        else sc5aMax =  Integer.parseInt(bat[20].substring(0, 1));
    }

    public void setSc5aMin()
    {
        if(bat[20].equals("N/A")) sc5aMin = 0;
        else sc5aMin =  Integer.parseInt(bat[20].substring(2, 3));
    }

    public void setSc5bMax()
    {
        if(bat[21].equals("N/A")) sc5bMax = 0;
        else sc5bMax =  Integer.parseInt(bat[21].substring(0, 1));
    }

    public void setSc5bMin()
    {
        if(bat[21].equals("N/A")) sc5bMin = 0;
        else sc5bMin =  Integer.parseInt(bat[21].substring(2, 3));
    }

    public void setSc5cMax()
    {
        if(bat[22].equals("N/A")) sc5cMax = 0;
        else sc5cMax =  Integer.parseInt(bat[22].substring(0, 1));
    }

    public void setSc5cMin()
    {
        if(bat[22].equals("N/A")) sc5cMin = 0;
        else sc5cMin =  Integer.parseInt(bat[22].substring(2, 3));
    }

    public void setSc6()
    {
        if(bat[23].equals("N/A")) sc6 = false;
        if(bat[23].equals("Yes")) sc6 = true;
    }

    public void setSc7()
    {
        if(bat[24].equals("N/A")) sc7 = false;
        if(bat[24].equals("Yes")) sc7 = true;
    }

    public void setSc8()
    {
        if(bat[25].equals("N/A")) sc8 = 0;
        else sc8 = Integer.parseInt(bat[25]);
    }

    public void setSc9()
    {
        if(bat[26].equals("N/A")) sc9 = 0;
        else sc9 = Integer.parseInt(bat[26]);
    }

    public boolean getHc1() {
        return hc1;
    }

    public boolean getHc2() {
        return hc2;
    }

    public double getHc3() {
        return hc3;
    }

    public boolean getHc4() {
        return hc4;
    }

    public LocalTime getHc51a() {
        return hc51a;
    }

    public LocalTime getHc51b() {
        return hc51b;
    }

    public LocalTime getHc52a() {
        return hc52a;
    }

    public LocalTime getHc52b() {
        return hc52b;
    }

    public LocalTime getHc53a() {
        return hc53a;
    }

    public LocalTime getHc53b() {
        return hc53b;
    }

    public int getHc6() {
        return hc6;
    }

    public double getHc7() {
        return hc7;
    }

    public int getSc1a() {
        return sc1a;
    }

    public int getSc1b() {
        return sc1b;
    }

    public int getSc1c() {
        return sc1c;
    }

    public int getSc2() {
        return sc2;
    }

    public int getSc3a() {
        return sc3a;
    }

    public int getSc3b() {
        return sc3b;
    }

    public int getSc3c() {
        return sc3c;
    }

    public int getSc4() {
        return sc4;
    }

    public int getSc5aMax() {
        return sc5aMax;
    }

    public int getSc5aMin() {
        return sc5aMin;
    }

    public int getSc5bMax() {
        return sc5bMax;
    }

    public int getSc5bMin() {
        return sc5bMin;
    }

    public int getSc5cMax() {
        return sc5cMax;
    }

    public int getSc5cMin() {
        return sc5cMin;
    }

    public boolean getSc6() {
        return sc6;
    }

    public boolean getSc7() {
        return sc7;
    }

    public int getSc8() {
        return sc8;
    }

    public int getSc9() {
        return sc9;
    }
}

