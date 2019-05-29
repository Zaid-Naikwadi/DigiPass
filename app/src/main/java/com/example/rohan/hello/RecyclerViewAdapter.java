package com.example.rohan.hello;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rohan.hello.RecyclerViewActivity;
import com.example.rohan.hello.ShareViewActivity;
import com.example.rohan.hello.onMoveAndSwipedListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import static com.example.rohan.hello.RecyclerViewActivity.CONTACTS_NO;
import static com.example.rohan.hello.RecyclerViewActivity.MY_PREFS_NAME;
import static com.example.rohan.hello.RecyclerViewActivity.NO_CONTACTS;
import static com.example.rohan.hello.RecyclerViewActivity.sharedPrefs1;

/**
 * Created by zhang on 2016.08.07.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements onMoveAndSwipedListener {

    private Context context;
    private List<String> mItems;
    private List<String> mNumbers;
    private int color = 0;
    private View parentView;

    private final int TYPE_NORMAL = 1;
    private final int TYPE_FOOTER = 2;
    private final String FOOTER = "footer";

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        mItems = new ArrayList();
        mNumbers = new ArrayList();
    }

    public void setItems(List<String> data,List<String> numbers) {
        this.mItems.addAll(data);
        this.mNumbers.addAll(numbers);
        notifyDataSetChanged();
    }

    public void addItem(int position, String insertData,String number) {
        mItems.add(position, insertData);
        mNumbers.add(position,number);
        notifyItemInserted(position);
    }

    public void addItems(List<String> data) {
        mItems.addAll(data);
        notifyItemInserted(mItems.size() - 1);
    }

    public void addFooter() {
        mItems.add(FOOTER);
        notifyItemInserted(mItems.size() - 1);
    }

    public void removeFooter() {
        mItems.remove(mItems.size() - 1);
        notifyItemRemoved(mItems.size());
    }

    public void setColor(int color) {
        this.color = color;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parentView = parent;
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view, parent, false);
            return new RecyclerViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_footer, parent, false);
              return new FooterViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RecyclerViewHolder) {
            final RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
            ((RecyclerViewHolder) holder).tv_contactName.setText(mItems.get(position).toString());
            ((RecyclerViewHolder) holder).tv_contactNumber.setText(mNumbers.get(position).toString());
            ((RecyclerViewHolder) holder).contactPhoto.setImageResource(R.drawable.ic_person_24dp);

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_recycler_item_show);
            recyclerViewHolder.mView.startAnimation(animation);

            AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
            aa1.setDuration(400);
            recyclerViewHolder.contactPhoto.startAnimation(aa1);
           // recyclerViewHolder.rela_round.startAnimation(aa1);

            AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
            aa.setDuration(400);

          /*  if (color == 1) {
                recyclerViewHolder.rela_round.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.google_blue)));
            } else if (color == 2) {
                recyclerViewHolder.rela_round.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.google_green)));
            } else if (color == 3) {
                recyclerViewHolder.rela_round.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.google_yellow)));
            } else if (color == 4) {
                recyclerViewHolder.rela_round.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.google_red)));
            } else {
                recyclerViewHolder.rela_round.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.gray)));
            }*/

            recyclerViewHolder.contactPhoto.startAnimation(aa);

            recyclerViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShareViewActivity.class);
                    intent.putExtra("color", color);
                    context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation
                            ((Activity) context, recyclerViewHolder.contactPhoto, "shareView").toBundle());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        String s = mItems.get(position);
        if (s.equals(FOOTER)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        Collections.swap(mNumbers,fromPosition,toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(final int position) {
         String contactId="";

        Map<String, ?> allEntries1 = sharedPrefs1.getAll();
        for (Map.Entry<String, ?> entry : allEntries1.entrySet()) {
            if(mNumbers.get(position) == entry.getValue().toString())
                contactId = entry.getKey();
        }

        final SharedPreferences.Editor editor = getPrefs(context,MY_PREFS_NAME).edit();
        final SharedPreferences.Editor editor1 = getPrefs(context,CONTACTS_NO).edit();

        editor.remove(contactId);
        editor1.remove(contactId);

        editor.apply();
        editor1.apply();

        mItems.remove(position);
        mNumbers.remove(position);

        if(mItems.isEmpty()){
            SharedPreferences.Editor editor2 = getPrefs(context,NO_CONTACTS).edit();
            editor2.putBoolean("noContacts",true);
            editor2.apply();
        }
        notifyItemRemoved(position);

      /*  Snackbar.make(parentView, context.getString(R.string.item_swipe_dismissed), Snackbar.LENGTH_SHORT)
                .setAction(context.getString(R.string.item_swipe_undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            addItem(position, mItems.get(position), mNumbers.get(position));
                            editor.putString(contactId, mItems.get(position));
                            editor1.putString(contactId, mNumbers.get(position));
                            editor.apply();
                            editor1.apply();
                        }catch (Exception e){
                            Toast.makeText(context, e.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();*/
    }


    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private View mView;

        private ImageView contactPhoto;
        private TextView tv_contactName,tv_contactNumber;

        private RecyclerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
         //   rela_round = itemView.findViewById(R.id.rela_round);
            tv_contactName = itemView.findViewById(R.id.tv_recycler_item_1);
            tv_contactNumber = itemView.findViewById(R.id.tv_recycler_item_2);
            contactPhoto = itemView.findViewById(R.id.imageView1);
        }
    }

   private class FooterViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progress_bar_load_more;

       private FooterViewHolder(View itemView) {
            super(itemView);
            progress_bar_load_more = itemView.findViewById(R.id.progress_bar_load_more);
        }
    }

    private static SharedPreferences getPrefs(Context context,String prefName) {
        return context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

}
