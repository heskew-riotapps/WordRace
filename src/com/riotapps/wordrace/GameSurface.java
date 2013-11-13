package com.riotapps.wordrace;

import java.util.ArrayList;
import java.util.List;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.riotapps.wordrace.R;
import com.riotapps.wordrace.hooks.GameService;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.hooks.WordService;
import com.riotapps.wordbase.interfaces.ICloseDialog;
import com.riotapps.wordbase.ui.CustomButtonDialog;
import com.riotapps.wordbase.ui.CustomProgressDialog;
import com.riotapps.wordbase.ui.DialogManager;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.PreconditionException;
import com.riotapps.wordrace.hooks.Game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.TextView;

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
	 private CountDownTimer countdown = null;
	 private TextView tvCountdown;
	 private CustomButtonDialog customDialog = null; 
	private List<String> possibleWords = new ArrayList<String>();
	private PreLoadTask preloadTask = null;
	private CustomProgressDialog spinner;
	 
	 
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
		
	 	this.loadVars();
	 	this.setupMenu();
	 	this.setupGame();
	 	this.loadLetterButtons();
	 	this.loadButtons();
	}
	
	private void loadVars(){
		this.tvCountdown = (TextView)this.findViewById(R.id.tvCountdown);
	}

	private void loadButtons(){
		Button bStart = (Button)this.findViewById(R.id.bStart);
		Button bCancel = (Button)this.findViewById(R.id.bCancel);
		Button bShuffle = (Button)this.findViewById(R.id.bShuffle);
		Button bRecall = (Button)this.findViewById(R.id.bRecall);
		Button bPlay = (Button)this.findViewById(R.id.bPlay);
		
		bStart.setOnClickListener(this);
		bCancel.setOnClickListener(this);
		bShuffle.setOnClickListener(this);
		bRecall.setOnClickListener(this);
		bPlay.setOnClickListener(this);
		
		if (this.game.isStarted()){
			bStart.setVisibility(View.GONE);
			bCancel.setVisibility(View.GONE);
			bShuffle.setVisibility(View.VISIBLE);
			bRecall.setVisibility(View.VISIBLE);
			bPlay.setVisibility(View.VISIBLE);
		}
		else if (this.game.isActive()){
			bStart.setVisibility(View.VISIBLE);
			bCancel.setVisibility(View.VISIBLE);
			bShuffle.setVisibility(View.GONE);
			bRecall.setVisibility(View.GONE);
			bPlay.setVisibility(View.GONE);			
		}
		else{ ///completed
		
		}
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
 
	}
	
