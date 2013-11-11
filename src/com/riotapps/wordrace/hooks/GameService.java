package com.riotapps.wordrace.hooks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.riotapps.wordbase.R;
import com.riotapps.wordrace.hooks.Game;
import com.riotapps.wordbase.hooks.AlphabetService;
import com.riotapps.wordbase.hooks.Opponent;
import com.riotapps.wordbase.hooks.OpponentService;
import com.riotapps.wordbase.hooks.PlayedTurn;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.hooks.PlayerGame;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Check;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.DesignByContractException;
import com.riotapps.wordbase.utils.Logger;
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
		 TextView tvWordsLeft = (TextView)context.findViewById(com.riotapps.wordrace.R.id.tvWordsLeft);
	 
		 
		 tvPlayerScore.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvOpponentName.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvOpponentScore.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvPlayerName.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvWordsLeft.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	 
	 
		 tvPlayerScore.setText(String.valueOf(game.getPlayerScore()));
		 tvOpponentScore.setText(String.valueOf(game.getOpponentScore())); 
		 
		 Opponent o = OpponentService.getOpponent(game.getOpponentId());
		 
		 tvOpponentName.setText(o.getName()); 
		 
		 int opponentImageId = context.getResources().getIdentifier(context.getString(R.string.namespace) + ":drawable/" + o.getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_SMALL), null, null);
		 ivOpponent.setImageResource(opponentImageId);

		 
		 if (game.getHopper().size() == 1){
	 		 tvWordsLeft.setText(com.riotapps.wordrace.R.string.scoreboard_1_word_left); 
		 }
		 else{
			 tvWordsLeft.setText(String.format(context.getString(com.riotapps.wordrace.R.string.scoreboard_words_left), game.getNumPossibleWords() - game.getPlayedWords().size()));
		 }

	 }
	
	public static Game createGame(Context ctx, Player contextPlayer, int opponentId) throws DesignByContractException{
		//Check.Require(PlayerService.getPlayerFromLocal().getId().equals(contextPlayer.getId()), ctx.getString(R.string.validation_incorrect_context_player));
    	Check.Require(contextPlayer.getActiveGameId().length() == 0, ctx.getString(R.string.validation_create_game_duplicate));
		
		
		Game game = new Game();
    	
		DateFormat df = new SimpleDateFormat("MMddyyyyHHmmss", Locale.US);
		Date now = Calendar.getInstance().getTime();        
	 
		//ported from rails
		game.setId(df.format(now));
    	game.setCreateDate(new Date());
    	game.setStatus(1); //active
    	game.setOpponentScore(0);
    	game.setPlayerScore(0);
    	game.setOpponentId(opponentId);
    	game.setHopper(AlphabetService.getRaceLetters());
     	
    	//determine how many total words will be available
    	game.setNumPossibleWords(300);
    	
    	contextPlayer.setActiveGameId(game.getId());
    	PlayerService.savePlayer(contextPlayer);
    	saveGame(game);
    	
    	
    	return game;
    	 
	}
	
	public static void saveGame(Game game){
		//game.getPlayerGames().get(1).getPlacedResults().clear();
		GameData.saveGame(game);
	}	
}
