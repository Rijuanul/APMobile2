package id.advancepro.com.advancepro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.advancepro.com.advancepro.R;
import id.advancepro.com.advancepro.model.BoundSettingsEntity;
import id.advancepro.com.advancepro.model.InboundHeaderDetailsEntity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by TASIV on 3/29/2017.
 */

public class InboundHeaderDetailsAdapter extends ArrayAdapter{
        private List<InboundHeaderDetailsEntity> resultList;
        private int resource;
        private LayoutInflater inflater;
        String[] monthList = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        private BoundSettingsEntity boundSettingsEntityRef;
        private String inboundDate="";
        private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        private Integer monthOfDate;
        private Integer dateOfMonth;
        private String monthName;
        private String suffix;
        private Context finalcontext;
        public InboundHeaderDetailsAdapter(Context context, int resource, List<InboundHeaderDetailsEntity> result, BoundSettingsEntity boundSettingsEntity) {
            super(context, resource,result);
            resultList=result;
            this.resource=resource;
            finalcontext=context;
            boundSettingsEntityRef = boundSettingsEntity;
            inflater=(LayoutInflater) finalcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            try {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.inboundparentlayout,null);
            }

            TextView inboundPO=(TextView)convertView.findViewById(R.id.inboundPOtxt);
            TextView inboundQty=(TextView)convertView.findViewById(R.id.inboundQtytxt);
            TextView dateInbound=(TextView)convertView.findViewById(R.id.inboundDatetxt);
            TextView companyName=(TextView)convertView.findViewById(R.id.inboundCompanytxt);
            TextView statusImage=(TextView)convertView.findViewById(R.id.inboundStatusImage);
            TextView vendorProdNameAndQtyText=(TextView)convertView.findViewById(R.id.vendorProdNameAndQtytxt);
            TextView inboundOrderNo=(TextView)convertView.findViewById(R.id.inboundOrderNo);

            //ListView subItemList=(ListView)convertView.findViewById(R.id.inboundHeaderSubitemList);

            inboundOrderNo.setText(resultList.get(position).getOrderNo());
            inboundPO.setText("PO#"+ resultList.get(position).getPurchaseorderno());
            //System.out.println("PO#"+ resultList.get(position).getPurchaseorderno());
            if(Integer.parseInt(boundSettingsEntityRef.getIsShowOrderQty())==1) {
                inboundQty.setText(resultList.get(position).getQuantityreceived() + "/" + resultList.get(position).getQuantity());
            }else{
                inboundQty.setText("");
            }

           // System.out.println("Inbound Ref:" + boundSettingsEntityRef.getIsShowProdName() +" "+ boundSettingsEntityRef.getIsShowOrderDate() +" "+ boundSettingsEntityRef.getIsShowExpectedDate());
            if(Integer.parseInt(boundSettingsEntityRef.getIsShowOrderDate())==1){
                inboundDate=resultList.get(position).getOrderdate();
            }else if(Integer.parseInt(boundSettingsEntityRef.getIsShowExpectedDate())==1){
                inboundDate=resultList.get(position).getExpecteddate();
            }

            if(!StringUtils.isEmpty(inboundDate)){
                Date date = dateFormat.parse(inboundDate);
                dateOfMonth=date.getDate();
                monthOfDate=date.getMonth();
                monthName=monthList[monthOfDate];
                if (dateOfMonth == 1 || dateOfMonth == 21 || dateOfMonth == 31) {
                    suffix = "st";
                } else if (dateOfMonth == 2 || dateOfMonth == 22) {
                    suffix = "nd";
                } else if (dateOfMonth == 3 || dateOfMonth == 23) {
                    suffix = "rd";
                } else {
                    suffix = "th";
                }
                dateInbound.setText(Html.fromHtml(monthName +" "+ dateOfMonth +"<sup>"+suffix+"</sup>"));
            }else{
                dateInbound.setText(inboundDate);
            }

            if(Integer.parseInt(resultList.get(position).getStatus())== 0){
                statusImage.setBackgroundColor(Color.parseColor("#43729A"));

            }else if(Integer.parseInt(resultList.get(position).getStatus())== 1){
                statusImage.setBackgroundColor(Color.parseColor("#FFA500"));

            }else if(Integer.parseInt(resultList.get(position).getStatus())== 2){
                statusImage.setBackgroundColor(Color.RED);

            }

            if(Integer.parseInt(boundSettingsEntityRef.getIsShowName())==1) {
                companyName.setText(resultList.get(position).getCompanyname());
            }else{
                companyName.setText("");
            }

            vendorProdNameAndQtyText.setText(Html.fromHtml(resultList.get(position).getVendorProductNameAndQty()));
            //ArrayAdapter<String> subItemAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item,resultList.get(position).getVendorProductNameAndQty());
            //subItemList.setAdapter(subItemAdapter);

            }catch (Exception ex){
                ex.printStackTrace();
            }

            return convertView;
        }
}
