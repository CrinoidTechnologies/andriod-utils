package com.crinoidtechnologies.general.template.recyclerView;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

/**
 * Created by saif on 6/9/2016.
 */
public interface OnRecyclerViewHolderClickListener {

    boolean recyclerViewListClicked(RecyclerView.ViewHolder v, int position);

    boolean recyclerViewListItemLongClicked(RecyclerView.ViewHolder v, int position);

    void onImportantEvent(int type, int position, Bundle info);

}
