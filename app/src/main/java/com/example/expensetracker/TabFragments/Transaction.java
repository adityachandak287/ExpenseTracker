package com.example.expensetracker.TabFragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.example.expensetracker.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.*;
/**
 * A simple {@link Fragment} subclass.
 */
public class Transaction extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private TransactionListener listener;
    public Spinner categorySpinner;
    public ArrayAdapter<String> arrayAdapter;
    Button incomeButton, expenseButton, addButton;
    EditText amountEditText;
    private int transactionType;

    TextView tv;
    public Transaction() {
        // Required empty public constructor
        transactionType = 0;
    }

    public interface TransactionListener {
        void onAddButtonClicked();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_transaction, container, false);

        incomeButton = v.findViewById(R.id.incomeToggleButton);
        expenseButton = v.findViewById(R.id.expenseToggleButton);
        addButton = v.findViewById(R.id.addButton);
        amountEditText = v.findViewById(R.id.amountEditText);
        tv = v.findViewById(R.id.readTextView);



        incomeButton.setOnClickListener(this);
        expenseButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        categorySpinner = v.findViewById(R.id.categorySpinner);

        String[] arraySpinner = new String[] {};

        arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item,arraySpinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        categorySpinner.setAdapter(arrayAdapter);
        categorySpinner.setOnItemSelectedListener(this);


        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof TransactionListener) {
            listener = (TransactionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " needs to implement TransactionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked

        String arraySpinner[]; // = new String[] {};

        switch (v.getId()) {
            case R.id.incomeToggleButton:
                transactionType = 1;

                incomeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                incomeButton.setTextColor(getResources().getColor(R.color.white));

                expenseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                expenseButton.setTextColor(getResources().getColor(R.color.failure));


                arraySpinner = new String[] {
                        "Select Category",
                        "Pocket Money",
                        "Friend",
                        "Refund",
                        "Prize"
                };

                arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item, arraySpinner);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                categorySpinner.setPrompt("Income");
                categorySpinner.setAdapter(arrayAdapter);
                break;
            case R.id.expenseToggleButton:

                transactionType = -1;

                expenseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                expenseButton.setTextColor(getResources().getColor(R.color.white));

                incomeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//                incomeButton.setTextColor(getResources().getColor(R.color.success));

                arraySpinner = new String[] {
                        "Select Category",
                        "Food",
                        "Travel",
                        "Shopping",
                        "Groceries",
                        "Entertainment"
                };

                arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item, arraySpinner);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                categorySpinner.setAdapter(arrayAdapter);
                break;
            case R.id.addButton:

                if(categorySpinner.getCount() == 0 )    {
                    makeToast("Select Income/Expense!");
                    return;
                }

                final String category = categorySpinner.getSelectedItem().toString();
                if(category.equalsIgnoreCase("Select Category")) {
                    makeToast("Select Category!");
                    return;
                }
//                try {
                String amountText = amountEditText.getText().toString();
                if(amountText.equals(""))  {
                    makeToast("Enter amount!");
                    return;
                }
                if(!amountText.matches("[0-9\\.]+"))  {
                    makeToast("Amount in digits only!");
                    return;
                }
                final double amount = Double.parseDouble(amountText);
                makeToast(category + " - " + amount);
//                }
//                catch(Exception e)  {
//                    makeToast(e.toString());
//                }

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


                    FileWriter fw = new FileWriter(file, true);
                    Date date = new Date();
                    fw.append(getCurrentDate(date));
                    fw.append(",");
                    fw.append(getCurrentTime(date));
                    fw.append(",");
                    fw.append(category);
                    fw.append(",");
                    final double amt = transactionType * amount;
                    fw.append(String.valueOf(amt));
                    fw.append("\n");

                    fw.flush();
                    fw.close();

                    amountEditText.setText("");
                    categorySpinner.setSelection(0,true);
                    incomeButton.setSelected(false);
                    expenseButton.setSelected(false);
                    incomeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    expenseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                    String[] def = new String[] {};

                    arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_spinner_item, def);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    categorySpinner.setAdapter(arrayAdapter);

                    listener.onAddButtonClicked();
                    makeToast("Transaction saved!");


                }
                catch (Exception e) {
                    makeToast(e.toString());
                }
                break;
        }
    }

    private void makeToast(String msg)  {
        Toast.makeText(this.getContext(), msg , Toast.LENGTH_LONG).show();
    }

    private String getCurrentDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        return formatter.format(date);
    }

    private String getCurrentTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
        return formatter.format(date);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent,
                               View view, int pos, long id) {
//        Toast.makeText(this.getContext(), "Selected: " + categorySpinner.getSelectedItem().toString() , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView parent) {
        // Do nothing.
    }


}
