package com.riotapps.wordrace.hooks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.riotapps.wordbase.hooks.Opponent;
import com.riotapps.wordbase.hooks.OpponentService;
import com.riotapps.wordbase.hooks.PlayedTile;
import com.riotapps.wordbase.hooks.PlayedTurn;
import com.riotapps.wordbase.hooks.PlayedWord;
import com.riotapps.wordbase.hooks.PlayerGame;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.Utils;
import com.riotapps.wordrace.R;

 
public class Game {
	private static final String TAG = Game.class.getSimpleName();

	@SerializedName("id")
	private String id = "";
	
	@SerializedName("played_words")
	private List<PlayedWord> playedWords = new ArrayList<PlayedWord>();
	
	@SerializedName("hop")
	private List<String> hopper;
	
	@SerializedName("cr_d")
	private Date createDate = new Date(0);   

	@SerializedName("opp")
	private int opponentId;
	
	@SerializedName("oppSc")
	private int opponentScore;
	
	@SerializedName("plySc")
	private int playerScore;
	
	@SerializedName("st")
	private int status;
	
	@SerializedName("n_w")
	private int numPossibleWords;
	
	@SerializedName("g_t")
	private int gameType = 0;

	@SerializedName("r")
	private int round = 1;

	@SerializedName("cd")
	private long countdown = 0;
	
	@SerializedName("wn")
	private int winNum = 0;
	
	private boolean isDirty = false;
	
	
	private Opponent opponent;

	public Opponent getOpponent(){
		if (opponent == null){
			opponent = OpponentService.getOpponent(this.opponentId);
		}
		return opponent;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.isDirty = true;
	}

	public List<PlayedWord> getPlayedWords() {
		return playedWords;
	}

	public void setPlayedWords(List<PlayedWord> playedWords) {
		this.playedWords = playedWords;
		this.isDirty = true;
	}

	public List<String> getHopper() {
		return hopper;
	}

	public void setHopper(List<String> hopper) {
		this.hopper = hopper;
		this.isDirty = true;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
		this.isDirty = true;
	}

	public int getOpponentId() {
		return opponentId;
	}

	public void setOpponentId(int opponentId) {
		this.opponentId = opponentId;
		this.isDirty = true;
	}

	public int getOpponentScore() {
		return opponentScore;
	}

	public void setOpponentScore(int opponentScore) {
		this.opponentScore = opponentScore;
		this.isDirty = true;
	}

	public int getPlayerScore() {
		return playerScore;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		this.isDirty = true;
	}

	public void setPlayerScore(int playerScore) {
		this.playerScore = playerScore;
		this.isDirty = true;
	}
	
	public boolean isCompleted(){
		return this.status == 3;
	}
	
	public boolean isActive(){
		return this.status == 1;
	}

	public boolean isStarted(){
		return this.status == 5;
	}
	public int getNumPossibleWords() {
		return numPossibleWords;
	}

	public void setNumPossibleWords(int numPossibleWords) {
		this.numPossibleWords = numPossibleWords;
		this.isDirty = true;
	}
	public int getGameType() {
		return gameType;
	}
	public void setGameType(int gameType) {
		this.gameType = gameType;
		this.isDirty = true;
	}
	
	public boolean isOriginalType()
	{
		return this.gameType == Constants.RACE_GAME_TYPE_90_SECOND_DASH;
	}
	
	public boolean isSpeedRoundType()
	{
		return this.gameType == Constants.RACE_GAME_TYPE_SPEED_ROUNDS;
	}
	
	public boolean isDoubleTimeType()
	{
		return this.gameType == Constants.RACE_GAME_TYPE_DOUBLE_TIME;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
		this.isDirty = true;
	}
	public long getCountdown() {
		return countdown;
	}
	public void setCountdown(long countdown) {
		this.countdown = countdown;
		this.isDirty = true;
	}
	public String getLastActionText(Context context){
		String timeSince = Utils.getTimeSinceString(context, this.getCreateDate());
		 
		Logger.d(TAG, "getLastActionText game=" + this.id);
		
		if (this.getOpponentScore() > this.getPlayerScore()){
			return String.format(context.getString(R.string.game_opponent_winner_message), timeSince, this.getOpponent().getName(), this.getOpponentScore(), this.getPlayerScore());
		}
		else if(this.getPlayerScore() > this.getOpponentScore()){
			return String.format(context.getString(R.string.game_player_winner_message), timeSince, this.getPlayerScore(), this.getOpponentScore());
		}
		else {
			return String.format(context.getString(R.string.game_draw_message), timeSince, this.getPlayerScore(), this.getOpponentScore());
		}	 
	}
	
	
	
	public int getWinNum() {
		return winNum;
	}
	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}
	public boolean isDirty() {
		return isDirty;
	}
	
	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}
	
	public String getTypeTitle(Context context){
		if (this.isOriginalType()) { return context.getString(R.string.game_surface_dash_title); }
		else if (this.isSpeedRoundType()){ return context.getString(R.string.game_surface_speed_rounds_title); }
		else { return context.getString(R.string.game_surface_double_time_title); }
 	}
}
