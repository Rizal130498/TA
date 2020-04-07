import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import javax.swing.*;
import javax.xml.bind.SchemaOutputResolver;
import java.awt.*;
import java.io.*;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Scanner;

public class TA {
    public static String SAMPLE_XLSX_FILE_PATH = "";
    public static String fileOptimasi;
    public static Karyawan[] kar;
    public static Shift[] shifts;
    public static Batasan cons;
    public static String[][] wanted;
    public static String[][] unwanted;
    public static Pattern [] want;
    public static Pattern [] unwant;
    public static int[][] nodays;
    public static int[][] noend;
    public static double [][] batasjam;
    public static double[] fix;
    public static int[][] pdc;
    public static int[][] ndc;
    public static int[] manpowcat;
    public static double[][] mec;
    public static int [] jumweek;
    public static int nomor;
    public static int df;
    public static int pilihan;
    public static Scanner in;
    public static void main (String arg[]) throws IOException, InvalidFormatException, ParseException
    {
        System.out.println("SELAMAT DATANG DI TUGAS AKHIR :)\n");
        System.out.println("Menu : ");
        System.out.println("1. Pembuatan Initial Solution");
        System.out.println("2. Optimasi dari Initial Solution \n");
        System.out.println("Masukkan nomor yang anda inginkan : ");
        in = new Scanner(System.in);
        pilihan  = in.nextInt();
        System.out.println("Pilihan File : ");
        for(int i=0; i<7; i++)
            System.out.println((i+1) + ". Optur "  + (i+1));
        System.out.println("Masukkan nomor file yang anda inginkan : ");
        nomor = in.nextInt();
//        Frame frame = null;
//        FileDialog dialog = new FileDialog(frame, "Select File to Open", FileDialog.LOAD);
//        dialog.setVisible(true);
//        dialog.setAlwaysOnTop(true);
//        if(dialog.isVisible())
//            dialog.toFront();
//        String file = dialog.getFile();
//        System.out.println(file + " chosen.");
//        nomor = Integer.parseInt(file.substring(5,6));
        //SAMPLE_XLSX_FILE_PATH = dialog.getDirectory()+file;
        fileOptimasi = "E://Kuliah/TA/SolusiAwal/Optur" + (nomor) +".txt";
        SAMPLE_XLSX_FILE_PATH = "E://Kuliah/TA/OpTur" + (nomor)+".xls";


        String [][] matkar = readsh1().clone();
        String [][] matshift2 = readsh2().clone();
        String [][] matshift3 = readsh3().clone();
        String [] constraint = readsh4().clone();
        wanted = readsh5a().clone();
        unwanted = readsh5b().clone();
        if(nomor == 7)
            df = 2;
        if(nomor == 3)
            df = 1;
        if(nomor!=7 && nomor!=3)
            df = 1;

        int [] hari = {12, 6, 6, 24, 4, 12, 6};
        jumweek = hari.clone();
//        for(int i=0; i<wanted.length; i++)
//        {
//            for(int j=0; j<wanted[i].length; j++)
//            {
//                System.out.print(wanted[i][j] + " " );
//            }
//            System.out.println();
//        }
//        System.out.println(wanted.length);
//        System.exit(0);
        ////////////////////////////////////// MEMBUAT OBJEK KARYAWAN///////////////////////////

        String [][] wk = new String[matkar.length][];
        for(int i=0; i<matkar.length; i++)
            wk[i] = matkar[i][2].split(", |,");
        int maks = 0;
        for(int i=0; i<wk.length; i++)
        {
            if(maks<wk[i].length)
                maks = wk[i].length;
        }
        int [][] weekendkerja = new int[wk.length][maks];
        for(int i=0; i<wk.length; i++)
            for (int j = 0; j < wk[i].length; j++)
                weekendkerja[i][j] = Integer.parseInt(wk[i][j]);
        kar = new Karyawan [matkar.length];
        for(int i=0; i<matkar.length; i++)
            kar[i] = new Karyawan(Integer.parseInt(matkar[i][0]), Double.parseDouble(matkar[i][1]),
                    weekendkerja[i], matkar[i][3]);

        /////////////////END///////////////////

        //////////////////////////MEMBUAT OBJEK SHIFT///////////////////////////////

        DateFormat waktu = new SimpleDateFormat("hh:mm");
        LocalTime[][] jammulaiakhir = new LocalTime[matshift2.length][2];
        for(int i=0; i<matshift2.length; i++)
        {
            String awal = matshift2[i][10]+":"+matshift2[i][11];
            String akhir = matshift2[i][12]+":"+matshift2[i][13];
            Time a = new Time(waktu.parse(awal).getTime());
            Time b = new Time(waktu.parse(akhir).getTime());
            LocalTime c = a.toLocalTime();
            LocalTime d = b.toLocalTime();
            jammulaiakhir[i][0] = c;
            jammulaiakhir[i][1] = d;
        }

        double[][] durasi = new double[matshift2.length][7];
        for(int i=0; i<durasi.length; i++)
            for(int j=0; j<durasi[i].length; j++)
                durasi[i][j] = Double.parseDouble(matshift2[i][j+1]);
        int[][] kebutuhan = new int[matshift3.length][7];
        for(int i=0; i<kebutuhan.length; i++)
            for(int j=0; j<kebutuhan[i].length; j++)
                kebutuhan[i][j] = Integer.parseInt(matshift3[i][j+1]);

        shifts = new Shift[matshift2.length];
        for(int i=0; i<shifts.length; i++)
            shifts[i] = new Shift(Integer.parseInt(matshift2[i][0]), durasi[i],
            Integer.parseInt(matshift2[i][8]), matshift2[i][9], jammulaiakhir[i][0],
            jammulaiakhir[i][1], matshift2[i][14]);

        for(int i=0; i<shifts.length; i++)
            shifts[i].setKebutuhan(kebutuhan[i]);

        ////////////////END/////////////////////

        ////////////////////////////////MEMBUAT OBJEK BATASAN////////////////////////////
        cons = new Batasan(constraint);
        cons.setHc1(); cons.setHc2(); cons.setHc3(); cons.setHc4(); cons.setHc51a(); cons.setHc51b();
        cons.setHc52a(); cons.setHc52b(); cons.setHc53a(); cons.setHc53b(); cons.setHc6(); cons.setHc7();
        cons.setSc1a(); cons.setSc1b(); cons.setSc1c(); cons.setSc2(); cons.setSc3a(); cons.setSc3b(); cons.setSc3c();
        cons.setSc4(); cons.setSc5aMax(); cons.setSc5aMin(); cons.setSc5bMax();  cons.setSc5bMin();
        cons.setSc5cMax(); cons.setSc5cMin(); cons.setSc6(); cons.setSc7(); cons.setSc8(); cons.setSc9();

        ////////////////////////////////////END///////////////////////////////////////////

        /////////////////////////////////////////MEMBUAT POLA SHIFT YANG DILARANG///////////////////////////////////////
        int hitung = 0;
        for(int i=0; i<shifts.length; i++)
            for(int j=0; j<shifts.length; j++)
            {
                if (shifts[i].getKat() == 3 && shifts[j].getKat() == 2)
                    if (cons.getHc51a().compareTo(shifts[j].getMulai().minusHours(shifts[i].getAkhir().getHour()).minusMinutes(shifts[i].getAkhir().getMinute())) > 0)
                        hitung++;
                if (shifts[i].getKat() == 2 && shifts[j].getKat() == 1)
                    if (cons.getHc52a().compareTo(shifts[j].getMulai().minusHours(shifts[i].getAkhir().getHour()).minusMinutes(shifts[i].getAkhir().getMinute())) > 0)
                        hitung++;
                if(shifts[i].getKat() == 3 && shifts[j].getKat()==1)
                    hitung++;
                ///gapake 1 3 soale lebih  dari 24 jam
            }
        nodays = new int[hitung][2];
        hitung = 0;
        for(int i=0; i<shifts.length; i++)
            for(int j=0; j<shifts.length; j++)
            {
                if (shifts[i].getKat() == 3 && shifts[j].getKat() == 2)
                    if (cons.getHc51a().compareTo(shifts[j].getMulai().minusHours(shifts[i].getAkhir().getHour()).minusMinutes(shifts[i].getAkhir().getMinute())) > 0)
                    {
                        nodays[hitung][0] = i+1;
                        nodays[hitung][1] = j+1;
                        hitung++;
                    }
                if (shifts[i].getKat() == 2 && shifts[j].getKat() == 1)
                    if (cons.getHc52a().compareTo(shifts[j].getMulai().minusHours(shifts[i].getAkhir().getHour()).minusMinutes(shifts[i].getAkhir().getMinute())) > 0)
                    {
                        nodays[hitung][0] = i+1;
                        nodays[hitung][1] = j+1;
                        hitung++;
                    }
                if(shifts[i].getKat()==3 && shifts[j].getKat() == 1)
                {
                    nodays[hitung][0] = i+1;
                    nodays[hitung][1] = j+1;
                    hitung++;
                }
                ///gapake 1 3 soale lebih  dari 24 jam
            }
        hitung = 0;
        for(int i=0; i<shifts.length; i++)
            for(int j=0; j<shifts.length; j++)
            {
                if (shifts[i].getKat() == 3 && shifts[j].getKat() == 2)
                    if (cons.getHc51b().compareTo(shifts[j].getMulai().minusHours(shifts[i].getAkhir().getHour()).minusMinutes(shifts[i].getAkhir().getMinute())) > 0)
                        hitung++;
                if (shifts[i].getKat() == 2 && shifts[j].getKat() == 1)
                    if (cons.getHc52b().compareTo(shifts[j].getMulai().minusHours(shifts[i].getAkhir().getHour()).minusMinutes(shifts[i].getAkhir().getMinute())) > 0)
                        hitung++;
                if(shifts[i].getKat() == 3 && shifts[j].getKat() == 1)
                    hitung++;
                ///gapake 1 3 soale lebih  dari 24 jam
            }
        noend = new int[hitung][2];
        hitung = 0;
        for(int i=0; i<shifts.length; i++)
            for(int j=0; j<shifts.length; j++)
            {
                if (shifts[i].getKat() == 3 && shifts[j].getKat() == 2)
                    if (cons.getHc51b().compareTo(shifts[j].getMulai().minusHours(shifts[i].getAkhir().getHour()).minusMinutes(shifts[i].getAkhir().getMinute())) > 0)
                    {
                        noend[hitung][0] = i+1;
                        noend[hitung][1] = j+1;
                        hitung++;
                    }
                if (shifts[i].getKat() == 2 && shifts[j].getKat() == 1)
                    if (cons.getHc52b().compareTo(shifts[j].getMulai().minusHours(shifts[i].getAkhir().getHour()).minusMinutes(shifts[i].getAkhir().getMinute())) > 0)
                    {
                        noend[hitung][0] = i+1;
                        noend[hitung][1] = j+1;
                        hitung++;
                    }
                if(shifts[i].getKat()==3 && shifts[j].getKat() == 1)
                {
                    noend[hitung][0] = i+1;
                    noend[hitung][1] = j+1;
                    hitung++;
                }
                ///gapake 1 3 soale lebih  dari 24 jam
            }


        ///////////////////////////////////////////////////END//////////////////////////////////////////////////////////

        ///////////////////////////////////////MEMBUAT WANTED SHIFT PATTERN/////////////////////////////////////////////
        if(cons.getSc8()!=0)
        {
            int [] startWanted  = new int[cons.getSc8()];
            int index = 0;
            for(int i=0; i<wanted.length; i++)
            {
                if(wanted[i][1].equals("Monday")) {
                    startWanted[index] = 0; index++;
                }
                if(wanted[i][1].equals("Tuesday")) {
                    startWanted[index] = 1; index++;
                }
                if(wanted[i][1].equals("Wednesday")) {
                    startWanted[index] = 2; index++;
                }
                if(wanted[i][1].equals("Thursday")) {
                    startWanted[index] = 3; index++;
                }
                if(wanted[i][1].equals("Friday")) {
                    startWanted[index] = 4; index++;
                }
                if(wanted[i][1].equals("Saturday")) {
                    startWanted[index] = 5; index++;
                }
                if(wanted[i][1].equals("Sunday")) {
                    startWanted[index] = 6; index++;
                }
            }
            String [][] wh = new String[cons.getSc8()][];
            index = 0;
            for(int i=1; i<wanted.length; i++) {
                wh[index] = wanted[i][2].split(", |,");
                index++;
            }
            want = new Pattern[cons.getSc8()];
            for(int i=0; i<cons.getSc8(); i++)
                want[i]  = new Pattern(startWanted[i], wh[i]);
        }

        //////////////////////////////////////////////////END///////////////////////////////////////////////////////////

        ///////////////////////////////////////MEMBUAT UNWANTED SHIFT PATTERN///////////////////////////////////////////
        if(cons.getSc9()!=0)
        {
            int [] startUnwanted  = new int[cons.getSc9()];
            int index = 0;
            for(int i=0; i<unwanted.length; i++)
            {
                if(wanted[i][1].equals("Monday")) {
                    startUnwanted[index] = 0; index++;
                }
                if(wanted[i][1].equals("Tuesday")) {
                    startUnwanted[index] = 1; index++;
                }
                if(wanted[i][1].equals("Wednesday")) {
                    startUnwanted[index] = 2; index++;
                }
                if(wanted[i][1].equals("Thursday")) {
                    startUnwanted[index] = 3; index++;
                }
                if(wanted[i][1].equals("Friday")) {
                    startUnwanted[index] = 4; index++;
                }
                if(wanted[i][1].equals("Saturday")) {
                    startUnwanted[index] = 5; index++;
                }
                if(wanted[i][1].equals("Sunday")) {
                    startUnwanted[index] = 6; index++;
                }
            }
            String [][] uh = new String[cons.getSc9()][];
            index = 0;
            for(int i=1; i<unwanted.length; i++) {
                uh[index] = unwanted[i][2].split(", |,");
                index++;
            }
            unwant = new Pattern[cons.getSc9()];
            for(int i=0; i<cons.getSc9(); i++)
                want[i]  = new Pattern(startUnwanted[i], uh[i]);
        }

        //////////////////////////////////////////////////END///////////////////////////////////////////////////////////

        batasjam = new double[kar.length][3];
        for(int i=0; i<batasjam.length; i++)
        {
            batasjam[i][0] = kar[i].getJam() - (kar[i].getJam()*0.02);
            batasjam[i][1] = kar[i].getJam();
            batasjam[i][2] = kar[i].getJam() + (kar[i].getJam()*0.02);
        }

        if(pilihan==1)
            InitialSolution();
        if(pilihan==2)
            Optimization();
    }

