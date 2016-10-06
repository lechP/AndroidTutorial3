package com.lpi.andt3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;

public class BookJSONAdapter extends BaseAdapter {

    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    private Context context;
    private LayoutInflater inflater;
    private JSONArray jsonArray;

    public BookJSONAdapter(Context context, LayoutInflater inflater) {
        this.context = context;
        this.inflater = inflater;
        jsonArray = new JSONArray();
    }

    public void updateData(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return jsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_book, null);
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.text_title);
            holder.authorTextView = (TextView) convertView.findViewById(R.id.text_author);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        handleDisplayedValues(position, holder);

        return convertView;
    }

    private void handleDisplayedValues(int position, ViewHolder holder) {
        JSONObject jsonObject = (JSONObject) getItem(position);
        if (jsonObject.has("cover_i")) {
            String imageID = jsonObject.optString("cover_i");
            String imageURL = IMAGE_URL_BASE + imageID + "-S.jpg";
            Picasso.with(context).load(imageURL).placeholder(R.drawable.ic_books).into(holder.thumbnailImageView);
        } else {
            holder.thumbnailImageView.setImageResource(R.drawable.ic_books);
        }

        String bookTitle = "";
        String authorName = "";
        if (jsonObject.has("title")) {
            bookTitle = jsonObject.optString("title");
        }
        if (jsonObject.has("author_name")) {
            authorName = jsonObject.optJSONArray("author_name").optString(0);
        }
        holder.titleTextView.setText(bookTitle);
        holder.authorTextView.setText(authorName);
    }

    private static class ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView;
        TextView authorTextView;
    }

}
