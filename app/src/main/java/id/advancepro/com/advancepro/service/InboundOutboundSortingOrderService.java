package id.advancepro.com.advancepro.service;

import java.util.ArrayList;
import java.util.List;

import id.advancepro.com.advancepro.model.OutboundInboundSortingOrderEntity;


/**
 * Created by TASIV on 5/1/2017.
 */

public class InboundOutboundSortingOrderService {

    private List<OutboundInboundSortingOrderEntity> outboundInboundSortingOrderEntityList;
    private OutboundInboundSortingOrderEntity outboundInboundSortingOrderEntity;
    private List<String> sortingOrderNameList;


    public List<OutboundInboundSortingOrderEntity> getOutboundSortingOrder(){
        getOutboundInboundSortingList();
        setOutboundInboundSortingOrderValue("Outbound");
        return outboundInboundSortingOrderEntityList;
    }

    public List<OutboundInboundSortingOrderEntity> getInboundSortingOrder(){
        getOutboundInboundSortingList();
        setOutboundInboundSortingOrderValue("Inbound");
        return outboundInboundSortingOrderEntityList;
    }

    public void getOutboundInboundSortingList(){
         /* <option value="0">Select</option><option value="orderdate">Order Date</option><option value="orderno">Order Number</option><option value="vendorname">Vendor Name</option><option value="expecteddate">Expected Date</option>
    <option value="0">Select</option><option value="orderdate">Order Date</option><option value="orderno">Order Number</option><option value="customername">Customer Name</option><option value="expecteddate">Expected Date</option>*/

        sortingOrderNameList=new ArrayList<>();
        sortingOrderNameList.add("Order Date");
        sortingOrderNameList.add("Order Number");
        sortingOrderNameList.add("Vendor Name");
        sortingOrderNameList.add("Customer Name");
        sortingOrderNameList.add("Expected Date");

    }

    public void setOutboundInboundSortingOrderValue(String type){
        outboundInboundSortingOrderEntityList=new ArrayList<>();
        for(int i=0;i< sortingOrderNameList.size();i++){
            outboundInboundSortingOrderEntity=new OutboundInboundSortingOrderEntity();
            if(sortingOrderNameList.get(i).equals("Order Date")){
                outboundInboundSortingOrderEntity.setSortingOrderName(sortingOrderNameList.get(i));
                outboundInboundSortingOrderEntity.setSortingOrderValue("orderdate");
            }
            else if(sortingOrderNameList.get(i).equals("Order Number")){
                outboundInboundSortingOrderEntity.setSortingOrderName(sortingOrderNameList.get(i));
                outboundInboundSortingOrderEntity.setSortingOrderValue("orderno");
            }
            else if(sortingOrderNameList.get(i).equals("Vendor Name") && type.equals("Inbound")){
                outboundInboundSortingOrderEntity.setSortingOrderName(sortingOrderNameList.get(i));
                outboundInboundSortingOrderEntity.setSortingOrderValue("vendorname");
            }
            else if(sortingOrderNameList.get(i).equals("Customer Name") && type.equals("Outbound")){
                outboundInboundSortingOrderEntity.setSortingOrderName(sortingOrderNameList.get(i));
                outboundInboundSortingOrderEntity.setSortingOrderValue("customername");
            }
            else if(sortingOrderNameList.get(i).equals("Expected Date")){
                outboundInboundSortingOrderEntity.setSortingOrderName(sortingOrderNameList.get(i));
                outboundInboundSortingOrderEntity.setSortingOrderValue("expecteddate");
            }

            if(outboundInboundSortingOrderEntity.getSortingOrderName()!=null) {
                outboundInboundSortingOrderEntityList.add(outboundInboundSortingOrderEntity);
            }
        }
    }
}
