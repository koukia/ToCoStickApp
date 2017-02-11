package com.example.kohki.tocostickapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
import java.text.ParseException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Kohki on 2017/02/11.
 */

public class DayFileExtracter {
    private static String pre_date="";
    private static String now_date="";

    private static float temp;
    private static float temp_sum;
    private static float temp_bot;
    private static float temp_top;
    private static int cnt_temp;

    private static float radi_sum;
    private static int cnt_radi;

    public static void extractDay(String inputFileName, String outputFileName){
        SimpleDateFormat sdf_ymdhm = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        SimpleDateFormat sdf_ymd    = new SimpleDateFormat("yyyy/MM/dd");

        try{
            File in_file = new File(inputFileName);
            BufferedReader br = new BufferedReader(new FileReader(in_file));

            br.readLine();//ka ra yo mi !

            String line;
            line = br.readLine();

            String[] pre_elements = line.split(",");

            pre_date = sdf_ymd.format(sdf_ymdhm.parse(pre_elements[0]));
            if(!pre_elements[1].equals("null")) {
                temp = Float.parseFloat(pre_elements[1]);
                temp_sum = temp;
                temp_bot = temp;
                temp_top = temp;
                cnt_temp = 1;

                radi_sum = 0;
                cnt_radi = 0;
            }else if(!pre_elements[2].equals("null")) {
                temp_sum = 0;
                cnt_temp = 0;

                radi_sum = Float.parseFloat(pre_elements[2]);
                cnt_radi = 1;
            }

            while ((line = br.readLine()) != null) {
                String[] elements = line.split(",");
                now_date = sdf_ymd.format(sdf_ymdhm.parse(elements[0]));

                if(now_date.equals(pre_date)) {
                    if(!pre_elements[1].equals("null")) {
                        temp = Float.parseFloat(elements[1]);
                        temp_sum += temp;
                        if(temp_top < temp || cnt_temp == 0)
                            temp_top = temp;
                        if(temp_bot > temp || cnt_temp == 0)
                            temp_bot = temp;
                        cnt_temp++;
                    }
                    if(!pre_elements[2].equals("null")) {
                        radi_sum += Float.parseFloat(elements[2]);
                        cnt_radi++;
                    }
                }else if(!now_date.equals(pre_date)){
                    writeFile(outputFileName);

                    if(!pre_elements[1].equals("null")) {
                        temp = Float.parseFloat(elements[1]);
                        temp_sum = temp;
                        temp_top = temp;
                        temp_bot = temp;
                        cnt_temp = 1;

                        radi_sum = 0;
                        cnt_radi = 0;
                    }
                    if(!pre_elements[2].equals("null")) {
                        temp_sum = 0;
                        cnt_temp = 0;

                        radi_sum = Float.parseFloat(elements[2]);
                        cnt_radi = 1;
                    }
                    pre_date = now_date;
                }
                if(!br.ready()){
                    pre_date = now_date;
                    writeFile(outputFileName);
                }
            }
        }catch(IOException e){
            System.out.println(e);
        }catch (NumberFormatException e) {
            System.out.println(e);
        }catch(ParseException e){
            System.out.println(e);
        }
    }
    private static void writeFile(String outputFileName) throws IOException{
        String write_row = "";
        write_row += pre_date+",";
        if(cnt_temp != 0){
            write_row += (float)Math.floor((temp_sum/cnt_temp)*10)/10+",";
            write_row += temp_top+",";
            write_row += temp_bot+",";
        }else{
            write_row += "null,null,null,";
        }
        if(cnt_temp != 0)
            write_row += (float)Math.floor((radi_sum/cnt_radi)*10)/10;
        else
            write_row += "0";

        FileOutputStream fs_ave = ChartActivity.getInstance().openFileOutput(outputFileName, MODE_PRIVATE);
        OutputStreamWriter every_day_file = new OutputStreamWriter(fs_ave);
        every_day_file.write(write_row);
    }
}
