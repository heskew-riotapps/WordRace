package com.riotapps.wordrace.ui;

//import com.riotapps.wordbase.GameSurface;
import com.riotapps.wordrace.R;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.Logger;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riotapps.wordbase.hooks.Opponent;
import com.riotapps.wordbase.hooks.OpponentService;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.interfaces.ICloseDialog;

public class CreateGameDialog extends AlertDialog{
	
	private static final String TAG = CreateGameDialog.class.getSimpleName();
		private Button bOK;
		private Button bCancel;
		private ImageView close;
	 	private Context context;
		private String dialogTitle;
		private String dialogText;
		private String okText;
		private String cancelText;
		private View layout;
	//	private boolean onCancelClickFinishActivity;
	//	private View.OnClickListener onCancel = null;
	//	private View.OnClickListener onOK = null;
		private int opponentId;
		
		private int onOKReturnCode = 0;
		private int onCancelReturnCode = 0;
		private int onCloseReturnCode = 0;

	 
		public CreateGameDialog(Context ctx, int opponentId) {
			super(ctx);
			
			this.opponentId = opponentId;
			this.onOKReturnCode = 100;
			this.onCancelReturnCode = Constants.RETURN_CODE_CREATE_GAME_DIALOG_CANCEL_CLICKED;
			this.onCloseReturnCode = Constants.RETURN_CODE_CREATE_GAME_DIALOG_CLOSE_CLICKED;
			 this.context = ctx;
		   // this.onCancelClickFinishActivity = onCancelClickFinishActivity;
			//this.dialogTitle = dialogTitle;
			//this.dialogText = dialogText;
			//this.cancelText = cancelText;
		//	this.onCancel = onCancel;
		//	this.onOK = onOK;
			this.okText = "OK";
		//	this.layoutId = layoutId;
		}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
 
		super.onStart();
	 
		LayoutInflater inflater = getLayoutInflater();//(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        this.layout = inflater.inflate(R.layout.creategamedialog, (ViewGroup) findViewById(R.id.root));

		Opponent o = OpponentService.getOpponent(this.opponentId);
        
		TextView title = (TextView) layout.findViewById(R.id.alert_title);  
		ImageView ivOpponent = (ImageView) layout.findViewById(R.id.ivOpponent);  
		title.setTypeface(ApplicationContext.getMainFontTypeface());
		title.setText(this.context.getString(R.string.main_game_start_prompt_title)); //create a game

		ivOpponent.setBackgroundResource(o.getSmallResourceId());
		
		TextView text = (TextView) layout.findViewById(R.id.alert_text);
		text.setTypeface(ApplicationContext.getMainFontTypeface());
		text.setText(String.format(this.context.getString(R.string.main_game_start_prompt), o.getName(), o.getSkillLevelTextLCase(this.context)));

		TextView alert_confirmation = (TextView) layout.findViewById(R.id.alert_confirmation);
		if (alert_confirmation != null){
			alert_confirmation.setTypeface(ApplicationContext.getMainFontTypeface());
		}
		
		RelativeLayout rlDash = (RelativeLayout) layout.findViewById(R.id.rlDash);
		TextView tvDash = (TextView) layout.findViewById(R.id.tvDash);
		tvDash.setTypeface(ApplicationContext.getMainFontTypeface());
		tvDash.setText(context.getString(R.string.create_game_dash_title));
		TextView tvDashSubTitle = (TextView) layout.findViewById(R.id.tvDashSubTitle);
		tvDashSubTitle.setTypeface(ApplicationContext.getMainFontTypeface());
		tvDashSubTitle.setText(context.getString(R.string.create_game_dash_sub_title));
		
		rlDash.setClickable(true);
		
		RelativeLayout rlSpeedRounds = (RelativeLayout) layout.findViewById(R.id.rlSpeedRounds);
		TextView tvSpeedRounds = (TextView) layout.findViewById(R.id.tvSpeedRounds);
		tvSpeedRounds.setTypeface(ApplicationContext.getMainFontTypeface());
		tvSpeedRounds.setText(context.getString(R.string.create_game_speed_rounds_title));
		TextView tvSpeedRoundsSubTitle = (TextView) layout.findViewById(R.id.tvSpeedRoundsSubTitle);
		tvSpeedRoundsSubTitle.setTypeface(ApplicationContext.getMainFontTypeface());
		tvSpeedRoundsSubTitle.setText(context.getString(R.string.create_game_speed_rounds_sub_title));
		TextView tvSpeedRoundsPreview = (TextView) layout.findViewById(R.id.tvSpeedRoundsPreview);
		tvSpeedRoundsPreview.setTypeface(ApplicationContext.getMainFontTypeface());
		if (!StoreService.isSpeedRoundsPurchased(this.context)){
			int remainingSpeedRoundsPreviews = PlayerService.getRemainingFreeUsesSpeedRounds(this.context);
			String speedRoundPrice = StoreService.getCachedInventoryItemPrice(context.getString(R.string.SKU_GOOGLE_PLAY_SPEED_ROUNDS));
			if (remainingSpeedRoundsPreviews > 0) {
				tvSpeedRoundsPreview.setText(String.format(context.getString(R.string.create_game_speed_rounds_previews_remaining),remainingSpeedRoundsPreviews, speedRoundPrice));
			}
			else {
				//tap goes to store
				tvSpeedRoundsPreview.setText(String.format(context.getString(R.string.create_game_speed_rounds_previews_gone), speedRoundPrice));
			}
		}
		else{
			tvSpeedRoundsPreview.setVisibility(View.GONE);
		}

