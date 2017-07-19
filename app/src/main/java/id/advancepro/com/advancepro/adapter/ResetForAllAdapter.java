package id.advancepro.com.advancepro.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import id.advancepro.com.advancepro.service.ResetForAllFunctionService;
import id.advancepro.com.advancepro.utils.Constant;

/**
 * Created by TASIV on 4/24/2017.
 */

public class ResetForAllAdapter {
    private Context context;
    public ResetForAllAdapter(String makeUrl,Context cntx){
        context=cntx;
        new ResetForAllFunction().execute(makeUrl);

    }

    public class ResetForAllFunction extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            ResetForAllFunctionService resetForAllFunctionService = new ResetForAllFunctionService();
            return resetForAllFunctionService.setReset(params[0]);
        }

        @Override
        protected void onPostExecute(String str) {
            //System.out.println("Reset for All function "+ str);
            if(!(str.equals(Constant.RESET_SUCCESS))){
                Toast.makeText(context, Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
