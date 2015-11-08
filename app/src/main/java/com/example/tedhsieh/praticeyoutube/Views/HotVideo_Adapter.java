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

public class HotVideo_Adapter extends BaseAdapter {

    private LayoutInflater hotvideo_inflater;
    ArrayList<HotVideo_Item> hotvideo_item;

    public HotVideo_Adapter(Context context) {
        hotvideo_inflater = LayoutInflater.from(context);
        hotvideo_item = new ArrayList<>();
    }

    public void addChannelView(){
        hotvideo_item.add(new HotVideo_Item(HotVideo_Item.ItemType_Channel));
        notifyDataSetChanged();
    }

    public void addVideoItemView(String Title, String PicName, String Time, String Information){
        hotvideo_item.add(new HotVideo_Item(Title, PicName, Time, Information, HotVideo_Item.ItemType_Video));
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return hotvideo_item.size();
    }

    @Override
    public Object getItem(int arg0) {
        return hotvideo_item.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return hotvideo_item.indexOf(getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        return hotvideo_item.get(position).getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return HotVideo_Item.ItemType_MaxCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        ItemViewHolder itemview = null;
        HotVideo_Item item = (HotVideo_Item) getItem(position);

        if (convertView == null) {
            switch (item.getItemType()) {
                case HotVideo_Item.ItemType_Channel:
                    convertView = hotvideo_inflater.inflate(R.layout.hotvideo_channel, null);
                    convertView.setClickable(true);
                    itemview = CreateChannelView(convertView);
                    break;
                case HotVideo_Item.ItemType_Video:
                    convertView = hotvideo_inflater.inflate(R.layout.hotvideo_videoitem, null);
                    convertView.setClickable(false);
                    itemview = CreateVideoItemView(convertView);
                    break;
            }
            convertView.setTag(itemview);
        } else {
            itemview = (ItemViewHolder) convertView.getTag();
        }

        switch (item.getItemType()) {
            case HotVideo_Item.ItemType_Channel:
                SetChannelView(container, itemview, item);
                break;
            case PlayList_Item.ItemType_Song:
                SetVideoItemView(container, itemview, item);
                break;
        }

        return convertView;
    }

    private ItemViewHolder CreateChannelView(View convertView) {
        ItemViewHolder itemview = new ItemViewHolder(
                (TextView) convertView.findViewById(R.id.hotvideo_channelmusic),
                (TextView) convertView.findViewById(R.id.hotvideo_channelgame),
                (TextView) convertView.findViewById(R.id.hotvideo_channelnews)
        );
        return itemview;
    }

    private void SetChannelView(ViewGroup container, ItemViewHolder itemview, HotVideo_Item item) {

        itemview.MusicChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity currentActivity = (Activity) view.getContext();
                Toast.makeText(currentActivity, "Channel Clicked: Music", Toast.LENGTH_SHORT).show();
            }
        });

        itemview.GameChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity currentActivity = (Activity) view.getContext();
                Toast.makeText(currentActivity, "Channel Clicked: Game", Toast.LENGTH_SHORT).show();
            }
        });

        itemview.NewsChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity currentActivity = (Activity) view.getContext();
                Toast.makeText(currentActivity, "Channel Clicked: News", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ItemViewHolder CreateVideoItemView(View convertView) {
        ItemViewHolder itemview = new ItemViewHolder(
                (TextView) convertView.findViewById(R.id.hotvideo_videoitem_title),
                (ImageView) convertView.findViewById(R.id.hotvideo_videoitem_pic),
                (TextView) convertView.findViewById(R.id.hotvideo_videoitem_time),
                (TextView) convertView.findViewById(R.id.hotvideo_videoitem_information),
                convertView.findViewById(R.id.hotvideo_videoitem_popmenu)
        );
        return itemview;
    }

    private void SetVideoItemView(ViewGroup container, ItemViewHolder itemview, HotVideo_Item item) {
        itemview.Title.setText(item.getTitle());
        int PicId = container.getResources().getIdentifier(item.getPicName(), "drawable", container.getContext().getPackageName());
        itemview.Pic.setImageResource(PicId);
        itemview.Time.setText(item.getTime());
        itemview.Information.setText(item.getInformation());

        // Set the item as the button's tag so it can be retrieved later
        itemview.PopMenu.setTag(item);
        // Set the fragment instance as the OnClickListener
        itemview.PopMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity currentActivity = (Activity) view.getContext();

                // Retrieve the clicked item from view's tag
                final HotVideo_Item item = (HotVideo_Item) view.getTag();

                // Create a PopupMenu, giving it the clicked view for an anchor
                PopupMenu popup = new PopupMenu(currentActivity, view);

                // Inflate our menu resource into the PopupMenu's Menu
                popup.getMenuInflater().inflate(R.menu.menu_hotvideo_videoitem, popup.getMenu());

                // Set a listener so we are notified if a menu item is clicked
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_hotvideo_playlater:
                                Toast.makeText(currentActivity, "Item Clicked: Playlater", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu_hotvideo_playlist:
                                Toast.makeText(currentActivity, "Item Clicked: Playlist", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu_hotvideo_share:
                                Toast.makeText(currentActivity, "Item Clicked: Share", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return true;
                    }
                });
                // Finally show the PopupMenu
                popup.show();
            }
        });

    }

    private class ItemViewHolder {
        TextView Title;
        ImageView Pic;
        TextView Time;
        TextView Information;
        View PopMenu;

        TextView MusicChannel;
        TextView GameChannel;
        TextView NewsChannel;

        public ItemViewHolder(TextView Title, ImageView Pic, TextView Time, TextView Information, View PopMenu) {
            this.Title = Title;
            this.Pic = Pic;
            this.Time = Time;
            this.Information = Information;
            this.PopMenu = PopMenu;
        }

        public ItemViewHolder(TextView MusicChannel, TextView GameChannel, TextView NewsChannel) {
            this.MusicChannel = MusicChannel;
            this.GameChannel = GameChannel;
            this.NewsChannel = NewsChannel;
        }
    }
}

