package io.tnmma.ezwallet.base.recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public abstract class BaseAdapter<T, B extends ViewDataBinding>
        extends ListAdapter<T, BaseViewHolder<B>> {

    private static final String TAG = BaseAdapter.class.getSimpleName();

    protected BaseAdapter(DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    protected abstract int getItemLayoutId();

    @NonNull
    @Override
    public BaseViewHolder<B> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()), getItemLayoutId(), parent, false);
        return new BaseViewHolder<>(binding);
    }
}