		rlSpeedRounds.setClickable(true);
		
		RelativeLayout rlDoubleTime = (RelativeLayout) layout.findViewById(R.id.rlDoubleTime);
		TextView tvDoubleTime = (TextView) layout.findViewById(R.id.tvDoubleTime);
		tvDoubleTime.setTypeface(ApplicationContext.getMainFontTypeface());
		tvDoubleTime.setText(context.getString(R.string.create_game_double_time_title));
		TextView tvDoubleTimeSubTitle = (TextView) layout.findViewById(R.id.tvDoubleTimeSubTitle);
		tvDoubleTimeSubTitle.setTypeface(ApplicationContext.getMainFontTypeface());
		tvDoubleTimeSubTitle.setText(context.getString(R.string.create_game_double_time_sub_title));
		TextView tvDoubleTimePreview = (TextView) layout.findViewById(R.id.tvDoubleTimePreview);
		tvDoubleTimePreview.setTypeface(ApplicationContext.getMainFontTypeface());
		if (!StoreService.isDoubleTimePurchased(this.context)){
			int remainingDoubleTimePreviews = PlayerService.getRemainingFreeUsesDoubleTime(this.context);
			String doubleTimePrice = StoreService.getCachedInventoryItemPrice(context.getString(R.string.SKU_GOOGLE_PLAY_DOUBLE_TIME));
			if (remainingDoubleTimePreviews > 0) {
				tvDoubleTimePreview.setText(String.format(context.getString(R.string.create_game_double_time_previews_remaining),remainingDoubleTimePreviews, doubleTimePrice));
			}
			else {
				tvDoubleTimePreview.setText(String.format(context.getString(R.string.create_game_double_time_previews_gone), doubleTimePrice));
			}
		}
		else{
			tvDoubleTimePreview.setVisibility(View.GONE);
		}
		rlDoubleTime.setClickable(true);
		
		//this.bOK = (Button) layout.findViewById(R.id.bOK);
	//	this.bOK.setTypeface(ApplicationContext.getMainFontTypeface());
		//this.bOK.setText(okText);
		
	//	this.bOK.setOnClickListener(this.onOK);
		
		//this.bCancel = (Button) layout.findViewById(R.id.bCancel);
		//this.bCancel.setTypeface(ApplicationContext.getMainFontTypeface());
		//this.bCancel.setText(cancelText);
		
		this.close = (ImageView) layout.findViewById(R.id.img_close);
		
		this.setCanceledOnTouchOutside(false);
		
		rlDash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//dismiss();
				Logger.d(TAG, "onClick rlDash");
				((ICloseDialog)context).dialogClose(Constants.RETURN_CODE_CREATE_GAME_DIALOG_ORIGINAL_GAME_CLICKED);
				
				//if(onCancelClickFinishActivity){
	            //	((Activity)context).finish();
	            //}
			}
		});
		rlSpeedRounds.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//dismiss();
				Logger.d(TAG, "onClick rlDash");
				((ICloseDialog)context).dialogClose(Constants.RETURN_CODE_CREATE_GAME_DIALOG_SPEED_ROUND_CLICKED);
				
				//if(onCancelClickFinishActivity){
	            //	((Activity)context).finish();
	            //}
			}
		});
		rlDoubleTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//dismiss();
				Logger.d(TAG, "onClick rlDash");
				((ICloseDialog)context).dialogClose(Constants.RETURN_CODE_CREATE_GAME_DIALOG_DOUBLE_TIME_CLICKED);
				
				//if(onCancelClickFinishActivity){
	            //	((Activity)context).finish();
	            //}
			}
		});
		
	/*	
//		if (this.onCancel == null){
			this.bCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//dismiss();
					Logger.d(TAG, "onClick bCancel");
					((ICloseDialog)context).dialogClose(onCancelReturnCode);
					
					//if(onCancelClickFinishActivity){
		            //	((Activity)context).finish();
		            //}
				}
			});

			this.bOK.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//dismiss();
					Logger.d(TAG, "onClick bOK");
					((ICloseDialog)context).dialogClose(onOKReturnCode);
					
					//if(onCancelClickFinishActivity){
		            //	((Activity)context).finish();
		            //}
				}
			});
			*/
			//if button is clicked, close the custom dialog
			this.close.setOnClickListener(new View.OnClickListener() {
		 		@Override
				public void onClick(View v) {
					//dismiss();
					Logger.d(TAG, "onClick close image");
					
					((ICloseDialog)context).dialogClose(onCloseReturnCode);
				//	((Activity)context).setResult(onCloseReturnCode);
					
					/*
					  Intent resultData = new Intent();
			            resultData.putExtra("TEST", "return data");
			            setResult(666, resultData);
			            dialog.cancel();
					*/
					//if(onCancelClickFinishActivity){
		            //	((Activity)context).finish();
		            //}
				}
			});
//		}
//		else {
//			this.close.setOnClickListener(onCancel);
//			this.bCancel.setOnClickListener(onCancel);
//		}
		//this.layout.setLayoutParams(new LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT));
		
		this.setContentView(this.layout);
	
	}
/*
	public void setOnOKClickListener(View.OnClickListener onClick){
		
		this.onOK = onClick;
		//this.bOK.setOnClickListener(onClick);
	}
	
	public void setOnDismissListener(View.OnClickListener onClick){
		this.onCancel = onClick;
		//this.close.setOnClickListener(onClick);
		//this.bCancel.setOnClickListener(onClick);
	}
	*/  
}
