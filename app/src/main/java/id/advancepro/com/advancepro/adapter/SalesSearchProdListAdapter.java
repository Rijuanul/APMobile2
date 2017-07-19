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
import id.advancepro.com.advancepro.model.SalesSearchProductEntity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by TASIV on 4/3/2017.
 */

public class SalesSearchProdListAdapter extends ArrayAdapter{

    private List<SalesSearchProductEntity> resultList;
    private int resource;
    private LayoutInflater inflater;
    private Context finalcontext;
    public SalesSearchProdListAdapter(Context context, int resource, List<SalesSearchProductEntity> result) {
        super(context, resource,result);
        resultList=result;
        this.resource=resource;
        finalcontext=context;
        inflater=(LayoutInflater) finalcontext.getSystemService(LAYOUT_INFLATER_SERVICE);
     /*   DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);*/ // Do it on Application start
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        try {
            if(convertView==null){
                convertView=inflater.inflate(R.layout.salessearchproductlistlayout,null);
            }

            TextView salesSearchProdName=(TextView)convertView.findViewById(R.id.salessearchprodname);
            TextView salesSearchProdSku=(TextView)convertView.findViewById(R.id.salessearchprodsku);
            TextView salesSearchProdUnit=(TextView)convertView.findViewById(R.id.salessearchprodunit);
            TextView salesSearchProdPrice=(TextView)convertView.findViewById(R.id.salessearchprodprice);
            TextView salesSearchProdQtyAvl=(TextView)convertView.findViewById(R.id.salessearchprodqtyavl);
            TextView salesSearchProdIdhide=(TextView)convertView.findViewById(R.id.salessearchprodid);
            TextView salesSearchProdCombIdhide=(TextView)convertView.findViewById(R.id.salessearchprodcombid);
            TextView salesSearchProdIsVarianthide=(TextView)convertView.findViewById(R.id.salessearchprodisvariant);
            //ImageView productImage=(ImageView) convertView.findViewById(R.id.salessearchprodimage);
            LinearLayout leftLayout=(LinearLayout)convertView.findViewById(R.id.childLeftLayout);
            LinearLayout rightLayout=(LinearLayout)convertView.findViewById(R.id.childRightLayout);

            salesSearchProdName.setText(resultList.get(position).getPRODUCT_NAME());
            salesSearchProdSku.setText("SKU :"+resultList.get(position).getSKU());
            salesSearchProdUnit.setText("Unit: "+ resultList.get(position).getUnitName());
            salesSearchProdPrice.setText("Price: "+ resultList.get(position).getPRICE());
            salesSearchProdQtyAvl.setText("ATS QTY: "+resultList.get(position).getQTY_AVAIL().toString());
            salesSearchProdIdhide.setText(resultList.get(position).getPRODUCT_ID().toString());
            salesSearchProdCombIdhide.setText(resultList.get(position).getCOMB_ID().toString());
            salesSearchProdIsVarianthide.setText(resultList.get(position).getIS_VARIANT().toString());
            //ImageLoader.getInstance().displayImage(resultList.get(position).getImagePath(), productImage);


           /* byte [] decodeByte= Base64.decode(resultList.get(position).getImagePath(),Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
            productImage.setImageBitmap(bitmap);*/

            leftLayout.setBackgroundColor(Color.parseColor("#77a1cc"));
            rightLayout.setBackgroundColor(Color.parseColor("#bbc9f1"));

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return convertView;
    }
}
