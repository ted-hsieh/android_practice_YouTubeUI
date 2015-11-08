package com.example.tedhsieh.praticeyoutube.Views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tedhsieh.praticeyoutube.R;

import java.util.ArrayList;

public class SubscribedChannel_Adapter extends BaseAdapter{
    private LayoutInflater subscribedchannel_inflater;
    ArrayList<SubscribedChannel_Item> subscribedchannel_item;
    private Context parentContext;

    public SubscribedChannel_Adapter(Context context) {
        subscribedchannel_inflater = LayoutInflater.from(context);
        subscribedchannel_item = new ArrayList<>();
        parentContext = context;
    }

    public void addListTitleItem(String Title){
        subscribedchannel_item.add(new SubscribedChannel_Item(Title, SubscribedChannel_Item.ItemType_ChannelListTitle));
        notifyDataSetChanged();
    }

    public void addListTitleItemAtIndex(int index, String Title){
        subscribedchannel_item.add(index, new SubscribedChannel_Item(Title, SubscribedChannel_Item.ItemType_ChannelListTitle));
        notifyDataSetChanged();
    }

    public void addChannelItem(String PicName, String Title, String Information){
        subscribedchannel_item.add(new SubscribedChannel_Item(PicName, Title, Information, SubscribedChannel_Item.ItemType_ChannelListItem));
        notifyDataSetChanged();
    }

    public void addChannelItemAtIndex(int index, String PicName, String Title, String Information){
        subscribedchannel_item.add(index, new SubscribedChannel_Item(PicName, Title, Information, SubscribedChannel_Item.ItemType_ChannelListItem));
        notifyDataSetChanged();
    }

    public void addMoreButton(){
        subscribedchannel_item.add(new SubscribedChannel_Item("MoreButton", SubscribedChannel_Item.ItemType_MoreButton));
        notifyDataSetChanged();
    }

    public void addTopBlock(){
        subscribedchannel_item.add(new SubscribedChannel_Item("TopBlock", SubscribedChannel_Item.ItemType_TopBlock));
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return subscribedchannel_item.size();
    }

    @Override
    public Object getItem(int arg0){
        return subscribedchannel_item.get(arg0);
    }

    @Override
    public long getItemId(int position){
        return subscribedchannel_item.indexOf(getItem(position));
    }

    @Override
    public int getItemViewType(int position){
        return subscribedchannel_item.get(position).getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return SubscribedChannel_Item.ItemType_MaxCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        ItemViewHolder itemview = null;
        SubscribedChannel_Item item = (SubscribedChannel_Item) getItem(position);

        if(convertView == null){
            switch (item.getItemType()){
                case SubscribedChannel_Item.ItemType_TopBlock:
                    convertView = subscribedchannel_inflater.inflate(R.layout.subscribedchannel_topblock, null);
                    convertView.setClickable(true);
                    break;
                case SubscribedChannel_Item.ItemType_ChannelListTitle:
                    convertView = subscribedchannel_inflater.inflate(R.layout.subscribedchannel_channeltitle, null);
                    itemview = CreateListTitleView(convertView);
                    convertView.setClickable(true);
                    break;
                case SubscribedChannel_Item.ItemType_ChannelListItem:
                    convertView = subscribedchannel_inflater.inflate(R.layout.subscribedchannel_channelitem, null);
                    itemview = CreateChannelItemView(convertView);
                    convertView.setClickable(false);
                    break;
                case SubscribedChannel_Item.ItemType_MoreButton:
                    convertView = subscribedchannel_inflater.inflate(R.layout.item_morebutton, null);
                    convertView.setClickable(false);
                    break;
            }
            convertView.setTag(itemview);
        }
        else{
            itemview = (ItemViewHolder) convertView.getTag();
        }

        switch (item.getItemType()){
            case SubscribedChannel_Item.ItemType_TopBlock:
                setTopBlockView(container, convertView);
                break;
            case SubscribedChannel_Item.ItemType_ChannelListTitle:
                SetListTitleView(container, itemview, item);
                break;
            case SubscribedChannel_Item.ItemType_ChannelListItem:
                SetChannelItemViewView(container, itemview, item);
                break;
        }

        return convertView;
    }

    private void setTopBlockView(final ViewGroup container, View convertView){

//        convertView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.getBackground().setHotspot(event.getX(), event.getY());
//                return false;
//            }
//        });

    }

    private ItemViewHolder CreateListTitleView(View convertView){
        ItemViewHolder itemview = new ItemViewHolder(
                (TextView) convertView.findViewById(R.id.subscribedchannel_channellist_listtitle)
        );
        return itemview;
    }

    private void SetListTitleView(ViewGroup container, ItemViewHolder itemview, SubscribedChannel_Item item){
        itemview.Title.setText(item.getTitle());
    }

    private ItemViewHolder CreateChannelItemView(View convertView){
        ItemViewHolder itemview = new ItemViewHolder(
                (ImageView) convertView.findViewById(R.id.subscribedchannel_channellist_itemimage),
                (TextView) convertView.findViewById(R.id.subscribedchannel_channellist_itemtitle),
                (TextView) convertView.findViewById(R.id.subscribedchannel_channellist_iteminformation)
        );

        return itemview;
    }

    private void SetChannelItemViewView(ViewGroup container, ItemViewHolder itemview, SubscribedChannel_Item item){
        int PicId = container.getResources().getIdentifier(item.getPicName(), "drawable", container.getContext().getPackageName());
        itemview.Pic.setImageResource(PicId);
        itemview.Title.setText(item.getTitle());
        itemview.Information.setText(item.getInformation());
    }

    private class ItemViewHolder{

        ImageView Pic;
        TextView Title;
        TextView Information;

        public ItemViewHolder(ImageView Pic,TextView Title, TextView Information){
            this.Pic = Pic;
            this.Title = Title;
            this.Information = Information;
        }

        public ItemViewHolder(TextView Title){
            this.Title = Title;
        }
    }

}
