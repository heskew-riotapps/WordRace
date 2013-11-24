package com.riotapps.wordrace.hooks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import com.riotapps.wordbase.hooks.GameListItem;
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
	private static final String TAG = GameService.class.getSimpleName();
	
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
		 TextView tvNumPoints = (TextView)context.findViewById(R.id.tvNumPoints);
		 
		 tvPlayerScore.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvOpponentName.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvOpponentScore.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvPlayerName.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvWordsLeft.setTypeface(ApplicationContext.getScoreboardFontTypeface());
		 tvNumPoints.setTypeface(ApplicationContext.getScoreboardFontTypeface());
	 
	 
		 tvPlayerScore.setText(String.valueOf(game.getPlayerScore()));
		 tvOpponentScore.setText(String.valueOf(game.getOpponentScore())); 
		 
		 Opponent o = OpponentService.getOpponent(game.getOpponentId());
		 
		 tvOpponentName.setText(o.getName()); 
		 
		 int opponentImageId = context.getResources().getIdentifier(context.getString(R.string.namespace) + ":drawable/" + o.getDrawableByMode(Constants.OPPONENT_IMAGE_MODE_SMALL), null, null);
		 ivOpponent.setImageResource(opponentImageId);

		 tvWordsLeft.setText(String.format(context.getString(com.riotapps.wordrace.R.string.scoreboard_words_left), game.getNumPossibleWords() - game.getPlayedWords().size()));

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
    	game.setNumPossibleWords(0);
    	
    	contextPlayer.setActiveGameId(game.getId());
    	PlayerService.savePlayer(contextPlayer);
    	saveGame(game);
    	
    	
    	return game;
    	 
	}
	
	public static void completeGame(Game game){
		 
		Player player = PlayerService.getPlayer();
    	//add 1 to opponent's wins, save opponent 
    	//add 1 to player's losses, save player
    	player.setActiveGameId(Constants.EMPTY_STRING);
    	
    	if (game.getPlayerScore() > game.getOpponentScore()){
        	player.setNumWins(player.getNumWins() + 1);
        	game.getOpponent().addLossToRecord();   		
    	}
    	else if (game.getOpponentScore() > game.getPlayerScore()){
        	player.setNumLosses(player.getNumLosses() + 1);
        	game.getOpponent().addWinToRecord();
    	}
    	else {
        	player.setNumDraws(player.getNumDraws() + 1);
        	game.getOpponent().addDrawToRecord();
    	}

    	PlayerService.savePlayer(player);
    	OpponentService.saveOpponentRecord(game.getOpponentId(), game.getOpponent().getRecord());
    	 
    	game.setStatus(3); //sets up enum for game status and playerGame status
     	
    	saveGame(game);
    	addGameToCompletedList(game);
				
	}
	
	public static void saveGame(Game game){
		//game.getPlayerGames().get(1).getPlacedResults().clear();
		GameData.saveGame(game);
	}	
	
	public static void startGame(Game game){
		game.setStatus(5);
		saveGame(game);
	}
	
	public static void removeGame(String gameId){
		 
		GameData.removeGame(gameId);
 	}
	public static void cancel(Player player, Game game){
		
		//Check.Require(game.getTurn() == 1, "can only cancel game on first turn");
		removeGame(game.getId());
		player.setActiveGameId("");
		PlayerService.savePlayer(player);
	}

	public static List<List<String>> getSortedRaceLetterSets(List<String> raceLetters){
		List<List<String>> letterSets = new ArrayList<List<String>>();
		
		letterSets.addAll(getSortedRaceLetterSetsByLength(raceLetters, 3));
		letterSets.addAll(getSortedRaceLetterSetsByLength(raceLetters, 4));
		letterSets.addAll(getSortedRaceLetterSetsByLength(raceLetters, 5));
		letterSets.addAll(getSortedRaceLetterSetsByLength(raceLetters, 6));
 		letterSets.addAll(getSortedRaceLetterSetsByLength(raceLetters, 7));
		letterSets.addAll(getSortedRaceLetterSetsByLength(raceLetters, 8));
 		letterSets.addAll(getSortedRaceLetterSetsByLength(raceLetters, 9));
 		letterSets.addAll(getSortedRaceLetterSetsByLength(raceLetters, 10));
		
		return letterSets;
	
	}
	public static List<List<String>> getSortedRaceLetterSetsByLength(List<String> raceLetters, int setLength){
		 //grab all unique sets of tray letters in batches of "setLength"
		 //so if the tray letters are P, R, T, A, L, B, Y and the setLength is 4
		 //return P, R, T, A...P, R, T, L,...P, R, T, B...etc
		 List<List<String>> letterSets = new ArrayList<List<String>>();
		 
		 Collections.sort(raceLetters);
	
		 //we only need 9 indexes here because if a set length of 10 is requested
		 //all letters are returned as the only eligible letter set
		 int x1 = 0;
		 int x2 = 1;
		 int x3 = 2;
		 int x4 = 3;
		 int x5 = 4;
		 int x6 = 5;
		 int x7 = 6;
		 int x8 = 7; 
		 int x9 = 8;
	//	 int x10 = 9;
	//	 int x11 = 10;
	
		 
		 int size = raceLetters.size();
		 int maxIndex = size - 1;
	
		 
		 if (size < setLength){
			return letterSets; 
		 }
		 else if (size == setLength){
			 //return all x letters as the single set to be returned
			 List<String> letterSet = new ArrayList<String>();
				
				for (int j = 0; j < setLength; j ++){
					letterSet.add(raceLetters.get(j));	/////sort this here???? if so update login in PlayerGame
				}
			Collections.sort(letterSet);
			letterSets.add(letterSet);
			return letterSets;
		 }
		 else if (setLength == 1){
			 //return all x letters as the x number of sets 
	
				for (int j = 0; j <= maxIndex; j ++){
					List<String> letterSet = new ArrayList<String>();
					letterSet.add(raceLetters.get(j));
					letterSets.add(letterSet);
				}
	
			return letterSets;
		 }
		 else{
			 boolean notAtEnd = true;
			 while (notAtEnd){
				 	//the idea here is to gather every combination of positions (not every single permutation of letters)
				 	//at the end we will ensure that duplicate tile sets are not added and sort them for index lookup
				 
					List<String> letterSet = new ArrayList<String>();
						 
					letterSet.add(raceLetters.get(x1));	
					if (setLength >= 2) { letterSet.add(raceLetters.get(x2)); }
					if (setLength >= 3) { letterSet.add(raceLetters.get(x3)); }
					if (setLength >= 4) { letterSet.add(raceLetters.get(x4)); }
					if (setLength >= 5) { letterSet.add(raceLetters.get(x5)); }
					if (setLength >= 6) { letterSet.add(raceLetters.get(x6)); }
					if (setLength >= 7) { letterSet.add(raceLetters.get(x7)); }
					if (setLength >= 8) { letterSet.add(raceLetters.get(x8)); }
					if (setLength == 9) { letterSet.add(raceLetters.get(x9)); }
		//			if (setLength == 10) { letterSet.add(raceLetters.get(x10)); }
		//			if (setLength == 11) { letterSet.add(raceLetters.get(x11)); }
					
		/*			if (setLength == 11 && x11 < (maxIndex - (setLength - 11))){
						x11 += 1;
					}
					else if (setLength == 10 && x10 < (maxIndex - (setLength - 10))){
						x10 += 1;
						x11 = x10+1;
					}*/
					if (setLength == 9 && x9 < (maxIndex - (setLength - 9))){
						x9 += 1;
					//	x10 = x9+1;
					//	x11 = x9+2;
						}
					else if (setLength >= 8 && x8 < (maxIndex - (setLength - 8))){
						x8 += 1;
						x9 = x8+1;
					//	x10 = x8+2;
					//	x11 = x8+3;
					}
					else if (setLength >= 7 && x7 < (maxIndex - (setLength - 7))){
						x7 += 1;
						x8 = x7+1;
						x9 = x7+2;
				//		x10 = x7+3;
				//		x11 = x7+4;
						}
					else if (setLength >= 6 && x6 < (maxIndex - (setLength - 6))){
						x6 += 1;
						x7 = x6+1;
						x8 = x6+2;
						x9 = x6+3;
					//	x10 = x6+4;
					//	x11 = x6+5;
					}
					else if(setLength >= 5 && x5 < (maxIndex - (setLength - 5))){
						x5 += 1;
						x6 = x5+1;
						x7 = x5+2;
						x8 = x5+3;
						x9 = x5+4;
					//	x10 = x5+5;
					//	x11 = x5+6;
					}
					else if(setLength >= 4 && x4 < (maxIndex - (setLength - 4))){
						x4 += 1;
						x5 = x4+1;
						x6 = x4+2;
						x7 = x4+3;
						x8 = x4+4;
						x9 = x4+5;
					//	x10 = x4+6;
					//	x11 = x4+7;
					}	
					else if(setLength >= 3 && x3 < (maxIndex - (setLength - 3))){
						x3 += 1;
						x4 = x3+1;
						x5 = x3+2;
						x6 = x3+3;
						x7 = x3+4;
						x8 = x3+5;
						x9 = x3+6;
					//	x10 = x3+7;
					//	x11 = x3+8;
					}
				 	else if(setLength >= 2 && x2 < (maxIndex - (setLength - 2))){
				 		x2 += 1;
				 		x3 = x2+1;
						x4 = x2+2;
						x5 = x2+3;
						x6 = x2+4;
						x7 = x2+5;
						x8 = x2+6;
						x9 = x2+7;
					//	x10 = x2+8;
					//	x11 = x2+9;
				 	}
				 	else if(x1 < (maxIndex - (setLength - 1))){
				 		x1 += 1;
				 		//reset other indexes
				 		x2 = x1+1;
						x3 = x1+2;
						x4 = x1+3;
						x5 = x1+4;
						x6 = x1+5;
						x7 = x1+6;
						x8 = x1+7;
						x9 = x1+8;
					//	x10 = x1+9;
					//	x11 = x1+10;
				 	}
				 	else {
				 		//nothing more to do, let's get out of this loopy logic
				 		notAtEnd = false;
				 	}
					
					//we have filled up this letterSet
					if (letterSet.size() == setLength){
						//check for dupes here
						
						/*
						if (setLength == 3){
							String a = "";
							
							for (String x : letterSet){
								a += x;
							}
							
							 Logger.d(TAG, "getSortedTrayLetterSets letterSet pre sort=" + a );
						}
						*/
						
						
						//sort for index lookup
						Collections.sort(letterSet);
						
						String newSet = "";
						for (String s : letterSet){
							newSet += s;
						}
						
						boolean exactSetMatch = false; //letterSets.size() > 0; //this will make exactSetMatch false for the first set
						for (int i = 0; i < letterSets.size(); i++){ // 
							String set = "";
							for (String s : letterSets.get(i)){
								set += s;
							}
							
							if (newSet.equals(set)){
								exactSetMatch = true;
								break;
							}
							
							/*
							boolean loopMatch = false;
							for (int j = 0; j < setLength; j++){ // 
		 						
								if (letterSets.get(i).get(j).equals(letterSet.get(j))){ 
								}
								else{
									exactSetMatch = false; 
									break;
								}	
				 			}
				 			*/
						}
						
						//only add it to collection if its not a complete duplicate of another sorted set
						//these sets will be used for index lookup, which is why they are sorted
						if (!exactSetMatch) {letterSets.add(letterSet);}
					}
			 }
			 
			 return letterSets;
		 }
	}
	
  	
	public static void addGameToCompletedList(Game game){
		 List<GameListItem> games = GameData.getCompletedGameList();
		 
		 Logger.d(TAG, "addGameToCompletedList size=" + games.size() + " " + Constants.NUM_LOCAL_COMPLETED_GAMES_TO_STORE);
		 
		 //put game at the beginning so that it saves in descending order
		 games.add(0, new  GameListItem(game.getId(), game.getCreateDate()));
		 
		 //only store 10 games in this list.  clean out game storage and list for 11 and above
		 if (games.size() > Constants.NUM_LOCAL_COMPLETED_GAMES_TO_STORE){
			 for (int i = games.size() - 1; i > Constants.NUM_LOCAL_COMPLETED_GAMES_TO_STORE - 1; i--){
				 
				 removeGame(games.get(i).getGameId());
				 games.remove(i);
			 }
			 
		 }
		 Logger.d(TAG, "addGameToCompletedList size=" + games.size());
		 
		 GameData.saveCompletedGameList(games);
	}
	
	public static List<Game> getCompletedGames(){
		 List<GameListItem> games = GameData.getCompletedGameList();
		 List<Game> gameList = new ArrayList<Game>();
		 
		 for (GameListItem gli : games){
			 try{
				 gameList.add(getGame(gli.getGameId()));
			 }
			 catch (Exception e){
				 //game was likely missing for some reason
			 }
		 }
 		 
		 return gameList;
	}
	
}
