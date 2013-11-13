package com.riotapps.wordrace.hooks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.riotapps.wordbase.hooks.PlayedTile;
import com.riotapps.wordbase.hooks.PlayedTurn;
import com.riotapps.wordbase.hooks.PlayedWord;
import com.riotapps.wordbase.hooks.PlayerGame;

 
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<PlayedWord> getPlayedWords() {
		return playedWords;
	}

	public void setPlayedWords(List<PlayedWord> playedWords) {
		this.playedWords = playedWords;
	}

	public List<String> getHopper() {
		return hopper;
	}

	public void setHopper(List<String> hopper) {
		this.hopper = hopper;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getOpponentId() {
		return opponentId;
	}

	public void setOpponentId(int opponentId) {
		this.opponentId = opponentId;
	}

	public int getOpponentScore() {
		return opponentScore;
	}

	public void setOpponentScore(int opponentScore) {
		this.opponentScore = opponentScore;
	}

	public int getPlayerScore() {
		return playerScore;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setPlayerScore(int playerScore) {
		this.playerScore = playerScore;
	}
	
	public boolean isCompleted(){
		return this.status == 3 || this.getStatus() == 4;
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
	}
	
}
