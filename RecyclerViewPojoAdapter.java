import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public abstract class RecyclerViewPojoAdapter<T, VH extends RecyclerViewPojoAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected RecyclerView recyclerView;
    private List<T> adapterContent = new ArrayList<>();

    public RecyclerViewPojoAdapter(RecyclerView recyclerView) {
        super();
        this.recyclerView = recyclerView;
    }


    public void switchData(@Nullable final List<T> newAdapterContent) {

        if (newAdapterContent == null) {
            notifyItemRangeRemoved(0, adapterContent.size());
            adapterContent.clear();
            return;
        }

        new AsyncTask<Void, Void, DiffUtil.DiffResult>() {

            @Override
            protected DiffUtil.DiffResult doInBackground(Void... params) {

                return DiffUtil.calculateDiff(new DiffUtil.Callback() {

                    @Override
                    public int getOldListSize() {
                        return adapterContent.size();
                    }

                    @Override
                    public int getNewListSize() {
                        return newAdapterContent.size();
                    }

                    @Override
                    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

                        return
                                hasStableIds() ?
                                        getItemId(adapterContent.get(oldItemPosition)) == getItemId(newAdapterContent.get(newItemPosition))
                                        :
                                        Objects.equals(adapterContent.get(oldItemPosition), newAdapterContent.get(newItemPosition));
                    }

                    @Override
                    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                        return Objects.equals(adapterContent.get(oldItemPosition), newAdapterContent.get(newItemPosition));
                    }


                });
            }

            @Override
            protected void onPostExecute(DiffUtil.DiffResult diffResult) {

                adapterContent = newAdapterContent;
                diffResult.dispatchUpdatesTo(RecyclerViewPojoAdapter.this);


            }

        }.execute();


    }


    @Override
    public int getItemCount() {
        return adapterContent.size();
    }

    @Override
    public final long getItemId(int position) {
        return getItemId(adapterContent.get(position));
    }

    abstract long getItemId(T item);
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
