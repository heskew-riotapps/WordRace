package com.riotapps.wordrace;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class FullRules extends FragmentActivity{
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.fullrules);
	        
	        TextView t2 = (TextView) findViewById(R.id.tvFullRulesBasics_2);
	        t2.setMovementMethod(LinkMovementMethod.getInstance());
	        
	        TextView tvFullRulesDictionary2 = (TextView) findViewById(R.id.tvFullRulesDictionary_2);
	        tvFullRulesDictionary2.setMovementMethod(LinkMovementMethod.getInstance());

			PlayerService.loadPlayerInHeader(this);  
	        MenuUtils.hideMenu(this);
	        MenuUtils.setHeaderTitle(this, this.getString(R.string.header_title_rules));
	        
	        this.setupFonts();
	        
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
		 EasyTracker.getInstance().activityStart(this);
	}


	@Override
	protected void onStop() {
	 
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	} 
	
	private void setupFonts(){
	
		TextView tvFullRulesBasics_1 = (TextView)findViewById(R.id.tvFullRulesBasics_1);	
		TextView tvFullRulesBasics_2 = (TextView)findViewById(R.id.tvFullRulesBasics_2);
		TextView tvFullRulesBasics_3 = (TextView)findViewById(R.id.tvFullRulesBasics_3);
		TextView tvFullRulesBasics_4 = (TextView)findViewById(R.id.tvFullRulesBasics_4);
		TextView tvFullRulesBasics_5 = (TextView)findViewById(R.id.tvFullRulesBasics_5);
		TextView tvFullRulesBasics_6 = (TextView)findViewById(R.id.tvFullRulesBasics_6);
 		TextView tvFullRulesScoring_1 = (TextView)findViewById(R.id.tvFullRulesScoring_1);
		TextView tvFullRulesScoring_2 = (TextView)findViewById(R.id.tvFullRulesScoring_2);
 		TextView tvFullRulesLetters_1_value = (TextView)findViewById(R.id.tvFullRulesLetters_1_value);
		TextView tvFullRulesLetters_2_value = (TextView)findViewById(R.id.tvFullRulesLetters_2_value);
		TextView tvFullRulesLetters_3_value = (TextView)findViewById(R.id.tvFullRulesLetters_3_value);
		TextView tvFullRulesLetters_4_value = (TextView)findViewById(R.id.tvFullRulesLetters_4_value);
		TextView tvFullRulesLetters_5_value = (TextView)findViewById(R.id.tvFullRulesLetters_5_value);
		TextView tvFullRulesLetters_6_value = (TextView)findViewById(R.id.tvFullRulesLetters_6_value);
		TextView tvFullRulesLetters_7_value = (TextView)findViewById(R.id.tvFullRulesLetters_7_value);
		TextView tvFullRulesLetters_8_value = (TextView)findViewById(R.id.tvFullRulesLetters_8_value);
		TextView tvFullRulesLetters_9_value = (TextView)findViewById(R.id.tvFullRulesLetters_9_value);
		TextView tvFullRulesLetters_10_value = (TextView)findViewById(R.id.tvFullRulesLetters_10_value);
		TextView tvFullRulesLetters_11_value = (TextView)findViewById(R.id.tvFullRulesLetters_11_value);
		TextView tvFullRulesLetters_12_value = (TextView)findViewById(R.id.tvFullRulesLetters_12_value);
		TextView tvFullRulesLetters_13_value = (TextView)findViewById(R.id.tvFullRulesLetters_13_value);
		TextView tvFullRulesLetters_14_value = (TextView)findViewById(R.id.tvFullRulesLetters_14_value);
		TextView tvFullRulesLetters_15_value = (TextView)findViewById(R.id.tvFullRulesLetters_15_value);
		TextView tvFullRulesLetters_16_value = (TextView)findViewById(R.id.tvFullRulesLetters_16_value);
		TextView tvFullRulesLetters_17_value = (TextView)findViewById(R.id.tvFullRulesLetters_17_value);
		TextView tvFullRulesLetters_18_value = (TextView)findViewById(R.id.tvFullRulesLetters_18_value);
		TextView tvFullRulesLetters_19_value = (TextView)findViewById(R.id.tvFullRulesLetters_19_value);
		TextView tvFullRulesLetters_20_value = (TextView)findViewById(R.id.tvFullRulesLetters_20_value);
		TextView tvFullRulesLetters_21_value = (TextView)findViewById(R.id.tvFullRulesLetters_21_value);
		TextView tvFullRulesLetters_22_value = (TextView)findViewById(R.id.tvFullRulesLetters_22_value);
		TextView tvFullRulesLetters_23_value = (TextView)findViewById(R.id.tvFullRulesLetters_23_value);
		TextView tvFullRulesLetters_24_value = (TextView)findViewById(R.id.tvFullRulesLetters_24_value);
		TextView tvFullRulesLetters_25_value = (TextView)findViewById(R.id.tvFullRulesLetters_25_value);
		TextView tvFullRulesLetters_26_value = (TextView)findViewById(R.id.tvFullRulesLetters_26_value);
  		TextView tvFullRulesDictionary_1 = (TextView)findViewById(R.id.tvFullRulesDictionary_1);
		TextView tvFullRulesDictionary_2 = (TextView)findViewById(R.id.tvFullRulesDictionary_2);
	
	 	tvFullRulesBasics_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_2.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_3.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_4.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_5.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesBasics_6.setTypeface(ApplicationContext.getMainFontTypeface());

	 	tvFullRulesScoring_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesScoring_2.setTypeface(ApplicationContext.getMainFontTypeface());
	 	tvFullRulesLetters_1_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_2_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_3_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_4_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_5_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_6_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_7_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_8_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_9_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_10_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_11_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_12_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_13_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_14_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_15_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_16_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_17_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_18_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_19_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_20_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_21_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_22_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_23_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_24_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_25_value.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesLetters_26_value.setTypeface(ApplicationContext.getMainFontTypeface());
	 	tvFullRulesDictionary_1.setTypeface(ApplicationContext.getMainFontTypeface());
		tvFullRulesDictionary_2.setTypeface(ApplicationContext.getMainFontTypeface());
	}
}
