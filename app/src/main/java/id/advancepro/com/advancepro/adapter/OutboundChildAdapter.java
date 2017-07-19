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
import id.advancepro.com.advancepro.model.OutboundPOChildDetilsEntity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by TASIV on 4/3/2017.
 */

public class OutboundChildAdapter extends ArrayAdapter{

    private List<OutboundPOChildDetilsEntity> resultList;
    private int resource;
    private LayoutInflater inflater;
    private Context finalcontext;
    public OutboundChildAdapter(Context context, int resource, List<OutboundPOChildDetilsEntity> result) {
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
                convertView=inflater.inflate(R.layout.outboundchildlayout,null);
            }

            TextView outboundChildQty=(TextView)convertView.findViewById(R.id.outboundchildqty);
            TextView outboundChildPickLocation=(TextView)convertView.findViewById(R.id.outboundchildproductlocation);
            TextView outboundChildSku=(TextView)convertView.findViewById(R.id.outboundchildsku);
            TextView outboundChildUpc=(TextView)convertView.findViewById(R.id.outboundchildupc);
            TextView outboundChildProductName=(TextView)convertView.findViewById(R.id.outboundchildproductname);
            TextView outboundChildStatus=(TextView)convertView.findViewById(R.id.outboundchildproductstatus);
            TextView outboundChildPosition=(TextView)convertView.findViewById(R.id.outboundchildposition);
            TextView outboundChildOrderDetailID=(TextView)convertView.findViewById(R.id.hiddenorderdetailid);
            LinearLayout leftLayout=(LinearLayout)convertView.findViewById(R.id.childleftlayout);
            LinearLayout rightLayout=(LinearLayout)convertView.findViewById(R.id.childrightlayout);


            if(Integer.parseInt(resultList.get(position).getQuantityRecd()) == Integer.parseInt(resultList.get(position).getQuantity()) ){
                leftLayout.setBackgroundColor(Color.parseColor("#00FF7F"));
                rightLayout.setBackgroundColor(Color.parseColor("#00FF7F"));
                outboundChildStatus.setText("1");
            }else{
                leftLayout.setBackgroundColor(Color.parseColor("#77a1cc"));
                rightLayout.setBackgroundColor(Color.parseColor("#bbc9f1"));
                outboundChildStatus.setText("0");
            }

            if(resultList.get(position).getQtyInMiddle()){//Change layout Color in between Item Pick/Pack
                leftLayout.setBackgroundColor(Color.parseColor("#FF4500"));
                rightLayout.setBackgroundColor(Color.parseColor("#FFA500"));
            }
            outboundChildQty.setText(resultList.get(position).getQuantityRecd() +"/"+ resultList.get(position).getQuantity());
            outboundChildPickLocation.setText(resultList.get(position).getLocationName());
            outboundChildSku.setText("SKU: "+ resultList.get(position).getProductSKU());
            outboundChildUpc.setText("UPC: "+ resultList.get(position).getProductUPC());
            outboundChildProductName.setText(resultList.get(position).getProductName());
            Integer pos=position;
            outboundChildPosition.setText(pos.toString());
            outboundChildOrderDetailID.setText(resultList.get(position).getOrderDetailId());


        }catch (Exception ex){
            ex.printStackTrace();
        }

        return convertView;
    }
}
