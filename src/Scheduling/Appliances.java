package Scheduling;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import ilog.concert.*;
import ilog.cplex.*;

public class Appliances {
    public static void main(String[] args) {
//        int[] apg = {100, 200};
//        for (int app = 0; app < apg.length; app++) {
//            for (int kg = 2; kg < 25; kg++) {
                String appDataPath = args[0]; //"Data/App_"+apg[app]+"/Appliance_Info_" + kg + ".dat";
                String genDataPath = args[1];//"Data/App_"+apg[app]+"/GenUnit_" + kg + ".dat";
                Data_Fetch d = new Data_Fetch();
                ArrayList<Integer[]> genData = d.data(genDataPath);
                int timeSlot = genData.size();
                int[] price = new int[timeSlot], res = new int[timeSlot];
                int index = 0;
                for (Integer[] i : genData) {
                    price[index] = i[0];
                    res[index] = i[1];
                    index++;
                }

                ArrayList<Integer[]> appData = d.data(appDataPath);
                int numberAppliance = appData.size();
                int[] executionTime = new int[numberAppliance], powerConsume = new int[numberAppliance];
                index = 0;
                for (Integer[] i : appData) {
                    executionTime[index] = i[0];
                    powerConsume[index] = i[1];
                    index++;
                }
                IloCplex modeler;
                try {
                    modeler = new IloCplex();
                    String[] bdhN = new String[timeSlot];
                    String[] bN = new String[timeSlot];
                    String[] gbN = new String[timeSlot];
                    String[] glN = new String[timeSlot];
                    for (int i = 0; i < timeSlot; i++) {
                        bdhN[i] = "bdh_" + i;
                        bN[i] = "b_" + i;
                        gbN[i] = "gb_" + i;
                        glN[i] = "gl_" + i;
                    }
                    IloIntVar[] bdh = modeler.intVarArray(timeSlot, 0, Constant.getDMAX(), bdhN);
                    IloIntVar[] b = modeler.boolVarArray(timeSlot, bN);
                    IloIntVar[] gb = modeler.intVarArray(timeSlot, 0, Constant.getCMAX(), gbN);
                    IloIntVar[] gl = modeler.intVarArray(timeSlot, 0, Integer.MAX_VALUE, glN);
                    IloIntVar[] rl = new IloIntVar[timeSlot];
                    IloIntVar[] rb = new IloIntVar[timeSlot];

                    for (int i = 0; i < timeSlot; i++) {
                        rl[i] = modeler.intVar(0, res[i], "rl_" + i);
                        rb[i] = modeler.intVar(0, Integer.min(res[i], Constant.getCMAX()), "rb_" + i);
                    }
                    IloIntVar[][] s = new IloIntVar[numberAppliance][timeSlot];
                    String[][] sN = new String[numberAppliance][timeSlot];
                    for (int i = 0; i < numberAppliance; i++)
                        for (int j = 0; j < timeSlot; j++) {
                            sN[i][j] = "s_" + i + "" + j;
                        }

                    for (int i = 0; i < numberAppliance; i++) {
                        s[i] = modeler.boolVarArray(timeSlot, sN[i]);
                        modeler.add(s[i]);
                    }

                    IloConstraint[] c1 = new IloConstraint[timeSlot];
//            IloIntVar[] var;
//            double [] cons;

                    IloNumExpr[] bch = new IloNumExpr[timeSlot];


                    for (int i = 0; i < timeSlot; i++) {

                        bch[i] = modeler.sum(modeler.prod(Constant.getNBR(), rb[i]), modeler.prod(Constant.getNLR(), gb[i]));
                        c1[i] = modeler.le(bch[i], Constant.getCMAX());
                        modeler.add(modeler.le(modeler.sum(rl[i], rb[i]), res[i]));

                    }
                    modeler.add(c1);
                    modeler.add(bdh);

                    IloLinearNumExpr bss = modeler.linearNumExpr();
                    IloNumExpr bs = modeler.constant(Constant.getBIN());
                    bss.setConstant(Constant.getBIN());
                    modeler.add(modeler.le(bdh[0], bs));
                    modeler.add(modeler.ifThen(modeler.eq(b[0], 1), modeler.range(Constant.getBMIN(), bs = modeler.sum(bs, bch[0]),
                            Constant.getBMAX())));

                    modeler.add(modeler.ifThen(modeler.eq(b[0], 0), modeler.range(Constant.getBMIN(),
                            bs = modeler.diff(bs, modeler.prod(Constant.getNLB(), bdh[0])),
                            Constant.getBMAX())));


                    for (int i = 1; i < timeSlot; i++) {

                        modeler.add(modeler.le(bdh[i], bs));
//                modeler.add(modeler.ifThen(modeler.eq(b[i], 1), modeler.eq(bdh[i],0)));
//                modeler.add(modeler.ifThen(modeler.eq(b[i], 0), modeler.le(bdh[i],bs)));
                        modeler.add(modeler.ifThen(modeler.eq(b[i], 1), modeler.range(Constant.getBMIN(),
                                bs = modeler.sum(bs, bch[i]), Constant.getBMAX())));

                        modeler.add(modeler.ifThen(modeler.eq(b[i], 0), modeler.range(Constant.getBMIN(),
                                bs = modeler.diff(bs, modeler.prod(Constant.getNLB(), bdh[i])),
                                Constant.getBMAX())));
                    }
                    //IloIntExpr[] x = new IloIntExpr[numberAppliance];

                    for (int i = 0; i < numberAppliance; i++) {
                        IloIntExpr temp = modeler.constant(0);
                        for (int j = 0; j < timeSlot - executionTime[i] + 1; j++) {
                            temp = modeler.sum(temp, s[i][j]);
                            IloIntExpr sumCheck = modeler.constant(0);
                            for (int k = j; k < Math.min(j + executionTime[i], timeSlot); k++)
                                sumCheck = modeler.sum(sumCheck, s[i][k]);
                            modeler.add(modeler.ifThen(modeler.eq(temp, 1), modeler.eq(sumCheck, executionTime[i])));
                        }

                        modeler.add(modeler.eq(modeler.sum(s[i]), executionTime[i]));
                        //x[i] = modeler.constant(0);
                    }


                    for (int i = 0; i < timeSlot; i++) {
                        IloIntExpr x = modeler.constant(0);
                        for (int j = 0; j < numberAppliance; j++)
                            x = modeler.sum(x, modeler.prod(s[j][i], powerConsume[j]));
                        IloIntExpr[] sumGen = new IloIntExpr[]{gl[i], rl[i], bdh[i]};

                        modeler.add(modeler.eq(modeler.diff(x, modeler.sum(sumGen)), 0));

                    }

                    modeler.addMinimize(modeler.sum(modeler.scalProd(price, gb), modeler.scalProd(price, gl)));
                    modeler.exportModel("lpex1.lp");
                    modeler.add(b);
                    if (modeler.solve()) {
                        System.out.println(modeler.getObjValue());
                        try {
                            BufferedWriter br = new BufferedWriter(new FileWriter("Result_"+ args[2]+".dat",
                                    true));
                            BufferedWriter br1 = new BufferedWriter(new FileWriter("TimeSpend_"+ args[2]+".dat",
                                    true));
                            br.write(modeler.getObjValue() + "\n");
                            br1.write(modeler.getCplexTime() + "\n");
                            br.close();
                            br1.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

//                    for (int i = 0; i < numberAppliance; i++) {
//                        for (int j = 0; j < timeSlot; j++) {
//                            System.out.println("S_" + i + j + " " + modeler.getValue(s[i][j]) + "\n");
//                            System.out.println(modeler.getValue(gl[j]) + " " + modeler.getValue(bdh[j]) + " "
//                                    + modeler.getValue(rl[j]) + " " + modeler.getValue(rb[j]) + "\n");
//                        }
//                    }

                    }


                } catch (IloException e) {
                    e.printStackTrace();
                }


            }
        }

