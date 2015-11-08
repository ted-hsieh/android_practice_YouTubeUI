package com.example.tedhsieh.praticeyoutube.Views;

import com.example.tedhsieh.praticeyoutube.TabSlider.SlidingTabLayout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tedhsieh.praticeyoutube.R;

public class Fragment_FirstPage_SlidingTabs extends Fragment{

    static final String Song_Message = "com.example.tedhsieh.praticeyoutube.Song_Message";
    static final String LOG_TAG = "SlidingTabsFragment";

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    PlayVideoWithTitleListener PlayVideoWithTitleCallback;


    // Container Activity must implement this interface
    public interface PlayVideoWithTitleListener {
        public void PlayVideoWithTitle(String Title);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_first_page, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            PlayVideoWithTitleCallback = (PlayVideoWithTitleListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mViewPager = (ViewPager) view.findViewById(R.id.pagecontent);
        mViewPager.setAdapter(new SlidingTabsAdapter());

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.firstpage_slidingtab);
        mSlidingTabLayout.setCustomTabView(R.layout.slidingtab_icon, 0);
        mSlidingTabLayout.setDividerColors(new int[]{ContextCompat.getColor(getContext(), R.color.colorPrimary)});
        mSlidingTabLayout.setSelectedIndicatorColors(new int[]{ContextCompat.getColor(getContext(), android.R.color.white)});
        mSlidingTabLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mSlidingTabLayout.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            String[] SlidingTabsTitle = getResources().getStringArray(R.array.slidingtab_title);

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getActivity().setTitle(SlidingTabsTitle[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class SlidingTabsAdapter extends PagerAdapter {

        private int[] imageResId = {
                R.drawable.ic_tab_home,
                R.drawable.ic_tab_hotvideo,
                R.drawable.ic_tab_playlist,
                R.drawable.ic_tab_account
        };

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable image = ContextCompat.getDrawable(getContext(),imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view;
            // Inflate a new layout from our resources
            switch (position){
                case 0:
                    view = initFragmentHomePage(container);
                    break;
                case 1:
                    view = initFragmentHotVideo(container);
                    break;
                case 2:
                    view = initFragmentSubscribedChannel(container);
                    break;
                case 3:
                    view = initFragmentAccountPage(container);
                    break;
                default:
                    view = initDebugPage(container, position);
                    break;
            }

            // Add the newly created View to the ViewPager
            container.addView(view);


//            Log.i(LOG_TAG, "instantiateItem() [position: " + position + "]");

            // Return the View
            return view;
        }

        private View initFragmentHomePage(ViewGroup container){
            View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_home_page,
                    container, false);

            View floatingbutton = view.findViewById(R.id.homepage_floatingbutton);
            floatingbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Floating Button Clicked ", Toast.LENGTH_SHORT).show();
                }
            });

            final ListView playlist = (ListView) view.findViewById(R.id.homepage_playlist);
            final PlayList_Adapter playlist_adapter = new PlayList_Adapter(getContext());

            setplaylisview(view, playlist, playlist_adapter);

            return view;
        }

        private View initFragmentHotVideo(ViewGroup container){
            View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_hotvideo, container, false);
            final ListView playlist = (ListView) view.findViewById(R.id.hotvideo_playlist);
            final HotVideo_Adapter playlist_adapter = new HotVideo_Adapter(getContext());

            sethotvideolistview(view, playlist, playlist_adapter);

