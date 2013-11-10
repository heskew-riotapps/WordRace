package com.riotapps.wordrace.hooks;

import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.riotapps.wordbase.R;
import com.riotapps.wordrace.hooks.Game;
import com.riotapps.wordbase.hooks.Opponent;
import com.riotapps.wordbase.hooks.OpponentService;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordrace.data.GameData;

public class GameService {
	
	public static Game getGame(String gameId){
		return GameData.getGame(gameId);
	}

	public static void loadScoreboard(final FragmentActivity context, Game game){
		
		 TextView tvPlayerScore = (TextView)context.findViewById(R.id.tvPlayerScore);
		 TextView tvOpponentName = (TextView)context.findViewById(R.id.tvOpponentName);
		 TextView tvOpponentScore = (TextView)context.findViewById(R.id.tvOpponentScore);
		 ImageView ivOpponent = (ImageView)context.findViewById(R.id.ivOpponent);
		 
		 TextView tvPlayerName = (TextView)context.findViewById(R.id.tvPlayerName);
		 TextView tvLettersLeft = (TextView)context.findViewById(R.id.tvLettersLeft);
		 TextView tvNumPoints = (TextView)context.findViewById(R.id.tvNumPoints);
		 
		 tvPlayerScore.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvOpponentName.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvOpponentScore.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvPlayerName.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvLettersLeft.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvNumPoints.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	 
		 tvPlayerScore.setText(String.valueOf(game.getPlayerScore()));
		 tvOpponentScore.setText(String.valueOf(game.getOpponentScore())); 
		 
		 Opponent o = OpponentService.getOpponent(game.getOpponentId());
		 
		 tvOpponentName.setText(o.getName()); 
		 
		 int opponentImageId = context.getResources().getIdentifier(context.getString(R.string.namespace) + ":drawable/" + o.getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_SMALL), null, null);
		 ivOpponent.setImageResource(opponentImageId);

		 
		 if (game.getHopper().size() == 1){
			 tvLettersLeft.setText(R.string.scoreboard_1_letter_left); 
		 }
		 else{
			 tvLettersLeft.setText(String.format(context.getString(R.string.scoreboard_letters_left), game.getHopper().size()));
		 }

	 }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