//	private void loadBitmaps(){
//		private Bitmap bm1;
//	}
	
	private void setupGame(){
		// Logger.d(TAG,"setupGame game turn=" + this.game.getTurn());
		spinner = new CustomProgressDialog(this);
		spinner.setMessage(this.getString(R.string.progress_loading));
		spinner.show();
		
		this.preloadTask = new PreLoadTask();
		this.preloadTask.execute();
		
		GameService.loadScoreboard(this, this.game);
	 	
		//if (!this.game.isCompleted()){
	//		this.fillGameState();
		//}
	 	
	 	//this.setupButtons();
	 	this.setupFonts();

	}
	 private class PreLoadTask extends AsyncTask<Void, Void, Void> {
		  
		 @Override
		 protected Void doInBackground(Void... params) {
			 if (GameSurface.this.game.isActive()){
					//go get words and store in memory in GameSurface
					List<List<String>> letterSets = GameService.getSortedRaceLetterSets(game.getHopper());
					/*
					String[] lettersetArray = new String[letterSets.size()];
					
					//create an array of strings so we can look up all valid words at once
					for (int i = 0; i < letterSets.size(); i++){
						String index = "";
						//put the letters into a string to use as a parameter to look up valid matches by index
						for (String s : letterSets.get(i)){
							index += s;
						}
						lettersetArray[i] = index.toLowerCase();
						
						
					}*/
			        WordService wordService = new WordService(GameSurface.this);

					
					//break up the fetch into batches of 50
					int x = 0;
					int processed = 0;
					String[] lettersetArray = null;
					
					for (List<String> letterSet : letterSets){
						

						if (x == 0){
							lettersetArray = new String[(letterSets.size() - x - 1 >= 50 ? 50 : letterSets.size() - x - 1)];	
						}
						
						String index = "";
						
						for (String s : letterSets.get(x)){
							index += s;
						}
						lettersetArray[x] = index.toLowerCase();
						x += 1;
						processed += 1;
						
						if (x >= 50){
							GameSurface.this.possibleWords.addAll(wordService.getMatchingWordsFromIndexArray(lettersetArray));
							lettersetArray = null;
							//start x over
							x = 0;
						}
					}
					
			//		this.possibleWords = wordService.getMatchingWordsFromIndexArray(lettersetArray);
					
					letterSets.clear();
					letterSets = null;
					wordService.finish();
				    wordService = null;
				      
					//save game to save the total number of possibilities
					 if (GameSurface.this.game.getNumPossibleWords() == 0){
						 GameSurface.this.game.setNumPossibleWords(GameSurface.this.possibleWords.size());
						 GameService.saveGame(GameSurface.this.game);
					 }
				}
			
	    	 
	    	 return null;
	    	 
	    	 //return game; 
	     }

	   //  protected void onProgressUpdate(Integer... progress) {
	   //      setProgressPercent(progress[0]);
	   //  }
	     protected void onPostExecute(Void param) {
	    	 if (GameSurface.this.game.isActive()){
	    		 TextView tvWordsLeft = (TextView)GameSurface.this.findViewById(com.riotapps.wordrace.R.id.tvWordsLeft); 
		    	 if (GameSurface.this.game.getHopper().size() == 1){
			 		 tvWordsLeft.setText(com.riotapps.wordrace.R.string.scoreboard_1_word_left); 
				 }
				 else{
					 tvWordsLeft.setText(String.format(GameSurface.this.getString(com.riotapps.wordrace.R.string.scoreboard_words_left), GameSurface.this.game.getNumPossibleWords() - GameSurface.this.game.getPlayedWords().size()));
				 }
	    	 }
	    	 if (GameSurface.this.spinner != null){
	    		 GameSurface.this.spinner.dismiss();
	    		 GameSurface.this.spinner = null;
	 		}
	     }

	 
	 }
	 
	
    private void dismissCustomDialog(){
		if (this.customDialog != null){
			customDialog.dismiss();
			customDialog = null;
		}
	}
	
	@Override
	public void dialogClose(int resultCode) {
		 switch(resultCode) { 
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_START_OK_CLICKED:
			   this.dismissCustomDialog();
	 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_START_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			
	 			this.handleStartOK(); 
	 			break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_START_CANCEL_CLICKED: 
			    this.dismissCustomDialog(); 

			    this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_START_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			    break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_START_CLOSE_CLICKED: 
		    	this.dismissCustomDialog(); 

		    	this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
       			Constants.TRACKER_LABEL_START_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
		    	break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_OK_CLICKED:
			    this.dismissCustomDialog(); 
	 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_CANCEL_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 			
	 			this.handleCancelOK();
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_CLOSE_CLICKED:
			    this.dismissCustomDialog(); 
	 			this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_CANCEL_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
	 		 	break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_CANCEL_CLICKED:	
			    this.dismissCustomDialog(); 

			    this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
	        			Constants.TRACKER_LABEL_CANCEL_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			    break;
	 		}
  
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
	 	if (v.getId() == R.id.bStart) {
			this.handleStartOnClick();
		} else if (v.getId() == R.id.bCancel) {
			this.handleCancelOnClick();
		} 		
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
		   
		
	   private void handleCancelOnClick(){
	   
	    	this.customDialog = new CustomButtonDialog(this, 
	    			this.getString(R.string.game_surface_cancel_game_confirmation_title), 
	    			this.getString(R.string.game_surface_cancel_game_confirmation_text),
	    			this.getString(R.string.yes),
	    			this.getString(R.string.no),
	    			Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_OK_CLICKED,
	    			Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_CANCEL_CLICKED,
	    			Constants.RETURN_CODE_CUSTOM_DIALOG_CANCEL_CLOSE_CLICKED);
	    
	    	this.customDialog.show();	
		   
	   }
	   
	   private void handleStartOnClick(){
	    	this.customDialog = new CustomButtonDialog(this, 
	    			this.getString(R.string.game_surface_start_game_confirmation_title), 
	    			this.getString(R.string.game_surface_start_game_confirmation_text),
	    			this.getString(R.string.yes),
	    			this.getString(R.string.no),
	    			Constants.RETURN_CODE_CUSTOM_DIALOG_START_OK_CLICKED,
					Constants.RETURN_CODE_CUSTOM_DIALOG_START_CANCEL_CLICKED,
					Constants.RETURN_CODE_CUSTOM_DIALOG_START_CLOSE_CLICKED);
	    
	    	this.customDialog.show();	

   	   }

	   private void handleCancelOK(){
		   GameService.cancel(this.getPlayer(), this.game);
		   
			((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_MAIN);
	 		this.finish();

	   }
	   
	   private void handleStartOK(){
		 //make countdown timer a variable so that it can be killed in stop/pause
		   //change game status to started (5)
		   GameService.startGame(this.game);
		   this.loadButtons();
			
		   //might need a start delay here at some point
		   
		   this.countdown = new CountDownTimer(180000, 1000) {

			     public void onTick(long millisUntilFinished) {
			    	 int secondsUntilFinished = (int) millisUntilFinished / 1000;
			    	 int minutesLeft = (int) secondsUntilFinished / 60;
			    	 int secondsLeft = secondsUntilFinished - (minutesLeft * 60);
			    
			         GameSurface.this.tvCountdown.setText(String.format(GameSurface.this.getString(R.string.scoreboard_countdown), minutesLeft, String.format("%02d", secondsLeft)));
			     }

			     public void onFinish() {
			        // mTextField.setText("done!");
			    	 GameSurface.this.tvCountdown.setText(GameSurface.this.getString(R.string.scoreboard_countdown_completed));
			     }
			  };
			  
			  this.countdown.start();
	 		 

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
			Button bCancel = (Button) findViewById(R.id.bCancel);
			Button bRecall = (Button) findViewById(R.id.bRecall);
			Button bStart = (Button) findViewById(R.id.bStart);

			bShuffle.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bPlay.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bCancel.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bRecall.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bStart.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
		}
		
}
