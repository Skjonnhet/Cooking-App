package com.example.ninah.cooking_app;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;
/*****************Class to allow a customized listView-adapter for the RecipeStartActivity************************************/
//source https://www.youtube.com/watch?v=YMJSBHAZsso, 23.09.17

/**
 * Created by Jonas on 23.09.2017.
 */

public class IngridentListViewAdapter extends ArrayAdapter<Ingrident> {
    private static Context context;
    private static List<Ingrident> ingridents;
    private Activity activity;

    //constructor need contest and the list of ingridents
    public IngridentListViewAdapter(Activity activity, Context context, List<Ingrident> ingridents) {
        super(context,R.layout.ingrident_listview_adapter,ingridents);
        Log.d("IngrAdapter","constructor constructed");
        this.context=context;
        this.ingridents=ingridents;
        this.activity=activity;
    }

    public void notifyChanges(List<Ingrident> ingridents) {
        ingridents.clear();
        ingridents.addAll(ingridents);
        this.notifyDataSetChanged();
    }

    //returns listsize
    @Override
    public int getCount() {
        return ingridents.size();
    }


    //returns single item of the lists at the position
    @Override
    public Ingrident getItem(int position) {
        return  ingridents.get(position);
    }
    //returns single item position of the lists at the position
    @Override
    public long getItemId(int position) {
        return position;
    }


    //is responsible how recipe is shown in the listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=View.inflate(this.context, R.layout.ingrident_listview_adapter,null);
        TextView name=(TextView) view.findViewById(R.id.ingrident_name);
        TextView amount=(TextView) view.findViewById(R.id.ingrident_amount);
        TextView unit=(TextView) view.findViewById(R.id.ingrident_unit);
        name.setText(ingridents.get(position).getName());
        amount.setText(String.valueOf(ingridents.get(position).getMenge()));
        unit.setText(ingridents.get(position).getEinheit());

        view.setTag(ingridents.get(position).getId());
        Log.d("IngrAdapter","getView" +position);
        return view;
    }

    @Override
    public void add(@Nullable Ingrident object) {
        super.add(object);
    }

    @Override
    public void addAll(@NonNull Collection<? extends Ingrident> collection) {
        super.addAll(collection);
    }

    @Override
    public void addAll(Ingrident... items) {
        super.addAll(items);
    }

    @Override
    public void insert(@Nullable Ingrident object, int index) {
        super.insert(object, index);
    }

    @Override
    public void remove(@Nullable Ingrident object) {
        super.remove(object);
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public void notifyDataSetChanged() {
        notifyIngridentsChanged();
        super.notifyDataSetChanged();
    }

    public void notifyIngridentsChanged() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IngridentListViewAdapter.super.notifyDataSetChanged();
            }
        });
    }
}