    public static void Optimization() throws IOException {
        int [][] solusiawal = new int[kar.length][jumweek[(nomor-1)]*7];
        File f = new File(fileOptimasi);

        BufferedReader b = new BufferedReader(new FileReader(f));

        String readLine = "";
        int index = 0;
        while ((readLine = b.readLine()) != null) {
            //System.out.println(readLine);
            String tmp[] = readLine.split(" ");
            for(int i=0;  i<solusiawal[index].length; i++)
                solusiawal[index][i] = Integer.parseInt(tmp[i]);
            index++;
        }

        //validasiHC5(solusiawal);
        Solusi sol = new Solusi(solusiawal);
        sol.Simualated();
        System.out.println(sol.countPenalty());

    }

    public static void InitialSolution() throws IOException {
        pdc = new int [3][7];
        int s1 = 0;
        int s2 = 0;
        int s3 = 0;
        for(int i=0; i<7; i++) {
            for (int j = 0; j < shifts.length; j++) {
                if (shifts[j].getKebutuhan(i) != 0) {
                    if(shifts[j].getKat()==1)
                        s1 =  s1 + shifts[j].getKebutuhan(i);
                    if(shifts[j].getKat()==2)
                        s2 = s2 + shifts[j].getKebutuhan(i);
                    if(shifts[j].getKat()==3)
                        s3 = s3 + shifts[j].getKebutuhan(i);
                }
            }
            pdc[0][i] = s1; s1 =0;
            pdc[1][i] = s2; s2 =0;
            pdc[2][i] = s3; s3 =0;
        }
        ndc = new int[3][7];
        int [][] clonepdc = new  int [3][7];
        int [][] clonendc = new int [3][7];
        for(int i=0; i<clonendc.length; i++)
            for(int j=0 ;  j<clonendc[i].length;j++)
            {
                clonendc[i][j] = ndc[i][j];
                clonepdc[i][j] = pdc[i][j];
            }
        //print(batasjam);
        manpowcat = new int[3];
        for(int i=0; i<7; i++)
            for(int j=0; j<shifts.length; j++)
                if(shifts[j].getKebutuhan(i)!=0)
                {
                    if(shifts[j].getKat()==1)
                        manpowcat[0] = manpowcat[0]+shifts[j].getKebutuhan(i);
                    if(shifts[j].getKat()==2)
                        manpowcat[1] = manpowcat[1]+shifts[j].getKebutuhan(i);
                    if(shifts[j].getKat()==3)
                        manpowcat[2] = manpowcat[2]+shifts[j].getKebutuhan(i);
                }
        for(int i=0; i<manpowcat.length; i++) {
            manpowcat[i] = manpowcat[i] * 6;
        }
        int []mpclone = new int[3];
        for(int i=0 ;i<mpclone.length; i++)
            mpclone[i] = manpowcat[i];
        mec = new double[3][kar.length];
        for(int i=0; i<kar.length; i++)
            for(int j=0; j<mec.length; j++)
                mec[j][i]  = manpowcat[j]/kar[i].getJam();

        int [][] solusiawal = new int[kar.length][(7*jumweek[(nomor-1)])];
        for(int i = 0; i<solusiawal.length; i++)
            for(int j=0; j<solusiawal[i].length;  j++)
                solusiawal[i][j] = 0;

        for (int i = 5; i < 7*jumweek[(nomor-1)]; i=i+7) {
            while (!validasiHarian(solusiawal, i)) {
                int s = apaYangKurang(solusiawal, i);
                int rand = (int) (Math.random() * kar.length);
                if (isOk2(solusiawal, i, s, rand))
                    solusiawal[rand][i] = s;
            }
        }
        for (int i = 6; i < 7*jumweek[(nomor-1)]; i=i+7) {
            while (!validasiHarian(solusiawal, i)) {
                int s = apaYangKurang(solusiawal, i);
                int rand = (int) (Math.random() * kar.length);
                if (isOk2(solusiawal, i, s, rand))
                    solusiawal[rand][i] = s;
            }
        }
        for (int i = 0; i < (7*jumweek[(nomor-1)]); i++) {
            if (i % 7 == 5 || i % 7 == 6)
                continue;
            while (!validasiHarian(solusiawal, i)) {
                int s = apaYangKurang(solusiawal, i);
                int rand = (int) (Math.random() * kar.length);
                if (isOk2(solusiawal, i, s, rand))
                    solusiawal[rand][i] = s;
                }
        }
//
//        Solusi sol = new Solusi(solusiawal);
//        for(int i=5; i<7*jumweek[(nomor-1)]; i=i+7)
//            for(int j=0; j<kar.length; j++)
//                for(int k=1; k<=shifts.length; k++)
//                    if(isOk2(solusiawal, i, k, j))
//                    {
//                        solusiawal[j][i] = k;
//                        break;
//                    }
//        for(int i=6; i<=7*jumweek[(nomor-1)]; i=i+7)
//            for(int j=0; j<kar.length; j++)
//                for(int k=1; k<=shifts.length; k++)
//                    if(isOk2(solusiawal, i, k, j))
//                    {
//                        solusiawal[j][i] = k;
//                        break;
//                    }
//        luar : for(int i=0; i<7*jumweek[(nomor-1)]; i++)
//        {
//            if(i%7==5||i%7==6)
//                continue luar;
//            for(int j=0; j<kar.length; j++)
//            {
//                for(int k=1; k<=shifts.length; k++)
//                {
//                    if(isOk2(solusiawal, i, k, j))
//                    {
//                        solusiawal[j][i] = k;
//                        break;
//                    }
//                }
//            }
//        }
//        System.out.println(sol.countPenalty());
//        print(solusiawal);
//        Solusi sol  = new Solusi(solusiawal);
//        System.out.println(sol.countPenalty());

//        exploreWeekend(solusiawal, 5, 0, 1);

//        pp: for(int x=0; x<7*jumweek[(nomor-1)]; x++) {
//            //System.out.println("hari " + x);
//            if(x%7==5||x%7==6)
//                continue pp;
//            if(x%7==0)
//            {
//                for(int u=0; u<ndc.length; u++)
//                    for(int t=0; t<ndc[u].length; t++)
//                    {
//                        ndc[u][t] = clonendc[u][t];
//                        pdc[u][t] = clonepdc[u][t];
//                    }
//                for(int u=0; u<mpclone.length; u++)
//                    manpowcat[u] = mpclone[u];
//            }
//            outer:
//            for (int f = 0; f < 10; f++) {
//                //System.out.println("hari " + x);
//                int cat = critShiftCat(x);
////                if(x==0)
////                    System.out.println("ini cat = " + cat );
//                if (cat != 0) {
//                    int[] sh = shiftFromCrit(cat).clone();
//                    int[] ky = critEmp2(cat).clone();
//                    int k = -1;
//                    int shi = 0;
//                    for (int i = 0; i < sh.length; i++) {
//                        shi = (sh[i] + 1);
//                        for (int m = 0; m < ky.length; m++) {
//                            k = ky[m];
//                            //System.out.print("assign shift " + shi + " untuk karyawan " + k + " pada hari " + x);
//                            if (isOk2(solusiawal, x, shi, k)) {
//                                //System.out.println("  Berhasil");
//                                solusiawal[k][x] = shi;
//                                pdc[(cat - 1)][x % 7]--;
//                                ndc[(cat - 1)][x % 7]++;
//                                manpowcat[(cat - 1)]--;
//                                mec[(cat - 1)][k] = (double) manpowcat[(cat - 1)] / (double) kar[k].getJam();
//                                for (int j = 0; j < mec.length; j++)
//                                    mec[j][k] = 0;
//                                continue outer;
//                            }
//                        }
//                    }
//                }
//            }
//            updateMec();
//        }

//        int hari = jumweek[(TA.nomor-1)]*7;
//        int [][] sol2  = new int[solusiawal.length][hari];
//        double initialcost = diffHour(solusiawal);
//        double [] penn = new double[200];
//        for(int i=0; i<penn.length; i++)
//            penn[i] = initialcost;
//        ganda(solusiawal, sol2);
//        int idle = 0;
//        for(int i=0; i<200000; i++)
//        {
//            int llh = (int) (Math.random()*3);
//            switch(llh)
//            {
//                case 0 :
//                    exchange2(solusiawal);
//                case 1 :
//                    exchange3(solusiawal);
//                case 2 :
//                    doubleExchange2(solusiawal);
//            }
//            if(validasiHC4(solusiawal) && validasiHC5(solusiawal) && validasiHC7(solusiawal))
//            {
//                int v = i%penn.length;
//                if(diffHour(solusiawal)<penn[v] || diffHour(solusiawal)<=initialcost)
//                {
//                    initialcost = diffHour(solusiawal);
//                    ganda(solusiawal, sol2);
//                }
//                else
//                    ganda(sol2, solusiawal);
//                if(initialcost<penn[v])
//                    penn[v] = initialcost;
//            }
//            else
//            {
//                ganda(sol2, solusiawal);
//            }
//            System.out.println("Iterasi ke " + (i+1) + " dengan penalti " + initialcost + " idle nya " + idle);
//        }

        int[][] kopi =  new int[kar.length][7*jumweek[(nomor-1)]];
        ganda(solusiawal, kopi);
        double jamsolusi = diffHour(solusiawal);
        for(int  i =0; i<50000; i++)
        {
            int llh = (int) (Math.random()*3);
            if(llh == 0)
                exchange2(kopi);
            if(llh == 1)
                exchange3(kopi);
            if(llh == 2)
                doubleExchange2(kopi);
            if(validasiHC4(kopi))
            {
                if(validasiHC5(kopi))
                {
                    if(validasiHC7(kopi))
                    {
                        if(diffHour(kopi)<=jamsolusi)
                        {
                            ganda(kopi, solusiawal);
                            jamsolusi = diffHour(kopi);
                        }
                        else
                        {
                            ganda(solusiawal, kopi);
                        }
                    }
                    else
                    {
                        ganda(solusiawal, kopi);
                    }
                }
                else
                {
                    ganda(solusiawal, kopi);
                }
            }
            else
            {
                ganda(solusiawal, kopi);
            }
            System.out.println("Iterasi ke " + (i+1) + " " + jamsolusi);
        }



//
        if(validasiHC2(solusiawal))
        {
            if(validasiHC3(solusiawal))
            {
                if(validasiHC4(solusiawal))
                {
                    if(validasiHC5(solusiawal))
                    {
                        if(validasiHC7(solusiawal))
                        {
                            if(validasiHC6(solusiawal))
                            {
                            }
                            else {
                                int[][] dua = new int[kar.length][7 * jumweek[(nomor - 1)]];
                                ganda(solusiawal, dua);
                                int px = 0;
                                do {
                                    System.out.println("Iterasi ke " + (px + 1) + " pada do while");
                                    px++;
                                    int llh = (int) (Math.random() * 3);
//                                    System.out.println("sudah sampek sini" + llh);
                                    if (llh == 0)
                                        exchange2(dua);
                                    if (llh == 1)
                                        exchange3(dua);
                                    if (llh == 2)
                                        doubleExchange2(dua);
                                    //System.out.println("sudah sampek sini");
                                    if (validasiHC3(dua)) {
                                        if (validasiHC4(dua)) {
                                            if (validasiHC5(dua)) {
                                                if (validasiHC7(dua)) {
                                                    ganda(dua, solusiawal);
                                                } else
                                                    ganda(solusiawal, dua);
                                            } else
                                                ganda(solusiawal, dua);
                                        } else
                                            ganda(solusiawal, dua);
                                    } else
                                        ganda(solusiawal, dua);
                                } while (!validasiHC6(solusiawal));
                            }
                        }
                        else
                            System.out.println("hc 7 tidak feasible");
                    }
                    else
                        System.out.println("hc5 tidak feasible");
                }
                else
                    System.out.println("hc4 tidak feasible");
            }
            else
                System.out.println("hc3 tidak feasible");
        }
        else
            System.out.println("hc 2 tidak feasible");


        if(validasiSemua(solusiawal)==0) {
            Solusi sol = new Solusi(solusiawal);
            print(solusiawal);
            System.out.println("\nSolusi awal untuk Optur" + nomor + " berhasil terbentuk dengan penalty = " + sol.countPenalty());
            System.out.println("Apakah anda ingin menyimpan solusi?");
            System.out.println("1. Ya \n2. Tidak");
            int save = in.nextInt();
            if(save == 1) {
                simpanSolusiNomor(solusiawal, nomor);
                simpanSolusiShift(solusiawal, nomor);
            }
        }
        else {
            System.out.println("HC " + validasiSemua(solusiawal) + " tidak feasible");
        }
    }

