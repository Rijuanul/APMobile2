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
import id.advancepro.com.advancepro.model.InboundChildEntity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by TASIV on 4/3/2017.
 */

public class InboundChildAdapter extends ArrayAdapter{

    private List<InboundChildEntity> resultList;
    private int resource;
    private LayoutInflater inflater;
    private Context finalcontext;
    public InboundChildAdapter(Context context, int resource, List<InboundChildEntity> result) {
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
                convertView=inflater.inflate(R.layout.inboundchildlayout,null);
            }

            TextView inboundChildQty=(TextView)convertView.findViewById(R.id.inboundChildQty);
            TextView inboundChildPickLocation=(TextView)convertView.findViewById(R.id.inboundChildProductLocation);
            TextView inboundChildSku=(TextView)convertView.findViewById(R.id.inboundChildSku);
            TextView inboundChildUpc=(TextView)convertView.findViewById(R.id.inboundChildUpc);
            TextView inboundChildProductName=(TextView)convertView.findViewById(R.id.inboundChildProductName);
            TextView inboundChildStatus=(TextView)convertView.findViewById(R.id.inboundChildProductStatus);
            TextView inboundChildPosition=(TextView)convertView.findViewById(R.id.inboundChildPositionn);
            TextView inboundChildOrderDetailID=(TextView)convertView.findViewById(R.id.hiddenOrderDetId);
            LinearLayout leftLayout=(LinearLayout)convertView.findViewById(R.id.childLeftLayout);
            LinearLayout rightLayout=(LinearLayout)convertView.findViewById(R.id.childRightLayout);


            if(Integer.parseInt(resultList.get(position).getQuantityReceived()) == Integer.parseInt(resultList.get(position).getTotQuantity()) ){
                leftLayout.setBackgroundColor(Color.parseColor("#00FF7F"));
                rightLayout.setBackgroundColor(Color.parseColor("#00FF7F"));
                inboundChildStatus.setText("1");
            }else{
                leftLayout.setBackgroundColor(Color.parseColor("#77a1cc"));
                rightLayout.setBackgroundColor(Color.parseColor("#bbc9f1"));
                inboundChildStatus.setText("0");
            }

            if(resultList.get(position).getQtyRecv()){//Change layout Color in between Item Receiving
                leftLayout.setBackgroundColor(Color.parseColor("#FF4500"));
                rightLayout.setBackgroundColor(Color.parseColor("#FFA500"));
            }
            inboundChildQty.setText(resultList.get(position).getQuantityReceived() +"/"+ resultList.get(position).getTotQuantity());
            inboundChildPickLocation.setText(resultList.get(position).getWareHouseName());
            inboundChildSku.setText("SKU: "+ resultList.get(position).getProductSKU());
            inboundChildUpc.setText("UPC: "+ resultList.get(position).getUPC());
            inboundChildProductName.setText(resultList.get(position).getProductName());
            Integer pos=position;
            inboundChildPosition.setText(pos.toString());
            inboundChildOrderDetailID.setText(resultList.get(position).getOrderDetailid());


        }catch (Exception ex){
            ex.printStackTrace();
        }

        return convertView;
    }
}
