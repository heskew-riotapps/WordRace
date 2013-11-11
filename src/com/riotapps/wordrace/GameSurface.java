package com.riotapps.wordrace;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.riotapps.wordrace.R;
import com.riotapps.wordrace.hooks.GameService;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.interfaces.ICloseDialog;
import com.riotapps.wordbase.ui.DialogManager;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordrace.hooks.Game;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

public class GameSurface  extends FragmentActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ICloseDialog{
	private static final String TAG = GameSurface.class.getSimpleName();
	
	 private PopupMenu popupMenu;
	 private String gameId = ""; 
	 
	 private Tracker tracker;
	 private Player player;
	 private Game game;
	 private boolean hideInterstitialAd;
	 private boolean isChartBoostActive;
	 private boolean isAdMobActive;
	 private Chartboost cb;
	 
	 public Tracker getTracker() {
		if (this.tracker == null){
			this.tracker = EasyTracker.getTracker();
		}
		return tracker;
	 }

	  public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	 }
	  
	  public Player getPlayer() {
			//return player;
			if (this.player == null){
				this.player = ((ApplicationContext)this.getApplicationContext()).getPlayer(); //PlayerService.getPlayerFromLocal();
			}
			return this.player;
		}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamesurface);
		
		this.setGameId();
	 	
	 	this.game = GameService.getGame(gameId); //(Game) i.getParcelableExtra(Constants.EXTRA_GAME);
		
	 	this.setupMenu();
	 	this.setupGame();
	 	this.loadLetterButtons();
	}

	private void loadLetterButtons(){
		Display display = getWindowManager().getDefaultDisplay();
	    Point size = new Point();
	 	display.getSize(size);
	 	 display.getSize(size);
		int fullWidth = size.x;
			 
		int letterTileSize = Math.round(fullWidth / 6.20f);	
	 	int maxTrayTileSize = this.getResources().getInteger(R.integer.maxTrayTileSize);
		if (letterTileSize > maxTrayTileSize){letterTileSize = maxTrayTileSize;}
		
		Button bLetter1 = (Button) findViewById(R.id.bLetter1);
		Button bLetter2 = (Button) findViewById(R.id.bLetter2);
		Button bLetter3 = (Button) findViewById(R.id.bLetter3);
		Button bLetter4 = (Button) findViewById(R.id.bLetter4);
		Button bLetter5 = (Button) findViewById(R.id.bLetter5);
		Button bLetter6 = (Button) findViewById(R.id.bLetter6);
		Button bLetter7 = (Button) findViewById(R.id.bLetter7);
		Button bLetter8 = (Button) findViewById(R.id.bLetter8);
		Button bLetter9 = (Button) findViewById(R.id.bLetter9);
		Button bLetter10 = (Button) findViewById(R.id.bLetter10);
		Button bLetter11 = (Button) findViewById(R.id.bLetter11);
		Button bLetter12 = (Button) findViewById(R.id.bLetter12);
		
		ViewGroup.LayoutParams params1 = bLetter1.getLayoutParams();
		params1.width = letterTileSize;
		params1.height = letterTileSize;
		bLetter1.setLayoutParams(params1);
		ViewGroup.LayoutParams params2 = bLetter2.getLayoutParams();
		params2.width = letterTileSize;
		params2.height = letterTileSize;
		bLetter2.setLayoutParams(params2);
		ViewGroup.LayoutParams params3 = bLetter3.getLayoutParams();
		params3.width = letterTileSize;
		params3.height = letterTileSize;
		bLetter3.setLayoutParams(params3);
		ViewGroup.LayoutParams params4 = bLetter4.getLayoutParams();
		params4.width = letterTileSize;
		params4.height = letterTileSize;
		bLetter4.setLayoutParams(params4);
		ViewGroup.LayoutParams params5 = bLetter5.getLayoutParams();
		params5.width = letterTileSize;
		params5.height = letterTileSize;
		bLetter5.setLayoutParams(params5);
		ViewGroup.LayoutParams params6 = bLetter6.getLayoutParams();
		params6.width = letterTileSize;
		params6.height = letterTileSize;
		bLetter6.setLayoutParams(params6);
		ViewGroup.LayoutParams params7 = bLetter7.getLayoutParams();
		params7.width = letterTileSize;
		params7.height = letterTileSize;
		bLetter7.setLayoutParams(params7);
		ViewGroup.LayoutParams params8 = bLetter8.getLayoutParams();
		params8.width = letterTileSize;
		params8.height = letterTileSize;
		bLetter8.setLayoutParams(params8);
		ViewGroup.LayoutParams params9 = bLetter9.getLayoutParams();
		params9.width = letterTileSize;
		params9.height = letterTileSize;
		bLetter9.setLayoutParams(params9);
		ViewGroup.LayoutParams params10 = bLetter10.getLayoutParams();
		params10.width = letterTileSize;
		params10.height = letterTileSize;
		bLetter10.setLayoutParams(params10);
		ViewGroup.LayoutParams params11 = bLetter11.getLayoutParams();
		params11.width = letterTileSize;
		params11.height = letterTileSize;
		bLetter11.setLayoutParams(params11);
		ViewGroup.LayoutParams params12 = bLetter12.getLayoutParams();
		params12.width = letterTileSize;
		params12.height = letterTileSize;
		bLetter1.setLayoutParams(params12);
	}
	
	private void setupGame(){
		// Logger.d(TAG,"setupGame game turn=" + this.game.getTurn());
		GameService.loadScoreboard(this, this.game);
	 	
		//if (!this.game.isCompleted()){
	//		this.fillGameState();
		//}
	 	
	 	//this.setupButtons();
	 	this.setupFonts();

	}
	
	@Override
	public void dialogClose(int resultCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dialogClose(int resultCode, String returnValue) {
		// TODO Auto-generated method stub
		
	}

	   private void setupMenu(){
	      	
		  	  this.popupMenu = new PopupMenu(this, findViewById(R.id.options));

		  	  MenuUtils.fillMenu(this, this.popupMenu);
		      this.popupMenu.setOnMenuItemClickListener(this);
		      findViewById(R.id.options).setOnClickListener(this);
		  }

			
		    @Override
		    public boolean onMenuItemClick(MenuItem item) {
		    	Logger.d(TAG, "onMenuItemClick");
		    	//probably need to stop thread here
		    	return MenuUtils.handleMenuClick(this, item.getItemId());
		    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	 private void setGameId(){
			Intent i = getIntent();
		 	//String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
		 	boolean fromCompletedGameList = i.getBooleanExtra(Constants.EXTRA_FROM_COMPLETED_GAME_LIST, false);
		 	
		 	//do this so that back button does not get crazy if one navigates to game from completed game list continuously
		 	if (fromCompletedGameList){
		 		MenuUtils.hideMenu(this);
		 	}
	 
		 //	this.captureTime("get game from local starting");
		 	
		 	//check to see if we are coming back from a child/downstream activity after game is over
		 	if (this.game != null && this.game.isCompleted() && !this.gameId.equals("")){
		 		//just keep gameId that is in context already
		 	}
		 	else{
		 		this.gameId = "";
		 	}
		 	
		 	if (this.game == null || this.gameId.equals("")){
			 	this.gameId = this.getPlayer().getActiveGameId();
			 	
			 	if (fromCompletedGameList){
			 		this.gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
			 	} 
		 	}
		 	
		 	Logger.d(TAG, "setGameId=" + (this.gameId == null ? "null" : this.gameId));
		 	
		 	if (this.gameId == null || this.gameId.equals("")){
		 		Logger.d(TAG, "setGameId starting over, sending user to main");
		 		//reroute player to main
		 		
				((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_MAIN);
		 		//Intent intent = new Intent(this, com.riotapps.wordbase.Main.class);
		 		//this.startActivity(intent); 
	   
		 		this.finish();
		 	}
	  }

		private void setupAdServer(){
			Logger.d(TAG, "setupAdServer called");
			
		 	boolean isAdMob = Constants.INTERSTITIAL_ADMOB;
		 	boolean isChartBoost = Constants.INTERSTITIAL_CHARTBOOST;
			boolean isRevMob = false; //Constants.INTERSTITIAL_REVMOB;
			final int useRevMob = 0;
			
		 	if (!StoreService.isHideInterstitialAdPurchased(this))
		 	{
		 	
		 		if (isChartBoost){
		 			Logger.d(TAG, "setupAdServer isCharBoost=true");
		 			
			 		this.isChartBoostActive = true;
			 		this.setupChartBoost();
		 		}
		 		else if (isAdMob){
		 			this.isAdMobActive = true;
		 		}
		 	}
		 	else{
		 		this.hideInterstitialAd = true;
		 	}
		 		 		
		 	
		 	Logger.d(TAG,  " chartBoost=" + this.isChartBoostActive);
		}
		   public void handlePostAdServer(){
		    //	if (!this.hasPostAdRun &&  this.postTurnMessage.length() > 0){  //chartboost dismiss and close both call this, lets make sure its not run twice
	    		 	
		    		//make sure the pre-primed hintlist is cleared
		    	//	this.placedResultsForWordHints.clear();
				//	this.hints.clear();

		    	//	this.hasPostAdRun = true;
	    		// 	this.unfreezeButtons();
	    		// 	Logger.d(TAG, "unfreeze handlePostAdServer");
	    		// 	if (spinner != null) {
	    		// 		spinner.dismiss();
	    		// 		spinner = null;
	    		// 	}
	    		// 	DialogManager.SetupAlert(context, this.postTurnTitle , this.postTurnMessage);
	    		 	
	    		 	//lets prime the pump for the next play
	    		 	if (this.game.isActive()){
	    		/* 		this.preAutoplayTask = new PreAutoplayTask();
	    		 		this.preAutoplayTask.execute();
	    		 		
	    		 		if(this.wordHintTask != null){
	    		 			this.isWordHintTaskRunning = false;
	    		 			this.wordHintTask.cancel(true);
	    		 			this.wordHintTask = null;
	    		 		}
	    		 		this.wordHintTask =  new WordHintTask();
	    		 		this.wordHintTask.execute(); */
	    		 	}
	    		 	else if (this.game.isCompleted()){
	    		 		//save completed event
	    		 	/*	if (this.game.getPlayerGames().get(0).getScore() == this.game.getPlayerGames().get(1).getScore()){
	    		 			this.trackEvent(Constants.TRACKER_ACTION_GAME_COMPLETED, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID_DRAW, this.game.getOpponentId()), (int) Constants.TRACKER_SINGLE_VALUE);

	    		 		}
	    		 		else if (this.game.getPlayerGames().get(0).getScore() > this.game.getPlayerGames().get(1).getScore()){
	    		 			this.trackEvent(Constants.TRACKER_ACTION_GAME_COMPLETED, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID_LOSS, this.game.getOpponentId()), this.game.getPlayerGames().get(0).getScore() - this.game.getPlayerGames().get(1).getScore());

	    		 		}
	    		 		else {
	    		 			this.trackEvent(Constants.TRACKER_ACTION_GAME_COMPLETED, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID_WON, this.game.getOpponentId()), this.game.getPlayerGames().get(1).getScore() - this.game.getPlayerGames().get(0).getScore());

	    		 		}*/
	    		 	}
		    	//}
		    }
		private void setupChartBoost(){
			this.cb = Chartboost.sharedChartboost();
			this.cb.onCreate(this, this.getString(R.string.chartboost_app_id), this.getString(R.string.chartboost_app_signature), this.chartBoostDelegate);
			this.cb.setImpressionsUseActivities(true);
			this.cb.startSession();
			this.cb.cacheInterstitial();

		}
		private ChartboostDelegate chartBoostDelegate = new ChartboostDelegate() { 

			/*
			 * Chartboost delegate methods
			 * 
			 * Implement the delegate methods below to finely control Chartboost's behavior in your app
			 * 
			 * Minimum recommended: shouldDisplayInterstitial()
			 */


			/* 
			 * shouldDisplayInterstitial(String location)
			 *
			 * This is used to control when an interstitial should or should not be displayed
			 * If you should not display an interstitial, return false
			 *
			 * For example: during gameplay, return false.
			 *
			 * Is fired on:
			 * - showInterstitial()
			 * - Interstitial is loaded & ready to display
			 */
			@Override
			public boolean shouldDisplayInterstitial(String location) {
				Logger.d(TAG, "SHOULD DISPLAY INTERSTITIAL '"+location+ "'?");
				return true;
			}

			/*
			 * shouldRequestInterstitial(String location)
			 * 
			 * This is used to control when an interstitial should or should not be requested
			 * If you should not request an interstitial from the server, return false
			 *
			 * For example: user should not see interstitials for some reason, return false.
			 *
			 * Is fired on:
			 * - cacheInterstitial()
			 * - showInterstitial() if no interstitial is cached
			 * 
			 * Notes: 
			 * - We do not recommend excluding purchasers with this delegate method
			 * - Instead, use an exclusion list on your campaign so you can control it on the fly
			 */
			@Override
			public boolean shouldRequestInterstitial(String location) {
				Logger.d(TAG, "SHOULD REQUEST INSTERSTITIAL '"+location+ "'?");
				return true;
			}

			/*
			 * didCacheInterstitial(String location)
			 * 
			 * Passes in the location name that has successfully been cached
			 * 
			 * Is fired on:
			 * - cacheInterstitial() success
			 * - All assets are loaded
			 * 
			 * Notes:
			 * - Similar to this is: cb.hasCachedInterstitial(String location) 
			 * Which will return true if a cached interstitial exists for that location
			 */
			@Override
			public void didCacheInterstitial(String location) {
				Logger.d(TAG, "INTERSTITIAL '"+location+"' CACHED");
			}

			/*
			 * didFailToLoadInterstitial(String location)
			 * 
			 * This is called when an interstitial has failed to load for any reason
			 * 
			 * Is fired on:
			 * - cacheInterstitial() failure
			 * - showInterstitial() failure if no interstitial was cached
			 * 
			 * Possible reasons:
			 * - No network connection
			 * - No publishing campaign matches for this user (go make a new one in the dashboard)
			 */
			@Override
			public void didFailToLoadInterstitial(String location) {
			    // Show a house ad or do something else when a chartboost interstitial fails to load

				Logger.d(TAG, "ChartBoost INTERSTITIAL '"+location+"' REQUEST FAILED");
				//Toast.makeText(context, "Interstitial '"+location+"' Load Failed",
				//		Toast.LENGTH_SHORT).show();
				handlePostAdServer();
				//handlePostTurnFinalAction(postTurnAction);
			}

			/*
			 * didDismissInterstitial(String location)
			 *
			 * This is called when an interstitial is dismissed
			 *
			 * Is fired on:
			 * - Interstitial click
			 * - Interstitial close
			 *
			 * #Pro Tip: Use the delegate method below to immediately re-cache interstitials
			 */
			@Override
			public void didDismissInterstitial(String location) {

				// Immediately re-caches an interstitial
				cb.cacheInterstitial(location);
				handlePostAdServer();
				//handlePostTurnFinalAction(postTurnAction);


				Logger.d(TAG, "ChartBoost INTERSTITIAL '"+location+"' DISMISSED");
				//Toast.makeText(context, "Dismissed Interstitial '"+location+"'",
				//		Toast.LENGTH_SHORT).show();
				
			}

			/*
			 * didCloseInterstitial(String location)
			 *
			 * This is called when an interstitial is closed
			 *
			 * Is fired on:
			 * - Interstitial close
			 */
			@Override
			public void didCloseInterstitial(String location) {
				Logger.i(TAG, "ChartBoost INSTERSTITIAL '"+location+"' CLOSED");
				//handlePostAdServer();
				//handlePostTurnFinalAction(postTurnAction);
				//Toast.makeText(context, "Closed Interstitial '"+location+"'",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didClickInterstitial(String location)
			 *
			 * This is called when an interstitial is clicked
			 *
			 * Is fired on:
			 * - Interstitial click
			 */
			@Override
			public void didClickInterstitial(String location) {
				Logger.i(TAG, "ChartBoost DID CLICK INTERSTITIAL '"+location+"'");
				handlePostAdServer();
				//handlePostTurnFinalAction(postTurnAction);
				//Toast.makeText(context, "Clicked Interstitial '"+location+"'",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didShowInterstitial(String location)
			 *
			 * This is called when an interstitial has been successfully shown
			 *
			 * Is fired on:
			 * - showInterstitial() success
			 */
			@Override
			public void didShowInterstitial(String location) {
				Logger.i(TAG, "ChartBoost INTERSTITIAL '" + location + "' SHOWN");
				//Toast.makeText(context, "Interstitial '"+location+"' shown",
				//		Toast.LENGTH_SHORT).show();
				
				//Dont do it here because boost does not use full page interstitial and 
				//tray can be seen through modal
				//handlePostTurnFinalAction(postTurnAction);
			}

			/*
			 * More Apps delegate methods
			 */

			/*
			 * shouldDisplayLoadingViewForMoreApps()
			 *
			 * Return false to prevent the pretty More-Apps loading screen
			 *
			 * Is fired on:
			 * - showMoreApps()
			 */
			@Override
			public boolean shouldDisplayLoadingViewForMoreApps() {
				return true;
			}

			/*
			 * shouldRequestMoreApps()
			 * 
			 * Return false to prevent a More-Apps page request
			 *
			 * Is fired on:
			 * - cacheMoreApps()
			 * - showMoreApps() if no More-Apps page is cached
			 */
			@Override
			public boolean shouldRequestMoreApps() {

				return true;
			}

			/*
			 * shouldDisplayMoreApps()
			 * 
			 * Return false to prevent the More-Apps page from displaying
			 *
			 * Is fired on:
			 * - showMoreApps() 
			 * - More-Apps page is loaded & ready to display
			 */
			@Override
			public boolean shouldDisplayMoreApps() {
				Logger.i(TAG, "ChartBoost SHOULD DISPLAY MORE APPS?");
				return true;
			}

			/*
			 * didFailToLoadMoreApps()
			 * 
			 * This is called when the More-Apps page has failed to load for any reason
			 * 
			 * Is fired on:
			 * - cacheMoreApps() failure
			 * - showMoreApps() failure if no More-Apps page was cached
			 * 
			 * Possible reasons:
			 * - No network connection
			 * - No publishing campaign matches for this user (go make a new one in the dashboard)
			 */
			@Override
			public void didFailToLoadMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS REQUEST FAILED");
				//Toast.makeText(context, "More Apps Load Failed",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didCacheMoreApps()
			 * 
			 * Is fired on:
			 * - cacheMoreApps() success
			 * - All assets are loaded
			 */
			@Override
			public void didCacheMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS CACHED");
			}

			/*
			 * didDismissMoreApps()
			 *
			 * This is called when the More-Apps page is dismissed
			 *
			 * Is fired on:
			 * - More-Apps click
			 * - More-Apps close
			 */
			@Override
			public void didDismissMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS DISMISSED");
				//Toast.makeText(context, "Dismissed More Apps",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didCloseMoreApps()
			 *
			 * This is called when the More-Apps page is closed
			 *
			 * Is fired on:
			 * - More-Apps close
			 */
			@Override
			public void didCloseMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS CLOSED");
				//Toast.makeText(context, "Closed More Apps",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didClickMoreApps()
			 *
			 * This is called when the More-Apps page is clicked
			 *
			 * Is fired on:
			 * - More-Apps click
			 */
			@Override
			public void didClickMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS CLICKED");
				//Toast.makeText(context, "Clicked More Apps",
				//		Toast.LENGTH_SHORT).show();
			}

			/*
			 * didShowMoreApps()
			 *
			 * This is called when the More-Apps page has been successfully shown
			 *
			 * Is fired on:
			 * - showMoreApps() success
			 */
			@Override
			public void didShowMoreApps() {
				Logger.i(TAG, "ChartBoost MORE APPS SHOWED");
			}

			/*
			 * shouldRequestInterstitialsInFirstSession()
			 *
			 * Return false if the user should not request interstitials until the 2nd startSession()
			 * 
			 */
			@Override
			public boolean shouldRequestInterstitialsInFirstSession() {
				return true;
			}
		};

		
	    private void trackEvent(String action, String label, int value){
	    	this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, action, label, (long)value);
	    }
		   
		public void trackEvent(String category, String action, String label, long value){
			try{
				this.getTracker().sendEvent(category, action,label, value);
			}
			catch (Exception e){
	  			Logger.d(TAG, "trackEvent category=" + (category == null ? "null" : category) + " action=" + (action == null ? "null" : action) 
	  					 + " label=" + (label == null ? "null" : label)  + " value=" + value +" e=" + e.toString());
	  			
			}
		}
		
		private void setupFonts(){
			//buttons
			Button bShuffle = (Button) findViewById(R.id.bShuffle);
			Button bPlay = (Button) findViewById(R.id.bPlay);
 
			bShuffle.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bPlay.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
		 
				
		}
		
}
