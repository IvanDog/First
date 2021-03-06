package com.example.parking;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PrintPreviewActivity extends Activity {
	private static final int PAYMENT_TYPE_CASH=201;
	private static final int PAYMENT_TYPE_ALIPAY=202;
	private static final int PAYMENT_TYPE_WECHATPAY=203;
	private static final int PAYMENT_TYPE_MOBILE=204;
	private static final int EVENT_PRINT_SUCCESS= 301;
	private Button mConfirmPrintBT;;
	private Button mCancelPrintBT;
	private TextView mParkNameTV;
	private TextView mParkNumberTV;
	private TextView mUserNumberTV;
	private TextView mLocationNumberTV;
	private TextView mLicenseNumberTV;
	private TextView mCarTypeTV;
	private TextView mParkTypeTV;
	private TextView mStartTimeTV;
	private TextView mLeaveTimeTV;
	private TextView mExpenseTotalTV;
	private TextView mFeeScaleTV;
	private TextView mChargeStandardTV;
	private TextView mSuperviseTelephoneTV;
	private int mPayType;
	private int mLocationNumber;
	private String mCarType;
	private String mParkType;
	private String mStartTime;
	private String mLeaveTime;
	private String mExpense;
	private String mLicensePlateNumber;
    private static String LOG_TAG = "PrintPreviewActivity";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_print_preview);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mPayType=bundle.getInt("paytype");
		mParkNameTV = (TextView) findViewById(R.id.tv_parking_name_print);
		mParkNameTV.setText(R.string.park_name_fixed);
		mParkNumberTV = (TextView) findViewById(R.id.tv_parking_number_print);
		mParkNumberTV.setText("车场编号:" + this.getString(R.string.park_number_fixed));
		mUserNumberTV = (TextView) findViewById(R.id.tv_user_number_print);
		mUserNumberTV.setText("工号:" + this.getString(R.string.user_number_fixed));
		mLicensePlateNumber=bundle.getString("licenseplate");
		mLocationNumber = bundle.getInt("locationnumber");
		mCarType=bundle.getString("cartype");
		mParkType=bundle.getString("parktype");
		mStartTime=bundle.getString("starttime");
		mLeaveTime=bundle.getString("leavetime");
		mExpense=bundle.getString("expense");
		mUserNumberTV=(TextView)findViewById(R.id.tv_user_number_print);
		mLocationNumberTV=(TextView)findViewById(R.id.tv_location_number_print);
		mLicenseNumberTV=(TextView)findViewById(R.id.tv_license_number_print);
		mCarTypeTV=(TextView)findViewById(R.id.tv_car_type_print);
		mParkTypeTV=(TextView)findViewById(R.id.tv_parking_type_print);
		mStartTimeTV=(TextView)findViewById(R.id.tv_start_time_print);
		mLeaveTimeTV=(TextView)findViewById(R.id.tv_leave_time_print);
		mExpenseTotalTV=(TextView)findViewById(R.id.tv_expense_total_print);
		mFeeScaleTV=(TextView)findViewById(R.id.tv_fee_scale_print);
		mLicenseNumberTV.setText("车牌号:" + mLicensePlateNumber);
		mLocationNumberTV.setText("泊位号:" + mLocationNumber);
    	mCarTypeTV.setText("车辆类型:" + mCarType);
        mParkTypeTV.setText("泊车类型:" + mParkType);
        mStartTimeTV.setText("入场时间:" + mStartTime);
        mLeaveTimeTV.setText("离场时间:" + mLeaveTime);
		//mParkTimeTV=(TextView)findViewById(R.id.tv_parking_time_print);
		mExpenseTotalTV.setText("费用总计:" + mExpense);
		mFeeScaleTV.setText("收费标准:" + this.getString(R.string.fee_scale_fixed));
		mChargeStandardTV=(TextView)findViewById(R.id.tv_charge_standard_print);
		mChargeStandardTV.setText(R.string.charge_standard_fixed);
		mSuperviseTelephoneTV=(TextView)findViewById(R.id.tv_supervise_telephone_print);
		mSuperviseTelephoneTV.setText(R.string.supervise_telephone_fixed);
		mConfirmPrintBT=(Button)findViewById(R.id.bt_confirm_print);
		mConfirmPrintBT.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Message msg = new Message();
                msg.what = EVENT_PRINT_SUCCESS;
                mHandler.sendMessage(msg);
                Intent intentBack = new Intent();
                intentBack.setAction("BackMain");
                sendBroadcast(intentBack);
				Intent intent = new Intent(PrintPreviewActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		mCancelPrintBT=(Button)findViewById(R.id.bt_cancel_print);
		mCancelPrintBT.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				if(mPayType==PAYMENT_TYPE_CASH){
                    Intent intentBack = new Intent();
                    intentBack.setAction("BackMain");
                    sendBroadcast(intentBack);
					Intent intent = new Intent(PrintPreviewActivity.this,MainActivity.class);
					startActivity(intent);
					finish();
				}else if(mPayType==PAYMENT_TYPE_MOBILE){
					Intent intent = new Intent(PrintPreviewActivity.this,MobilePaymentSuccessActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});

        getActionBar().setDisplayHomeAsUpEnabled(true);
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        registerReceiver(mReceiver, filter); 
	}

	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EVENT_PRINT_SUCCESS:
                	int stat = MyApp.ddi.ddi_prnt_esc(new byte[]{0x1B, 0x40}, 2); //初始化
                	
                	Log.e(LOG_TAG,"stat is " + stat ); 
                	StringBuffer sb = new StringBuffer();
                	sb.append(mUserNumberTV.getText()).append("\n").append(mParkNameTV.getText())
                	.append("\n").append(mParkNumberTV.getText()).append(" ").append(mLocationNumberTV.getText())
                	.append("\n").append(mLicenseNumberTV.getText())
                	.append("\n").append(mCarTypeTV.getText()).append(" ").append(mParkTypeTV.getText())
                	.append("\n").append(mStartTimeTV.getText()).append("\n").append(mLeaveTimeTV.getText())
                	.append("\n").append(mExpenseTotalTV.getText()).append("  ")
                	.append(mFeeScaleTV.getText()).append("\n").append(mChargeStandardTV.getText())
                	.append("\n").append(mSuperviseTelephoneTV.getText());
                	
                	print_string(sb.toString());
                	
                	Toast.makeText(getApplicationContext(), "该设备不支持打印功能", Toast.LENGTH_SHORT).show();
                	break;
                default:
                    break;
            }
        }
    };
    
    private void print_string(String text){
        try {
        	Log.e(LOG_TAG,"print_string->try" ); 
            byte [] prntBuf = text.getBytes("GBK");
            MyApp.ddi.ddi_prnt_esc(prntBuf, prntBuf.length);
            int stat = MyApp.ddi.ddi_prnt_esc(new byte[]{0x1B, 0x4A, 18}, 3);
        	Log.e(LOG_TAG,"print_string->try stat is " + stat ); 
        }catch (Exception e)
        {
        	Log.e(LOG_TAG,"print_string->exception" + e); 
            e.printStackTrace();
        }
    }
    
	public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) {  
	         case android.R.id.home:  
	        	 finish();
	             break;    
	        default:  
	             break;  
	    }  
	    return super.onOptionsItemSelected(item);  
	  }  
	
    private BroadcastReceiver mReceiver = new BroadcastReceiver(){  
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction()!=null && intent.getAction().equals("ExitApp")){
				finish();
			}
		}            
    }; 
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
