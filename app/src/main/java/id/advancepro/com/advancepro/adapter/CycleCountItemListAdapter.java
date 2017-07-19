package id.advancepro.com.advancepro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.advancepro.com.advancepro.R;
import id.advancepro.com.advancepro.model.CycleCountItemEntity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by TASIV on 5/5/2017.
 */

public class CycleCountItemListAdapter extends ArrayAdapter{

    private List<CycleCountItemEntity> resultList;
    private int resource;
    private LayoutInflater inflater;
    private Context finalcontext;
    public CycleCountItemListAdapter(Context context, int resource, List<CycleCountItemEntity> result) {
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
                convertView=inflater.inflate(R.layout.cyclecountitemlist,null);
            }

            TextView cycleCountDetailIDHide=(TextView)convertView.findViewById(R.id.cyclecountdetailidhide);
            TextView cycleCountIDHide=(TextView)convertView.findViewById(R.id.cyclecountidhide);
            TextView cycleCountProdIDHide=(TextView)convertView.findViewById(R.id.cyclecountprodidhide);
            TextView cycleCountItemName=(TextView)convertView.findViewById(R.id.cyclecountitemname);
            TextView cycleCountSkuName=(TextView)convertView.findViewById(R.id.cyclecountitemsku);
            TextView cycleCountPLQty=(TextView)convertView.findViewById(R.id.cyclecountplqty);
            TextView cycleCountNewQty=(TextView)convertView.findViewById(R.id.cyclecountnewqty);
            TextView cycleCountScannedQty=(TextView)convertView.findViewById(R.id.cyclecountscanqty);
            TextView cycleCountCmnts=(TextView)convertView.findViewById(R.id.cyclecountitemcmnts);

            cycleCountDetailIDHide.setText(resultList.get(position).getCycleCountdetaild());
            cycleCountIDHide.setText(resultList.get(position).getCycleCountId());
            cycleCountProdIDHide.setText(resultList.get(position).getProductid());
            cycleCountItemName.setText("Product: "+resultList.get(position).getProductName());
            cycleCountSkuName.setText("SKU: "+resultList.get(position).getProductSku());
            cycleCountPLQty.setText("PL QTY: "+resultList.get(position).getProductQuantity());
            cycleCountNewQty.setText("New QTY: "+resultList.get(position).getScannedqty());
            cycleCountScannedQty.setText(resultList.get(position).getScannedqty());
            cycleCountCmnts.setText(resultList.get(position).getComments());

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return convertView;
    }
}
