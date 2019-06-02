#!/bin/bash
app=200
test_case=100

javac -cp /home/pranay.pcs16/CIPLEX/cplex/lib/cplex.jar -d "classes" ./src/Scheduling/*java


if [ $? -eq 0 ]
then
	for ((i=1; i<$test_case; i++))
	do
        	app_path="Data/App_$app/Appliance_Info_$i.dat"
        	gen_path="Data/App_$app/GenUnit_$i.dat"
		/usr/java/jdk1.8.0_131/bin/java -Djava.library.path=/home/pranay.pcs16/CIPLEX/cplex/bin/x86-64_linux -cp /home/pranay.pcs16/CIPLEX/cplex/lib/cplex.jar:/home/pranay.pcs16/NetBeansProjects/SCh/classes/ Scheduling.Appliances $app_path $gen_path $app
		
			temp=$(wc -l Result_$app.dat | cut -d ' ' -f 1)
			#echo $temp
			if [ $temp -eq 25 ]
			then
				echo "hello"
				break
			fi

	done

fi

