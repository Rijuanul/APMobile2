package id.advancepro.com.advancepro.adapter;

/**
 * Created by TASIV on 7/13/2017.
 */


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

import id.advancepro.com.advancepro.R;
import id.advancepro.com.advancepro.model.SalesVariantItemEntity;

public class SalesVariantExpandableListViewAdapter extends BaseExpandableListAdapter {

        private Context _context;
        // child data in format of header title, child title
        private List<SalesVariantItemEntity> salesVariantItemEntityList;

        public SalesVariantExpandableListViewAdapter(Context context, List<SalesVariantItemEntity> salesVariantItemEntities) {
            this._context = context;
            this.salesVariantItemEntityList = salesVariantItemEntities;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this.salesVariantItemEntityList.get(groupPosition).getSalesVariantChildItem().get(childPosititon).getDescription();
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.salesvariantchilditem, null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.vname);
            txtListChild.setText(childText);

            TextView txtIsselect = (TextView) convertView.findViewById(R.id.vnameselected);
            if(this.salesVariantItemEntityList.get(groupPosition).getSalesVariantChildItem().get(childPosition).getSelect()!=null
                    && this.salesVariantItemEntityList.get(groupPosition).getSalesVariantChildItem().get(childPosition).getSelect()){
                txtIsselect.setText("X");
            }else{
                txtIsselect.setText("");
            }
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return  this.salesVariantItemEntityList.get(groupPosition).getSalesVariantChildItem().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return  this.salesVariantItemEntityList.get(groupPosition).getVariantheader();
        }

        @Override
        public int getGroupCount() {
            return  this.salesVariantItemEntityList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.salesvariantheaderitem, null);
            }
            String childName="";
            for(int i=0;i<this.salesVariantItemEntityList.get(groupPosition).getSalesVariantChildItem().size();i++){

                if(this.salesVariantItemEntityList.get(groupPosition).getSalesVariantChildItem().get(i).getSelect()!=null
                        && this.salesVariantItemEntityList.get(groupPosition).getSalesVariantChildItem().get(i).getSelect()){
                    childName=this.salesVariantItemEntityList.get(groupPosition).getSalesVariantChildItem().get(i).getDescription();
                }
            }


            TextView lblListHeader = (TextView) convertView.findViewById(R.id.vhname);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle+" "+childName);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

