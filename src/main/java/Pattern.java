public class Pattern {
    int start;
    String [] pat;

    public Pattern(int mulai, String [] pola)
    {
        start = mulai;
        pat = pola;
    }

    public int getStart() {
        return start;
    }

    public String[] getPat() {
        return pat;
    }
}
