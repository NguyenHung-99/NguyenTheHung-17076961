package com.example.nguyenthehung_17076961;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<Person> dataList;
    private Activity context;
    private RoomConfigDB database;

    public MainAdapter(List<Person> dataList, Activity context) {
        this.dataList = dataList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main,
                        parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        Person data = dataList.get(position);
        database = RoomConfigDB.getInstance(context);

        //set tên on views
        holder.tv_ten.setText(data.getName());
        //set tuổi on views
        holder.tv_tuoi.setText(String.valueOf(data.getTuoi()));
        holder.tv_gioiTinh.setText(data.getGioiTinh());
        // xử lí click edit
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialixe main data
                Person d = dataList.get(holder.getAdapterPosition());

                //Get id
                final int id = d.getId();
                //Get text
                String name = d.getName();
                int tuoi = d.getTuoi();
                String gioiTinh = d.getGioiTinh();
                //Create dialog
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_update);
                //Initialize width
                int width = WindowManager.LayoutParams.MATCH_PARENT;

                //Initialize height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width, height);
                //Show dialog
                dialog.show();
                //Initialize and assign variable
                EditText update_name = dialog.findViewById(R.id.update_name);
                EditText update_tuoi = dialog.findViewById(R.id.update_tuoi);
                Spinner snp_gioiTinh = (Spinner) dialog.findViewById(R.id.snp_gioiTinh);
                Button btUpdate = dialog.findViewById(R.id.bt_update);


                ArrayList<String> arrayListGioiTinh = new ArrayList<>();
                arrayListGioiTinh.add("Nam");
                arrayListGioiTinh.add("Nữ");
                ArrayAdapter arrayAdapter = new ArrayAdapter(dialog.getContext(), android.R.layout.simple_spinner_item,arrayListGioiTinh);
                snp_gioiTinh.setAdapter(arrayAdapter);

                //Set text on edit text
                update_name.setText(name);
                update_tuoi.setText(String.valueOf(tuoi));
                if (gioiTinh.equals("Nam")) {
                    //Nam = 0
                    snp_gioiTinh.setSelection(0);
                } else {
                    //Nữ =1
                    snp_gioiTinh.setSelection(1);
                }

                btUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Dismiss dialog
                        dialog.dismiss();

                        //get updated text from edit text
                        String uName = update_name.getText().toString().trim();
                        int uTuoi = Integer.parseInt(update_tuoi.getText().toString().trim());
                        String uGioiTinh = snp_gioiTinh.getSelectedItem().toString();

                        //Update text in database
                        database.personDAO().update(id, uName, uTuoi, uGioiTinh);
                        Toast.makeText(view.getContext(), "Update thành công!", Toast.LENGTH_LONG).show();
                        //load lại datalist sau khi update
                        dataList.clear();
                        dataList.addAll(database.personDAO().getAll());
                        notifyDataSetChanged();

                    }
                });
            }
        });
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize main data
                Person d = dataList.get(holder.getAdapterPosition());
                //Delete text from database
                database.personDAO().delete(d);
                Toast.makeText(view.getContext(), "Xóa Thành công!", Toast.LENGTH_LONG).show();
                //Notify when data is deleted
//                int position = holder.getAdapterPosition();
//                dataList.remove(position);
//                notifyItemChanged(position);
//                notifyItemRangeChanged(position, dataList.size());
                dataList.clear();
                dataList.addAll(database.personDAO().getAll());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ten, tv_tuoi, tv_gioiTinh;
        ImageView btEdit, btDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ten = itemView.findViewById(R.id.tv_Ten);
            tv_tuoi = itemView.findViewById(R.id.tv_tuoi);
            tv_gioiTinh = itemView.findViewById(R.id.tv_gioiTinh);
            btEdit = itemView.findViewById(R.id.bt_edit);
            btDelete = itemView.findViewById(R.id.bt_delete);
        }
    }
}
