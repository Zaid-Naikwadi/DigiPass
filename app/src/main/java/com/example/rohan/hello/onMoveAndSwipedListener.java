package com.example.rohan.hello;

/**
 * Created by zhang on 2016.08.21.
 */
public interface onMoveAndSwipedListener {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

}
