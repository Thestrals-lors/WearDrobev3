package com.bl4nk.weardrobe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bl4nk.weardrobe.R;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<Bitmap> images;
    private List<String> imageNames;
    private boolean isDeleteVisible = false;
    private OnDeleteClickListener onDeleteClickListener;

    public ImageAdapter(Context context, List<Bitmap> images, List<String> imageNames) {
        this.context = context;
        this.images = images;
        this.imageNames = imageNames;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        final Bitmap bitmap = images.get(position);
        final String name = imageNames.get(position);
        holder.imageView.setImageBitmap(bitmap);
        holder.textView.setText(name);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isDeleteVisible = true;
                notifyDataSetChanged(); // Notify adapter of data set change
                return true;
            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN && !isViewClicked(holder.imageView, event)) {
                    isDeleteVisible = false;
                    notifyDataSetChanged(); // Notify adapter of data set change
                    return true;
                }
                return false;
            }
        });

        if (isDeleteVisible) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onDeleteClickListener != null) {
                        onDeleteClickListener.onDeleteClick(position);
                    }
                }
            });
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    // Method to update bitmap images
    public void setImageBitmaps(List<Bitmap> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    // Method to update image names
    public void setImageNames(List<String> imageNames) {
        this.imageNames = imageNames;
        notifyDataSetChanged();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        ImageView deleteButton;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.imageName);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    // Helper method to determine if a view is clicked
    private boolean isViewClicked(View view, MotionEvent event) {
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int viewX = viewLocation[0];
        int viewY = viewLocation[1];
        int touchX = (int) event.getRawX();
        int touchY = (int) event.getRawY();
        return touchX >= viewX && touchX <= viewX + view.getWidth() &&
                touchY >= viewY && touchY <= viewY + view.getHeight();
    }
}
