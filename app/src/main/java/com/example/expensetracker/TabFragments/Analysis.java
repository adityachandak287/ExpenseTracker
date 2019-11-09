package com.example.expensetracker.TabFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetracker.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Analysis extends Fragment {

    public RecyclerView transactionsList;
    public TextView balanceNumberView;

    public Analysis() {
        // Required empty public constructor
    }

    public class TransactionRecord {
        String timeText;
        String dateText;

        public String getDateText() {
            return dateText;
        }

        public void setDateText(String dateText) {
            this.dateText = dateText;
        }

        public String getCategoryText() {
            return categoryText;
        }

        public void setCategoryText(String categoryText) {
            this.categoryText = categoryText;
        }

        public String getAmountText() {
            return amountText;
        }

        public void setAmountText(String amountText) {
            this.amountText = amountText;
        }

        String categoryText;
        String amountText;

        public String getTimeText() {
            return timeText;
        }

        public void setTimeText(String timeText) {
            this.timeText = timeText;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_analysis, container, false);
        transactionsList = v.findViewById(R.id.transactionsList);
        balanceNumberView = v.findViewById(R.id.balanceNumberView);

        transactionsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        readCSV();
//        ArrayList<String> transactions = readCSV();
//
//        transactionsList.setAdapter(new TransactionsAdapter(transactions));

        return v;
    }


    public void readCSV()   {
        ArrayList<TransactionRecord> records = new ArrayList<>();
        double balance = 0;
        try {

            File fileDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"ExpenseTracker");
            if(!fileDir.exists()){
                try{
                    fileDir.mkdir();
                } catch (Exception e) {
                    makeToast(e.toString());
                }
            }
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator +"ExpenseTracker"+File.separator+"transactions.csv");
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    makeToast(e.toString());
                }

            }

            String strLine;
            FileInputStream fis = new FileInputStream(file);
            BufferedReader r = new BufferedReader(new InputStreamReader(fis));
            while ((strLine = r.readLine()) != null)   {
                TransactionRecord tr = new TransactionRecord();
                String[] record = strLine.split(",");
                tr.setTimeText(record[1]);
                tr.setDateText(record[0]);
                tr.setCategoryText(record[2]);
                tr.setAmountText(record[3]);
                records.add(tr);
                balance += Double.parseDouble(tr.getAmountText());
            }
            r.close();
//            makeToast("Test Read done!");
        }
        catch (Exception e) {
            makeToast(e.toString());
        }
        finally {
            balanceNumberView.setText(String.valueOf(balance));
            transactionsList.setAdapter(new TransactionsAdapter(records, this.getContext()));
        }
    }

    private void makeToast(String msg)  {
        Toast.makeText(this.getContext(), msg , Toast.LENGTH_LONG).show();
    }

}
