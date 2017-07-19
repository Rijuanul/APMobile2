package id.advancepro.com.advancepro.adapter;

import android.content.Context;
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
import id.advancepro.com.advancepro.model.OutboundHeaderDetailsEntity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by TASIV on 4/19/2017.
 */

public class OutboundHeaderDetailsAdapter extends ArrayAdapter{

    private List<OutboundHeaderDetailsEntity> resultList;
    private int resource;
    private LayoutInflater inflater;
    String[] monthList = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private BoundSettingsEntity boundSettingsEntityRef;
    private String dateoutbound="";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Integer monthOfDate;
    private Integer dateOfMonth;
    private String monthName;
    private String suffix;
    private Context finalcontext;
    private String statusName;
    public OutboundHeaderDetailsAdapter(Context context, int resource, List<OutboundHeaderDetailsEntity> result, BoundSettingsEntity boundSettingsEntity) {
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
                convertView=inflater.inflate(R.layout.outboundparentlayout,null);
            }

            TextView outboundPO=(TextView)convertView.findViewById(R.id.outboundpotxt);
            TextView outboundQty=(TextView)convertView.findViewById(R.id.outboundqtytxt);
            TextView dateOutboundPo=(TextView)convertView.findViewById(R.id.outbounddatetxt);
            TextView companyName=(TextView)convertView.findViewById(R.id.outboundcompanytxt);
            TextView vendorProdNameAndQtyText=(TextView)convertView.findViewById(R.id.outboundvendornameandqtytxt);
            TextView outboundOrderNo=(TextView)convertView.findViewById(R.id.outboundorderno);
            TextView outboundStatusID=(TextView)convertView.findViewById(R.id.outboundstatusid);


            outboundOrderNo.setText(resultList.get(position).getOrderNo());
            outboundPO.setText("PO#"+ resultList.get(position).getPoNo());


            if(Integer.parseInt(boundSettingsEntityRef.getIsShowOrderDate())==1){
                dateoutbound=resultList.get(position).getOrderDate();
            }else if(Integer.parseInt(boundSettingsEntityRef.getIsShowExpectedDate())==1){
                dateoutbound=resultList.get(position).getExpectedDate();
            }

            if(!StringUtils.isEmpty(dateoutbound)){
                Date date = dateFormat.parse(dateoutbound);
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
                dateOutboundPo.setText(Html.fromHtml(monthName +" "+ dateOfMonth +"<sup>"+suffix+"</sup>"));
            }else{
                dateOutboundPo.setText(dateoutbound);
            }

            if(Integer.parseInt(resultList.get(position).getStatusID())== 55){
                outboundStatusID.setText("55");
                statusName="Pick";

            }else if(Integer.parseInt(resultList.get(position).getStatusID())== 56){
                outboundStatusID.setText("56");
                statusName="Pack";
            }

            if(Integer.parseInt(boundSettingsEntityRef.getIsShowOrderQty())==1) {
                outboundQty.setText(resultList.get(position).getQuantity() +"   To   "+ statusName) ;
            }else{
                outboundQty.setText("");
            }

            if(Integer.parseInt(boundSettingsEntityRef.getIsShowName())==1) {
                companyName.setText(resultList.get(position).getCompanyname());
            }else{
                companyName.setText("");
            }

            vendorProdNameAndQtyText.setText(Html.fromHtml(resultList.get(position).getVendorProductNameAndQuantity()));


        }catch (Exception ex){
            ex.printStackTrace();
        }

        return convertView;
    }
}
