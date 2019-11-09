package com.example.expensetracker.TabFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder> {
    private ArrayList<Analysis.TransactionRecord> data;
    private Context context;

    public TransactionsAdapter(ArrayList<Analysis.TransactionRecord> data, Context context)   {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_layout, parent, false);


        return new TransactionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsViewHolder holder, int position) {
        Analysis.TransactionRecord record = data.get(getItemCount() - 1 - position);
        holder.timeTextView.setText(record.getTimeText());
        holder.dateTextView.setText(record.getDateText());
        holder.categoryTextView.setText(record.getCategoryText());
        holder.amountTextView.setText(record.getAmountText());
        double amount = Double.parseDouble(record.getAmountText());
        if(amount < 0)  holder.amountTextView.setTextColor(context.getResources().getColor(R.color.failure));
        else holder.amountTextView.setTextColor(context.getResources().getColor(R.color.success));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class TransactionsViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, dateTextView, categoryTextView, amountTextView;
        public TransactionsViewHolder(View itemView)    {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeText);
            dateTextView = itemView.findViewById(R.id.dateText);
            categoryTextView= itemView.findViewById(R.id.categoryText);
            amountTextView = itemView.findViewById(R.id.amountText);
        }
    }

}
