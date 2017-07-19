package id.advancepro.com.advancepro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.advancepro.com.advancepro.R;
import id.advancepro.com.advancepro.model.LookupResultEntity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by TASIV on 4/3/2017.
 */

public class LookupListAdapter extends ArrayAdapter{

    private List<LookupResultEntity> resultList;
    private int resource;
    private LayoutInflater inflater;
    private Context finalcontext;
    public LookupListAdapter(Context context, int resource, List<LookupResultEntity> result) {
        super(context, resource,result);
        resultList=result;
        this.resource=resource;
        finalcontext=context;
        inflater=(LayoutInflater) finalcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        try {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.lookuplistlayout,null);
            }

            TextView lookupSkuTxt=(TextView)convertView.findViewById(R.id.lookupsku);
            TextView lookupUpcTxt=(TextView)convertView.findViewById(R.id.lookupupc);
            TextView lookupUnitTxt=(TextView)convertView.findViewById(R.id.lookupunit);
            TextView lookupPltxt=(TextView)convertView.findViewById(R.id.lookuppl);
            TextView lookupWhtxt=(TextView)convertView.findViewById(R.id.lookupwh);
            TextView lookupPlQtyTxt=(TextView)convertView.findViewById(R.id.lookupqtypl);
            TextView lookupAvlPlTxt=(TextView)convertView.findViewById(R.id.lookupqtyavlpl);



            lookupSkuTxt.setText(resultList.get(position).getSku());
            lookupUpcTxt.setText("UPC : "+resultList.get(position).getUpc());
            lookupUnitTxt.setText("Unit : "+resultList.get(position).getUnit());
            lookupPltxt.setText("PL : "+resultList.get(position).getPicklocname());
            lookupWhtxt.setText("WH : "+resultList.get(position).getWarehousename());
            lookupPlQtyTxt.setText("PL Qty : "+resultList.get(position).getStock());
            lookupAvlPlTxt.setText("PL Avl Qty : "+resultList.get(position).getAvailable());


        }catch (Exception ex){
            ex.printStackTrace();
        }

        return convertView;
    }
}
