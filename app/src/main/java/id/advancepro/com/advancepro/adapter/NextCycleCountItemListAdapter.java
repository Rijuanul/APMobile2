package id.advancepro.com.advancepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.advancepro.com.advancepro.R;
import id.advancepro.com.advancepro.model.NextCycleCountItemEntity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by TASIV on 5/5/2017.
 */

public class NextCycleCountItemListAdapter extends ArrayAdapter{

    private List<NextCycleCountItemEntity> resultList;
    private int resource;
    private LayoutInflater inflater;
    private Context finalcontext;
    public NextCycleCountItemListAdapter(Context context, int resource, List<NextCycleCountItemEntity> result) {
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
                convertView=inflater.inflate(R.layout.nextcyclecounnexttitemlist,null);
            }

            TextView nextCycleCountItemPrdIdHide=(TextView)convertView.findViewById(R.id.newcyclecountitemprodidhide);
            TextView nextCycleCountItemCombIdHide=(TextView)convertView.findViewById(R.id.newcyclecountitemcombidhide);
            TextView nextCycleCountItemSkuHide=(TextView)convertView.findViewById(R.id.newcyclecountitemskuhide);
            TextView nextCycleCountItemPrdName=(TextView)convertView.findViewById(R.id.newcyclecountitemname);
            TextView nextCycleCountItemSkuName=(TextView)convertView.findViewById(R.id.newcyclecountitemsku);
            TextView nextCycleCountItemSelected=(TextView)convertView.findViewById(R.id.newcyclecountitemselected);
            LinearLayout nextCycleCountItemLayout=(LinearLayout)convertView.findViewById(R.id.nextcyclecountitemlayout);


            if(resultList.get(position).getIsItemSelected()==0){
                nextCycleCountItemLayout.setBackgroundColor(Color.parseColor("#77a1cc"));
            }else{
                nextCycleCountItemLayout.setBackgroundColor(Color.parseColor("#237804"));
            }

            nextCycleCountItemPrdIdHide.setText(resultList.get(position).getProductID());
            nextCycleCountItemCombIdHide.setText(resultList.get(position).getComboid());
            nextCycleCountItemSkuHide.setText(resultList.get(position).getSku());
            nextCycleCountItemPrdName.setText("Product: "+resultList.get(position).getProductname());
            nextCycleCountItemSkuName.setText("SKU: "+resultList.get(position).getSku());
            nextCycleCountItemSelected.setText(resultList.get(position).getIsItemSelected().toString());


        }catch (Exception ex){
            ex.printStackTrace();
        }
        return convertView;
    }
}
