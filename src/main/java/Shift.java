import java.time.LocalTime;

public class Shift {
    int no;
    double[] durasi;
    int kat;
    String nama;
    LocalTime mulai;
    LocalTime akhir;
    String kompe;
    int[] kebutuhan;

    public Shift(int id, double[] duration, int cat, String name, LocalTime start, LocalTime end, String compe)
    {
        no = id; durasi=duration; kat = cat; nama = name; mulai = start; akhir = end; kompe = compe;
    }

    public LocalTime getMulai()
    {
        return mulai;
    }

    public LocalTime getAkhir()
    {
        return akhir;
    }

    public String getKompe()
    {
        return kompe;
    }

    public int getNo()
    {
        return no;
    }

    public double getDurasi(int index)
    {
        return durasi[index];
    }

    public int getKat()
    {
        return kat;
    }

    public String getNama()
    {
        return nama;
    }

    public void setKebutuhan(int[] need)
    {
        kebutuhan = need;
    }

    public int getKebutuhan(int index)
    {
        return kebutuhan[index];
    }

    public void editKebutuhan(int index)
    {
        kebutuhan[index]--;
    }



}

