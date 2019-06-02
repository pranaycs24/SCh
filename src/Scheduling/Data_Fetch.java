package Scheduling;


import java.io.*;
import java.util.ArrayList;


public class Data_Fetch {
    public ArrayList<Integer []> data(String path) {
        ArrayList <Integer []> input = new ArrayList<>();
        try {
            File f = new File(path);
            FileReader x = new FileReader(f);
            BufferedReader br = new BufferedReader(x);
            String st;
            String[] s;
            Integer[] in = new Integer[2];
//            ArrayList <Integer []> input = new ArrayList<>();
            while ((st = br.readLine()) != null) {
                s = st.split("\\s+");

                in[0] = Integer.parseInt(s[0]);
                in[1] = Integer.parseInt(s[1]);
                input.add(new Integer[]{in[0], in[1]});
            }
    } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        return input;
    }
}
