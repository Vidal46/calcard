package br.com.calcard.android.app.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.calcard.android.app.R;
import br.com.calcard.android.app.model.Menu;


public class MenuAdapter extends BaseAdapter {
    private List<Menu> menus;
    private Activity context;

    public MenuAdapter(List<Menu> menus, Activity context) {
        this.menus = menus;
        this.context = context;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = context.getLayoutInflater()
                .inflate(R.layout.recycler_view_menu_nav, parent, false);
        ImageView img = view.findViewById(R.id.imgIcon);
        TextView title= view.findViewById(R.id.tvtitle);
        Menu menu = menus.get(position);
        img.setImageResource(menu.getImg());
        title.setText(menu.getName());
        return view;
    }
}
