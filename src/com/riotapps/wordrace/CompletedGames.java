package com.riotapps.wordrace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
 
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
 
import com.riotapps.wordrace.hooks.Game;
import com.riotapps.wordrace.hooks.GameService;
import com.riotapps.wordrace.R;
import com.riotapps.wordbase.hooks.CompletedGame;
import com.riotapps.wordbase.hooks.Opponent;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.hooks.WordService;
import com.riotapps.wordbase.ui.CustomProgressDialog;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.IntentExtra;
import com.riotapps.wordbase.utils.Logger;

public class CompletedGames extends FragmentActivity {
	private static final String TAG = CompletedGames.class.getSimpleName();
	private PreLoadTask preloadTask;
	private CustomProgressDialog spinner;
	
	private final Context context = this;
	
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.completedgames);
	        
	         
	        
	        PlayerService.loadPlayerInHeader(this);
	         
	        ApplicationContext.captureTime(TAG, "oncreate about to hide menu");
	        MenuUtils.hideMenu(this);
	        MenuUtils.setHeaderTitle(this, this.getString(R.string.header_title_completed_games));
	        ApplicationContext.captureTime(TAG, "oncreate hide menu completed");
	    
	        spinner = new CustomProgressDialog(this);
			spinner.setMessage(this.getString(R.string.progress_loading));
			spinner.show();
	    //     this.preloadTask = new PreLoadTask();
	      //   this.preloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
	        
	         this.loadList();
	    //	if (StoreService.isHideBannerAdsPurchased(this)){
		//		AdView adView = (AdView)this.findViewById(R.id.adView);
		//		adView.setVisibility(View.GONE);
		//	}
	    //	   ApplicationContext.captureTime(TAG, "oncreate completed");
	    	
	        
    	    AdView adView = (AdView)this.findViewById(R.id.adView);
	    	if (StoreService.isHideBannerAdsPurchased(this)){	
				adView.setVisibility(View.GONE);
			}
	    	else {
	    		adView.loadAd(new AdRequest());
	    	}
	    	 
	 }
	
	@Override
	protected void onStart() {
		 
		super.onStart();

	    ApplicationContext.captureTime(TAG, "onStart about to loadlist"); 
	  //  CompletedGames.this.loadList();
        ApplicationContext.captureTime(TAG, "onStart loadlist copleted");

		EasyTracker.getInstance().activityStart(this);
	}


	@Override
	protected void onStop() {
	 
		super.onStop();
		if (this.preloadTask != null){
			this.preloadTask = null;
		}
		EasyTracker.getInstance().activityStop(this);
	}
	private class PreLoadTask extends AsyncTask<Void, Void, Void> {
		  
		 CompletedGames.CompletedGameArrayAdapter adapter;
		
		 @Override
		 protected Void doInBackground(Void... params) {
			 Logger.d(CompletedGames.TAG, "PreLoadTask doInBackground called");
			 ApplicationContext.captureTime(TAG, "loadList starting");
			  adapter = new CompletedGames.CompletedGameArrayAdapter(CompletedGames.this, GameService.getCompletedGames());
			  ApplicationContext.captureTime(TAG, "loadList ending");
	    	 return null;

	     }

	   //  protected void onProgressUpdate(Integer... progress) {
	   //      setProgressPercent(progress[0]);
	   //  }
	     protected void onPostExecute(Void param) {
			 Logger.d(CompletedGames.TAG, "PreLoadTask onPostExecute called");
		
			   		 
		    	ListView lvGames = (ListView) findViewById(R.id.lvGames);

		    	lvGames.setAdapter(adapter); 
				  ApplicationContext.captureTime(TAG, "setAdapter ending");
		    	
		    	lvGames.setOnItemClickListener(new OnItemClickListener() {
		            @Override
		            public void onItemClick(AdapterView<?> parent, View view, int position,
		                    long id) {
		    
		            	LinearLayout llItem = (LinearLayout) view.findViewById(R.id.llItem);
		            	
		            	String gameId = llItem.getTag().toString();
		  			
		            	//Intent intent = new Intent(CompletedGames.this, com.riotapps.wordbase.GameSurface.class);
		            	
		            	List<IntentExtra> extras = new ArrayList<IntentExtra>();
		            	extras.add(new IntentExtra(Constants.EXTRA_GAME_ID, gameId, String.class));
		            	extras.add(new IntentExtra(Constants.EXTRA_FROM_COMPLETED_GAME_LIST, true, Boolean.class));
		            	((ApplicationContext)CompletedGames.this.getApplication()).startNewActivity(CompletedGames.this, Constants.ACTIVITY_CLASS_GAME_SURFACE, extras);
		          		//intent.putExtra(Constants.EXTRA_GAME_ID, gameId, gameId.t);
		          		//intent.putExtra(Constants.EXTRA_FROM_COMPLETED_GAME_LIST, true);
		          		//CompletedGames.this.startActivity(intent); 
		            }
		        });
		    	
		    	if (CompletedGames.this.spinner != null){
		    		CompletedGames.this.spinner.dismiss();
		    		CompletedGames.this.spinner = null;
		 		}
	     }

	 
	 }
	 
	  private void loadList(){
		  ApplicationContext.captureTime(TAG, "loadList starting");
		  CompletedGameArrayAdapter adapter = new CompletedGameArrayAdapter(this, GameService.getCompletedGames());
		  ApplicationContext.captureTime(TAG, "loadList ending");
	    
			ListView lvGames = (ListView) findViewById(R.id.lvGames);

	    	lvGames.setAdapter(adapter); 
			  ApplicationContext.captureTime(TAG, "setAdapter ending");
	    	
	    	lvGames.setOnItemClickListener(new OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {
	    
	            	LinearLayout llItem = (LinearLayout) view.findViewById(R.id.llItem);
	            	
	            	String gameId = llItem.getTag().toString();
	  			
	            	//Intent intent = new Intent(CompletedGames.this, com.riotapps.wordbase.GameSurface.class);
	            	
	            	List<IntentExtra> extras = new ArrayList<IntentExtra>();
	            	extras.add(new IntentExtra(Constants.EXTRA_GAME_ID, gameId, String.class));
	            	extras.add(new IntentExtra(Constants.EXTRA_FROM_COMPLETED_GAME_LIST, true, Boolean.class));
	            	((ApplicationContext)CompletedGames.this.getApplication()).startNewActivity(CompletedGames.this, Constants.ACTIVITY_CLASS_GAME_SURFACE, extras);
	          		//intent.putExtra(Constants.EXTRA_GAME_ID, gameId, gameId.t);
	          		//intent.putExtra(Constants.EXTRA_FROM_COMPLETED_GAME_LIST, true);
	          		//CompletedGames.this.startActivity(intent); 
	            }
	        });
	    	
	    	if (CompletedGames.this.spinner != null){
	    		CompletedGames.this.spinner.dismiss();
	    		CompletedGames.this.spinner = null;
	 		}
		  

	    }
	  private class CompletedGameArrayAdapter extends ArrayAdapter<CompletedGame> {
	   	  private final CompletedGames context;
 
	   	  private int itemCount = 0;;
	   	  private List<CompletedGame> values;
	   	  LayoutInflater inflater;
	   
	   	  public CompletedGameArrayAdapter(CompletedGames context, List<CompletedGame> games){  
	   	    super(context, R.layout.completed_game_item, games);
	    	    this.context = context;
	    	    this.values = games;

	    	    this.itemCount = games.size();
	    	    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	  }
	   	  
	    	  @Override
	    	  public View getView(int position, View rowView, ViewGroup parent) {

	    		  if ( rowView == null ) {
	    			  rowView = inflater.inflate(R.layout.completed_game_item, parent, false);
	    		  }

	    	   // View rowView = inflater.inflate(R.layout.choosefbfrienditem, parent, false);
	    		  //RelativeLayout rlItem = (RelativeLayout) rowView.findViewById(R.id.rlItem);
	    		  ImageView ivOpponent = (ImageView) rowView.findViewById(R.id.ivOpponent);
	    		  TextView tvOpponent = (TextView) rowView.findViewById(R.id.tvOpponent);
	    		  TextView tvSummary = (TextView) rowView.findViewById(R.id.tvSummary);
	    		  TextView tvGameType = (TextView) rowView.findViewById(R.id.tvGameType);
	    		  TextView tvSkillLevel = (TextView) rowView.findViewById(R.id.tvSkillLevel);
	    		  LinearLayout llBottomBorder = (LinearLayout)rowView.findViewById(R.id.llBottomBorder);
	    		   
	    		  tvOpponent.setTypeface(ApplicationContext.getMainFontTypeface());
	    		  tvSummary.setTypeface(ApplicationContext.getMainFontTypeface());
	    		  tvSkillLevel.setTypeface(ApplicationContext.getMainFontTypeface());
	    		  tvGameType.setTypeface(ApplicationContext.getMainFontTypeface());
	    		  
	    		  CompletedGame game = values.get(position);// values[position];
		     
		    	  
		    	 //int opponentImageId = context.getResources().getIdentifier(CompletedGames.this.getString(R.string.namespace) + ":drawable/" + game.getOpponent().getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_SMALL), null, null);     		
 		  		  ivOpponent.setImageResource(game.getOpponentImageResourceId());
 		  		  
 		  		//  Bitmap bm = GameSurfaceView.decodeSampledBitmapFromResource(getResources(), opponentImageId, 64, 64);
 		  		  //Bitmap bm = Bitmap.createScaledBitmap(bm, 64, 64, false);
				  ApplicationContext.captureTime(TAG, "loadList getSmallBitmap starting opponent=" + game.getOpponentName());
 		  		 // ivOpponent.setImageBitmap(game.getOpponent().getSmallBitmap());
 		  		  ApplicationContext.captureTime(TAG, "loadList getSmallBitmap ending");
 		  		  tvOpponent.setText(game.getOpponentName()); //tvOpponent.setText(game.getOpponent().getName());
 		  		  tvSkillLevel.setText(String.format(CompletedGames.this.getString(R.string.parentheses_enclosure), game.getOpponentSkillLevel()));
 		  		  tvGameType.setText(game.getGameType());
 		  		  //tvSkillLevel.setText(String.format(context.getString(R.string.game_list_skill_level), game.getOpponent().getSkillLevelText(context)));
		    	  tvSummary.setText(game.getLastActionText(context));
		    	  
		    	  
		    	  if (position == this.itemCount - 1){ //last item
		    		   //Logger.d(TAG, "position=wordCount");
			   		//	RelativeLayout rlLineItem = (RelativeLayout)rowView.findViewById(R.id.rlItem);
			   		//	int bgLineItem = context.getResources().getIdentifier("com.riotapps.word:drawable/text_selector_bottom", null, null);
			   		//	rlLineItem.setBackgroundResource(bgLineItem);
			   			//LinearLayout llBottomBorder = (LinearLayout)rowView.findViewById(R.id.llBottomBorder);
			   			llBottomBorder.setVisibility(View.INVISIBLE);
		    	   }
		    	   else{
		    		   llBottomBorder.setVisibility(View.VISIBLE);
		    	   }
		    	// bm = null;
		    	  rowView.setTag(game.getId());
		    	  return rowView;
	    	  }
 
	    }
	
}
