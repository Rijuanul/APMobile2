package id.advancepro.com.advancepro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.advancepro.com.advancepro.R;
import id.advancepro.com.advancepro.model.InProgressCycleCountEntity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by TASIV on 5/5/2017.
 */

public class CycleCountProgressAdapter extends ArrayAdapter{

    private List<InProgressCycleCountEntity> resultList;
    private int resource;
    private LayoutInflater inflater;
    private Context finalcontext;
    public CycleCountProgressAdapter(Context context, int resource, List<InProgressCycleCountEntity> result) {
        super(context,resource,result);
        resultList=result;
        this.resource=resource;
        finalcontext=context;
        inflater=(LayoutInflater) finalcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        try {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.cyclecountinprogresslist,null);
            }

            TextView cycleCountProgressIDHide=(TextView)convertView.findViewById(R.id.cyclecountprogressidhide);
            TextView cycleCountProgressStstusIDHide=(TextView)convertView.findViewById(R.id.cyclecountprogressstatusidhide);
            TextView cycleCountProgressNameHide=(TextView)convertView.findViewById(R.id.cyclecountprogressnamehide);
            TextView cycleCountProgressPL=(TextView)convertView.findViewById(R.id.cyclecountprogresspl);
            TextView cycleCountProgressWarehouse=(TextView)convertView.findViewById(R.id.cyclecountprogresswarehouse);
            TextView cycleCountProgressName=(TextView)convertView.findViewById(R.id.cyclecountprogressname);
            TextView cycleCountProgressStatusName=(TextView)convertView.findViewById(R.id.cyclecountprogressstatusname);


            cycleCountProgressIDHide.setText(resultList.get(position).getID());
            cycleCountProgressStstusIDHide.setText(resultList.get(position).getStatusid());
            cycleCountProgressNameHide.setText(resultList.get(position).getName());
            cycleCountProgressPL.setText("PL: "+resultList.get(position).getPickLocname());
            cycleCountProgressWarehouse.setText("WH: "+resultList.get(position).getWarehousename());
            cycleCountProgressName.setText("Name: "+resultList.get(position).getName());
            cycleCountProgressStatusName.setText("Status: "+resultList.get(position).getStatusname());


        }catch (Exception ex){
            ex.printStackTrace();
        }

        return convertView;
    }
}
