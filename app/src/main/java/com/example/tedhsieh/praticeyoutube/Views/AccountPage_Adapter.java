package com.example.tedhsieh.praticeyoutube.Views;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tedhsieh.praticeyoutube.R;

import java.util.ArrayList;

public class AccountPage_Adapter extends BaseAdapter{

    private LayoutInflater accountpage_inflater;
    ArrayList<AccountPage_InformationItem> list_item;

    AccountPage_Adapter(Context context){
        accountpage_inflater = LayoutInflater.from(context);
        list_item = new ArrayList<>();
    }

    public void addListItem(String PicName, String Title){
        list_item.add(new AccountPage_InformationItem(PicName, Title));
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return list_item.size();
    }

    @Override
    public Object getItem(int arg0){
        return list_item.get(arg0);
    }

    @Override
    public long getItemId(int position){
        return list_item.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        ItemViewHolder itemview = null;
        AccountPage_InformationItem item = (AccountPage_InformationItem) getItem(position);

        if(convertView == null){
            convertView = accountpage_inflater.inflate(R.layout.accountpage_information, null);
            itemview = CreateListItemView(convertView);
            convertView.setTag(itemview);
        }
        else{
            itemview = (ItemViewHolder) convertView.getTag();
        }

        SetListItemView(container, itemview, item);
        if(position == (list_item.size()-1) ){
            itemview.TopBorder.setVisibility(View.VISIBLE);
            itemview.BottomBorder.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private ItemViewHolder CreateListItemView(View convertView){
        ItemViewHolder itemview = new ItemViewHolder(
                (TextView) convertView.findViewById(R.id.accountpage_information_topborder),
                (ImageView) convertView.findViewById(R.id.accountpage_information_image),
                (TextView) convertView.findViewById(R.id.accountpage_information_title),
                (TextView) convertView.findViewById(R.id.accountpage_information_bottomborder)
        );

        return itemview;
    }

    private void SetListItemView(ViewGroup container, ItemViewHolder itemview, AccountPage_InformationItem item){
        int PicId = container.getResources().getIdentifier(item.getPicName(), "drawable", container.getContext().getPackageName());
        itemview.Pic.setImageResource(PicId);
        itemview.Title.setText(item.getTitle());

        itemview.TopBorder.setVisibility(View.INVISIBLE);
        itemview.BottomBorder.setVisibility(View.INVISIBLE);
    }

    private class ItemViewHolder{

        TextView TopBorder;
        ImageView Pic;
        TextView Title;
        TextView BottomBorder;

        public ItemViewHolder(TextView TopBorder, ImageView Pic,TextView Title, TextView BottomBorder){
            this.TopBorder = TopBorder;
            this.Pic = Pic;
            this.Title = Title;
            this.BottomBorder = BottomBorder;
        }
    }
}
