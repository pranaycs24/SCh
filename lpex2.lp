\ENCODING=ISO-8859-1
\Problem name: ilog.cplex

Maximize
 obj: P1 + P2
Subject To
 c1: - j + x2  = 0
 c2: x1 - x2 >= 0
 c3: j + x6  = 1
 c4: x5 - x6 >= 0
 c5: j + x8  = 1
 c6: x7 - x8 >= 0
 IloI0: x1 = 1 <-> P1 + P2 <= 20
 IloI1: x5 = 1 <-> P2 - P1 >= 10
 IloI2: x7 = 1 <-> 2 P2 - P1  = 30
Bounds
 0 <= j <= 1
 0 <= x1 <= 1
 0 <= x2 <= 1
 0 <= P1 <= 10
 0 <= P2 <= 20
 0 <= x5 <= 1
 0 <= x6 <= 1
 0 <= x7 <= 1
 0 <= x8 <= 1
Binaries
 j  x1  x2  x5  x6  x7  x8 
Generals
 P1  P2 
End
