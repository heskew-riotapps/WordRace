package com.riotapps.wordrace;

import com.riotapps.wordbase.R;
import com.riotapps.wordrace.hooks.Game;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordrace.hooks.GameService;
import com.riotapps.wordbase.ui.DialogManager;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.DesignByContractException;

import android.content.Context;
import android.os.Bundle;

public class Main extends com.riotapps.wordbase.Main {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void handleGameStartOnClick(Context context, Player player, int opponentId){
    	//start game and go to game surface
    	try {
		
    		
			Game game = GameService.createGame(context, player, opponentId);

			super.trackEvent(Constants.TRACKER_ACTION_GAME_STARTED,String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, opponentId), (int) Constants.TRACKER_SINGLE_VALUE);
 
			((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_GAME_SURFACE);
	 
    		this.finish();
			
		} catch (DesignByContractException e) {
			// TODO Auto-generated catch block
			
			DialogManager.SetupAlert(this, this.getString(R.string.sorry), e.getMessage());
		}

    }

}
