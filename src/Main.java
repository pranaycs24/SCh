import ilog.concert.*;
import ilog.cplex.*;

public class Main {
    public static void sum(int a, int b)
    {

    }
    public static void main(String[] args) {
        IloCplex cplex;
        try {
            cplex = new IloCplex();
            IloIntVar var = cplex.intVar(0,10,"P1");
            IloIntVar var2 =cplex.intVar(0,20, "P2");
            IloRange [][] rng = new IloRange[1][];
            IloIntVar b = cplex.boolVar("j");
            IloNumExpr temp;
            cplex.add(b);
            cplex.add(cplex.ifThen(cplex.eq(b, 1), cplex.le(temp = cplex.sum(var,var2), 20)));
            cplex.add(cplex.ifThen(cplex.eq(b, 0), cplex.ge(temp = cplex.diff(var2, var), 10)));

            cplex.add(cplex.ifThen(cplex.eq(b, 0), cplex.eq(cplex.sum(temp, var2), 30)));
            cplex.addMaximize(cplex.sum(var,var2));
            cplex.exportModel("lpex2.lp");
            if (cplex.solve())
            {
                System.out.println(cplex.getObjValue());
                System.out.println(cplex.getValue(b));
            }
            int a;
            sum(10,a =20);
            System.out.println(a);
            int[] b1 = new int[]{1,2,3,4,5};
            System.out.println(b1[4]);

        } catch (IloException e) {
            e.printStackTrace();
        }
    }
}

