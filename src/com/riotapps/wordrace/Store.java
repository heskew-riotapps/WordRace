package com.riotapps.wordrace;

import com.google.ads.AdRequest;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.riotapps.wordbase.billing.IabHelper;
import com.riotapps.wordbase.billing.IabResult;
import com.riotapps.wordbase.billing.Inventory;
import com.riotapps.wordbase.billing.SkuDetails;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.ui.DialogManager;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.NetworkConnectivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class Store  extends FragmentActivity implements View.OnClickListener{
	private static final String TAG = Store.class.getSimpleName();
	 
	private ApplicationContext appContext;
	private	LayoutInflater inflater;
	private IabHelper mHelper;
	private Tracker tracker;
	
	public Tracker getTracker() {
		if (this.tracker == null){
			this.tracker = EasyTracker.getTracker();
		}
		return tracker;
	}
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.store);
	        
	        PlayerService.loadPlayerInHeader(this);
	        this.appContext = (ApplicationContext)this.getApplicationContext(); 
	        this.inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	       
	        MenuUtils.hideMenu(this);
	        MenuUtils.setHeaderTitle(this, this.getString(R.string.header_title_store));
	        this.setupFonts();
	        
	        NetworkConnectivity connection = new NetworkConnectivity(this);
    		boolean isConnected = connection.checkNetworkConnectivity();
    		
    		if (isConnected){
    			 // compute your public key and store it in base64EncodedPublicKey
    	        mHelper = new IabHelper(this, StoreService.getIABPublicKey(this));
    	        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
    	        	   public void onIabSetupFinished(IabResult result) {
    	        	                
    	        	         // Hooray, IAB is fully set up!
    	        	      Logger.d(TAG, "In-app Billing is ready to go...almost ");
    	        	      onSetupFinished(result);
    	        	   }
    	        	});
    		}
    		else{
    			TextView tvNotConnected = (TextView)this.findViewById(R.id.tvNotConnected);
    			tvNotConnected.setTypeface(ApplicationContext.getMainFontTypeface());
    			tvNotConnected.setText(String.format(this.getString(R.string.store_not_connected), this.getString(R.string.app_name)));
    			ScrollView svStore = (ScrollView)this.findViewById(R.id.svStore);
    			svStore.setVisibility(View.GONE);
    			tvNotConnected.setVisibility(View.VISIBLE);
    		}
	        
	       
	        
	 }
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	      Logger.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

	      // Pass on the activity result to the helper for handling
	      if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
	          // not handled, so handle it ourselves (here's where you'd
	          // perform any handling of activity results not related to in-app
	          // billing...
	          super.onActivityResult(requestCode, resultCode, data);
	      }
	      else {
	          Logger.d(TAG, "onActivityResult handled by IABUtil.");
	      }
	  }
	  
	 public void onSetupFinished(IabResult result){
		 if (!result.isSuccess()) {
	         // Oh noes, there was a problem.
	         Logger.d(TAG, "Problem setting up In-app Billing: " + result);
	      }  
		 else{

				mHelper.queryInventoryAsync(true, StoreService.getAllSkus(this), mGotInventoryListener);
				//mHelper.queryInventoryAsync(mGotInventoryListener);
		 }
		 
	 }
	 
	 IabHelper.QueryInventoryFinishedListener mGotInventoryListener 
				= new IabHelper.QueryInventoryFinishedListener() {
			public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
				onPurchaseCheck(result, inventory);
			
			}
		};
	 
	 
	 IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener 
				= new IabHelper.OnIabPurchaseFinishedListener() {
			public void onIabPurchaseFinished(IabResult result, com.riotapps.wordbase.billing.Purchase purchase) 
			{
				onPurchaseFinished(result, purchase);
			}
	 	};
	 
 	@Override
 	protected void onStart() {
 		 
 		super.onStart();
 		 EasyTracker.getInstance().activityStart(this);
 	}


 	@Override
 	protected void onStop() {
 	 
 		super.onStop();
 		EasyTracker.getInstance().activityStop(this);
 	}
	 	
 	private void trackEvent(String action, String label, long value){
		try{
			this.getTracker().sendEvent(Constants.TRACKER_CATEGORY_STORE, action,label, value);
		}
		catch (Exception e){
  			Logger.d(TAG, "trackEvent action=" + (action == null ? "null" : action) 
  					 + " label=" + (label == null ? "null" : label)  + " value=" + value +" e=" + e.toString());
  			
		}
	}
 	
	 public void onPurchaseCheck(IabResult result, Inventory inventory){
		  Logger.d(TAG, "onPurchaseCheck failed=" + result.isFailure());
		 
		  //put this as method in StoreService
		  
		  if (result.isFailure()) {
		         // handle error here
		       }
	       else {
	         // update the local purchases first just in case something is out of sync
	         StoreService.syncPurchases(inventory, this);
	         
	    	 this.loadStore(inventory);

	       }
	 }
	
	 @Override
	 public void onDestroy() {
	    super.onDestroy();
	    if (mHelper != null) mHelper.dispose();
	    mHelper = null;
	 }
	 
	 private void onPurchaseFinished(IabResult result, com.riotapps.wordbase.billing.Purchase purchase){
		 
		 //Security Recommendation: When you receive the purchase response from Google Play, make sure to check the returned data signature, 
		 //the orderId, and the developerPayload string in the Purchase object to make sure that you are getting the expected values. 
		 //You should verify that the orderId is a unique value that you have not previously processed, and the developerPayload string
		 //matches the token that you sent previously with the purchase request. As a further security precaution, 
		 //you should perform the verification on your own secure server.
		 
		 Logger.d(TAG, "onPurchaseFinished failed=" + result.isFailure());
		 
		 if (result.isFailure()) {
             Logger.d(TAG, "Error purchasing: " + result);
             return;
          }  
		 else {
			 StoreService.savePurchase(purchase.getSku(), purchase.getToken());
			 
			 String thankYouMessage = this.getString(R.string.thank_you);
			 if (purchase.getSku().equals(this.getString(R.string.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL))){
				 thankYouMessage = this.getString(R.string.purchase_thanks_hide_interstitial);
			 }
			 else if (purchase.getSku().equals(this.getString(R.string.SKU_GOOGLE_PLAY_SPEED_ROUNDS))){
				 thankYouMessage = this.getString(R.string.purchase_thanks_speed_rounds);
			 }
			 else if (purchase.getSku().equals(this.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE))){
				 thankYouMessage = this.getString(R.string.purchase_thanks_premium_upgrade);
			 }
			 else if (purchase.getSku().equals(this.getString(R.string.SKU_GOOGLE_PLAY_WORD_DEFINITIONS))){
				 thankYouMessage = this.getString(R.string.purchase_thanks_word_definitions);
			 }
			 else if (purchase.getSku().equals(this.getString(R.string.SKU_GOOGLE_PLAY_DOUBLE_TIME))){
				 thankYouMessage = this.getString(R.string.purchase_thanks_double_time);
			 }
				 
			 //popup a thank you dialog
			 DialogManager.SetupAlert(this, this.getString(R.string.thank_you), thankYouMessage);
	 
			 this.resetPriceButtons(purchase.getSku());
		 }
       
	 }
	 private void resetPriceButtons(String sku){
		 if (sku.equals(this.getString(R.string.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL))){
			 Button bNoAdsPrice = (Button)this.findViewById(R.id.bNoAdsPrice);
			 ImageView ivNoAdsPurchased = (ImageView)this.findViewById(R.id.ivNoAdsPurchased);
			 bNoAdsPrice.setVisibility(View.GONE);
			 ivNoAdsPurchased.setVisibility(View.VISIBLE);
		 }
		 else if (sku.equals(this.getString(R.string.SKU_GOOGLE_PLAY_SPEED_ROUNDS))){
			 Button bSpeedRoundsPrice = (Button)this.findViewById(R.id.bSpeedRoundsPrice);
			 ImageView ivSpeedRoundsPurchased = (ImageView)this.findViewById(R.id.ivSpeedRoundsPurchased);
			 bSpeedRoundsPrice.setVisibility(View.GONE);
			 ivSpeedRoundsPurchased.setVisibility(View.VISIBLE);
		 }
		 else if (sku.equals(this.getString(R.string.SKU_GOOGLE_PLAY_WORD_DEFINITIONS))){
			 Button bWordDefinitionsPrice = (Button)this.findViewById(R.id.bWordDefinitionsPrice);
			 ImageView ivWordDefinitionsPurchased = (ImageView)this.findViewById(R.id.ivWordDefinitionsPurchased);
			 bWordDefinitionsPrice.setVisibility(View.GONE);
			 ivWordDefinitionsPurchased.setVisibility(View.VISIBLE);
		 }
		 else if (sku.equals(this.getString(R.string.SKU_GOOGLE_PLAY_DOUBLE_TIME))){
			 Button bDoubleTimePrice = (Button)this.findViewById(R.id.bDoubleTimePrice);
			 ImageView ivDoubleTimePurchased = (ImageView)this.findViewById(R.id.ivDoubleTimePurchased);
			 bDoubleTimePrice.setVisibility(View.GONE);
			 ivDoubleTimePurchased.setVisibility(View.VISIBLE);
		 }
		 else if (sku.equals(this.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE))){
			 Button bPremiumUpgradePrice = (Button)this.findViewById(R.id.bPremiumUpgradePrice);
			 ImageView ivPremiumUpgradePurchased = (ImageView)this.findViewById(R.id.ivPremiumUpgradePurchased);
			 bPremiumUpgradePrice.setVisibility(View.GONE);
			 ivPremiumUpgradePurchased.setVisibility(View.VISIBLE);
			 
			 Button bNoAdsPrice = (Button)this.findViewById(R.id.bNoAdsPrice);
			 ImageView ivNoAdsPurchased = (ImageView)this.findViewById(R.id.ivNoAdsPurchased);
			 bNoAdsPrice.setVisibility(View.GONE);
			 ivNoAdsPurchased.setVisibility(View.VISIBLE);
			 
			 Button bSpeedRoundsPrice = (Button)this.findViewById(R.id.bSpeedRoundsPrice);
			 ImageView ivSpeedRoundsPurchased = (ImageView)this.findViewById(R.id.ivSpeedRoundsPurchased);
			 bSpeedRoundsPrice.setVisibility(View.GONE);
			 ivSpeedRoundsPurchased.setVisibility(View.VISIBLE);
			 
			 Button bWordDefinitionsPrice = (Button)this.findViewById(R.id.bWordDefinitionsPrice);
			 ImageView ivWordDefinitionsPurchased = (ImageView)this.findViewById(R.id.ivWordDefinitionsPurchased);
			 bWordDefinitionsPrice.setVisibility(View.GONE);
			 ivWordDefinitionsPurchased.setVisibility(View.VISIBLE);
			 
			 Button bDoubleTimePrice = (Button)this.findViewById(R.id.bDoubleTimePrice);
			 ImageView ivDoubleTimePurchased = (ImageView)this.findViewById(R.id.ivDoubleTimePurchased);
			 bDoubleTimePrice.setVisibility(View.GONE);
			 ivDoubleTimePurchased.setVisibility(View.VISIBLE);
		 }
		 
		// else if (sku.equals(Constants.SKU_GOOGLE_PLAY_WORD_HINTS)){
		//	 thankYouMessage = this.getString(R.string.purchase_thanks_word_hints);
		// }
	 }
	 
	 
	 
	 private void purchaseItem(String sku){
		 this.trackEvent(Constants.TRACKER_ACTION_PURCHASE, sku, Constants.TRACKER_SINGLE_VALUE);
		 
		 mHelper.launchPurchaseFlow(this, sku, 10001,   
				   mPurchaseFinishedListener, "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
	 }
	 
	 
	 private void loadStore(Inventory inventory){
		 //if premium upgrade is purchased, dont let them buy the other items that are already covered
		 
		 TextView tvPremiumUpgradeTitle = (TextView)this.findViewById(R.id.tvPremiumUpgradeTitle);
		 TextView tvPremiumUpgradeDescription = (TextView)this.findViewById(R.id.tvPremiumUpgradeDescription);
		 Button bPremiumUpgradePrice = (Button)this.findViewById(R.id.bPremiumUpgradePrice);
		 ImageView ivPremiumUpgradePurchased = (ImageView)this.findViewById(R.id.ivPremiumUpgradePurchased);
		 
		 TextView tvNoAdsTitle = (TextView)this.findViewById(R.id.tvNoAdsTitle);
		 TextView tvNoAdsDescription = (TextView)this.findViewById(R.id.tvNoAdsDescription);
		 Button bNoAdsPrice = (Button)this.findViewById(R.id.bNoAdsPrice);
		 ImageView ivNoAdsPurchased = (ImageView)this.findViewById(R.id.ivNoAdsPurchased);
		 
		 TextView tvWordDefinitionsTitle = (TextView)this.findViewById(R.id.tvWordDefinitionsTitle);
		 TextView tvWordDefinitionsDescription = (TextView)this.findViewById(R.id.tvWordDefinitionsDescription);
		 Button bWordDefinitionsPrice = (Button)this.findViewById(R.id.bWordDefinitionsPrice);
		 ImageView ivWordDefinitionsPurchased = (ImageView)this.findViewById(R.id.ivWordDefinitionsPurchased);
		 
		 TextView tvDoubleTimeTitle = (TextView)this.findViewById(R.id.tvDoubleTimeTitle);
		 TextView tvDoubleTimeDescription = (TextView)this.findViewById(R.id.tvDoubleTimeDescription);
		 Button bDoubleTimePrice = (Button)this.findViewById(R.id.bDoubleTimePrice);
		 ImageView ivDoubleTimePurchased = (ImageView)this.findViewById(R.id.ivDoubleTimePurchased);
		 
		 TextView tvSpeedRoundsTitle = (TextView)this.findViewById(R.id.tvSpeedRoundsTitle);
		 TextView tvSpeedRoundsDescription = (TextView)this.findViewById(R.id.tvSpeedRoundsDescription);
		 Button bSpeedRoundsPrice = (Button)this.findViewById(R.id.bSpeedRoundsPrice);
		 ImageView ivSpeedRoundsPurchased = (ImageView)this.findViewById(R.id.ivSpeedRoundsPurchased);
		 
		 SkuDetails skuPremiumUpgrade = inventory.getSkuDetails(this.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE));
		 tvPremiumUpgradeTitle.setText(this.getString(R.string.store_item_premium_upgrade_title));
		 tvPremiumUpgradeDescription.setText(this.getString(R.string.store_item_premium_upgrade_description));
		 if (inventory.hasPurchase(this.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE))){
			 bPremiumUpgradePrice.setVisibility(View.GONE); 
			 }
		 else{
			 bPremiumUpgradePrice.setText(skuPremiumUpgrade.getPrice());
			 bPremiumUpgradePrice.setTag(this.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE));
			 bPremiumUpgradePrice.setOnClickListener(this);
			 ivPremiumUpgradePurchased.setVisibility(View.GONE);
		 }

		 SkuDetails skuNoAds = inventory.getSkuDetails(this.getString(R.string.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL));
		 tvNoAdsTitle.setText(this.getString(R.string.store_item_no_ads_title));
		 tvNoAdsDescription.setText(this.getString(R.string.store_item_no_ads_description));
		 if (inventory.hasPurchase(this.getString(R.string.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL)) || inventory.hasPurchase(this.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE))) {
			 bNoAdsPrice.setVisibility(View.GONE); 
			 }
		 else{
			 bNoAdsPrice.setText(skuNoAds.getPrice());
			 bNoAdsPrice.setTag(this.getString(R.string.SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL));
			 bNoAdsPrice.setOnClickListener(this);
			 ivNoAdsPurchased.setVisibility(View.GONE);
		 }
	 	 
		 SkuDetails skuWordDefinitions = inventory.getSkuDetails(this.getString(R.string.SKU_GOOGLE_PLAY_WORD_DEFINITIONS));
		 tvWordDefinitionsTitle.setText(this.getString(R.string.store_item_word_definitions_title));
		 tvWordDefinitionsDescription.setText(this.getString(R.string.store_item_word_definitions_description));
		 if (inventory.hasPurchase(this.getString(R.string.SKU_GOOGLE_PLAY_WORD_DEFINITIONS)) || inventory.hasPurchase(this.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE))) {
			 bWordDefinitionsPrice.setVisibility(View.GONE); 
			 }
		 else{
			 bWordDefinitionsPrice.setText(skuWordDefinitions.getPrice());
			 bWordDefinitionsPrice.setTag(this.getString(R.string.SKU_GOOGLE_PLAY_WORD_DEFINITIONS));
			 bWordDefinitionsPrice.setOnClickListener(this);
			 ivWordDefinitionsPurchased.setVisibility(View.GONE);
		 }
		 
		 
		 SkuDetails skuDoubleTime = inventory.getSkuDetails(this.getString(R.string.SKU_GOOGLE_PLAY_DOUBLE_TIME));
		 tvDoubleTimeTitle.setText(this.getString(R.string.store_item_double_time_title));
		 tvDoubleTimeDescription.setText(this.getString(R.string.store_item_double_time_description));
		 if (inventory.hasPurchase(this.getString(R.string.SKU_GOOGLE_PLAY_DOUBLE_TIME)) || inventory.hasPurchase(this.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE))) {
			 bDoubleTimePrice.setVisibility(View.GONE); 
			 }
		 else{
			 bDoubleTimePrice.setText(skuDoubleTime.getPrice());
			 bDoubleTimePrice.setTag(this.getString(R.string.SKU_GOOGLE_PLAY_DOUBLE_TIME));
			 bDoubleTimePrice.setOnClickListener(this);
			 ivDoubleTimePurchased.setVisibility(View.GONE);
		 }
		 
		 
		 SkuDetails skuSpeedRounds = inventory.getSkuDetails(this.getString(R.string.SKU_GOOGLE_PLAY_SPEED_ROUNDS));
		 tvSpeedRoundsTitle.setText(this.getString(R.string.store_item_speed_rounds_title));
		 tvSpeedRoundsDescription.setText(this.getString(R.string.store_item_speed_rounds_description));
		 if (inventory.hasPurchase(this.getString(R.string.SKU_GOOGLE_PLAY_SPEED_ROUNDS)) || inventory.hasPurchase(this.getString(R.string.SKU_GOOGLE_PLAY_PREMIUM_UPGRADE))) {
			 bSpeedRoundsPrice.setVisibility(View.GONE); 
			 }
		 else{
			 bSpeedRoundsPrice.setText(skuSpeedRounds.getPrice());
			 bSpeedRoundsPrice.setTag(this.getString(R.string.SKU_GOOGLE_PLAY_SPEED_ROUNDS));
			 bSpeedRoundsPrice.setOnClickListener(this);
			 ivSpeedRoundsPurchased.setVisibility(View.GONE);
		 }
	 }
	 
 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		this.purchaseItem(v.getTag().toString());
 
	}
	
	private void setupFonts(){
		TextView tvPremiumUpgradeTitle = (TextView)findViewById(R.id.tvPremiumUpgradeTitle);	
		TextView tvPremiumUpgradeDescription = (TextView)findViewById(R.id.tvPremiumUpgradeDescription);	
		TextView tvNoAdsTitle = (TextView)findViewById(R.id.tvNoAdsTitle);
		TextView tvNoAdsDescription = (TextView)findViewById(R.id.tvNoAdsDescription);
		TextView tvWordDefinitionsTitle = (TextView)findViewById(R.id.tvWordDefinitionsTitle);
		TextView tvWordDefinitionsDescription = (TextView)findViewById(R.id.tvWordDefinitionsDescription);
		TextView tvSpeedRoundsTitle = (TextView)findViewById(R.id.tvSpeedRoundsTitle);
		TextView tvSpeedRoundsDescription = (TextView)findViewById(R.id.tvSpeedRoundsDescription);

		TextView tvDoubleTimeTitle = (TextView)findViewById(R.id.tvDoubleTimeTitle);
		TextView tvDoubleTimeDescription = (TextView)findViewById(R.id.tvDoubleTimeDescription);

		tvPremiumUpgradeTitle.setTypeface(ApplicationContext.getMainFontTypeface());
		tvPremiumUpgradeDescription.setTypeface(ApplicationContext.getMainFontTypeface());
		tvNoAdsTitle.setTypeface(ApplicationContext.getMainFontTypeface());
		tvNoAdsDescription.setTypeface(ApplicationContext.getMainFontTypeface());
		tvWordDefinitionsTitle.setTypeface(ApplicationContext.getMainFontTypeface());
		tvWordDefinitionsDescription.setTypeface(ApplicationContext.getMainFontTypeface());
		tvDoubleTimeTitle.setTypeface(ApplicationContext.getMainFontTypeface());
		tvDoubleTimeDescription.setTypeface(ApplicationContext.getMainFontTypeface());

		tvSpeedRoundsTitle.setTypeface(ApplicationContext.getMainFontTypeface());
		tvSpeedRoundsDescription.setTypeface(ApplicationContext.getMainFontTypeface());

		
		
		Button bPremiumUpgradePrice = (Button)findViewById(R.id.bPremiumUpgradePrice);
		Button bNoAdsPrice = (Button)findViewById(R.id.bNoAdsPrice);
		Button bWordDefinitionsPrice = (Button)findViewById(R.id.bWordDefinitionsPrice);
		Button bDoubleTimePrice = (Button)findViewById(R.id.bDoubleTimePrice);
		Button bSpeedRoundsPrice = (Button)findViewById(R.id.bSpeedRoundsPrice);
 		
		bPremiumUpgradePrice.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
		bNoAdsPrice.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
		bWordDefinitionsPrice.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
		bDoubleTimePrice.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());

		bSpeedRoundsPrice.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
	 	
	}
	

}
