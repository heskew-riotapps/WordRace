package com.riotapps.wordrace;

import com.riotapps.wordrace.hooks.GameService;

import android.os.Bundle;

public class GameLookup extends com.riotapps.wordbase.GameLookup {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void loadGame(String gameId){
		 GameService.loadScoreboard(this, GameService.getGame(gameId));
	}
	
}
