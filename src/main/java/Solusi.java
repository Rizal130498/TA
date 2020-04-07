import jdk.packager.internal.legacy.builders.mac.MacAppImageBuilder;

public class Solusi {
    int[][] sol;
    public Solusi(int[][] solution)
    {
        sol = solution;
    }

    public double countPenalty()
    {
        return penalty1() + penalty2() + penalty3()  + penalty4() + penalty5() +
                penalty6() + penalty7() + penalty8() + penalty9();
    }

    private double penalty1()
    {
        double p1 = 0;
        for(int i=0; i<3; i++)
        {
            int hari = 0;
            int sc = -1;
            if(i==0) {
                sc = TA.cons.getSc1a();
                if (TA.cons.getSc1a() != 0) {
                    hari = (TA.jumweek[(TA.nomor - 1)] * 7) - (TA.cons.getSc1a());
                }
                else continue;
            }
            if(i==1) {
                sc = TA.cons.getSc1b();
                if (TA.cons.getSc1b() != 0) {
                    hari = (TA.jumweek[(TA.nomor - 1)] * 7) - (TA.cons.getSc1b());
                }
                else continue;
            }
            if(i==2) {
                sc = TA.cons.getSc1c();
                if (TA.cons.getSc1c() != 0) {
                    hari = (TA.jumweek[(TA.nomor - 1)] * 7) - (TA.cons.getSc1c());
                }
                else continue;
            }
            for(int j=0; j<TA.kar.length; j++)
            {
                for(int k=0; k<hari; k++)
                {
                    int count = (sc+1);
                    for(int l=k; l<=k+sc; l++)
                    {
                        if(sol[j][l]!=0) {
                            if (TA.shifts[sol[j][l] - 1].getKat() == (i + 1))
                                count--;

                        }
                    }
                    if (count == 0)
                        p1 = p1 + 1;
                }
            }
        }
        return p1;
    }

    private double penalty2()
    {
        double p2 = 0;
        int sc = TA.cons.getSc2();
        if(sc!=0) {
            //System.out.println("masuk");
            int hari = (TA.jumweek[(TA.nomor - 1)] * 7) - sc;
            for (int i = 0; i < TA.kar.length; i++) {
                for (int j = 0; j < hari; j++) {
                    int count = (sc + 1);
                    //System.out.println(count);
                    for (int k = j; k <= j + sc; k++) {
                        if (sol[i][k] > 0)
                            count--;
                    }
                    if (count == 0)
                        p2 = p2 + 1;
                }
            }
        }
        return p2;
    }

    private double penalty3()
    {
        double p3 = 0;
        for(int i=0; i<3; i++)
        {
            int hari = 0;
            int sc = -1;
            if(i==0) {
                sc = TA.cons.getSc3a();
                if (TA.cons.getSc3a() != 0) {
                    hari = (TA.jumweek[(TA.nomor - 1)] * 7) - (TA.cons.getSc3a());
                }
                else continue;
            }
            if(i==1) {
                sc = TA.cons.getSc3b();
                if (TA.cons.getSc3b() != 0) {
                    hari = (TA.jumweek[(TA.nomor - 1)] * 7) - (TA.cons.getSc3b());
                }
                else continue;
            }
            if(i==2) {
                sc = TA.cons.getSc3c();
                if (TA.cons.getSc3c() != 0) {
                    hari = (TA.jumweek[(TA.nomor - 1)] * 7) - (TA.cons.getSc3c());
                }
                else continue;
            }
            for(int j=0; j<TA.kar.length; j++)
            {
                for(int k=0; k<hari; k++)
                {
                    if(sol[j][k+1]!=0)
                    {
                        if(TA.shifts[sol[j][k+1]-1].getKat()==(i+1))
                        {
                            if(sol[j][k]==0 || TA.shifts[sol[j][k]-1].getKat()!=(i+1))
                            {
                                double hitung =0;
                                for(int l=k+1; l<=k+sc; l++)
                                {
                                    int count = 1;
                                    for(int m = k+1; m<=k+sc; m++)
                                    {
                                        if(sol[j][m]==0 || TA.shifts[sol[j][m]-1].getKat()!=(i+1))
                                            count=0;
                                    }
                                    if(count==1)
                                        hitung = hitung+1;
                                }
                                double x = sc - hitung;
                                p3 = p3 + x;
                            }
                        }
                    }
                }
            }
        }
        return p3;
    }

