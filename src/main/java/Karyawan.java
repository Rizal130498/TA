public class Karyawan
{
    int no;
    Double jam;
    int[] minggu;
    String ahli;
    public Karyawan(int id, Double hours, int[] week, String compe)
    {
        no = id; jam=hours; minggu = week; ahli = compe;
    }

    public int getId()
    {
        return no;
    }

    public Double getJam()
    {
        return jam;
    }

    public int[] getMinggu()
    {
        return minggu;
    }

    public String getAhli()
    {
        return ahli;
    }

}