            return view;
        }

        private View initFragmentSubscribedChannel(ViewGroup container){
            View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_subcribedchannel, container, false);
            final ListView chnanellist = (ListView) view.findViewById(R.id.subscibedchannel_channellist);
            final SubscribedChannel_Adapter channellist_adapter = new SubscribedChannel_Adapter(getContext());

            setsubcribedchannellistview(view, chnanellist, channellist_adapter);

            return view;
        }

        private View initFragmentAccountPage(ViewGroup container){
            View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_accountpage, container, false);

            final ListView informationlist = (ListView) view.findViewById(R.id.accountpage_information);
            final AccountPage_Adapter informationlist_adapter = new AccountPage_Adapter(getContext());

            setaccountpage(view, informationlist, informationlist_adapter);
            return view;
        }

        private View initDebugPage(ViewGroup container, int position){
            View view = getActivity().getLayoutInflater().inflate(R.layout.debug_settitle_page,
                    container, false);

            TextView viewtext = (TextView) view.findViewById(R.id.item_title);
            viewtext.setText("test" + position);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
//            Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
        }

        public void setplaylisview(final View view, ListView playlist, final PlayList_Adapter playlist_adapter){

            playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PlayList_Item item = (PlayList_Item) playlist_adapter.getItem(position);

                    switch (item.getItemType()){
                        case PlayList_Item.ItemType_ListTitle:
                            // Show a toast if the user clicks on a list
                            Toast.makeText(getContext(), "List Clicked: " + item.getOwner(), Toast.LENGTH_SHORT).show();
                            break;
                        case PlayList_Item.ItemType_Song:
//                            Intent intent = new Intent(getActivity(), Activity_VideoView.class);
//                            intent.putExtra(Song_Message, item.getTitle());
//                            startActivity(intent);
                            PlayVideoWithTitleCallback.PlayVideoWithTitle(item.getTitle());
//                            Toast.makeText(getContext(), "Song Of " + item.getOwner() + " Clicked : " +item.getTitle(), Toast.LENGTH_SHORT).show();
                            break;
                        case PlayList_Item.ItemType_MoreButton:
                            playlist_adapter.addSongItemAtIndex(position, "song_2", "Song_2", item.getOwner(), "Nice Song_2", "30:35");
                            playlist_adapter.addSongItemAtIndex(position, "song_1", "Song_1", item.getOwner(), "Nice Song_1", "12:35");
                            Toast.makeText(getContext(), "MoreButton Of " + item.getOwner() + "Clicked.", Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            });

            playlist.setOnScrollListener(new AbsListView.OnScrollListener() {
                int LastFirstVisibleItem = 0;

                final View flaotingbutton = view.findViewById(R.id.homepage_floatingbutton);
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                    LastFirstVisibleItem = view.getFirstVisiblePosition();
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    boolean onTop = firstVisibleItem == 0 && view.getChildAt(0) != null && view.getChildAt(0).getTop() == 0;
                    if( (firstVisibleItem + 6 < LastFirstVisibleItem) || onTop ){
                        flaotingbutton.setVisibility(View.VISIBLE);
                    }

                    if (firstVisibleItem  > LastFirstVisibleItem) {
                        flaotingbutton.setVisibility(View.INVISIBLE);
                    }

                }
            });

            playlist.setDivider(null);

            playlist_adapter.addListItem("Owner_1");
            playlist_adapter.addSongItem("song_1", "Song_1", "Owner_1", "Nice Song_1", "5:02");
            playlist_adapter.addSongItem("song_2", "Song_2", "Owner_1", "Nice Song_2", "10:31");
            playlist_adapter.addSongItem("song_1", "Song_1", "Owner_1", "Nice Song_1", "30:35");
            playlist_adapter.addMoreButtonItem("Owner_1");

            playlist_adapter.addListItem("Owner_2");
            playlist_adapter.addSongItem("song_2", "Song_2", "Owner_2", "Great Song_2", "5:02");
            playlist_adapter.addSongItem("song_1", "Song_1", "Owner_2", "Great Song_1", "10:31");
            playlist_adapter.addSongItem("song_2", "Song_2", "Owner_2", "Great Song_2", "30:35");
            playlist_adapter.addMoreButtonItem("Owner_2");

            playlist_adapter.addListItem("Owner_3");
            playlist_adapter.addSongItem("song_2", "Song_2", "Owner_3", "Great Song_2", "5:02");
            playlist_adapter.addSongItem("song_1", "Song_1", "Owner_3", "Great Song_1", "10:31");
            playlist_adapter.addSongItem("song_2", "Song_2", "Owner_3", "Great Song_2", "30:35");
            playlist_adapter.addSongItem("song_1", "Song_1", "Owner_3", "Great Song_1", "5:02");
            playlist_adapter.addSongItem("song_2", "Song_2", "Owner_3", "Great Song_2", "10:31");
            playlist_adapter.addSongItem("song_1", "Song_1", "Owner_3", "Great Song_1", "30:35");
            playlist_adapter.addMoreButtonItem("Owner_3");

            playlist.setAdapter(playlist_adapter);

        }

        public void sethotvideolistview(final View view, final ListView playlist, final HotVideo_Adapter playlist_adapter){

            playlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HotVideo_Item item = (HotVideo_Item) playlist_adapter.getItem(position);

                    switch (item.getItemType()) {
                        case HotVideo_Item.ItemType_Channel:
                            break;
                        case HotVideo_Item.ItemType_Video:
                            // Show a toast if the user clicks on a list
                            PlayVideoWithTitleCallback.PlayVideoWithTitle(item.getTitle());
//                            Toast.makeText(getContext(), "Video Clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            break;
                    }

                }
            });

            playlist.setDivider(null);

            playlist_adapter.addChannelView();
            playlist_adapter.addVideoItemView("first song", "song_1", "12:43", "popsong 觀看次數: 66k");
            playlist_adapter.addVideoItemView("this song again", "song_2", "32:13", "greatsong 觀看次數: 100k");
            playlist_adapter.addVideoItemView("another song", "song_1", "59:59", "longvideo 觀看次數: 88k");


            playlist.setAdapter(playlist_adapter);
        }

        public void setsubcribedchannellistview(final View view, final ListView channellist, final SubscribedChannel_Adapter channellist_adapter){

            channellist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SubscribedChannel_Item item = (SubscribedChannel_Item) channellist_adapter.getItem(position);

                    switch (item.getItemType()) {
                        case SubscribedChannel_Item.ItemType_ChannelListItem:
                            // Show a toast if the user clicks on a list
                            Toast.makeText(getContext(), "Video Clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            break;
                        case SubscribedChannel_Item.ItemType_MoreButton:
                            channellist_adapter.addChannelItemAtIndex(position, "ic_action_search", "#Game",  "訂閱人數: 1,092,984");
                            channellist_adapter.addChannelItemAtIndex(position, "ic_tab_account", "#Music",  "訂閱人數: 9,091,156");
                            break;
                    }
                }
            });

            channellist.setDivider(null);

            channellist_adapter.addTopBlock();
            channellist_adapter.addListTitleItem("YouTube 精選");
            channellist_adapter.addChannelItem("ic_action_search", "#Game", "訂閱人數: 1,092,984");
            channellist_adapter.addChannelItem("ic_tab_account", "#Music", "訂閱人數: 9,091,156");
            channellist_adapter.addMoreButton();

            channellist_adapter.addListTitleItem("Music");
            channellist_adapter.addChannelItem("ic_tab_hotvideo", "#Sport", "訂閱人數: 1,092,984");
            channellist_adapter.addChannelItem("ic_tab_playlist", "#News", "訂閱人數: 9,091,156");
            channellist_adapter.addMoreButton();

            channellist.setAdapter(channellist_adapter);

        }

        public void setaccountpage(final View view, final ListView informationllist, final AccountPage_Adapter informationllist_adapter){

            view.findViewById(R.id.accountpage_rippleeffect).setClickable(true);

//            view.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    v.findViewById(R.id.accountpage_accountitem).getBackground().setHotspot(event.getX(), event.getY());
//                    return false;
//                }
//            });

            //TODO set account page
            informationllist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AccountPage_InformationItem item = (AccountPage_InformationItem) informationllist_adapter.getItem(position);
                    Toast.makeText(getContext(), item.getTitle() + " Clicked.", Toast.LENGTH_SHORT).show();
                }
            });

            informationllist.setDivider(null);

            informationllist_adapter.addListItem("ic_tab_home", "觀看紀錄");
            informationllist_adapter.addListItem("ic_tab_account", "我的影片");
            informationllist_adapter.addListItem("ic_tab_hotvideo", "稍後觀看");
            informationllist_adapter.addListItem("ic_tab_playlist", "YouTube Red");

            informationllist.setAdapter(informationllist_adapter);

        }

    }

}