    private double penalty4()
    {
        double p4 = 0;
        int sc = TA.cons.getSc4();
        if(sc!=0) {
            //System.out.println(sc);
            int hari = (TA.jumweek[(TA.nomor - 1)] * 7) - sc;
            for (int i = 0; i < TA.kar.length; i++) {
                for (int j = 0; j < hari; j++) {
                    if (sol[i][j + 1] != 0 && sol[i][j] == 0) {
                        double hitung = 0;
                        for (int k = j + 1; k <= j + sc; k++) {
                            int count = 1;
                            for (int l = j + 1; l <= j + sc; l++) {
                                if (sol[i][l] == 0)
                                    count = 0;
                            }
                            if (count == 1)
                                hitung = hitung + 1;
                        }
                        double x = sc - hitung;
                        p4 = p4 + x;
                    }
                }
            }
        }
        return p4;
    }

    private double penalty5()
    {
        double p5 = 0;
        for(int i=0; i<3; i++)
        {
            int min = 0;
            int max = 0;
            if(i==0)
            {
                max = TA.cons.getSc5aMax();
                min = TA.cons.getSc5aMin();
            }
            if(i==1)
            {
                max = TA.cons.getSc5bMax();
                min = TA.cons.getSc5bMin();
            }
            if(i==2)
            {
                max = TA.cons.getSc5cMax();
                min = TA.cons.getSc5cMin();
            }
            if(max == 0 && min == 0)
                continue;

            for(int j=0 ;  j<TA.kar.length; j++)
            {
                int jumlah = 0;
                for(int k=0; k<(TA.jumweek[(TA.nomor-1)])*7; k++)
                {
                    if(sol[j][k]!=0)
                        if(TA.shifts[sol[j][k]-1].getKat()==(i+1))
                            jumlah++;
                }
                min = min - jumlah;
                max = jumlah - max;
                if(min>max && min>0)
                    p5 = p5 + (min*min);
                if(max>min && max>0)
                    p5 = p5 + (max*max);
            }
        }
        p5 = Math.sqrt(p5);
        return p5;
    }

    private double penalty6()
    {
        double p6 = 0;
        if(TA.cons.getSc6())
        {
            for (int i = 0; i < TA.kar.length; i++)
            {
                double jam = (TA.batasjam[i][1]) * (TA.jumweek[(TA.nomor - 1)]);
                double harian = 0;
                for (int j = 0; j < TA.jumweek[(TA.nomor - 1)] * 7; j++)
                {
                    if (sol[i][j] != 0)
                        harian = harian + TA.shifts[sol[i][j] - 1].getDurasi(j % 7);
                }
                jam = jam - harian;
                p6 = p6 + (jam * jam);
            }
            p6 = Math.sqrt(p6);
        }
        return p6;
    }

    private double penalty7()
    {
        double p7 = 0;
        if(TA.cons.getSc7())
        {
            for(int i=0; i<TA.kar.length; i++)
            {
                int count = 0;
                for(int j=0; j<(TA.jumweek[(TA.nomor - 1)] * 7)-1; j++)
                {
                    if(sol[i][j]!=0 && sol[i][(j+1)]==0)
                        count++;
                }
                p7 = p7 + (count*count);
            }
            p7 = Math.sqrt(p7);
        }
        return p7;
    }

