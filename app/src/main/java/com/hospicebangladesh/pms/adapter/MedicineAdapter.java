package com.hospicebangladesh.pms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.hospicebangladesh.pms.R;
import com.hospicebangladesh.pms.model.Medicine;

import java.util.List;

/**
 * Created by Tushar on 1/26/2018.
 */

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private List<Medicine> mDataset;
    private Context context;
    private static final String TAG = MedicineAdapter.class.getSimpleName();
    private int counter=0;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView si, medicine, time, instruction, duration;


        public ViewHolder(View v) {
            super(v);
            //  v.setOnClickListener(this);

            medicine = v.findViewById(R.id.textViewMedicine);
            time = v.findViewById(R.id.textViewTime);
            instruction = v.findViewById(R.id.textViewInstruction);
            duration = v.findViewById(R.id.textViewDuration);


        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MedicineAdapter(List<Medicine> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_card_prescription, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        counter++;

        String medicine =mDataset.get(position).getMedicine();
        String times =mDataset.get(position).getTimes();
        String instruction = mDataset.get(position).getInstruction();
        String duration =mDataset.get(position).getDuration();
        String dtime = mDataset.get(position).getDtime();
        Log.d(TAG,medicine);

        holder.medicine.setText(counter+".  "+medicine);
        holder.time.setText(times);
        holder.instruction.setText(instruction);
        holder.duration.setText(duration);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}