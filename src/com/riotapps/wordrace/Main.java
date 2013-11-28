package com.riotapps.wordrace;

import com.riotapps.wordbase.R;
import com.riotapps.wordrace.hooks.Game;
import com.riotapps.wordbase.hooks.Opponent;
import com.riotapps.wordbase.hooks.OpponentService;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordrace.hooks.GameService;
import com.riotapps.wordrace.ui.CreateGameDialog;
import com.riotapps.wordbase.ui.CustomButtonDialog;
import com.riotapps.wordbase.ui.DialogManager;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.DesignByContractException;
import com.riotapps.wordbase.utils.Logger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Main extends com.riotapps.wordbase.Main {
	private static final String TAG = Main.class.getSimpleName();
	private CreateGameDialog createGameDialog;
	private int opponentId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
    protected void handleGameStartPrompt(int opponentId){
		////Opponent o = OpponentService.getOpponent(opponentId);
     //	this.chosenOpponentId = opponentId;
    	this.opponentId = opponentId;
    	this.createGameDialog = new CreateGameDialog(this, opponentId);
     
    	this.createGameDialog.show();	
    }
	@Override
	public void dialogClose(int resultCode) {
		// TODO Auto-generated method stub
		Logger.d(TAG, "dialogClose called resultCode=" + resultCode);
		
		/*public static final int RETURN_CODE_CREATE_GAME_DIALOG_OK_CLICKED = 125;	
		public static final int RETURN_CODE_CREATE_GAME_DIALOG_CLOSE_CLICKED = 126;
		public static final int RETURN_CODE_CREATE_GAME_DIALOG_CANCEL_CLICKED = 127;
		public static final int RETURN_CODE_CREATE_GAME_DIALOG_ORIGINAL_GAME_CLICKED = 128;
		public static final int RETURN_CODE_CREATE_GAME_DIALOG_SPEED_ROUND_CLICKED = 129;
		public static final int RETURN_CODE_CREATE_GAME_DIALOG_DOUBLE_TIME_CLICKED = 130;
		public static final int RETURN_CODE_CREATE_GAME_DIALOG_SPEED_ROUND_STORE_CLICKED = 131;
		public static final int RETURN_CODE_CREATE_GAME_DIALOG_DOUBLE_TIME_STORE_CLICKED = 132;
		*/
		 switch(resultCode) { 
		   case Constants.RETURN_CODE_CREATE_GAME_DIALOG_CLOSE_CLICKED:
			   this.dismissCreateGameDialog();
			   super.trackEvent(Constants.TRACKER_ACTION_BUTTON_TAPPED,
					     			Constants.TRACKER_LABEL_START_GAME_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			   break;
		   case Constants.RETURN_CODE_CREATE_GAME_DIALOG_ORIGINAL_GAME_CLICKED:
			   this.dismissCreateGameDialog();

			   this.handleGameStartOnClick(this, Constants.RACE_GAME_TYPE_90_SECOND_DASH);
			   break;
		   case Constants.RETURN_CODE_CREATE_GAME_DIALOG_SPEED_ROUND_CLICKED:
			   this.dismissCreateGameDialog();

			   this.handleGameStartOnClick(this, Constants.RACE_GAME_TYPE_SPEED_ROUNDS);
			   break; 
		   case Constants.RETURN_CODE_CREATE_GAME_DIALOG_DOUBLE_TIME_CLICKED:
			   this.dismissCreateGameDialog();

			   this.handleGameStartOnClick(this, Constants.RACE_GAME_TYPE_DOUBLE_TIME);
			   break;
		 }
		
	}
	
	 private void dismissCreateGameDialog(){
			if (this.createGameDialog != null){
				createGameDialog.dismiss();
				createGameDialog = null;
			}
		}
	
 
	public void handleGameStartOnClick(Context context, int type){
    	//start game and go to game surface
    	try {
    		
    		
			Game game = GameService.createGame(context, super.player, this.opponentId, type);

			super.trackEvent(Constants.TRACKER_ACTION_90_SECOND_GAME_STARTED,String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, opponentId), (int) Constants.TRACKER_SINGLE_VALUE);
 
			((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_GAME_SURFACE);
	 
    		this.finish();
			
		} catch (DesignByContractException e) {
			// TODO Auto-generated catch block
			
			DialogManager.SetupAlert(this, this.getString(R.string.sorry), e.getMessage());
		}

    }

}