    public static int validasiSemua(int[][] solusi)
    {
        if(!validasiHC2(solusi)) return 2;
        if(!validasiHC3(solusi)) return 3;
        if(!validasiHC4(solusi)) return 4;
        if(!validasiHC5(solusi)) return 5;
        if(!validasiHC6(solusi)) return 6;
        if(!validasiHC7(solusi)) return 7;
        return 0;
    }

    public static void ganda(int [][] asli, int[][] kopi)
    {
        for(int i=0;  i<kopi.length; i++)
            for(int j=0; j<kopi[i].length; j++)
                kopi[i][j] = asli[i][j];
    }

    public static double[] hitungJamMingguan(int[][] solusi, int minggu)
    {
        double jam [] = new double[kar.length];
        for(int i=minggu*7; i<(minggu+1)*7; i++)
            for(int j=0; j<kar.length; j++)
                if(solusi[j][i]!=0)
                    jam[j] = jam[j] + shifts[solusi[j][i]-1].getDurasi(i%7);
        return jam;
    }

    public static double[] jamSemua(int[][] solusi, int jumlahweek)
    {
        double jam[] = new double[kar.length];
        for(int i=0; i<jumlahweek; i++)
            for(int j=0; j<kar.length; j++)
                jam[j]  = jam[j] + hitungJamMingguan(solusi, i)[j];
        for(int i=0; i<kar.length; i++)
            jam[i] =  (jam[i]/jumlahweek);
        return jam;
    }

