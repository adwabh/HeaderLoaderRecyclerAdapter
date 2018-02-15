package io.app.fabogo.util.adapterutils;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static io.app.fabogo.util.adapterutils.HeaderLoadMoreAdapter.VIEW_TYPE.CELL;
import static io.app.fabogo.util.adapterutils.HeaderLoadMoreAdapter.VIEW_TYPE.HEADER;
import static io.app.fabogo.util.adapterutils.HeaderLoadMoreAdapter.VIEW_TYPE.LOADER;
import static io.app.fabogo.util.adapterutils.HeaderLoadMoreAdapter.VIEW_TYPE.ERROR;


/**
 * Created by adwait on 18/12/17.
 */

public abstract class HeaderLoadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean isLoading;
    private boolean isHeader;

    public HeaderLoadMoreAdapter(boolean isHeader) {
        this.isHeader = isHeader;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({HEADER, LOADER, CELL})
    public @interface VIEW_TYPE {
        public static final int HEADER = 0;
        public static final int LOADER = 1;
        public static final int CELL = 2;
        public static final int ERROR = 3;
    }

    private boolean isError;

    @Override
    final public int getItemCount() {
        int count = getChildItemCount();
        count = isHeader ? count + 1 : count;
        return isLoading ? count + 1 : count;
    }

    @Override
    final public int getItemViewType(int position) {
        if (isError) {
            return ERROR;
        } else if (isHeader && position == 0) {
            return HEADER;
        } else if (isLoading && position == getItemCount() - 1) {
            return LOADER;
        } else {
            return getChildItemViewType(isHeader ? position - 1 : position);
        }
    }


    @Override
    final public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ERROR:
                break;
            case HEADER:
                return onCreateHeaderViewHolder(parent, viewType);
            case LOADER:
                return onCreateLoaderViewHolder(parent, viewType);
            default:
                return onCreateCellViewHolder(parent, viewType);

        }
        return null;
    }

    @Override
    final public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int newPosition = isHeader ? position - 1 : position;
        if (holder instanceof BaseHeaderViewHolder) {
            onBindHeaderViewHolder(holder, newPosition);
        } else if (holder instanceof BaseLoaderViewHolder) {
            onBindLoaderViewHolder(holder, newPosition, isLoading);
        } else {
            onBindCellViewHolder(holder, newPosition);
        }
    }

    protected class BaseHeaderViewHolder extends RecyclerView.ViewHolder {
        public BaseHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class BaseLoaderViewHolder extends RecyclerView.ViewHolder {
        public BaseLoaderViewHolder(View itemView) {
            super(itemView);
        }
    }


    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
        notifyDataSetChanged();
    }

    public void setIsHeader(boolean isHeader) {
        this.isHeader = isHeader;
        notifyDataSetChanged();
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
        try {
        if (isLoading) {
            notifyItemInserted(getItemCount());
        } else {
            notifyDataSetChanged();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*try {
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    abstract protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent,
                                                                        int viewType);

    abstract protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position);

    abstract protected RecyclerView.ViewHolder onCreateLoaderViewHolder(ViewGroup parent,
                                                                        int viewType);

    abstract protected void onBindLoaderViewHolder(RecyclerView.ViewHolder holder, int position,
                                                   boolean isLoading);

    protected abstract RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent,
                                                                      int viewType);

    abstract protected void onBindCellViewHolder(RecyclerView.ViewHolder holder, int position);

    protected abstract int getChildItemCount();

    protected abstract int getChildItemViewType(int position);

}
