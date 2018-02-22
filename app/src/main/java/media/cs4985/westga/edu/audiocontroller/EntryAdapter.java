package media.cs4985.westga.edu.audiocontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.List;

/**
 * Created by Dallas Wyant on 2/21/2018.
 */

public class EntryAdapter extends ArrayAdapter<Entry> {

    private Context context;
    private int resource;
    private List<Entry> list;

    public EntryAdapter(Context context, int resource, List<Entry> list) {
        super(context, resource, list);
        this.context = context;
        this.resource = resource;
        this.list = list;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(this.context);
        View entryView = inflater.inflate(R.layout.entry_view, parent, false);
        Entry entry = this.list.get(position);

        TextView text = entryView.findViewById(R.id.textviewname);
        text.setText(entry.getName());


        return entryView;
    }

}