    public static double carapenalty6(int [][] solusi)
    {
        double p6 = 0;
        for (int i = 0; i < TA.kar.length; i++)
        {
            double jam = (TA.batasjam[i][1]) * (TA.jumweek[(TA.nomor - 1)]);
            double harian = 0;
            for (int j = 0; j < TA.jumweek[(TA.nomor - 1)] * 7; j++)
            {
                if (solusi[i][j] != 0)
                    harian = harian + TA.shifts[solusi[i][j] - 1].getDurasi(j % 7);
            }
            jam = jam - harian;
            p6 = p6 + (jam * jam);
        }
        p6 = Math.sqrt(p6);
        return p6;
    }

    public static double diffHour(int[][] solusi)
    {
        double jam = 0;
        double []first = jamSemua(solusi, jumweek[(nomor-1)]).clone();
        for(int i=0; i<first.length; i++) {
            jam = jam + (double) (Math.abs(first[i]-batasjam[i][df]));
        }
        return jam;
    }

    public static void updateMec()
    {
        for(int i=0; i<kar.length; i++)
            for(int j=0; j<mec.length; j++)
                mec[j][i]  = manpowcat[j]/kar[i].getJam();
    }
    public static int critEmp(int shiftCat)
    {
        double highest = 0;
        int indexh = -1;
        for(int i=0; i<kar.length; i++)
            if(mec[(shiftCat-1)][i]>highest)
            {
                highest = mec[(shiftCat-1)][i];
                indexh = i;
            }
        return indexh;
    }

