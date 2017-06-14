package Models.Adapters;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.b3.idscannerb3.R;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import Models.Entities.MRTDRecord;

/**
 * Created by billy on 25-5-2017.
 */

public class CustomScannedListAdapter extends ArrayAdapter<MRTDRecord> {
    public CustomScannedListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CustomScannedListAdapter(Context context, int resource, ArrayList<MRTDRecord> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.scanned_list_row, null);
        }

        MRTDRecord mrtdRecord = getItem(position);

        if (mrtdRecord != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.name);
            //TextView tt2 = (TextView) v.findViewById(R.id.dateOfExpiry);
            TextView tt3 = (TextView) v.findViewById(R.id.dateTimeStamp);
            TextView scanNumberTv = (TextView) v.findViewById(R.id.scanNumber);

            if (tt1 != null) {
                tt1.setText(mrtdRecord.getFirstName() + " " + mrtdRecord.getLastName());
            }

            /*if (tt2 != null) {
                tt2.setText(mrtdRecord.getDateOfExpiry());
            }*/

            if (tt3 != null) {
                Date date = mrtdRecord.getDateTimeStamp();
                DateFormat dtStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                tt3.setText(dtStamp.format(date));
            }

            if (scanNumberTv != null) {
                scanNumberTv.setText(String.valueOf(mrtdRecord.getId()));
            }
        }

        return v;
    }
}
