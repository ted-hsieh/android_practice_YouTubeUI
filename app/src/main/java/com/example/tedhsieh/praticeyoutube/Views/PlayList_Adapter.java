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

public class PlayList_Adapter extends BaseAdapter {

    private LayoutInflater playlist_inflater;
    ArrayList<PlayList_Item> playlist_item;

    PlayList_Adapter(Context context){
        playlist_inflater = LayoutInflater.from(context);
        playlist_item = new ArrayList<>();
    }

    public void addListItem(String Owner){
        playlist_item.add(new PlayList_Item(Owner, PlayList_Item.ItemType_ListTitle));
        notifyDataSetChanged();
    }

    public void addListItem(int index, String Owner){
        playlist_item.add(index, new PlayList_Item(Owner, PlayList_Item.ItemType_ListTitle));
        notifyDataSetChanged();
    }

    public void addSongItem(String PicName, String Title, String Owner, String Information, String Time){
        playlist_item.add(new PlayList_Item(PicName, Title, Owner, Information, Time, PlayList_Item.ItemType_Song) );
        notifyDataSetChanged();
    }

    public void addSongItemAtIndex(int index, String PicName, String Title, String Owner, String Information, String Time){
        playlist_item.add(index, new PlayList_Item(PicName, Title, Owner, Information, Time, PlayList_Item.ItemType_Song) );
        notifyDataSetChanged();
    }

    public void addMoreButtonItem(String Owner){
        playlist_item.add(new PlayList_Item(Owner, PlayList_Item.ItemType_MoreButton) );
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return playlist_item.size();
    }

    @Override
    public Object getItem(int arg0){
        return playlist_item.get(arg0);
    }

    @Override
    public long getItemId(int position){
        return playlist_item.indexOf(getItem(position));
    }

    @Override
    public int getItemViewType(int position){
        return playlist_item.get(position).getItemType();
    }

    @Override
    public int getViewTypeCount() {
        return PlayList_Item.ItemType_MaxCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        ItemViewHolder itemview = null;
        PlayList_Item item = (PlayList_Item) getItem(position);

        if(convertView == null){
            switch (item.getItemType()){
                case PlayList_Item.ItemType_ListTitle:
                    convertView = playlist_inflater.inflate(R.layout.playlist_item_listtitle, null);
                    itemview = CreateListTitleView(convertView);
                    break;
                case PlayList_Item.ItemType_Song:
                    convertView = playlist_inflater.inflate(R.layout.playlist_item_song, null);
                    itemview = CreateSongView(convertView);
                    break;
                case PlayList_Item.ItemType_MoreButton:
                    convertView = playlist_inflater.inflate(R.layout.item_morebutton, null);
                    break;
            }
            convertView.setTag(itemview);
        }
        else{
            itemview = (ItemViewHolder) convertView.getTag();
        }

        switch (item.getItemType()){
            case PlayList_Item.ItemType_ListTitle:
                SetListTitleView(container, itemview, item);
                break;
            case PlayList_Item.ItemType_Song:
                SetSongView(container, itemview, item);
                break;
        }

        return convertView;
    }

    private ItemViewHolder CreateListTitleView(View convertView){
        ItemViewHolder itemview = new ItemViewHolder(
                (TextView) convertView.findViewById(R.id.list_title),
                convertView.findViewById(R.id.list_popmenu)
        );
        return itemview;
    }

    private void SetListTitleView(ViewGroup container, ItemViewHolder itemview, PlayList_Item item){
        itemview.Owner.setText(item.getOwner());

        // Set the item as the button's tag so it can be retrieved later
        itemview.PopMenu.setTag(item);
        // Set the fragment instance as the OnClickListener
        itemview.PopMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity currentActivity = (Activity) view.getContext();

                // Retrieve the clicked item from view's tag
                final PlayList_Item item = (PlayList_Item) view.getTag();

                // Create a PopupMenu, giving it the clicked view for an anchor
                PopupMenu popup = new PopupMenu(currentActivity, view);

                // Inflate our menu resource into the PopupMenu's Menu
                popup.getMenuInflater().inflate(R.menu.menu_playlist_item_listtile, popup.getMenu());

                // Set a listener so we are notified if a menu item is clicked
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_allvideolists_notinsterenting:
                                Toast.makeText(currentActivity, "Item Clicked: AllVideoList_Notinsterenting", Toast.LENGTH_SHORT).show();
                                return true;
                        }
                        return false;
                    }
                });
                // Finally show the PopupMenu
                popup.show();
            }
        });
    }

    private ItemViewHolder CreateSongView(View convertView){
        ItemViewHolder itemview = new ItemViewHolder(
                (ImageView) convertView.findViewById(R.id.song_thumbnail),
                (TextView) convertView.findViewById(R.id.song_title),
                (TextView) convertView.findViewById(R.id.song_owner),
                (TextView) convertView.findViewById(R.id.song_information),
                (TextView) convertView.findViewById(R.id.song_time),
                convertView.findViewById(R.id.song_popmenu)
        );

        return itemview;
    }

    private void SetSongView(ViewGroup container, ItemViewHolder itemview, PlayList_Item item){
        int PicId = container.getResources().getIdentifier(item.getPicName(), "drawable", container.getContext().getPackageName());
        itemview.Pic.setImageResource(PicId);
        itemview.Title.setText(item.getTitle());
        itemview.Owner.setText(item.getOwner());
        itemview.Information.setText(item.getInformation());
        itemview.Time.setText(item.getTime());

        // Set the item as the button's tag so it can be retrieved later
        itemview.PopMenu.setTag(item);
        // Set the fragment instance as the OnClickListener
        itemview.PopMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity currentActivity = (Activity) view.getContext();

                // Retrieve the clicked item from view's tag
                final PlayList_Item item = (PlayList_Item) view.getTag();

                // Create a PopupMenu, giving it the clicked view for an anchor
                PopupMenu popup = new PopupMenu(currentActivity, view);

                // Inflate our menu resource into the PopupMenu's Menu
                popup.getMenuInflater().inflate(R.menu.menu_playlist_item_song, popup.getMenu());

                // Set a listener so we are notified if a menu item is clicked
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_notinsterenting:
                                Toast.makeText(currentActivity, "Item Clicked: Notinsterenting", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu_playlater:
                                Toast.makeText(currentActivity, "Item Clicked: Playlater", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu_playlist:
                                Toast.makeText(currentActivity, "Item Clicked: Playlist", Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.menu_share:
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

    private class ItemViewHolder{
        ImageView Pic;
        TextView Title;
        TextView Owner;
        TextView Information;
        TextView Time;
        View PopMenu;

        public ItemViewHolder(ImageView Pic,TextView Title, TextView Owner, TextView Information, TextView Time, View PopMenu){
            this.Pic = Pic;
            this.Title = Title;
            this.Owner = Owner;
            this.Information = Information;
            this.Time = Time;
            this.PopMenu = PopMenu;
        }

        public ItemViewHolder(TextView Owner, View PopMenu){
            this.Owner = Owner;
            this.PopMenu = PopMenu;
        }
    }
}