    public static int[] critEmp2(int shiftCat)
    {
        double[][] pekerja = new double[2][kar.length];
        for(int i=0; i<mec[(shiftCat-1)].length; i++)
        {
            pekerja[0][i] = i;
            pekerja[1][i] = mec[(shiftCat-1)][i];
        }
        double[] pekerja2 = new double[kar.length];
        int [] urutanKar = new int[kar.length];

        for(int i=0; i<pekerja2.length; i++)
        {
            double highest = -1;
            int index = -1;
            for(int j=0; j<pekerja[1].length; j++)
            {
                if(pekerja[1][j]>highest)
                {
                    highest = pekerja[1][j];
                    index = j;
                }
            }
            urutanKar[i] = index;
            pekerja2[i] = highest;
            pekerja[1][index] = -1;
        }
        //System.out.println("Karyawan " + urutanKar[0] + " dengan mec  " +mec[(shiftCat-1)][urutanKar[0]]);
        return urutanKar;
    }

    public static int[] shiftFromCrit(int shiftCat)
    {
        int panjang = 0;
        for(int i=0; i<shifts.length; i++)
            if(shifts[i].getKat()==shiftCat)
                panjang++;
        String [] fixshift1 = new String[panjang];
        int [] fixshift = new int[panjang];
        int index = 0;
        for(int i=0; i<shifts.length; i++)
            if(shifts[i].getKat()==shiftCat)
            {
                fixshift1[index] = shifts[i].getNama();
                index++;
            }
        Arrays.sort(fixshift1);
        for(int i=0; i<fixshift1.length; i++)
            for(int j=0; j<shifts.length; j++)
                if(fixshift1[i].equals(shifts[j].getNama()))
                {
                    fixshift[i] = j;
                }

        return fixshift;
    }

    public static int critShiftCat(int hari)
    {
        int nolndc = 0;
        for(int i=0; i<ndc.length; i++)
            if(ndc[i][hari%7]==0)
                nolndc++;
//        if(hari==7)
//            System.out.println(nolndc);
        if(nolndc==3)
        {
            int highest = 0;
            int indexh = -1;
            for(int i=0; i<pdc.length; i++)
                if(pdc[i][hari%7]>highest)
                {
                    highest = pdc[i][hari%7];
                    indexh = i;
                }
            return (indexh+1);
        }
        if(nolndc==2)
        {
            int highest = 0;
            int indexh = -1;
            for(int i=0; i<ndc.length; i++)
            {
                if(ndc[i][hari%7]==0)
                    if(pdc[i][hari%7]>highest)
                    {
                        highest = pdc[i][hari%7];
                        indexh = i;
                    }
            }
            return (indexh+1);
        }
        if(nolndc==1)
        {
            for(int i=0; i<ndc.length; i++)
                if(ndc[i][hari%7]==0)
                    return (i+1);
        }
        if(nolndc==0)
        {
            double highest = 0;
            double compare = 0;
            int indexh = -1;
            for(int i=0; i<ndc.length; i++) {
                compare = ((double)pdc[i][hari%7])/((double) ndc[i][hari%7]);
                if (compare > highest) {
                    highest = compare;
                    indexh = i;
                }
            }
//            if(hari==7) {
//                print(pdc);
//            }
            return (indexh+1);
        }
        return 0;
    }

    public static void doubleExchange2(int[][] solusi)
    {
        int randkar1 = -1;
        int randkar2 = -1;
        int randhari1 = -1;
        int randhari2 = -1;
        while (randkar1 == -1 ||  randkar2 == -1) {
            do {
                randhari1 = (int) (Math.random() * (7 * jumweek[(nomor - 1)]));
                randhari2 = (int) (Math.random() * (7 * jumweek[(nomor - 1)]));
            }while(randhari1%7==5 || randhari1%7==6 || randhari2%7==5 ||randhari2%7==6);
            for (int i = 0; i < kar.length; i++) {
                for (int j = 0; j < kar.length; j++) {
                    if (solusi[i][randhari1] != 0 && solusi[j][randhari2] != 0 && shifts[solusi[i][randhari1] - 1].getKat() == shifts[solusi[j][randhari2] - 1].getKat()) {
                        if (solusi[i][randhari2] == 0 && solusi[j][randhari1] == 0) {
                            randkar1 = i;
                            randkar2 = j;
                        }
                    }
                }
            }
        }
        solusi[randkar2][randhari1] = solusi[randkar1][randhari1];
        solusi[randkar1][randhari1] = 0;
        solusi[randkar1][randhari2] = solusi[randkar2][randhari2];
        solusi[randkar2][randhari2] = 0;
    }

    public static void exchange3(int [][] solusi)
    {
        int randhari = (int) (Math.random() * (7 * jumweek[nomor - 1]));
        int randkar1 = -1;
        int randkar2 = -1;
        int randkar3 = -1;
        do{
            randkar1 = (int) (Math.random()*kar.length);
        }while(solusi[randkar1][randhari]==0 || randkar1==randkar2 || randkar1==randkar3);
        do{
            randkar2 = (int) (Math.random()*kar.length);
        }while(solusi[randkar2][randhari]==0 || randkar2==randkar1 || randkar2==randkar3);
        do{
            randkar3 = (int) (Math.random()*kar.length);
        }while(solusi[randkar3][randhari]==0 || randkar3==randkar1 || randkar3==randkar2);
        int temp = solusi[randkar1][randhari];
        solusi[randkar1][randhari] = solusi[randkar2][randhari];
        solusi[randkar2][randhari] = solusi[randkar3][randhari];
        solusi[randkar3][randhari] = temp;
    }

    public static void exchange2(int[][] solusi) {
        int randhari  =-1;
        do {
            randhari = (int) (Math.random() * (7 * jumweek[nomor - 1]));
        }while(randhari%7==5||randhari%7==6);
        int randkar1 = -1;
        int randkar2 = -1;
        do{
            randkar1 = (int) (Math.random()*kar.length);
        }while(solusi[randkar1][randhari]==0);
        do{
            randkar2 = (int) (Math.random()*kar.length);
        }while (solusi[randkar2][randhari]!=0);

        int copy = solusi[randkar1][randhari];
        solusi[randkar1][randhari] = solusi[randkar2][randhari];
        solusi[randkar2][randhari] =  copy;
    }