    private double penalty8()
    {
        int hari = (TA.jumweek[(TA.nomor-1)]*7);
        int kar = TA.kar.length;
        double p8 = 0;
        if(TA.cons.getSc8()!=0) {
            p8 = (double) kar*hari;
            for (int i = 0; i < kar; i++) {
                for (int j = 0; j < hari; j++) {
                    for (int k = 0; k < TA.want.length; k++) {
                        if (hari % 7 == TA.want[k].start) {
                            if (sol[i][j] != 0) {
                                if (TA.shifts[sol[i][j] - 1].getNama().equals(TA.want[k].pat[0])) {
                                    if (j <= hari - (TA.want[k].pat.length)) {
                                        int count = TA.want[k].pat.length;
                                        for (int l = 0; l < TA.want[k].pat.length; l++) {
                                            if (sol[i][j + l] != 0) {
                                                if (TA.shifts[sol[i][j + l] - 1].getNama().equals(TA.want[k].pat[l])) {
                                                    count--;
                                                }
                                            } else {
                                                if (TA.want[k].pat[l].equals("<Free>")) {
                                                    count--;
                                                }
                                            }
                                        }
                                        if (count == 0)
                                            p8 = p8 - 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return p8;
    }

    private double penalty9()
    {
        double p9 = 0;
        int hari = (TA.jumweek[(TA.nomor-1)]*7);
        int kar = TA.kar.length;
        if(TA.cons.getSc9()!=0) {
            for (int i = 0; i < kar; i++) {
                for (int j = 0; j < hari; j++) {
                    for (int k = 0; k < TA.unwant.length; k++) {
                        if (hari % 7 == TA.unwant[k].start) {
                            if (sol[i][j] != 0) {
                                if (TA.shifts[sol[i][j] - 1].getNama().equals(TA.unwant[k].pat[0])) {
                                    if (j <= hari - (TA.unwant[k].pat.length)) {
                                        int count = TA.unwant[k].pat.length;
                                        for (int l = 0; l < TA.unwant[k].pat.length; l++) {
                                            if (sol[i][j + l] != 0) {
                                                if (TA.shifts[sol[i][j + l] - 1].getNama().equals(TA.unwant[k].pat[l])) {
                                                    count--;
                                                }
                                            } else {
                                                if (TA.want[k].pat[l].equals("<Free>")) {
                                                    count--;
                                                }
                                            }
                                        }
                                        if (count == 0)
                                            p9 = p9 + 1;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return p9;
    }

    public void LAHC()
    {
        int hari = TA.jumweek[(TA.nomor-1)]*7;
        int [][] sol2  = new int[sol.length][hari];
        double initialcost = countPenalty();
        double [] penn = new double[500];
        for(int i=0; i<penn.length; i++)
            penn[i] = countPenalty();
        TA.ganda(sol, sol2);
        int idle = 0;
        for(int i=0; i<1000000; i++)
        {
            int llh = (int) (Math.random()*3);
            switch(llh)
            {
                case 0 :
                    TA.exchange2(sol);
                case 1 :
                    TA.exchange3(sol);
                case 2 :
                    TA.doubleExchange2(sol);
            }
            if(TA.validasiSemua(sol)==0)
            {
                if(countPenalty()>=initialcost) {
                    idle = idle + 1;
                    if(idle>20000)
                        break;
                }
                else
                    idle = 0;
                int v = i%penn.length;
                if(countPenalty()<penn[v] || countPenalty()<=initialcost)
                {
                    initialcost = countPenalty();
                    TA.ganda(sol, sol2);
                }
                else
                    TA.ganda(sol2, sol);
                if(initialcost<penn[v])
                    penn[v] = initialcost;
            }
            else
            {
                idle = idle + 1;
                TA.ganda(sol2, sol);
                if(idle>20000)
                    break;
            }
            System.out.println("Iterasi ke " + (i+1) + " dengan penalti " + countPenalty() + " idle nya " + idle);
        }
        TA.print(sol);
        System.out.println(countPenalty());
    }

    public void HillClimbing()
    {
        int hari = TA.jumweek[(TA.nomor-1)]*7;
        int [][] sol2  = new int[sol.length][hari];
        double initialcost = countPenalty();
        TA.ganda(sol, sol2);
        for(int i=0; i<1000000; i++)
        {
            int llh = (int) (Math.random()*3);
            switch(llh)
            {
                case 0 :
                    TA.exchange2(sol);
                case 1 :
                    TA.exchange3(sol);
                case 2 :
                    TA.doubleExchange2(sol);
            }
            if(TA.validasiSemua(sol)==0) {
                if(countPenalty()<=initialcost)
                {
                    initialcost = countPenalty();
                    TA.ganda(sol, sol2);
                }
                else
                {
                    TA.ganda(sol2, sol);
                }
            }
            else
                TA.ganda(sol2, sol);
            System.out.println("Iterasi ke " + (i+1) + " dengan penalti " + countPenalty());
        }
        TA.print(sol);
    }

    public void Simualated()
    {
        int hari = TA.jumweek[(TA.nomor-1)]*7;
        int [][] sol2  = new int[sol.length][hari];
        double initialcost = countPenalty();
        double [] penn = new double[500];
        for(int i=0; i<penn.length; i++)
            penn[i] = countPenalty();
        TA.ganda(sol, sol2);
        int idle = 0;
        double suhu = 10;
        int i = 0;
        double p =0;
        double pp =0;
        double x = 0;
        double d = 0;
        for(double s = suhu; s>0.000001; s=s-0.000001)
        {
            i++;
            int llh = (int) (Math.random()*3);
            switch(llh)
            {
                case 0 :
                    TA.exchange2(sol);
                case 1 :
                    TA.exchange3(sol);
                case 2 :
                    TA.doubleExchange2(sol);
            }
            if(TA.validasiSemua(sol)==0) {
                x = countPenalty() - initialcost;
                if (x<=0) {
                    TA.ganda(sol, sol2);
                    initialcost = countPenalty();
                } else {
                    d = Math.abs(x)/suhu;
                    p = Math.pow(Math.E, -d);
                    if (p>Math.random()) {
                        TA.ganda(sol, sol2);
                        initialcost = countPenalty();
                    } else {
                        TA.ganda(sol2, sol);
                    }
                }
            }
            else {
                TA.ganda(sol2, sol);
            }
            System.out.println("Iterasi ke " + (i+1) + " dengan penalti " + initialcost + " s "  + s + " p " + p + " d " + d);
        }
        System.out.println("Hasil Penalti " + initialcost);
    }

    public void greedyLAHC()
    {

        int hari = TA.jumweek[(TA.nomor-1)]*7;
        int[][] solx = new int [sol.length][hari];
        TA.ganda(sol, solx);
        int [][] sol2  = new int[sol.length][hari];
        double initialcost = countPenalty();
        double cost1 =  countPenalty();
        double cost2 = countPenalty();
        double cost3 = countPenalty();
        int[][]sol1 = new int[sol.length][hari];
        int[][]sol3 = new int[sol.length][hari];
        double [] penn = new double[500];
        for(int i=0; i<penn.length; i++)
            penn[i] = countPenalty();
        TA.ganda(sol, sol2);
        TA.ganda(sol, sol1);
        TA.ganda(sol, sol3);
        int idle = 0;
        for(int i=0; i<1000000; i++)
        {
            TA.exchange2(sol); cost1 = countPenalty(); TA.ganda(sol, sol1); TA.ganda(solx, sol);
            TA.exchange3(sol); cost2 = countPenalty(); TA.ganda(sol, sol2); TA.ganda(solx, sol);
            TA.doubleExchange2(sol); cost3 = countPenalty(); TA.ganda(sol, sol3); TA.ganda(solx, sol);
            if(cost1<cost2 && cost1<cost3)
            {
                TA.ganda(sol1, sol); TA.ganda(sol1, sol2); TA.ganda(sol1, sol3);
            }
            if(cost2<cost1&&cost2<cost3)
            {
                TA.ganda(sol2, sol); TA.ganda(sol2, sol1); TA.ganda(sol2, sol3);
            }
            if(cost3<cost1&&cost3<cost2)
            {
                TA.ganda(sol3, sol); TA.ganda(sol3, sol1); TA.ganda(sol3, sol2);
            }
            if(cost1==cost2 && cost1==cost3)
            {
                int llh = (int) (Math.random()*3);
                switch(llh)
                {
                    case 0 : {
                        TA.ganda(sol1, sol); TA.ganda(sol1, sol2); TA.ganda(sol1, sol3);
                    }
                    case 1 : {
                        TA.ganda(sol2, sol); TA.ganda(sol2, sol1); TA.ganda(sol2, sol3);
                    }
                    case 2 : {
                        TA.ganda(sol3, sol); TA.ganda(sol3, sol1); TA.ganda(sol3, sol2);
                    }
                }
            }
            if(cost1==cost2 && cost1<cost3)
            {
                int llh = (int) (Math.random()*2);
                switch(llh)
                {
                    case 0 : {
                        TA.ganda(sol1, sol); TA.ganda(sol1, sol2); TA.ganda(sol1, sol3);
                    }
                    case 1 : {
                        TA.ganda(sol2, sol); TA.ganda(sol2, sol1); TA.ganda(sol2, sol3);
                    }
                }
            }
            if(cost1==cost3 && cost1<cost2)
            {
                int llh = (int) (Math.random()*2);
                switch(llh)
                {
                    case 0 : {
                        TA.ganda(sol1, sol); TA.ganda(sol1, sol2); TA.ganda(sol1, sol3);
                    }
                    case 1 : {
                        TA.ganda(sol3, sol); TA.ganda(sol3, sol1); TA.ganda(sol3, sol2);
                    }
                }
            }
            if(cost2==cost3 && cost2<cost1)
            {
                int llh = (int) (Math.random()*2);
                switch(llh)
                {
                    case 0 : {
                        TA.ganda(sol2, sol); TA.ganda(sol2, sol1); TA.ganda(sol2, sol3);
                    }
                    case 1 : {
                        TA.ganda(sol3, sol); TA.ganda(sol3, sol1); TA.ganda(sol3, sol2);
                    }
                }
            }
            if(TA.validasiSemua(sol)==0)
            {
                if(countPenalty()>=initialcost) {
                    idle = idle + 1;
                    if(idle>20000)
                        break;
                }
                else
                    idle = 0;
                int v = i%penn.length;
                if(countPenalty()<penn[v] || countPenalty()<=initialcost)
                {
                    initialcost = countPenalty();
                    TA.ganda(sol, sol1); TA.ganda(sol, sol2); TA.ganda(sol, sol3); TA.ganda(sol, solx);
                }
                else {
                    TA.ganda(solx, sol); TA.ganda(solx, sol1); TA.ganda(solx, sol2); TA.ganda(solx, sol3);
                }
                if(initialcost<penn[v])
                    penn[v] = initialcost;
            }
            if(TA.validasiSemua(sol)!=0)
            {
                idle = idle + 1;
                TA.ganda(solx, sol); TA.ganda(solx, sol1); TA.ganda(solx, sol2); TA.ganda(solx, sol3);
                if(idle>20000)
                    break;
            }
            System.out.println("Iterasi ke " + (i+1) + " dengan penalti " + countPenalty() + " idle nya " + idle);
        }
        TA.print(sol);
        System.out.println(TA.validasiSemua(sol));
        //System.out.println(countPenalty());
    }

    public void VNS()
    {
        int hari = TA.jumweek[(TA.nomor-1)]*7;
        int [][] sol2  = new int[sol.length][hari];
        double initialcost = countPenalty();
        TA.ganda(sol, sol2);
        int llh  = 0;
        for(int i=0; i<1000000; i++)
        {
            if(llh==0)
                TA.exchange2(sol);
            if(llh==1)
                TA.exchange3(sol);
            if(llh==2)
                TA.doubleExchange2(sol);
            if(TA.validasiSemua(sol)==0) {
                if(countPenalty()<=initialcost)
                {
                    initialcost = countPenalty();
                    TA.ganda(sol, sol2);
                }
                else
                {
                    if(llh<2)
                        llh++;
                    else llh = 0;
                    TA.ganda(sol2, sol);
                }
            }
            else {
                if(llh<2)
                    llh++;
                else llh = 0;
                TA.ganda(sol2, sol);
            }
            System.out.println("Iterasi ke " + (i+1) + " dengan penalti " + countPenalty());
        }
        TA.print(sol);
        System.out.println(TA.validasiSemua(sol));
    }
}