    public static void exploreWeekend(int[][] solusi, int hari, int karyawan, int shift)
    {
        if(hari<7*jumweek[(nomor-1)])
        {
            if(isOk2(solusi, hari, shift, karyawan))
            {
                solusi[karyawan][hari] = shift;
                if(karyawan<kar.length-1)
                    exploreWeekend(solusi, hari, karyawan+1, 1);
                else
                {
                    if(hari%7==5)
                        exploreWeekend(solusi, hari+1, 0, 1);
                    if(hari%7==6)
                        exploreWeekend(solusi, hari+6, 0, 1);
                }
            }
            else
            {
                if(shift<shifts.length)
                    exploreWeekend(solusi, hari, karyawan, shift+1);
                else
                {
                    if(karyawan<kar.length-1)
                        exploreWeekend(solusi, hari, karyawan+1, 1);
                    else
                    {
                        if(hari%7==5)
                            exploreWeekend(solusi, hari+1, 0, 1);
                        if(hari%7==6)
                            exploreWeekend(solusi, hari+6, 0, 1);
                    }
                }
            }
        }
        else {
            explore(solusi, 0, 0, 1);
        }
    }

    public static void explore(int[][] solusi, int hari, int karyawan, int shift)
    {
        if(hari<7*jumweek[(nomor-1)])
        {
            if(isOk2(solusi, hari,  shift, karyawan))
            {
                solusi[karyawan][hari] = shift;
                if(karyawan<kar.length-1)
                    explore(solusi, hari, karyawan+1, 1);
                else
                {
                    if((hari+1)%7==5)
                        explore(solusi, hari+3, 0, 1);
                    else
                        explore(solusi, hari+1, 0, 1);
                }
            }
            else
            {
                if(shift<shifts.length)
                    explore(solusi, hari, karyawan, shift+1);
                else
                {
                    if(karyawan<kar.length-1)
                        explore(solusi, hari, karyawan+1, 1);
                    else
                    {
                        if((hari+1)%7==5) {
                            explore(solusi, hari + 3, 0, 1);
                        }
                        else
                            explore(solusi, hari+1, 0, 1);
                    }
                }
            }
        }
    }

    public static boolean isOk2(int[][]solusi, int hari, int shift, int karyawan)
    {
        if(shifts[shift-1].getKebutuhan(hari%7)>cekKebutuhan(solusi, hari, shift))
            if(isOk4com2(karyawan, shift))
                if(isOk4week(karyawan, hari))
                    if(isOk7(solusi, karyawan, hari, shift))
                        if(isOk5(solusi, karyawan, shift, hari))
                            return true;
        //System.out.println("Error di ok2");
        return false;
    }

    public static int cekKebutuhan(int[][]solusi, int hari, int shift)
    {
        int count = 0;
        for(int i=0; i<solusi.length; i++)
            if(solusi[i][hari]==shift)
                count++;
        return count;
    }

    public static boolean isOk3(int[][] solusi) {
        int minggu = jumweek[(nomor-1)];
        double[][] jam = new double[minggu][kar.length];
        for (int i = 0; i < minggu; i++)
            jam[i] = hitungJamMingguan(solusi, i);
        fix = new double[kar.length];
        for (int j = 0; j < minggu; j++) {
            for (int i = 0; i < fix.length; i++) {
                fix[i] = fix[i] + jam[j][i];
            }
        }
        for(int i=0; i<fix.length; i++)
            fix[i] = fix[i]/minggu;
        for(int i=0; i<fix.length; i++)
            if(fix[i]<batasjam[i][0] || fix[i]>batasjam[i][2]) {
                //System.out.println("Eror di ok3");
                return false;
            }
        return true;
    }

    public static boolean isOk4com2(int karyawan, int shift)
    {
        if(shifts[shift-1].getKompe().equals("A") && kar[karyawan].getAhli().equals("")) {
            //System.out.println("erot di ok4com");
            return false;
        }
        return true;
    }

    public static boolean isOk4week(int karyawan, int hari)
    {
        if(hari%7 == 5 || hari%7 == 6)
        {
            for(int i=0; i<kar[karyawan].getMinggu().length; i++)
            {
                if(hari/7 == kar[karyawan].getMinggu()[i]-1)
                    return true;
            }
        }
        else
            return true;
        //System.out.println("eror di ok4week");
        return false;
    }

    public static boolean isOk5(int [][] solusi, int karyawan ,int shift, int hari)
    {
        if(hari==0)
            return true;
        if(hari%7==5 || hari%7==6) {
            for (int i = 0; i < noend.length; i++) {
                if (solusi[karyawan][hari - 1] == noend[i][0] && shift == noend[i][1]) {
                    //System.out.println("eror di ok5");
                    return false;
                }
                if (hari < (7*jumweek[(nomor-1)])-1)
                    if (solusi[karyawan][hari + 1] == noend[i][1] && shift == noend[i][0]) {
                        //System.out.println("eror di ok5");
                        return false;
                    }
            }
        }
        if(hari%7==0 || hari%7==1 || hari%7==2 || hari%7==3 || hari%7==4) {
            for (int i = 0; i < nodays.length; i++) {
                if (solusi[karyawan][hari - 1] == nodays[i][0] && shift == nodays[i][1]) {
                    //System.out.println("eror di ok5");
                    return false;
                }
                if (hari < (7*jumweek[(nomor-1)])-1)
                    if (solusi[karyawan][hari + 1] == nodays[i][1] && shift == nodays[i][0]) {
                        //System.out.println("eror di ok5");
                        return false;
                    }
            }
        }
        return true;
    }

    public static boolean isOk7(int[][] solusi, int karyawan, int hari, int shift)
    {
        double durasi = shifts[shift-1].getDurasi(hari%7);
        if(hitungJamperMinggu(solusi, hari, karyawan)+durasi<=cons.getHc7())
            return true;
        //System.out.println("eror di ok7");
        return false;
    }

    public static double hitungJamperMinggu(int[][] solusi, int hari, int karyawan)
    {
        double hitung = 0;
        int nomer = hari/7;
        for(int i=7*nomer; i<7*(nomer+1); i++)
            if(solusi[karyawan][i]!=0)
                hitung = hitung + shifts[solusi[karyawan][i]-1].getDurasi(i%7);
        return hitung;
    }
    public static void print(int[][] cetak)
    {
        for(int i = 0; i<cetak.length; i++)
        {
            for(int j=0; j<cetak[i].length; j++)
            {
                System.out.print(cetak[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static boolean validasiHC2(int[][] solusi)
    {
        for(int i=0; i<(7*jumweek[(nomor-1)]); i++)
        {
            for(int j=0; j<shifts.length; j++)
            {
                if(shifts[j].getKebutuhan(i%7)!=cekKebutuhan(solusi, i, j+1)) {
                    //System.out.println("shift " + (j+1) + " pada hari " + i + ", harusya " + shifts[j].getKebutuhan(i%7) + " tapi masih ada " + cekKebutuhan(solusi, i, j+1));
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean validasiHC3(int[][] solusi)
    {
        double[] jam = jamSemua(solusi, jumweek[(nomor-1)]).clone();
        for(int i=0; i<jam.length; i++) {
            if (jam[i] > batasjam[i][2])
            {
                //System.out.println("Karyawan " + (i+1) + " jamnya " +  jam[i] + " melebihi " + batasjam[i][2]);
                return false;
            }
            if(jam[i]<batasjam[i][0]) {
                //System.out.println("Karyawan " + (i+1) + " jamnya " +  jam[i] + " kurangdari " + batasjam[i][0]);
                return false;
            }
        }
        return true;
    }

    public static boolean validasiHC4(int[][] solusi)
    {
        for(int i=0; i<solusi.length; i++)
        {
            for(int j=0; j<solusi[i].length; j++)
            {
                if(solusi[i][j]!=0)
                    if(shifts[solusi[i][j]-1].getKompe().equals("A") && kar[i].getAhli().equals(""))
                        return false;
            }
        }
        return true;
    }

    public static boolean validasiHC5(int[][] solusi)
    {
        for(int i=0; i<(7*jumweek[(nomor-1)]); i++)
        {
            for(int j=0; j<kar.length; j++)
            {
                if(i%7!=5||i%7!=6) {
                    for (int k = 0; k < nodays.length; k++) {
                        if (i < (7 * jumweek[(nomor - 1)]) - 1)
                            if (solusi[j][i] == nodays[k][0] && solusi[j][(i + 1)] == nodays[k][1]) {
                               //System.out.println("pada hari ke " + i + " ada shift " + nodays[k][0] + " " + nodays[k][1]);
                                return false;
                            }
                    }
                }
                else
                {
                    for (int k = 0; k < noend.length; k++) {
                        if (i < (7 * jumweek[(nomor - 1)]) - 1)
                            if (solusi[j][i] == noend[k][0] && solusi[j][(i + 1)] == noend[k][1]) {
                                //System.out.println("pada hari ke " + i + " ada shift " + nodays[k][0] + " " + nodays[k][1]);
                                return false;
                            }
                    }
                }
            }
        }
        return true;
    }

    public static boolean[] cekLiburMingguan(int[][] solusi, int minggu)
    {

        boolean[] sip = new boolean[kar.length];
        for(int  i=0; i<sip.length; i++)
            sip[i] = false;
        ll : for(int j=0;  j<kar.length; j++)
        {
            for(int i=7*minggu; i<(minggu+1)*7;i++)
            {
                if(solusi[j][i]==0)
                {
                    if(i==minggu*7)
                    {
                        if(solusi[j][i+1]==0)
                        {
                            sip[j] = true;
                            continue ll;
                        }
                        else
                        {
                            if(shifts[solusi[j][i+1]-1].getKat()!=1)
                            {
                                sip[j] = true;
                                continue ll;
                            }
                            else
                            {
                                LocalTime kurang =  LocalTime.parse("08:00");
                                if(shifts[solusi[j][i+1]-1].getMulai().isAfter(kurang));
                                {
                                    sip[j] = true;
                                    continue ll;
                                }
                            }
                        }
                    }
                    if(i>minggu*7 && i<((minggu+1)*7)-1)
                    {
                        if(solusi[j][i+1]==0||solusi[j][i-1]==0)
                        {
                            sip[j] = true;
                            continue ll;
                        }
                        if(solusi[j][i+1]!=0&&shifts[solusi[j][i+1]-1].getKat()!=1)
                        {
                            sip[j] = true;
                            continue ll;
                        }
                        if(solusi[j][i+1]!=0&&shifts[solusi[j][i+1]-1].getKat()==1)
                        {
                            if(shifts[solusi[j][i-1]-1].getKat()!=3)
                            {
                                LocalTime  kurang  = LocalTime.parse("08:00");
                                LocalTime batas = LocalTime.parse("00:00");
                                LocalTime sebelum = batas.minusHours(shifts[solusi[j][i-1]-1].getAkhir().getHour()).minusMinutes(shifts[solusi[j][i-1]-1].getAkhir().getMinute());
                                LocalTime total = sebelum.plusHours(shifts[solusi[j][i+1]-1].getMulai().getHour()).minusMinutes(shifts[solusi[j][i+1]-1].getMulai().getMinute());
                                if(total.isAfter(kurang))
                                {
                                    sip[j] = true;
                                    continue ll;
                                }
                            }
                        }
                    }
                    if(i==((minggu+1)*7)-1)
                    {
                        if(solusi[j][i-1]==0)
                        {
                            sip[j] = true;
                            continue ll;
                        }
                        else
                        {
                            LocalTime  kurang  = LocalTime.parse("08:00");
                            LocalTime batas = LocalTime.parse("00:00");
                            LocalTime sebelum = batas.minusHours(shifts[solusi[j][i-1]-1].getAkhir().getHour()).minusMinutes(shifts[solusi[j][i-1]-1].getAkhir().getMinute());
                            if(sebelum.isAfter(kurang))
                            {
                                sip[j] = true;
                                continue ll;
                            }
                        }
                    }
                }
            }
        }
        return sip;
    }

    public static boolean validasiHC6(int[][] solusi)
    {
        boolean[][] cek = new boolean [kar.length][jumweek[(nomor-1)]];
        for(int i=0;i<jumweek[(nomor-1)];i++)
        {
            boolean [] z  = cekLiburMingguan(solusi, i).clone();
            {
                for(int j =0 ;j<z.length; j++)
                {
                    cek[j][i] = z[j];
                }
            }
        }

        for(int i=0; i<cek.length;  i++)
            for(int j=0;j<cek[i].length;j++)
                if(!cek[i][j]) {
                    return false;
                }
        return true;
    }

    public static boolean validasiHC7(int[][] solusi)
    {
        for(int i=0; i<(7*jumweek[(nomor-1)]); i++)
        {
            for(int j=0; j<kar.length; j++)
            {
                if(hitungJamperMinggu(solusi, i, j)>cons.getHc7())
                    return false;
            }
        }
        return true;
    }

    public static boolean validasiHarian(int[][] solusi, int hari)
    {
        for(int i=0; i<shifts.length; i++)
            if(shifts[i].getKebutuhan(hari%7)!=cekKebutuhan(solusi, hari, i+1))
                return false;
        return true;
    }

    public static int apaYangKurang(int[][]solusi, int hari)
    {
        for(int i =0; i<shifts.length; i++)
            if(shifts[i].getKebutuhan(hari%7)>cekKebutuhan(solusi, hari, i+1))
                return i+1;
        return 0;
    }

    public static void simpanSolusiNomor(int [][] solusi, int nomor) throws IOException {
        FileWriter writer = new FileWriter("E:\\Kuliah\\TA\\SolusiAwal\\Optur" + nomor + ".txt", false);
        for (int i = 0; i <solusi.length; i++) {
            for (int j = 0; j <solusi[i].length; j++) {
                writer.write(solusi[i][j]+ " ");
            }
            //this is the code that you change, this will make a new line between each y value in the array
            writer.write("\n");   // write new line
        }
        writer.close();
    }

    public static void simpanSolusiShift(int [][] solusi, int nomor) throws IOException {
        FileWriter writer = new FileWriter("E:\\Kuliah\\TA\\SolusiAwal\\Optur" + nomor + "Shift.txt", false);
        for (int i = 0; i <solusi.length; i++) {
            for (int j = 0; j <solusi[i].length; j++) {
                if(solusi[i][j]!=0)
                    writer.write(shifts[solusi[i][j]-1].getNama()+ " ");
                else writer.write("<Free>" + " ");
            }
            //this is the code that you change, this will make a new line between each y value in the array
            writer.write("\n");   // write new line
        }
        writer.close();
    }

//    public static int[][] readSol()
//    {
//
//    }

    public static String[][] readsh1() throws IOException, InvalidFormatException
    {
        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        int bawah = 0;
        int sampingx = 0;
        int sampingf = 0;
        int start = 6;
        Row row;
        for(int i=start-1; i<=sheet.getLastRowNum(); i++)
        {
            row = sheet.getRow(i);
            for (Cell cell : row)
                sampingx++;
            if(sampingf<sampingx)
                sampingf = sampingx;
            sampingx=0;
            bawah++;
        }
        String [][] xx = new String[bawah][sampingf];
        bawah = 0;
        for(int i=start-1; i<=sheet.getLastRowNum(); i++)
        {
            row = sheet.getRow(i);
            for (Cell cell : row) {
                String cellValue = dataFormatter.formatCellValue(cell);
                xx[bawah][sampingx] = cellValue;
                sampingx++;
            }
            sampingx=0;
            bawah++;
        }
        return xx;
    }

    public static String[][] readsh2() throws IOException, InvalidFormatException, ParseException
    {
        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(1);
        DataFormatter dataFormatter = new DataFormatter();
        int bawah = 0;
        int sampingx = 0;
        int sampingf = 0;
        int start = 5;
        Row row;
        for(int i=start-1; i<=sheet.getLastRowNum(); i++)
        {
            row = sheet.getRow(i);
            for (Cell cell : row)
                sampingx++;
            if(sampingf<sampingx)
                sampingf = sampingx;
            sampingx = 0;
            bawah++;
        }
        String [][]x = new String[bawah][sampingf];
        //System.out.println(bawah);
        bawah = 0;
        for(int i=start-1; i<=sheet.getLastRowNum(); i++)
        {
            row  = sheet.getRow(i);
            for(Cell cell : row)
            {
                String cellValue =  dataFormatter.formatCellValue(cell);
                x[bawah][sampingx] = cellValue;
                sampingx++;
            }
            sampingx = 0;
            bawah++;
        }
        return x;
    }

    public static String[][] readsh3() throws IOException, InvalidFormatException
    {
        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(2);
        DataFormatter dataFormatter = new DataFormatter();
        int start = 6;
        int bawah = 0;
        int sampingx = 0;
        int sampingf = 0;
        Row row;
        for(int i=start-1; i<=sheet.getLastRowNum(); i++)
        {
            row = sheet.getRow(i);
            for(Cell cell : row)
                sampingx++;
            if(sampingf<sampingx)
                sampingf = sampingx;
            sampingx=0;
            bawah++;
        }
        String [][] x = new String[bawah][sampingf];
        bawah = 0;
        for(int i=start-1; i<=sheet.getLastRowNum(); i++)
        {
            row = sheet.getRow(i);
            for(Cell cell : row)
            {
                String cellValue = dataFormatter.formatCellValue(cell);
                x[bawah][sampingx] = cellValue;
                sampingx++;
            }
            sampingx = 0;
            bawah++;
        }
        return x;
            //for(int i=0; i<kebutuhan.length; i++)
            //shift[i].
    }

    public static String[] readsh4() throws IOException, InvalidFormatException
    {
        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(3);
        DataFormatter dataFormatter = new DataFormatter();
        int sampingf = 0;
        int sampingx = 0;
        int bawah = 0;
        for(Row row : sheet)
        {
            for(Cell cell : row)
                sampingx++;
            if(sampingf<sampingx)
                sampingf = sampingx;
            sampingx = 0;
            bawah++;
        }
        String[][] hasil = new String[bawah][sampingf];
        bawah = 0;
        for(Row row : sheet)
        {
            for(Cell cell : row)
            {
                String cellValue = dataFormatter.formatCellValue(cell);
                hasil[bawah][sampingx] = cellValue;
                sampingx++;
            }
            sampingx = 0;
            bawah++;
        }
        String[]x = new String[27];
        boolean start = false;
        int p = 0;
        for(int i=0; i<hasil.length; i++)
        {
            if(start) {
                if(hasil[i][3].isEmpty())
                    continue;
                x[p] = hasil[i][3];
                p++;
            }
            if(hasil[i][0].equals("SOFT CONSTRAINTS"))
                start = true;
        }
        return x;
    }

    public static String[][] readsh5a() throws IOException, InvalidFormatException
    {
        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(4);
        DataFormatter dataFormatter = new DataFormatter();
        int bawah = 0;
        int sampingx = 0;
        int sampingf = 0;
        for(Row row : sheet)
        {
            for(Cell cell : row)
                sampingx++;
            if(sampingf<sampingx)
                sampingf = sampingx;
            sampingx = 0;
            bawah++;
        }
        String [][] kotak = new String[bawah][sampingf];
        bawah = 0;
        for(Row row : sheet)
        {
            for(Cell cell : row)
            {
                String cellValue = dataFormatter.formatCellValue(cell);
                kotak[bawah][sampingx] = cellValue;
                sampingx++;
            }
            sampingx = 0;
            bawah++;
        }
        int panjang = 0;
        String tes =  "ID";
        boolean start = false;
        for(int i=0; i<kotak.length; i++)
        {
            if(kotak[i][0].equals(tes))
                start = true;
            if(start) {
                if(kotak[i][0].isEmpty())
                    break;
                panjang++;
            }
        }
        String [][] wanted = new String[panjang][3];
        for(int i=0; i<wanted.length; i++)
            for(int j=0; j<wanted[i].length; j++)
                wanted[i][j] = kotak[i+1][j];
        return wanted;
    }

    public static String[][] readsh5b() throws IOException, InvalidFormatException
    {
        Workbook workbook = WorkbookFactory.create(new File(SAMPLE_XLSX_FILE_PATH));
        Sheet sheet = workbook.getSheetAt(4);
        DataFormatter dataFormatter = new DataFormatter();
        int bawah = 0;
        int sampingx = 0;
        int sampingf = 0;
        for(Row row : sheet)
        {
            for(Cell cell : row)
                sampingx++;
            if(sampingf<sampingx)
                sampingf = sampingx;
            sampingx = 0;
            bawah++;
        }
        String [][] kotak = new String[bawah][sampingf];
        bawah = 0;
        for(Row row : sheet)
        {
            for(Cell cell : row)
            {
                String cellValue = dataFormatter.formatCellValue(cell);
                kotak[bawah][sampingx] = cellValue;
                sampingx++;
            }
            sampingx = 0;
            bawah++;
        }
        int panjang = 0;
        int index = 0;
        String id = "ID";
        boolean x = false;
        for(int i=0; i<kotak.length; i++)
        {
            if(kotak[i][0].equals(id))
                index++;
            if(index==2)
                x =  true;
            if(x)
            {
                if(kotak[i][0].isEmpty())
                    break;
                panjang++;
            }
        }
        String [][] unwanted = new String[panjang][3];
        for(int i=0; i<unwanted.length; i++)
            for(int j=0; j<unwanted[i].length; j++)
                unwanted[i][j] = kotak[i+1][j];
        return unwanted;
    }
}
