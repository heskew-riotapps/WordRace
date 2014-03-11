package com.riotapps.wordrace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TimerTask;
import java.util.TreeSet;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.riotapps.wordrace.R;
import com.riotapps.wordrace.hooks.GameService;
import com.riotapps.wordbase.utils.NetworkConnectivity;
import com.riotapps.wordbase.hooks.AlphabetService;
import com.riotapps.wordbase.hooks.PlayedWord;
import com.riotapps.wordbase.hooks.Player;
import com.riotapps.wordbase.hooks.PlayerService;
import com.riotapps.wordbase.hooks.StoreService;
import com.riotapps.wordbase.hooks.WordService;
import com.riotapps.wordbase.interfaces.ICloseDialog;
import com.riotapps.wordbase.ui.CustomButtonDialog;
import com.riotapps.wordbase.ui.CustomProgressDialog;
import com.riotapps.wordbase.ui.CustomToast;
import com.riotapps.wordbase.ui.DialogManager;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.DesignByContractException;
import com.riotapps.wordbase.utils.ImageHelper;
import com.riotapps.wordbase.utils.IntentExtra;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.Utils;
import com.riotapps.wordrace.hooks.Game;
import com.riotapps.wordrace.ui.CreateGameDialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class GameSurface  extends FragmentActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ICloseDialog{
	private static final String TAG = GameSurface.class.getSimpleName();
	
	 private PopupMenu popupMenu;
	 private String gameId = ""; 
	 private CreateGameDialog createGameDialog;
	 private Tracker tracker;
	 private Player player;
	 private Game game;
	 private boolean hideInterstitialAd;
	 private boolean isChartBoostActive;
	 private boolean isAdMobActive;
	 private Chartboost cb;
	 private CountDownTimer countdown = null;
	 private TextView tvShortInfo;
	 private TextView tvCountdown;
	 private TextView tvNumPoints;
	 private TextView tvWinner;
	 private TextView tvLookupDefinitions;
	 private TextView tvWordsLeft;
	 private TextView tvOpponentScore;
	 private TextView tvPlayerScore;
	 private ListView lvOpponent;
	 private ListView lvPlayer;
	 private PlayedWordAdapter playerListadapter;
	 private PlayedWordAdapter opponentListadapter;
	 private boolean isCompletedThisSession = false;
	 private LinearLayout llButtons;
	 private LinearLayout llAdWrapper;
	 private List<OpponentWord> opponentWords = new ArrayList<OpponentWord>();
	 private boolean isCountdownRunning = false;
	 private boolean freezeAction = false;
 
	 private boolean hasPostAdRun = false;
	private	LayoutInflater inflater;

	 private CustomButtonDialog customDialog = null; 
	private List<String> possibleWords = new ArrayList<String>();
	private PreLoadTask preloadTask = null;
	private int letterTileSize;
	private int playedLetterTileSize;
	private AutoPlayTask autoPlayTask;
	
	private SortedSet<String> wordsPlayedByOpponent = new TreeSet<String>();
	private SortedSet<String> wordsPlayedByPlayer = new TreeSet<String>();
	
	private List<PlayedWord> wordListPlayedByOpponent = new ArrayList<PlayedWord>();
	private List<PlayedWord> wordListPlayedByPlayer = new ArrayList<PlayedWord>();
	
	private CustomProgressDialog spinner;
	 private static Bitmap trayLetter_A;
	 private static Bitmap trayLetter_B;
	 private static Bitmap trayLetter_C;
	 private static Bitmap trayLetter_D;
	 private static Bitmap trayLetter_E;
	 private static Bitmap trayLetter_F;
	 private static Bitmap trayLetter_G;
	 private static Bitmap trayLetter_H;
	 private static Bitmap trayLetter_I;
	 private static Bitmap trayLetter_J;
	 private static Bitmap trayLetter_K;
	 private static Bitmap trayLetter_L;
	 private static Bitmap trayLetter_M;
	 private static Bitmap trayLetter_N;
	 private static Bitmap trayLetter_O;
	 private static Bitmap trayLetter_P;
	 private static Bitmap trayLetter_Q;
	 private static Bitmap trayLetter_R;
	 private static Bitmap trayLetter_S;
	 private static Bitmap trayLetter_T;
	 private static Bitmap trayLetter_U;
	 private static Bitmap trayLetter_V;
	 private static Bitmap trayLetter_W;
	 private static Bitmap trayLetter_X;
	 private static Bitmap trayLetter_Y;
	 private static Bitmap trayLetter_Z;
	 private static Bitmap playedLetter_A;
	 private static Bitmap playedLetter_B;
	 private static Bitmap playedLetter_C;
	 private static Bitmap playedLetter_D;
	 private static Bitmap playedLetter_E;
	 private static Bitmap playedLetter_F;
	 private static Bitmap playedLetter_G;
	 private static Bitmap playedLetter_H;
	 private static Bitmap playedLetter_I;
	 private static Bitmap playedLetter_J;
	 private static Bitmap playedLetter_K;
	 private static Bitmap playedLetter_L;
	 private static Bitmap playedLetter_M;
	 private static Bitmap playedLetter_N;
	 private static Bitmap playedLetter_O;
	 private static Bitmap playedLetter_P;
	 private static Bitmap playedLetter_Q;
	 private static Bitmap playedLetter_R;
	 private static Bitmap playedLetter_S;
	 private static Bitmap playedLetter_T;
	 private static Bitmap playedLetter_U;
	 private static Bitmap playedLetter_V;
	 private static Bitmap playedLetter_W;
	 private static Bitmap playedLetter_X;
	 private static Bitmap playedLetter_Y;
	 private static Bitmap playedLetter_Z;
	 private static Bitmap trayEmpty;
	 private static Bitmap playedEmpty;
	 private static Bitmap playedEmpty_6;
	 private static Bitmap playedEmpty_8;
	 private static Bitmap playedEmpty_10;
	 
	 private ImageView ivPlayedLetter1;
	 private ImageView ivPlayedLetter2;
	 private ImageView ivPlayedLetter3;
	 private ImageView ivPlayedLetter4;
	 private ImageView ivPlayedLetter5;
	 private ImageView ivPlayedLetter6;
	 private ImageView ivPlayedLetter7;
	 private ImageView ivPlayedLetter8;
	 private ImageView ivPlayedLetter9;
	 private ImageView ivPlayedLetter10;

	 private ImageView bLetter1;
	 private ImageView bLetter2;
	 private ImageView bLetter3;
	 private ImageView bLetter4;
	 private ImageView bLetter5;
	 private ImageView bLetter6;
	 private ImageView bLetter7;
	 private ImageView bLetter8;
	 private ImageView bLetter9;
	 private ImageView bLetter10;	

	 private List<String> hopper = new ArrayList<String>();
	 private List<String> currentWord = new ArrayList<String>();
	 
	 private static Bitmap getTrayLetter_A(Context context, int tileSize){
		if (GameSurface.trayLetter_A == null){
			GameSurface.trayLetter_A = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_a, tileSize, tileSize);
		}
		return GameSurface.trayLetter_A;
	 }

	 private static Bitmap getTrayLetter_B(Context context, int tileSize){
		if (GameSurface.trayLetter_B == null){
			GameSurface.trayLetter_B = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_b, tileSize, tileSize);
		}
		return GameSurface.trayLetter_B;
	 }

	 private static Bitmap getTrayLetter_C(Context context, int tileSize){
		if (GameSurface.trayLetter_C == null){
			GameSurface.trayLetter_C = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_c, tileSize, tileSize);
			}
		return GameSurface.trayLetter_C;
     }
	
	 private static Bitmap getTrayLetter_D(Context context, int tileSize){
			if (GameSurface.trayLetter_D == null){
				GameSurface.trayLetter_D = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_d, tileSize, tileSize);
			}
			return GameSurface.trayLetter_D;
	 }
	 private static Bitmap getTrayLetter_E(Context context, int tileSize){
			if (GameSurface.trayLetter_E == null){
				GameSurface.trayLetter_E = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_e, tileSize, tileSize);
			}
			return GameSurface.trayLetter_E;
	 }
	 private static Bitmap getTrayLetter_F(Context context, int tileSize){
			if (GameSurface.trayLetter_F == null){
				GameSurface.trayLetter_F = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_f, tileSize, tileSize);
			}
			return GameSurface.trayLetter_F;
	}
	 private static Bitmap getTrayLetter_G(Context context, int tileSize){
			if (GameSurface.trayLetter_G == null){
				GameSurface.trayLetter_G = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_g, tileSize, tileSize);
			}
			return GameSurface.trayLetter_G;
	 }
	 private static Bitmap getTrayLetter_H(Context context, int tileSize){
			if (GameSurface.trayLetter_H == null){
				GameSurface.trayLetter_H = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_h, tileSize, tileSize);
			}
			return GameSurface.trayLetter_H;
	 }
	 private static Bitmap getTrayLetter_I(Context context, int tileSize){
			if (GameSurface.trayLetter_I == null){
				GameSurface.trayLetter_I = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_i, tileSize, tileSize);
			}
			return GameSurface.trayLetter_I;
	 }
	 private static Bitmap getTrayLetter_J(Context context, int tileSize){
			if (GameSurface.trayLetter_J == null){
				GameSurface.trayLetter_J = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_j, tileSize, tileSize);
			}
			return GameSurface.trayLetter_J;
	 }
	 private static Bitmap getTrayLetter_K(Context context, int tileSize){
			if (GameSurface.trayLetter_K == null){
				GameSurface.trayLetter_K = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_k, tileSize, tileSize);
			}
			return GameSurface.trayLetter_K;
	}
	 private static Bitmap getTrayLetter_L(Context context, int tileSize){
			if (GameSurface.trayLetter_L == null){
				GameSurface.trayLetter_L = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_l, tileSize, tileSize);
			}
			return GameSurface.trayLetter_L;
	 }
	 private static Bitmap getTrayLetter_M(Context context, int tileSize){
			if (GameSurface.trayLetter_M == null){
				GameSurface.trayLetter_M = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_m, tileSize, tileSize);
			}
			return GameSurface.trayLetter_M;
	 }
	 private static Bitmap getTrayLetter_N(Context context, int tileSize){

			if (GameSurface.trayLetter_N == null){
				GameSurface.trayLetter_N = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_n, tileSize, tileSize);
			}
			return GameSurface.trayLetter_N;
	 }
	 private static Bitmap getTrayLetter_O(Context context, int tileSize){
			if (GameSurface.trayLetter_O == null){
				GameSurface.trayLetter_O = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_o, tileSize, tileSize);
			}
			return GameSurface.trayLetter_O;
	 }
	 private static Bitmap getTrayLetter_P(Context context, int tileSize){
			if (GameSurface.trayLetter_P == null){
				GameSurface.trayLetter_P = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_p, tileSize, tileSize);
				//GameSurface.trayLetter_P = BitmapFactory.decodeResource(context.getResources(), Utils.getResourceId(context, context.getString(R.string.tray_letter_file_P)));
			}
			return GameSurface.trayLetter_P;
		}
	 private static Bitmap getTrayLetter_Q(Context context, int tileSize){
			if (GameSurface.trayLetter_Q == null){
				GameSurface.trayLetter_Q = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_q, tileSize, tileSize);
			}
			return GameSurface.trayLetter_Q;
	 }
	 private static Bitmap getTrayLetter_R(Context context, int tileSize){
			if (GameSurface.trayLetter_R == null){
				GameSurface.trayLetter_R = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_r, tileSize, tileSize);
			}
			return GameSurface.trayLetter_R;
	 }
	 private static Bitmap getTrayLetter_S(Context context, int tileSize){
			if (GameSurface.trayLetter_S == null){
				GameSurface.trayLetter_S = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_s, tileSize, tileSize);
			}
			return GameSurface.trayLetter_S;
	 }
	 private static Bitmap getTrayLetter_T(Context context, int tileSize){
			if (GameSurface.trayLetter_T == null){
				GameSurface.trayLetter_T = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_t, tileSize, tileSize);
			}
			return GameSurface.trayLetter_T;
	 }
	 private static Bitmap getTrayLetter_U(Context context, int tileSize){
			if (GameSurface.trayLetter_U == null){
				GameSurface.trayLetter_U = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_u, tileSize, tileSize);
			}
			return GameSurface.trayLetter_U;
	 }
	 private static Bitmap getTrayLetter_V(Context context, int tileSize){
		 	if (GameSurface.trayLetter_V == null){
				GameSurface.trayLetter_V = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_v, tileSize, tileSize);
			}
			return GameSurface.trayLetter_V;
	 }
	 private static Bitmap getTrayLetter_W(Context context, int tileSize){
		 	if (GameSurface.trayLetter_W == null){
				GameSurface.trayLetter_W = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_w, tileSize, tileSize);
			}
			return GameSurface.trayLetter_W;
	 }
	 private static Bitmap getTrayLetter_X(Context context, int tileSize){
		 	if (GameSurface.trayLetter_X == null){
				GameSurface.trayLetter_X = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_x, tileSize, tileSize);
			}
			return GameSurface.trayLetter_X;
	 }
	 private static Bitmap getTrayLetter_Y(Context context, int tileSize){
			if (GameSurface.trayLetter_Y == null){
				GameSurface.trayLetter_Y = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_y, tileSize, tileSize);
			}
			return GameSurface.trayLetter_Y;
		}
	 private static Bitmap getTrayLetter_Z(Context context, int tileSize){
			if (GameSurface.trayLetter_Z == null){
				GameSurface.trayLetter_Z = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_letter_z, tileSize, tileSize);
			}
			return GameSurface.trayLetter_Z;
		}
	 private static Bitmap getTrayEmpty(Context context, int tileSize){
			if (GameSurface.trayEmpty == null){
				GameSurface.trayEmpty = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.tray_tile_empty_bg, tileSize, tileSize);
			}
			return GameSurface.trayEmpty;
		}
	 
	 
	 private static Bitmap getPlayedLetter_A(Context context, int tileSize){
			if (GameSurface.playedLetter_A == null){
				GameSurface.playedLetter_A = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_a, tileSize, tileSize);
			}
			return GameSurface.playedLetter_A;
		 }

		 private static Bitmap getPlayedLetter_B(Context context, int tileSize){
			if (GameSurface.playedLetter_B == null){
				GameSurface.playedLetter_B = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_b, tileSize, tileSize);
			}
			return GameSurface.playedLetter_B;
		 }

		 private static Bitmap getPlayedLetter_C(Context context, int tileSize){
			if (GameSurface.playedLetter_C == null){
				GameSurface.playedLetter_C = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_c, tileSize, tileSize);
				}
			return GameSurface.playedLetter_C;
	     }
		
		 private static Bitmap getPlayedLetter_D(Context context, int tileSize){
				if (GameSurface.playedLetter_D == null){
					GameSurface.playedLetter_D = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_d, tileSize, tileSize);
				}
				return GameSurface.playedLetter_D;
		 }
		 private static Bitmap getPlayedLetter_E(Context context, int tileSize){
				if (GameSurface.playedLetter_E == null){
					GameSurface.playedLetter_E = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_e, tileSize, tileSize);
				}
				return GameSurface.playedLetter_E;
		 }
		 private static Bitmap getPlayedLetter_F(Context context, int tileSize){
				if (GameSurface.playedLetter_F == null){
					GameSurface.playedLetter_F = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_f, tileSize, tileSize);
				}
				return GameSurface.playedLetter_F;
		}
		 private static Bitmap getPlayedLetter_G(Context context, int tileSize){
				if (GameSurface.playedLetter_G == null){
					GameSurface.playedLetter_G = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_g, tileSize, tileSize);
				}
				return GameSurface.playedLetter_G;
		 }
		 private static Bitmap getPlayedLetter_H(Context context, int tileSize){
				if (GameSurface.playedLetter_H == null){
					GameSurface.playedLetter_H = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_h, tileSize, tileSize);
				}
				return GameSurface.playedLetter_H;
		 }
		 private static Bitmap getPlayedLetter_I(Context context, int tileSize){
				if (GameSurface.playedLetter_I == null){
					GameSurface.playedLetter_I = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_i, tileSize, tileSize);
				}
				return GameSurface.playedLetter_I;
		 }
		 private static Bitmap getPlayedLetter_J(Context context, int tileSize){
				if (GameSurface.playedLetter_J == null){
					GameSurface.playedLetter_J = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_j, tileSize, tileSize);
				}
				return GameSurface.playedLetter_J;
		 }
		 private static Bitmap getPlayedLetter_K(Context context, int tileSize){
				if (GameSurface.playedLetter_K == null){
					GameSurface.playedLetter_K = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_k, tileSize, tileSize);
				}
				return GameSurface.playedLetter_K;
		}
		 private static Bitmap getPlayedLetter_L(Context context, int tileSize){
				if (GameSurface.playedLetter_L == null){
					GameSurface.playedLetter_L = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_l, tileSize, tileSize);
				}
				return GameSurface.playedLetter_L;
		 }
		 private static Bitmap getPlayedLetter_M(Context context, int tileSize){
				if (GameSurface.playedLetter_M == null){
					GameSurface.playedLetter_M = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_m, tileSize, tileSize);
				}
				return GameSurface.playedLetter_M;
		 }
		 private static Bitmap getPlayedLetter_N(Context context, int tileSize){

				if (GameSurface.playedLetter_N == null){
					GameSurface.playedLetter_N = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_n, tileSize, tileSize);
				}
				return GameSurface.playedLetter_N;
		 }
		 private static Bitmap getPlayedLetter_O(Context context, int tileSize){
				if (GameSurface.playedLetter_O == null){
					GameSurface.playedLetter_O = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_o, tileSize, tileSize);
				}
				return GameSurface.playedLetter_O;
		 }
		 private static Bitmap getPlayedLetter_P(Context context, int tileSize){
				if (GameSurface.playedLetter_P == null){
					GameSurface.playedLetter_P = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_p, tileSize, tileSize);
					//GameSurface.playedLetter_P = BitmapFactory.decodeResource(context.getResources(), Utils.getResourceId(context, context.getString(R.string.played_letter_file_P)));
				}
				return GameSurface.playedLetter_P;
			}
		 private static Bitmap getPlayedLetter_Q(Context context, int tileSize){
				if (GameSurface.playedLetter_Q == null){
					GameSurface.playedLetter_Q = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_q, tileSize, tileSize);
				}
				return GameSurface.playedLetter_Q;
		 }
		 private static Bitmap getPlayedLetter_R(Context context, int tileSize){
				if (GameSurface.playedLetter_R == null){
					GameSurface.playedLetter_R = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_r, tileSize, tileSize);
				}
				return GameSurface.playedLetter_R;
		 }
		 private static Bitmap getPlayedLetter_S(Context context, int tileSize){
				if (GameSurface.playedLetter_S == null){
					GameSurface.playedLetter_S = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_s, tileSize, tileSize);
				}
				return GameSurface.playedLetter_S;
		 }
		 private static Bitmap getPlayedLetter_T(Context context, int tileSize){
				if (GameSurface.playedLetter_T == null){
					GameSurface.playedLetter_T = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_t, tileSize, tileSize);
				}
				return GameSurface.playedLetter_T;
		 }
		 private static Bitmap getPlayedLetter_U(Context context, int tileSize){
				if (GameSurface.playedLetter_U == null){
					GameSurface.playedLetter_U = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_u, tileSize, tileSize);
				}
				return GameSurface.playedLetter_U;
		 }
		 private static Bitmap getPlayedLetter_V(Context context, int tileSize){
			 	if (GameSurface.playedLetter_V == null){
					GameSurface.playedLetter_V = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_v, tileSize, tileSize);
				}
				return GameSurface.playedLetter_V;
		 }
		 private static Bitmap getPlayedLetter_W(Context context, int tileSize){
			 	if (GameSurface.playedLetter_W == null){
					GameSurface.playedLetter_W = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_w, tileSize, tileSize);
				}
				return GameSurface.playedLetter_W;
		 }
		 private static Bitmap getPlayedLetter_X(Context context, int tileSize){
			 	if (GameSurface.playedLetter_X == null){
					GameSurface.playedLetter_X = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_x, tileSize, tileSize);
				}
				return GameSurface.playedLetter_X;
		 }
		 private static Bitmap getPlayedLetter_Y(Context context, int tileSize){
				if (GameSurface.playedLetter_Y == null){
					GameSurface.playedLetter_Y = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_y, tileSize, tileSize);
				}
				return GameSurface.playedLetter_Y;
			}
		 private static Bitmap getPlayedLetter_Z(Context context, int tileSize){
				if (GameSurface.playedLetter_Z == null){
					GameSurface.playedLetter_Z = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_letter_z, tileSize, tileSize);
				}
				return GameSurface.playedLetter_Z;
		 }
	 private static Bitmap getPlayedEmpty(Context context, int tileSize){
			if (GameSurface.playedEmpty == null){
				GameSurface.playedEmpty = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_tile_empty_bg, tileSize, tileSize);
			}
			return GameSurface.playedEmpty;
		}
/*
	 private static Bitmap getPlayedEmpty_6(Context context, int tileSize){
			if (GameSurface.playedEmpty_6 == null){
				GameSurface.playedEmpty_6 = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_tile_empty_6_bg, tileSize, tileSize);
			}
			return GameSurface.playedEmpty_6;
		}
	 private static Bitmap getPlayedEmpty_8(Context context, int tileSize){
			if (GameSurface.playedEmpty_8 == null){
				GameSurface.playedEmpty_8 = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_tile_empty_8_bg, tileSize, tileSize);
			}
			return GameSurface.playedEmpty_8;
		}
	 private static Bitmap getPlayedEmpty_10(Context context, int tileSize){
			if (GameSurface.playedEmpty_10 == null){
				GameSurface.playedEmpty_10 = ImageHelper.decodeSampledBitmapFromResource(context.getResources(), R.drawable.played_tile_empty_10_bg, tileSize, tileSize);
			}
			return GameSurface.playedEmpty_10;
		}
	*/ 
	 
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
		
		Logger.d(TAG, "onCreate called");
		this.handleKickoff();
		
		this.setupAdServer();
		this.checkFirstTimeStatus();
	 //	this.setupPreloadTask();
	}
	
	 private void checkFirstTimeStatus(){
		 //first check to see if this score has already been alerted (from local storage) 
		 
		 if (!PlayerService.checkFirstTimeGameSurfaceAlertAlreadyShown(this)) {
			 DialogManager.SetupAlert(this, this.getString(R.string.game_surface_first_time_alert_title), this.getString(R.string.game_surface_first_time_alert_message));
		 }
		 
	 }
	private void setStartState(){
		//if the game was stopped before completion
		
		//if game was stopped before countdown started
		if (this.game.getCountdown() == 0 && this.game.isStarted()){
			this.game.setCountdown(this.getTimerStart());
		}

		Logger.d(TAG, "setStartState game.isStarted() " + this.game.isStarted() + " type=" + this.game.isSpeedRoundType() + " countdown=" + this.game.getCountdown() + " round=" + this.game.getRound());
		if (this.game.getCountdown() == 0 && this.game.isSpeedRoundType() & (this.game.getRound() == 2 || this.game.getRound() == 3)){
			this.game.setCountdown(this.getResources().getInteger(R.integer.gameTypeSpeedRoundsCountdownStart));
		}
		Logger.d(TAG, "setStartState game.isStarted() " + this.game.isStarted() + " type=" + this.game.isSpeedRoundType() + " countdown=" + this.game.getCountdown() + " round=" + this.game.getRound());

		if (this.game.isStarted() && this.game.getCountdown() > 0){
			this.setCountdown(this.game.getCountdown());
		}
	}
	
	 
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		//override back button in case user just started game. this will make sure they don;t back through 
		//all of the pick opponent activities
		Logger.d(TAG, "onBackPressed called");
/*
		if (this.countdown != null){
			this.countdown.cancel();
			this.countdown = null;
		}
	
		if (this.preloadTask != null){
			this.preloadTask = null;
		}

		if (this.autoPlayTask != null){
			this.autoPlayTask = null;
		}
*/
		
		if (this.isChartBoostActive && this.cb.onBackPressed())
			// If a Chartboost view exists, close it and return
			return;
		else if (this.game.isCompleted() && !this.isCompletedThisSession){
			//if game is completed, just go back to whatever activity is in the stack
			 
	//		this.game = null;
	//		this.player = null;

			super.onBackPressed();
		}
		else {
			this.handleBack();
		}
	}
	
	private void handleBack(){
		boolean backToMain = false;
		if (this.game.isCompleted()) {
			backToMain = true;
		}
		/*
		else {
			GameService.saveGame(this.game);
		}
		//disable back if started??? or just come back to proper state?
		
		if (this.autoPlayTask != null){
    		this.autoPlayTask = null;
    	}
		
		if (this.preloadTask != null){
			this.preloadTask = null;
		}
		
		this.game = null;
		this.player = null;
		*/
		if (backToMain){
			//in this case the game was completed in this session so lets just send player to main activity
			((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_MAIN);

			//Intent intent = new Intent(this, com.riotapps.wordbase.Main.class);
    		//this.startActivity(intent); 
    		this.finish();
		}	
		else{
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
			this.finish(); 	
		}
	
}

	private void loadButtons(){
		Button bStart = (Button)this.findViewById(R.id.bStart);
		Button bCancel = (Button)this.findViewById(R.id.bCancel);
		Button bShuffle = (Button)this.findViewById(R.id.bShuffle);
		Button bRecall = (Button)this.findViewById(R.id.bRecall);
		Button bPlay = (Button)this.findViewById(R.id.bPlay);
		this.llAdWrapper = (LinearLayout)this.findViewById(R.id.llAdWrapper);
		this.llButtons = (LinearLayout)this.findViewById(R.id.llButtons);
		
		bStart.setOnClickListener(this);
		bCancel.setOnClickListener(this);
		bShuffle.setOnClickListener(this);
		bRecall.setOnClickListener(this);
		bPlay.setOnClickListener(this);
		
		llAdWrapper.setVisibility(View.GONE);
		
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
			bStart.setVisibility(View.GONE);
			bCancel.setVisibility(View.GONE);
			bShuffle.setVisibility(View.GONE);
			bRecall.setVisibility(View.GONE);
			bPlay.setVisibility(View.GONE);
			
			llButtons.setVisibility(View.GONE);
			llAdWrapper.setVisibility(View.VISIBLE);
			
			AdView adView = (AdView)this.findViewById(R.id.adView);
	    	if (StoreService.isHideBannerAdsPurchased(this)){	
				adView.setVisibility(View.GONE);
			}
	    	else {
	    		adView.loadAd(new AdRequest());
	    	}
		}
	}
	
	 private void dismissCreateGameDialog(){
			if (this.createGameDialog != null){
				createGameDialog.dismiss();
				createGameDialog = null;
			}
		}

	
	private void setCompletedState(){
		if (this.game.isCompleted()){
			this.tvShortInfo.setVisibility(View.VISIBLE);
			 this.tvShortInfo.setText(this.getString(R.string.game_surface_game_over));
			 this.tvNumPoints.setVisibility(View.INVISIBLE);
 		 
			 Button bRematch = (Button) this.findViewById(R.id.bRematch);
			 bRematch.setVisibility(View.VISIBLE);
			 bRematch.setText(String.format(this.getString(R.string.game_surface_rematch), this.game.getOpponent().getName()));
			 bRematch.setClickable(true);
			 bRematch.setOnClickListener(this);
			 this.tvCountdown.setVisibility(View.GONE);
	 	 
			 
			// GameSurface.this.runOnUiThread(new handleCountdownViewState(0));
			 LinearLayout llPlayedWord = (LinearLayout) this.findViewById(R.id.llPlayedWord);
			 RelativeLayout llGameComplete = (RelativeLayout) this.findViewById(R.id.llGameComplete);
			 llPlayedWord.setVisibility(View.GONE);
			 llGameComplete.setVisibility(View.VISIBLE);
			 
			/*
			this.ivPlayedLetter1.setVisibility(View.GONE);
			this.ivPlayedLetter2.setVisibility(View.GONE);
			this.ivPlayedLetter3.setVisibility(View.GONE);
			this.ivPlayedLetter4.setVisibility(View.GONE);
			this.ivPlayedLetter5.setVisibility(View.GONE);
			this.ivPlayedLetter6.setVisibility(View.GONE);
			this.ivPlayedLetter7.setVisibility(View.GONE);
			this.ivPlayedLetter8.setVisibility(View.GONE);
			this.ivPlayedLetter9.setVisibility(View.GONE);
			this.ivPlayedLetter10.setVisibility(View.GONE);
			
			this.tvLookupDefinitions.setVisibility(View.VISIBLE);
			this.tvWinner.setVisibility(View.VISIBLE);
			*/
			if (this.game.getOpponentScore() > this.game.getPlayerScore()){
				this.tvWinner.setText(String.format(this.getString(R.string.game_surface_opponent_winner_message), this.game.getOpponent().getName()));
			}
			else if(this.game.getPlayerScore() > this.game.getOpponentScore()){
				this.tvWinner.setText(String.format(this.getString(R.string.game_surface_player_winner_message), this.game.getWinNum()));
			}
			else {
				this.tvWinner.setText(this.getString(R.string.game_surface_draw_message));
			}	 
			
			this.llButtons.setVisibility(View.GONE);
			this.llAdWrapper.setVisibility(View.VISIBLE);
			
			AdView adView = (AdView)this.findViewById(R.id.adView);
			View vBottomFill = (View)this.findViewById(R.id.vBottomFill);
	 
	    	if (StoreService.isHideBannerAdsPurchased(this)){	
				adView.setVisibility(View.GONE);
				vBottomFill.setVisibility(View.VISIBLE);  
			}
	    	else {
	    		NetworkConnectivity connection = new NetworkConnectivity(this);
	    		boolean isConnected = connection.checkNetworkConnectivity();
	    		
	    		if (isConnected){
	    			vBottomFill.setVisibility(View.GONE); 
	    			adView.loadAd(new AdRequest());
	    			
	    		}
	    		else{
	    			adView.setVisibility(View.GONE);
	    			vBottomFill.setVisibility(View.VISIBLE);
	    		}
	    	}
			
	    	this.initializeHopper();
			this.initializeTray();
			this.setListenerOnLists();
		}
	}
	
	private void loadViews(){
		this.inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.tvShortInfo = (TextView)this.findViewById(R.id.tvShortInfo);
		this.tvCountdown = (TextView)this.findViewById(R.id.tvCountdown);
		this.tvNumPoints = (TextView)this.findViewById(R.id.tvNumPoints);
		this.tvLookupDefinitions = (TextView)this.findViewById(R.id.tvLookupDefinitions);
		this.tvWinner = (TextView)this.findViewById(R.id.tvWinner);
		this.tvWordsLeft = (TextView)this.findViewById(R.id.tvWordsLeft);
		this.tvOpponentScore = (TextView)this.findViewById(R.id.tvOpponentScore);
		this.tvPlayerScore = (TextView)this.findViewById(R.id.tvPlayerScore);
		this.lvOpponent = (ListView)this.findViewById(R.id.lvOpponent);
		this.lvPlayer = (ListView)this.findViewById(R.id.lvPlayer);
		TextView tvType = (TextView)this.findViewById(R.id.tvType);
		Button bRematch = (Button)this.findViewById(R.id.bRematch);
		bRematch.setVisibility(View.GONE);
		tvType.setText(this.game.getTypeTitle(this));
		 
		
		this.tvCountdown.setText(this.getString(R.string.scoreboard_countdown_start));
		if (this.game.isSpeedRoundType()){
			this.tvCountdown.setText(this.getString(R.string.scoreboard_countdown_speed_round_start));
		}
		else if (this.game.isDoubleTimeType()){
			this.tvCountdown.setText(this.getString(R.string.scoreboard_countdown_double_time_start));
		}
		
		TextView tvOpponentWordListTitle = (TextView)this.findViewById(R.id.tvOpponentWordListTitle);
		tvOpponentWordListTitle.setText(String.format(this.getString(R.string.game_surface_opponents_words), this.game.getOpponent().getName()));
		//this.llPlayerWords = (LinearLayout)this.findViewById(R.id.llPlayerWords);
		
	//	this.tvLookupDefinitions.setVisibility(View.GONE);
	//	this.tvWinner.setVisibility(View.GONE);
		
		this.bLetter1 = (ImageView) findViewById(R.id.bLetter1);
		this.bLetter2 = (ImageView) findViewById(R.id.bLetter2);
		this.bLetter3 = (ImageView) findViewById(R.id.bLetter3);
		this.bLetter4 = (ImageView) findViewById(R.id.bLetter4);
		this.bLetter5 = (ImageView) findViewById(R.id.bLetter5);
		this.bLetter6 = (ImageView) findViewById(R.id.bLetter6);
		this.bLetter7 = (ImageView) findViewById(R.id.bLetter7);
		this.bLetter8 = (ImageView) findViewById(R.id.bLetter8);
		this.bLetter9 = (ImageView) findViewById(R.id.bLetter9);
		this.bLetter10 = (ImageView) findViewById(R.id.bLetter10);

		this.ivPlayedLetter1 = (ImageView) findViewById(R.id.ivPlayedLetter1);
		this.ivPlayedLetter2 = (ImageView) findViewById(R.id.ivPlayedLetter2);
		this.ivPlayedLetter3 = (ImageView) findViewById(R.id.ivPlayedLetter3);
		this.ivPlayedLetter4 = (ImageView) findViewById(R.id.ivPlayedLetter4);
		this.ivPlayedLetter5 = (ImageView) findViewById(R.id.ivPlayedLetter5);
		this.ivPlayedLetter6 = (ImageView) findViewById(R.id.ivPlayedLetter6);
		this.ivPlayedLetter7 = (ImageView) findViewById(R.id.ivPlayedLetter7);
		this.ivPlayedLetter8 = (ImageView) findViewById(R.id.ivPlayedLetter8);
		this.ivPlayedLetter9 = (ImageView) findViewById(R.id.ivPlayedLetter9);
		this.ivPlayedLetter10 = (ImageView) findViewById(R.id.ivPlayedLetter10);

	}
	
	private void loadLetterViews(){
		Display display = getWindowManager().getDefaultDisplay();
	    Point size = new Point();
	 	display.getSize(size);
	 	 display.getSize(size);
		int fullWidth = size.x;

		this.playedLetterTileSize = Math.round(fullWidth / 11.00f);
		this.letterTileSize = Math.round(fullWidth / 5.80f);	
	 	int maxTrayTileSize = this.getResources().getInteger(R.integer.maxTrayTileSize);
		if (letterTileSize > maxTrayTileSize){letterTileSize = maxTrayTileSize;} 
		
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
		
		this.initializeTray();
		
		bLetter1.setOnClickListener(this);
		bLetter2.setOnClickListener(this);
		bLetter3.setOnClickListener(this);
		bLetter4.setOnClickListener(this);
		bLetter5.setOnClickListener(this);
		bLetter6.setOnClickListener(this);
		bLetter7.setOnClickListener(this);
		bLetter8.setOnClickListener(this);
		bLetter9.setOnClickListener(this);
		bLetter10.setOnClickListener(this);
		
		ViewGroup.LayoutParams params_p1 = ivPlayedLetter1.getLayoutParams();
		params_p1.width = playedLetterTileSize;
		params_p1.height = playedLetterTileSize;
		ivPlayedLetter1.setLayoutParams(params_p1);
		
		ViewGroup.LayoutParams params_p2 = ivPlayedLetter2.getLayoutParams();
		params_p2.width = playedLetterTileSize;
		params_p2.height = playedLetterTileSize;
		ivPlayedLetter2.setLayoutParams(params_p2);
		
		ViewGroup.LayoutParams params_p3 = ivPlayedLetter3.getLayoutParams();
		params_p3.width = playedLetterTileSize;
		params_p3.height = playedLetterTileSize;
		ivPlayedLetter3.setLayoutParams(params_p3);

		ViewGroup.LayoutParams params_p4 = ivPlayedLetter4.getLayoutParams();
		params_p4.width = playedLetterTileSize;
		params_p4.height = playedLetterTileSize;
		ivPlayedLetter4.setLayoutParams(params_p4);
		
		ViewGroup.LayoutParams params_p5 = ivPlayedLetter5.getLayoutParams();
		params_p5.width = playedLetterTileSize;
		params_p5.height = playedLetterTileSize;
		ivPlayedLetter5.setLayoutParams(params_p5);

		ViewGroup.LayoutParams params_p6 = ivPlayedLetter6.getLayoutParams();
		params_p6.width = playedLetterTileSize;
		params_p6.height = playedLetterTileSize;
		ivPlayedLetter6.setLayoutParams(params_p6);

		ViewGroup.LayoutParams params_p7 = ivPlayedLetter7.getLayoutParams();
		params_p7.width = playedLetterTileSize;
		params_p7.height = playedLetterTileSize;
		ivPlayedLetter7.setLayoutParams(params_p7);

		ViewGroup.LayoutParams params_p8 = ivPlayedLetter8.getLayoutParams();
		params_p8.width = playedLetterTileSize;
		params_p8.height = playedLetterTileSize;
		ivPlayedLetter8.setLayoutParams(params_p8);

		ViewGroup.LayoutParams params_p9 = ivPlayedLetter9.getLayoutParams();
		params_p9.width = playedLetterTileSize;
		params_p9.height = playedLetterTileSize;
		ivPlayedLetter9.setLayoutParams(params_p9);

		ViewGroup.LayoutParams params_p10 = ivPlayedLetter10.getLayoutParams();
		params_p10.width = playedLetterTileSize;
		params_p10.height = playedLetterTileSize;
		ivPlayedLetter10.setLayoutParams(params_p10);

		ivPlayedLetter1.setOnClickListener(this);
		ivPlayedLetter2.setOnClickListener(this);
		ivPlayedLetter3.setOnClickListener(this);
		ivPlayedLetter4.setOnClickListener(this);
		ivPlayedLetter5.setOnClickListener(this);
		ivPlayedLetter6.setOnClickListener(this);
		ivPlayedLetter7.setOnClickListener(this);
		ivPlayedLetter8.setOnClickListener(this);
		ivPlayedLetter9.setOnClickListener(this);
		ivPlayedLetter10.setOnClickListener(this);
		
		this.initializePlayedTiles();
	}
	
	
	private static Bitmap getTrayLetterImage(Context context, String letter, int tileSize){
		 
		//resize bitmap before loading
		if (letter.equals("A")){
			return GameSurface.getTrayLetter_A(context, tileSize);
		}
		else if (letter.equals("B")){
			return GameSurface.getTrayLetter_B(context, tileSize);
		}
		else if (letter.equals("C")){
			return GameSurface.getTrayLetter_C(context, tileSize);
		}
		else if (letter.equals("D")){
			return GameSurface.getTrayLetter_D(context, tileSize);
		}
		else if (letter.equals("E")){
			return GameSurface.getTrayLetter_E(context, tileSize);
		}
		else if (letter.equals("F")){
			return GameSurface.getTrayLetter_F(context, tileSize);
		}
		else if (letter.equals("G")){
			return GameSurface.getTrayLetter_G(context, tileSize);
		}
		else if (letter.equals("H")){
			return GameSurface.getTrayLetter_H(context, tileSize);
		}
		else if (letter.equals("I")){
			return GameSurface.getTrayLetter_I(context, tileSize);
		}
		else if (letter.equals("J")){
			return GameSurface.getTrayLetter_J(context, tileSize);
		}
		else if (letter.equals("K")){
			return GameSurface.getTrayLetter_K(context, tileSize);
		}
		else if (letter.equals("L")){
			return GameSurface.getTrayLetter_L(context, tileSize);
		}
		else if (letter.equals("M")){
			return GameSurface.getTrayLetter_M(context, tileSize);
		}
		else if (letter.equals("N")){
			return GameSurface.getTrayLetter_N(context, tileSize);
		}
		else if (letter.equals("O")){
			return GameSurface.getTrayLetter_O(context, tileSize);
		}
		else if (letter.equals("P")){
			return GameSurface.getTrayLetter_P(context, tileSize);
		}
		else if (letter.equals("Q")){
			return GameSurface.getTrayLetter_Q(context, tileSize);
		}
		else if (letter.equals("R")){
			return GameSurface.getTrayLetter_R(context, tileSize);
		}
		else if (letter.equals("S")){
			return GameSurface.getTrayLetter_S(context, tileSize);
		}
		else if (letter.equals("T")){
			return GameSurface.getTrayLetter_T(context, tileSize);
		}
		else if (letter.equals("U")){
			return GameSurface.getTrayLetter_U(context, tileSize);
		}
		else if (letter.equals("V")){
			return GameSurface.getTrayLetter_V(context, tileSize);
		}
		else if (letter.equals("W")){
			return GameSurface.getTrayLetter_W(context, tileSize);
		}
		else if (letter.equals("X")){
			return GameSurface.getTrayLetter_X(context, tileSize);
		}
		else if (letter.equals("Y")){
			return GameSurface.getTrayLetter_Y(context, tileSize);
		}
		else if (letter.equals("Z")){
			return GameSurface.getTrayLetter_Z(context, tileSize);
		}
		else {
			return GameSurface.getTrayEmpty(context, tileSize);
		}

 	}
	
	private static Bitmap getPlayedLetterImage(Context context, String letter, int tileSize, int position){
 		
		if (letter.equals("A")){
			return GameSurface.getPlayedLetter_A(context, tileSize);
		}
		else if (letter.equals("B")){
			return GameSurface.getPlayedLetter_B(context, tileSize);
		}
		else if (letter.equals("C")){
			return GameSurface.getPlayedLetter_C(context, tileSize);
		}
		else if (letter.equals("D")){
			return GameSurface.getPlayedLetter_D(context, tileSize);
		}
		else if (letter.equals("E")){
			return GameSurface.getPlayedLetter_E(context, tileSize);
		}
		else if (letter.equals("F")){
			return GameSurface.getPlayedLetter_F(context, tileSize);
		}
		else if (letter.equals("G")){
			return GameSurface.getPlayedLetter_G(context, tileSize);
		}
		else if (letter.equals("H")){
			return GameSurface.getPlayedLetter_H(context, tileSize);
		}
		else if (letter.equals("I")){
			return GameSurface.getPlayedLetter_I(context, tileSize);
		}
		else if (letter.equals("J")){
			return GameSurface.getPlayedLetter_J(context, tileSize);
		}
		else if (letter.equals("K")){
			return GameSurface.getPlayedLetter_K(context, tileSize);
		}
		else if (letter.equals("L")){
			return GameSurface.getPlayedLetter_L(context, tileSize);
		}
		else if (letter.equals("M")){
			return GameSurface.getPlayedLetter_M(context, tileSize);
		}
		else if (letter.equals("N")){
			return GameSurface.getPlayedLetter_N(context, tileSize);
		}
		else if (letter.equals("O")){
			return GameSurface.getPlayedLetter_O(context, tileSize);
		}
		else if (letter.equals("P")){
			return GameSurface.getPlayedLetter_P(context, tileSize);
		}
		else if (letter.equals("Q")){
			return GameSurface.getPlayedLetter_Q(context, tileSize);
		}
		else if (letter.equals("R")){
			return GameSurface.getPlayedLetter_R(context, tileSize);
		}
		else if (letter.equals("S")){
			return GameSurface.getPlayedLetter_S(context, tileSize);
		}
		else if (letter.equals("T")){
			return GameSurface.getPlayedLetter_T(context, tileSize);
		}
		else if (letter.equals("U")){
			return GameSurface.getPlayedLetter_U(context, tileSize);
		}
		else if (letter.equals("V")){
			return GameSurface.getPlayedLetter_V(context, tileSize);
		}
		else if (letter.equals("W")){
			return GameSurface.getPlayedLetter_W(context, tileSize);
		}
		else if (letter.equals("X")){
			return GameSurface.getPlayedLetter_X(context, tileSize);
		}
		else if (letter.equals("Y")){
			return GameSurface.getPlayedLetter_Y(context, tileSize);
		}
		else if (letter.equals("Z")){
			return GameSurface.getPlayedLetter_Z(context, tileSize);
		}
		else {
			return GameSurface.getPlayedEmpty(context, tileSize);
		}

 	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Logger.d(TAG, "onResume called");
	 
		super.onResume();
		
	//	this.handleKickoff();
 	 	this.setupPreloadTask();
	}

	@Override
	protected void onStart() {
		Logger.d(TAG, "onStart called");
		super.onStart();
		EasyTracker.getInstance().activityStart(this); // Add this method.

		if (this.isChartBoostActive) {
			if (this.cb == null) {
				this.setupChartBoost();	
			}
			this.cb.onStart(this);
		}
		
		this.handleKickoff();
	}

	@Override
	protected void onStop() {
		Logger.d(TAG, "onStop called");
		super.onStop();
		
		this.handlePause();
		super.onStop();
		 EasyTracker.getInstance().activityStop(this);
		if (this.isChartBoostActive){
			
			if (this.cb.hasCachedInterstitial()){ this.cb.clearCache(); }
			this.cb.onStop(this);
		 
			//this.cb = n//ull;
		}

	//	this.game = null;
	//	this.player = null;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		if (this.isChartBoostActive){
			
			this.cb.onDestroy(this);
			this.cb = null;
		}
		
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Logger.d(TAG, "onPause called");
		super.onPause();
		
		this.handlePause();

	//	this.game = null;
	//	this.player = null;
	}


	private void handlePause(){
		if (this.countdown != null){
			Logger.d(TAG, "countdown handlePause called");
			if (this.game != null) {
				Logger.d(TAG, "handlePause countdown=" + this.game.getCountdown());
			}
			
			this.countdown.cancel();
			this.countdown = null;
		}
	
		if (this.preloadTask != null){
			Logger.d(TAG, "preloadTask stop called");
			this.preloadTask = null;
		}

		if (this.autoPlayTask != null){
			Logger.d(TAG, "autoPlayTask Stop called");
			this.autoPlayTask.stop();
			this.autoPlayTask = null;
		}
		
		if (this.game != null && !this.game.isCompleted() && this.game.isDirty()) {
			GameService.saveGame(this.game);
		}
	}
	
	private void handleKickoff(){
		Logger.d(TAG, "handleKickoff game is null " + (game == null));
		if (this.game == null){
			this.setGameId();
		 	this.game = GameService.getGame(gameId); //(Game) i.getParcelableExtra(Constants.EXTRA_GAME);
			
		 	if (this.game != null){
			 	this.loadViews();
			 	this.setupMenu();
			 	this.setupGame();
			 	this.loadLetterViews();
			 	this.loadButtons();
			 	this.initializeWordLists();
			 	this.setCompletedState();
		 	}
		 	else{
		 		//if bad gameId is saved as active game, clear it
		 		if (this.player.getActiveGameId() == gameId){
		 			this.player.setActiveGameId("");
		 			PlayerService.savePlayer(player);
		 		}
		 		((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_MAIN);
		 	}
		}
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Logger.d(TAG, "onRestart called");
	 
		super.onRestart();
	}

	private void setupPreloadTask(){
		this.setupPreloadTask(this.getString(R.string.progress_loading));
	}

	private void setupPreloadTask(String message){
		Logger.d(TAG, "setupPreloadTask called");
		if ((this.game.isActive() || this.game.isStarted()) && this.possibleWords.size() == 0 ){
			Logger.d(TAG, "setupPreloadTask this.possibleWords.size() == 0");
			this.preloadTask = new PreLoadTask();
			
			spinner = new CustomProgressDialog(this);
			spinner.setMessage(message);
			spinner.show();
			
			Logger.d(TAG, "PreloadTask about to be called");
			//this.preloadTask.execute();
			this.preloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
		}
		else{
			 GameSurface.this.setStartState();		 
		}
	}
	
	private void setupGame(){
		// Logger.d(TAG,"setupGame game turn=" + this.game.getTurn());
	//	spinner = new CustomProgressDialog(this);
	//	spinner.setMessage(this.getString(R.string.progress_loading));
	//	spinner.show();
		
		GameService.loadScoreboard(this, this.game);
		
		this.initializeHopper();
	 	
		//if (!this.game.isCompleted()){
	//		this.fillGameState();
		//}
	 	
	 	//this.setupButtons();
	 	this.setupFonts();

	}
	 
	private class PreLoadTask extends AsyncTask<Void, Void, Void> {
		  
		 @Override
		 protected Void doInBackground(Void... params) {
			 Logger.d(GameSurface.TAG, "PreLoadTask doInBackground called");
			 
			  if (GameSurface.this.game.isActive() || GameSurface.this.game.isStarted() ){  
				 
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
					
					Logger.d(TAG, "PreLoadTask about to load word service");
			        WordService wordService = new WordService(GameSurface.this);

					
					//break up the fetch into batches of 50
					int x = 0;
			 
					String[] lettersetArray = null;
					
					for (List<String> letterSet : letterSets){
						

						if (x == 0){
							lettersetArray = new String[(letterSets.size() - x - 1 >= 50 ? 50 : letterSets.size() - x - 1)];	
						}
						
						String index = "";
						
						for (String s : letterSet){
							index += s;
						}
						lettersetArray[x] = index.toLowerCase();
						x += 1;
			 
						
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
				    
				    String logword = "";
				    for (String word : GameSurface.this.possibleWords){
				    	logword += word + " ";
				    	opponentWords.add(new OpponentWord(word.toUpperCase(), GameSurface.this.calculatePoints(word.toUpperCase()))); 
					 }
				
				    Logger.d(TAG, "possible words=" + logword);
					 Collections.sort(opponentWords, new OpponentWordComparator());
				      
					//save game to save the total number of possibilities
					// if (GameSurface.this.game.getNumPossibleWords() == 0){
						 GameSurface.this.game.setNumPossibleWords(GameSurface.this.possibleWords.size());
						 
						 GameService.saveGame(GameSurface.this.game);
			//	GameSurface.this.autoPlay();
			//			 TextView tvWordsLeft = (TextView)GameSurface.this.findViewById(com.riotapps.wordrace.R.id.tvWordsLeft); 
						  
			//			 tvWordsLeft.setText(String.format(GameSurface.this.getString(com.riotapps.wordrace.R.string.scoreboard_words_left), GameSurface.this.possibleWords.size()));
				 
			}
	    	 
	    	 return null;

	     }

	   //  protected void onProgressUpdate(Integer... progress) {
	   //      setProgressPercent(progress[0]);
	   //  }
	     protected void onPostExecute(Void param) {
			 Logger.d(GameSurface.TAG, "PreLoadTask onPostExecute called");
	    //	 if (GameSurface.this.game.isActive()){
			 if (GameSurface.this.game.isActive() || GameSurface.this.game.isStarted() ){  
				 TextView tvWordsLeft = (TextView)GameSurface.this.findViewById(com.riotapps.wordrace.R.id.tvWordsLeft); 
				 tvWordsLeft.setText(String.format(GameSurface.this.getString(com.riotapps.wordrace.R.string.scoreboard_words_left), GameSurface.this.game.getNumPossibleWords() - GameSurface.this.game.getNumWordsPlayedThisRound()));
			 }			 
	    	// }
	    	 if (GameSurface.this.spinner != null){
	    		 GameSurface.this.spinner.dismiss();
	    		 GameSurface.this.spinner = null;
	 		}
			
	    	// if (GameSurface.this.game.isSpeedRoundType() && (GameSurface.this.game.getRound() == 2 || GameSurface.this.game.getRound() == 3)){
	    	//	 GameSurface.this.tvCountdown.setText(GameSurface.this.getString(R.string.scoreboard_countdown_speed_round_start));
	    	//	 GameSurface.this.runOnUiThread(new handleCountdownViewState(GameSurface.this.game.getRound()));
	    	 //}
	    	 
	    	 GameSurface.this.setStartState();		 

	     } 
	 
	 }
	 
	private void autoPlay(){
		Logger.d(TAG, "autoPlay called");
		this.autoPlayTask =  new AutoPlayTask();
		this.autoPlayTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null); //.execute();
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
	 			
	 			this.handleStartOnClick(); 
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
		   case Constants.RETURN_CODE_CREATE_GAME_DIALOG_CLOSE_CLICKED:
			   this.dismissCreateGameDialog();
			   this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
					     			Constants.TRACKER_LABEL_START_GAME_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			   break;
		   case Constants.RETURN_CODE_CREATE_GAME_DIALOG_ORIGINAL_GAME_CLICKED:
			   this.dismissCreateGameDialog();

			   this.handleGameStartOnClick(this, Constants.RACE_GAME_TYPE_90_SECOND_DASH);
			   break;
		   case Constants.RETURN_CODE_CREATE_GAME_DIALOG_SPEED_ROUND_CLICKED:
			   this.dismissCreateGameDialog();

			   this.handleGameStartOnClick(this, Constants.RACE_GAME_TYPE_SPEED_ROUNDS);
			   break; 
		   case Constants.RETURN_CODE_CREATE_GAME_DIALOG_DOUBLE_TIME_CLICKED:
			   this.dismissCreateGameDialog();

			   this.handleGameStartOnClick(this, Constants.RACE_GAME_TYPE_DOUBLE_TIME);
			   break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_CANCEL_CLICKED:
			   this.dismissCustomDialog();
			   this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
					     			Constants.TRACKER_LABEL_HIDE_INTERSTITIAL_REMINDER_DISMISS, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			   break;
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_CLOSE_CLICKED:
			   this.dismissCustomDialog();
			   this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
					     			Constants.TRACKER_LABEL_HIDE_INTERSTITIAL_REMINDER_CANCEL, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			   break;  
		   case Constants.RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_OK_CLICKED:
			   this.dismissCustomDialog();
			   this.trackEvent(Constants.TRACKER_CATEGORY_GAMEBOARD, Constants.TRACKER_ACTION_BUTTON_TAPPED,
					     			Constants.TRACKER_LABEL_HIDE_INTERSTITIAL_REMINDER_OK, Constants.TRACKER_DEFAULT_OPTION_VALUE);
			  
			   ((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_STORE);
			   break; 
		   case Constants.RETURN_CODE_CUSTOM_TOAST_READY_FINISHED:
			   this.onStartSet();
			   break;
		   case Constants.RETURN_CODE_CUSTOM_TOAST_SET_FINISHED:
			   this.onStartGo();
			   break;
		   case Constants.RETURN_CODE_CUSTOM_TOAST_GO_FINISHED:
			   this.onStartGoFinished();
			   break;
			}
		}
 
	public void handleGameStartOnClick(Context context, int type){
    	//start game and go to game surface
    	try {
    		if (type == Constants.RACE_GAME_TYPE_DOUBLE_TIME && !StoreService.isDoubleTimePurchased(this) && PlayerService.getRemainingFreeUsesDoubleTime(this) < 1){
    			((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_STORE);
    			return;
    		}
    		else if (type == Constants.RACE_GAME_TYPE_SPEED_ROUNDS && !StoreService.isSpeedRoundsPurchased(this) && PlayerService.getRemainingFreeUsesSpeedRounds(this) < 1){
   			 	((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_STORE);
   			 	return;
    		}
    		
    		Game game = GameService.createGame(context, this.player, this.game.getOpponentId(), type);

    		if (type == Constants.RACE_GAME_TYPE_DOUBLE_TIME){ 
    			this.trackEvent(Constants.TRACKER_ACTION_DOUBLE_TIME_GAME_STARTED,String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), (int) Constants.TRACKER_SINGLE_VALUE);
    		}
    		else if (type == Constants.RACE_GAME_TYPE_SPEED_ROUNDS){
    			this.trackEvent(Constants.TRACKER_ACTION_SPEED_ROUNDS_GAME_STARTED,String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), (int) Constants.TRACKER_SINGLE_VALUE);
    		}
    		else if (type == Constants.RACE_GAME_TYPE_90_SECOND_DASH){
    			this.trackEvent(Constants.TRACKER_ACTION_90_SECOND_GAME_STARTED,String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID, this.game.getOpponentId()), (int) Constants.TRACKER_SINGLE_VALUE);
    		}
 
			((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_GAME_SURFACE);
	 
    		this.finish();
			
		} catch (DesignByContractException e) {
			// TODO Auto-generated catch block
			
			DialogManager.SetupAlert(this, this.getString(R.string.sorry), e.getMessage());
		}

    }
	

	@Override
	public void dialogClose(int resultCode, String returnValue) {
		// TODO Auto-generated method stub
		
	}

	private class AutoPlayTask extends AsyncTask<Void, PlayedWord, Void> {
		  
		private boolean stopLoop = false;
		//private List<OpponentWord> words = new ArrayList<OpponentWord>();
//		private Timer timer;
		 @Override
		 protected Void doInBackground(Void... params) {
			// if (GameSurface.this.game.isActive()){
			 
			 //first fill the list
		//	 for (String word : GameSurface.this.possibleWords){
		//		words.add(new OpponentWord(word.toUpperCase(), GameSurface.this.calculatePoints(word.toUpperCase()))); 
		//	 }
		
		//	 Collections.sort(words, new OpponentWordComparator());
			 
			//this.timer = new Timer();
			 
			int minDelay = 1;
			int maxDelay = 1;
			
			switch (GameSurface.this.game.getOpponent().getSkillLevel()){
				case Constants.SKILL_LEVEL_NOVICE:
					minDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelNoviceMinDelay);
					maxDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelNoviceMaxDelay);
					break;
				case Constants.SKILL_LEVEL_AMATEUR:
					minDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelAmateurMinDelay);
					maxDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelAmateurMaxDelay);
					break;
				case Constants.SKILL_LEVEL_SEMI_PRO:
					minDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelSemiProMinDelay);
					maxDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelSemiProMaxDelay);
					break;
				case Constants.SKILL_LEVEL_PROFESSIONAL:
					minDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelProfessionalMinDelay);
					maxDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelProfessionalMaxDelay);
					break;
				case Constants.SKILL_LEVEL_EXPERT:
					minDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelExpertMinDelay);
					maxDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelExpertMaxDelay);
					break;
				case Constants.SKILL_LEVEL_MASTER:
					minDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelMasterMinDelay);
					maxDelay = GameSurface.this.getResources().getInteger(R.integer.skillLevelMasterMaxDelay);
					break;
			}

			boolean play = true;
			
			//temp 
			int x = 0;
			
			while (!stopLoop && play && GameSurface.this.isCountdownRunning && !GameSurface.this.freezeAction){
				if(!GameSurface.this.game.isStarted()) {
					play = false;
					break;
				}
				
				if (stopLoop){
					break;
				}
				x += 1;
				
			//	if (x >=20) {play=false;break;}
				try {
					Thread.sleep(Utils.getRandomNumberFromRange(minDelay, maxDelay));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (stopLoop){
					break;
				}
				
				//in case game ends before thread if completed
				if(GameSurface.this.game == null || !GameSurface.this.game.isStarted()) {
					play = false;
					break;
				}
				
				int numOptions = opponentWords.size();
				int randomIndex = 0;
				int start = 0;
				int base = 1;
				int randomBetterPlay = 0;
				
 
				if (numOptions > 9){
					
					switch (game.getOpponent().getSkillLevel()){
						case Constants.SKILL_LEVEL_NOVICE:
							//from bottom 50%
							base = Math.round(numOptions * .6f);//Math.round(numOptions / 2);
							
							start = 0;
							//with a coin flip, determine if we grab from top half of range only
							if (Utils.coinFlip() == Constants.COIN_FLIP_HEADS){
								start = Math.round(base / 2); 
							}
							
							//every so often (1 in 50 chance) let novice get lucky and play a better word from upper half
							randomBetterPlay = Utils.getRandomNumberFromRange(1, 50);  //make these constants
							if (randomBetterPlay == 7){
								//grab from top 50%
								start = base;
								base = numOptions - 1;
							}
							
							randomIndex = Utils.getRandomNumberFromRange(start, numOptions - base);
							break;
						case Constants.SKILL_LEVEL_AMATEUR:
							//from 25% to 75%
							//start = Math.round(numOptions / 4);
							//base = numOptions - start;
							start = 0;
							base = Math.round(numOptions / 2); //numOptions - Math.round(numOptions / 4);
							
							//with a coin flip, determine if we grab from top half of range only
							if (Utils.coinFlip() == Constants.COIN_FLIP_HEADS){
								start = Math.round(base / 4); 
							}
							
							//every so often (1 in 30 chance) let amateur get lucky and play a better word from upper half
							randomBetterPlay = Utils.getRandomNumberFromRange(1, 30);  //make these constants
							if (randomBetterPlay == 7){
								//grab from top 75%
								start = base;
								base = numOptions - 1;
							}
							
							randomIndex = Utils.getRandomNumberFromRange(start, base);					
							break;
						case Constants.SKILL_LEVEL_SEMI_PRO:
							//from 50% to 90%
							start = 0; //Math.round(numOptions / 2);
							base = Math.round(numOptions * .4F); //Math.round(numOptions / 10);
							
							//if semi=pro opponent is losing, pull a word from upper part of his range
							//if (GameSurface.this.game.getPlayerScore() > GameSurface.this.game.getOpponentScore() + 20){
							//	//from 75% to 90%
							//	start = numOptions - Math.round(numOptions / 4); 
							//	//Logger.d(TAG, "autoplay SKILL_LEVEL_SEMI_PRO losing...start=" + start + " end=" + (numOptions - base));
							//	
							//}
							//every so often (1 in 15 chance) let semi-pro get lucky and play a better word  
							randomBetterPlay = Utils.getRandomNumberFromRange(1, 15);  //make these constants
							if (randomBetterPlay == 7){
								//grab from top 90%
								start = numOptions - base;
								base = 1;
							}
							randomIndex = Utils.getRandomNumberFromRange(start, numOptions - base);					
							break;
						case Constants.SKILL_LEVEL_PROFESSIONAL:
							//first 80% unless losing by 100
							base = Math.round(numOptions / 5); 
							start = 0;
							
							//if opponent is losing by 100 or more, play from the top 5% 
							//if (GameSurface.this.game.getPlayerScore() > GameSurface.this.game.getOpponentScore() + 20){
							//	base = Math.round(numOptions / 20); 
//							//	Logger.d(TAG, "autoplay SKILL_LEVEL_PROFESSIONAL losing...start=" + (numOptions - base) + " end=" + (numOptions - 1));
//							//	
							//}

							//randomIndex = Utils.getRandomNumberFromRange(numOptions - base, numOptions - 1);
							randomIndex = Utils.getRandomNumberFromRange(start, numOptions - base);
							
							break;
						case Constants.SKILL_LEVEL_EXPERT:
							//top 20% (or top 10%) unless losing by 80
							
							base = Math.round(numOptions / 10); 
							start = Math.round(numOptions / 10);	
							if (Utils.coinFlip() == Constants.COIN_FLIP_HEADS){
								base = Math.round(numOptions / 10); 
							}

							randomIndex = Utils.getRandomNumberFromRange(start, numOptions - base);
							
							//if opponent is losing by 100 or more, play the highest score or second highest score 
							//if (GameSurface.this.game.getPlayerScore() > GameSurface.this.game.getOpponentScore() + 20){
	 						//	randomIndex = (Utils.coinFlip() == Constants.COIN_FLIP_HEADS ? numOptions - 1 : numOptions - 2);
							//}
							break;
						case Constants.SKILL_LEVEL_MASTER:
							base = Math.round(numOptions / 4); 
							start = Math.round(numOptions * .2F);
							//if opponent is losing by 100 or more, play from the top 5% 
							if (GameSurface.this.game.getPlayerScore() > GameSurface.this.game.getOpponentScore() + 20){
								start = numOptions - Math.round(numOptions / 20); 
//								Logger.d(TAG, "autoplay SKILL_LEVEL_PROFESSIONAL losing...start=" + (numOptions - base) + " end=" + (numOptions - 1));
//								
							}

							randomIndex = Utils.getRandomNumberFromRange(start, numOptions - 1);
							
							//grab from top 2 choices with a coin toss
							 
 						//	randomIndex = (Utils.coinFlip() == Constants.COIN_FLIP_HEADS ? numOptions - 1 : numOptions - 2);
 							
 							//if opponent is losing, play the highest score  
						//	if (GameSurface.this.game.getPlayerScore() > GameSurface.this.game.getOpponentScore() + 10){
						//		randomIndex = numOptions - 1;
						//	}
							break;
					}
				}
				else if (numOptions == 1){
					randomIndex = 0;
				}
				else {
					//can tighten this into skill levels later if needed
					randomIndex = Utils.getRandomNumberFromRange(0, numOptions - 1);
				}
			
				if(!GameSurface.this.game.isStarted()) {
					play = false;
					break;
				}
				if (stopLoop){
					break;
				}
				//in case there is a timing issue between rounds
				if (GameSurface.this.opponentWords.size() < randomIndex || GameSurface.this.freezeAction){
					//skip this
				}
				else{
					OpponentWord chosenWord = opponentWords.get(randomIndex);
					
					PlayedWord playedWord = new PlayedWord();
					playedWord.setPlayedDate(new Date());
					playedWord.setOpponentPlay(true);
					playedWord.setPointsScored(chosenWord.getPoints());
					playedWord.setWord(chosenWord.getWord().toUpperCase());
					
					this.publishProgress(playedWord);
				}
			}
			/*
			OpponentPlayTask task = new OpponentPlayTask();
			this.timer.scheduleAtFixedRate(task, delay, interval);
	    	 //}
	    	  
	    	  */
	    	 return null;

	     }
			 	 
		 public void stop(){
			 this.stopLoop = true; 
		 }

		    protected void onProgressUpdate(PlayedWord... playedWord) {
		    	//make sure that game.PlayedWord does not already contain word
		  
		    	if (GameSurface.this.freezeAction){ return; }
		    	
				if (GameSurface.this.wordsPlayedByOpponent.contains(playedWord[0].getWord())){
	 				return;
			 	}
				
				if (GameSurface.this.wordsPlayedByPlayer.contains(playedWord[0].getWord())){
	 				return;
			 	}
		    	
				GameSurface.this.wordsPlayedByOpponent.add(playedWord[0].getWord());
		    	GameSurface.this.wordListPlayedByOpponent.add(playedWord[0]);
				//this.llPlayerWords.addView(this.getPlayedWordView(playedWord));
		    	GameSurface.this.opponentListadapter.notifyDataSetChanged();
		    	GameSurface.this.lvOpponent.setSelection(GameSurface.this.opponentListadapter.getCount() - 1);
		    	GameSurface.this.game.getPlayedWords().add(playedWord[0]);
		    	GameSurface.this.game.incrementNumWordsPlayedThisRound();
		    	GameSurface.this.game.setOpponentScore(GameSurface.this.game.getOpponentScore() + playedWord[0].getPointsScored());
				
		    	GameSurface.this.tvOpponentScore.setText(String.valueOf(GameSurface.this.game.getOpponentScore()));
			 	
		    	GameSurface.this.tvWordsLeft.setText(String.format(GameSurface.this.getString(com.riotapps.wordrace.R.string.scoreboard_words_left), GameSurface.this.game.getNumPossibleWords() - GameSurface.this.game.getNumWordsPlayedThisRound()));

		    	
		    }
		    
	
	    protected void onPostExecute(Void param) {
	    	  
	    	 //do something
	    	  
	     }

	 
	 }

	public class OpponentWord {
		private String word;
		private int points;
		
		public OpponentWord(String word, int points){
			this.word = word;
			this.points = points;
		}
		
		public String getWord() {
			return word;
		}
		public void setWord(String word) {
			this.word = word;
		}
		public int getPoints() {
			return points;
		}
		public void setPoints(int points) {
			this.points = points;
		}
	
	}
	
	private class OpponentWordComparator implements Comparator<OpponentWord> {

		@Override
		public int compare(OpponentWord r1, OpponentWord r2) {
			return ((Integer)r1.getPoints()).compareTo((Integer)r2.getPoints());
		}
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
		ApplicationContext.captureTime(TAG, "onClick called");
		if (v.getId() == R.id.options) {
			if (!this.game.isStarted()){
				popupMenu.show();
			}
		}
		else if (v.getId() == R.id.bStart) {
			this.handleStartOnClick();
		} 
	 	else if (v.getId() == R.id.bCancel) {
			this.handleCancelOnClick();
		}
	 	else if (
	 			 v.getId() == R.id.bLetter1 || v.getId() == R.id.bLetter2 || v.getId() == R.id.bLetter3 || v.getId() == R.id.bLetter4 || v.getId() == R.id.bLetter5 ||
	 			 v.getId() == R.id.bLetter6 || v.getId() == R.id.bLetter7 || v.getId() == R.id.bLetter8 || v.getId() == R.id.bLetter9 || v.getId() == R.id.bLetter10
	 			 ){
	 		this.handleTrayOnClick(v.getId());
	 	}
		else if (
	 			 v.getId() == R.id.ivPlayedLetter1 || v.getId() == R.id.ivPlayedLetter2 || v.getId() == R.id.ivPlayedLetter3 || v.getId() == R.id.ivPlayedLetter4 || v.getId() == R.id.ivPlayedLetter5 ||
	 			 v.getId() == R.id.ivPlayedLetter6 || v.getId() == R.id.ivPlayedLetter7 || v.getId() == R.id.ivPlayedLetter8 || v.getId() == R.id.ivPlayedLetter9 || v.getId() == R.id.ivPlayedLetter10
	 			 ){
	 		this.handlePlayedLetterOnClick(v.getId());
	 	}
	 	else if (v.getId() == R.id.bRecall){
	 		this.handleRecall();
	 	}
	 	else if (v.getId() == R.id.bShuffle){
	 		this.handleShuffle();
	 	}
	 	else if (v.getId() == R.id.bPlay){
	 		this.handlePlay();
	 	}
	 	else if (v.getId() == R.id.bRematch){
	 		this.handleRematchPrompt();
	 	}

	}
	
	private void handleRematchPrompt(){
		this.createGameDialog = new CreateGameDialog(this, this.game.getOpponentId());
    	this.createGameDialog.show();
	}

	private void loadPlayedTilesWithCurrentWord(){
 
		ivPlayedLetter1.setImageBitmap(GameSurface.getPlayedLetterImage(this, (this.currentWord.size() >= 1 ? this.currentWord.get(0) : ""), this.playedLetterTileSize, 1));
		ivPlayedLetter2.setImageBitmap(GameSurface.getPlayedLetterImage(this, (this.currentWord.size() >= 2 ? this.currentWord.get(1) : ""), this.playedLetterTileSize, 2));
		ivPlayedLetter3.setImageBitmap(GameSurface.getPlayedLetterImage(this, (this.currentWord.size() >= 3 ? this.currentWord.get(2) : ""), this.playedLetterTileSize, 3));
		ivPlayedLetter4.setImageBitmap(GameSurface.getPlayedLetterImage(this, (this.currentWord.size() >= 4 ? this.currentWord.get(3) : ""), this.playedLetterTileSize, 4));
		ivPlayedLetter5.setImageBitmap(GameSurface.getPlayedLetterImage(this, (this.currentWord.size() >= 5 ? this.currentWord.get(4) : ""), this.playedLetterTileSize, 5));
		ivPlayedLetter6.setImageBitmap(GameSurface.getPlayedLetterImage(this, (this.currentWord.size() >= 6 ? this.currentWord.get(5) : ""), this.playedLetterTileSize, 6));
		ivPlayedLetter7.setImageBitmap(GameSurface.getPlayedLetterImage(this, (this.currentWord.size() >= 7 ? this.currentWord.get(6) : ""), this.playedLetterTileSize, 7));
		ivPlayedLetter8.setImageBitmap(GameSurface.getPlayedLetterImage(this, (this.currentWord.size() >= 8 ? this.currentWord.get(7) : ""), this.playedLetterTileSize, 8));
		ivPlayedLetter9.setImageBitmap(GameSurface.getPlayedLetterImage(this, (this.currentWord.size() >= 9 ? this.currentWord.get(8) : ""), this.playedLetterTileSize, 9));
		ivPlayedLetter10.setImageBitmap(GameSurface.getPlayedLetterImage(this, (this.currentWord.size() >= 10 ? this.currentWord.get(9) : ""), this.playedLetterTileSize, 10));

	}

	
	private void initializePlayedTiles(){
		ivPlayedLetter1.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));
		ivPlayedLetter2.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));
		ivPlayedLetter3.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));
		ivPlayedLetter4.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));
		ivPlayedLetter5.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));
		ivPlayedLetter6.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));
		ivPlayedLetter7.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));
		ivPlayedLetter8.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));
		ivPlayedLetter9.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));
		ivPlayedLetter10.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));

	}
	
	private void initializeTray(){
		
		bLetter1.setImageBitmap(GameSurface.getTrayLetterImage(this, this.hopper.get(0), letterTileSize));
		bLetter2.setImageBitmap(GameSurface.getTrayLetterImage(this, this.hopper.get(1), letterTileSize));
		bLetter3.setImageBitmap(GameSurface.getTrayLetterImage(this, this.hopper.get(2), letterTileSize));
		bLetter4.setImageBitmap(GameSurface.getTrayLetterImage(this, this.hopper.get(3), letterTileSize));
		bLetter5.setImageBitmap(GameSurface.getTrayLetterImage(this, this.hopper.get(4), letterTileSize));
		bLetter6.setImageBitmap(GameSurface.getTrayLetterImage(this, this.hopper.get(5), letterTileSize));
		bLetter7.setImageBitmap(GameSurface.getTrayLetterImage(this, this.hopper.get(6), letterTileSize));
		bLetter8.setImageBitmap(GameSurface.getTrayLetterImage(this, this.hopper.get(7), letterTileSize));
		bLetter9.setImageBitmap(GameSurface.getTrayLetterImage(this, this.hopper.get(8), letterTileSize));
		bLetter10.setImageBitmap(GameSurface.getTrayLetterImage(this, this.hopper.get(9), letterTileSize));
	}
	
	private void initializeHopper(){
		this.hopper.clear();
		for (String letter : this.game.getHopper()){
			this.hopper.add(letter);
			Logger.d(TAG, "initializeHopper letter=" + letter);
		}
		//for (String letter : this.game.getHopper()){
		//	this.hopperState.add(letter);
		//}
	}
	
	private void handleRecall(){
		if (this.freezeAction){ return; }
		this.initializePlayedTiles();
		this.initializeHopper();
		this.initializeTray();
		this.currentWord.clear();
		this.updatePointsView();
		
		//reset points
		
	}
	private void updatePointsView(){
		int points = this.calculatePoints(this.currentWord);
		
		if (points > 0){
 			tvNumPoints.setText(String.format(this.getString(R.string.scoreboard_num_points),points));
 			tvNumPoints.setVisibility(View.VISIBLE);
		}
		else{
 			tvNumPoints.setVisibility(View.INVISIBLE);
		}
	}
	
	private int calculatePoints(String word){
		//split word into list of letters
		List<String> letters = new ArrayList<String>();
		
		String[] letterArray = word.trim().split("");
		//because java adds empty string as the first element, its a bit annoying
		//use 1 to get the first  element  
		for (String letter : letterArray ){
			if (letter.length() > 0){
				letters.add(letter);
			}
		}
		
		return calculatePoints(letters);
	}
	
	
	private int calculatePoints(List<String> letters){
		
		if (letters.size() < 3){ return 0; }
		
		String word = "";
		
		int points = 0;
		
		for (String letter : letters){
			points += AlphabetService.getLetterValue(letter);
			word += letter;
		}
		
		//if not a valid word no points are calculated
		if (!this.possibleWords.contains(word.toLowerCase())) { return 0; }
		
	//	if (letters.size() == 10){
	//		points += 15; //30; ///make this a constant on int resource
	//	}
	/*	else if (letters.size() >= 8){
			points += 15;
		}
		else if (letters.size() >= 6){
			points += 5;
		}
		*/
		return points;
	}
	
	private void handlePlay(){
		//<string name="game_surface_race_word_too_short">words must be at least 3 letters long</string>
		// <string name="game_surface_race_word_opponent_played">%1$s has already played %2$s</string>	
		// <string name="game_surface_race_word_already_played">you have already played %2$s</string> 
		// <string name="game_surface_race_word_invalid">%1$s has already played %2$s</string>
		if (this.freezeAction){ return; }
		
		String word = "";
 
		for (String letter : this.currentWord){
			word += letter;
		}
		
		if (this.currentWord.size() < this.getResources().getInteger(R.integer.minWordLength)){
			new CustomToast(this, this.getString(R.string.game_surface_race_word_too_short)).show();
			return;
	 	}
		if (this.wordsPlayedByOpponent.contains(word)){
			new CustomToast(this, String.format(this.getString(R.string.game_surface_race_word_opponent_played), this.game.getOpponent().getName(), word)).show();
			return;
	 	}
		
		if (this.wordsPlayedByPlayer.contains(word)){
			new CustomToast(this, String.format(this.getString(R.string.game_surface_race_word_already_played), word)).show();
			return;
	 	}
		
		if (!this.possibleWords.contains(word.toLowerCase())){
			new CustomToast(this, String.format(this.getString(R.string.game_surface_race_word_invalid), word)).show();
			return;
	 	}
		int points = this.calculatePoints(this.currentWord);
		
		this.handleRecall();
		this.wordsPlayedByPlayer.add(word);
		 
		
		PlayedWord playedWord = new PlayedWord();
		playedWord.setPlayedDate(new Date());
		playedWord.setOpponentPlay(false);
		playedWord.setPointsScored(points);
		playedWord.setWord(word);
		
		//scroll.fullScroll(View.FOCUS_DOWN) also should work.
		this.wordListPlayedByPlayer.add(playedWord);
		//this.llPlayerWords.addView(this.getPlayedWordView(playedWord));
		this.playerListadapter.notifyDataSetChanged();
		this.lvPlayer.setSelection(this.playerListadapter.getCount() - 1);
		// this.playerListadapter.addToList(playedWord);
		//this.playerListadapter.add(playedWord);
	 // context.adapter.updateList(context.workingList); 
		// this.playerListadapter.
		/* runOnUiThread(new ha() {
		        public void run() {
		            // use data here
		        //	unloadPlaySpinner();
		        	GameSurface.this.llPlayerWords.addView(GameSurface.this.getPlayedWordView(playedWord));
		        //	GameSurface.this.playerListadapter.notifyDataSetChanged();
		        }
		    });
		    */
		//this.playerListadapter.notifyDataSetChanged();
		
		this.game.getPlayedWords().add(playedWord);
		this.game.setPlayerScore(this.game.getPlayerScore() + points);
		
		this.tvPlayerScore.setText(String.valueOf(this.game.getPlayerScore()));
		this.game.incrementNumWordsPlayedThisRound();
	 	
		this.tvWordsLeft.setText(String.format(this.getString(com.riotapps.wordrace.R.string.scoreboard_words_left), game.getNumPossibleWords() - game.getNumWordsPlayedThisRound()));
 
		//save game upon completion, not here
	}
	 

	
	private void initializeWordLists(){

	 	this.playerListadapter = new PlayedWordAdapter(this, this.wordListPlayedByPlayer);
		this.lvPlayer.setAdapter(this.playerListadapter); 
		
	 	this.opponentListadapter = new PlayedWordAdapter(this, this.wordListPlayedByOpponent);
		this.lvOpponent.setAdapter(this.opponentListadapter); 
		
		if (this.game.getPlayedWords().size() > 0 && (this.wordListPlayedByPlayer.size() == 0 || this.wordListPlayedByOpponent.size() == 0) ){
			//game is completed or underway, load up lists
			for (PlayedWord word : this.game.getPlayedWords()){
				if (word.isOpponentPlay()){
					this.wordListPlayedByOpponent.add(word);
					this.wordsPlayedByOpponent.add(word.getWord());
				}
				else {
					this.wordListPlayedByPlayer.add(word);
					this.wordsPlayedByPlayer.add(word.getWord());
				}
			}
			
			this.playerListadapter.notifyDataSetChanged();
			this.opponentListadapter.notifyDataSetChanged();
			
		}
	}
	
	private void setListenerOnLists(){
		this.lvPlayer.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position,
	                long id) {
	        	
	        	//this will eventually call wordnik for definition lookup
	        	List<IntentExtra> extras = new ArrayList<IntentExtra>();
	        	extras.add(new IntentExtra(Constants.EXTRA_GAME_ID, GameSurface.this.game.getId(), String.class));	
	        	extras.add(new IntentExtra(Constants.EXTRA_WORD_LOOKUP, view.getTag().toString(), String.class));	
				((ApplicationContext)GameSurface.this.getApplication()).startNewActivity(GameSurface.this, Constants.ACTIVITY_CLASS_GAME_LOOKUP, extras);

	        }
	    });

		this.lvOpponent.setOnItemClickListener(new OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position,
	                long id) {
	        	
	        	//this will eventually call wordnik for definition lookup
	        	List<IntentExtra> extras = new ArrayList<IntentExtra>();
	        	extras.add(new IntentExtra(Constants.EXTRA_GAME_ID, GameSurface.this.game.getId(), String.class));	
	        	extras.add(new IntentExtra(Constants.EXTRA_WORD_LOOKUP, view.getTag().toString(), String.class));	
				((ApplicationContext)GameSurface.this.getApplication()).startNewActivity(GameSurface.this, Constants.ACTIVITY_CLASS_GAME_LOOKUP, extras);

	        }
	    });
	}
	
	
	
	private void handleShuffle(){
		//only allow shuffle if all letters are in tray?
		Logger.d(TAG, "handleShuffle called");
		if (this.freezeAction){ return; }
		Boolean allow = true;
		
		for (String letter : this.hopper){
			if (letter.equals("")){
				allow = false;
				break;
			}
		}
		
		if (allow){
			Collections.shuffle(this.game.getHopper());
			
			this.initializeHopper();
			this.initializeTray();
		}
	
		//save game at end or between rounds
	}
	
	private void handlePlayedLetterOnClick(int id){
		if (this.freezeAction){ return; }
		if (!this.game.isStarted()) { return; }
		 int opening = this.findFirstOpenTraySlot();

		 if (id == R.id.ivPlayedLetter1){
			if (this.currentWord.size() < 1 || this.currentWord.get(0).equals("")) { return; }
			this.hopper.set(opening, this.currentWord.get(0));
			this.currentWord.remove(0);
		 }
		 else if (id == R.id.ivPlayedLetter2){
			if (this.currentWord.size() < 2 || this.currentWord.get(1).equals("")) { return; }
			this.hopper.set(opening, this.currentWord.get(1));
			this.currentWord.remove(1);
		 }
		 else if (id == R.id.ivPlayedLetter3){
			if (this.currentWord.size() < 3 || this.currentWord.get(2).equals("")) { return; }
			this.hopper.set(opening, this.currentWord.get(2));
			this.currentWord.remove(2);
		 }
		 else if (id == R.id.ivPlayedLetter4){
			if (this.currentWord.size() < 4 || this.currentWord.get(3).equals("")) { return; }
			this.hopper.set(opening, this.currentWord.get(3));
			this.currentWord.remove(3);
		 }
		 else if (id == R.id.ivPlayedLetter5){
			if (this.currentWord.size() < 5 || this.currentWord.get(4).equals("")) { return; }
			this.hopper.set(opening, this.currentWord.get(4));
			this.currentWord.remove(4);
		 }
		 else if (id == R.id.ivPlayedLetter6){
		    if (this.currentWord.size() < 6 || this.currentWord.get(5).equals("")) { return; }
			this.hopper.set(opening, this.currentWord.get(5));
			this.currentWord.remove(5);
		 } 
		 else if (id == R.id.ivPlayedLetter7){
			if (this.currentWord.size() < 7 || this.currentWord.get(6).equals("")) { return; }
			this.hopper.set(opening, this.currentWord.get(6));
			this.currentWord.remove(6);
		 }
		 else if (id == R.id.ivPlayedLetter8){
			if (this.currentWord.size() < 8 || this.currentWord.get(7).equals("")) { return; }
			this.hopper.set(opening, this.currentWord.get(7));
			this.currentWord.remove(7);
		 }
		 else if (id == R.id.ivPlayedLetter9){
			if (this.currentWord.size() < 9 || this.currentWord.get(8).equals("")) { return; }
			this.hopper.set(opening, this.currentWord.get(8));
			this.currentWord.remove(8);
		 }
		 else if (id == R.id.ivPlayedLetter10){
			if (this.currentWord.size() < 10 || this.currentWord.get(9).equals("")) { return; }
			this.hopper.set(opening, this.currentWord.get(9));
			this.currentWord.remove(9);
		 }
		 
		 this.loadPlayedTilesWithCurrentWord();
		 this.initializeTray();
		 this.updatePointsView();
	}
	
	private void handleTrayOnClick(int id){
		if (this.freezeAction){ return; }
		if (!this.game.isStarted()) { return; }
		int locationForClickedLetter = findFirstOpenWordSlot();
		
		ImageView ivLocation = this.ivPlayedLetter10;
		if (locationForClickedLetter == 1) {
			ivLocation = this.ivPlayedLetter1;
		}
		else if (locationForClickedLetter == 2) {
			ivLocation = this.ivPlayedLetter2;
		}
		else if (locationForClickedLetter == 3) {
			ivLocation = this.ivPlayedLetter3;
		}
		else if (locationForClickedLetter == 4) {
			ivLocation = this.ivPlayedLetter4;
		}
		else if (locationForClickedLetter == 5) {
			ivLocation = this.ivPlayedLetter5;
		}
		else if (locationForClickedLetter == 6) {
			ivLocation = this.ivPlayedLetter6;
		}
		else if (locationForClickedLetter == 7) {
			ivLocation = this.ivPlayedLetter7;
		}
		else if (locationForClickedLetter == 8) {
			ivLocation = this.ivPlayedLetter8;
		}
		else if (locationForClickedLetter == 9) {
			ivLocation = this.ivPlayedLetter9;
		}
 
	 	
		if (id == R.id.bLetter1){
			 if (this.hopper.get(0).equals("")){ return; }
			 ivLocation.setImageBitmap(GameSurface.getPlayedLetterImage(this, this.hopper.get(0), playedLetterTileSize, 1));
			 this.bLetter1.setImageBitmap(GameSurface.getTrayLetterImage(this, "", letterTileSize));
			 addLetterToCurrentWord(locationForClickedLetter,this.hopper.get(0));
			 this.hopper.set(0, "");
		}
		else if (id == R.id.bLetter2){
			 if (this.hopper.get(1).equals("")){ return; }

			 ivLocation.setImageBitmap(GameSurface.getPlayedLetterImage(this, this.hopper.get(1), playedLetterTileSize, 2));
			 this.bLetter2.setImageBitmap(GameSurface.getTrayLetterImage(this, "", letterTileSize));
			 addLetterToCurrentWord(locationForClickedLetter,this.hopper.get(1));
			 this.hopper.set(1, "");
		}
		else if (id == R.id.bLetter3){
			 if (this.hopper.get(2).equals("")){ return; }

			 ivLocation.setImageBitmap(GameSurface.getPlayedLetterImage(this, this.hopper.get(2), playedLetterTileSize, 3));
			 this.bLetter3.setImageBitmap(GameSurface.getTrayLetterImage(this, "", letterTileSize));
			 addLetterToCurrentWord(locationForClickedLetter,this.hopper.get(2));
			 this.hopper.set(2, "");
		}
		else if (id == R.id.bLetter4){
			 if (this.hopper.get(3).equals("")){ return; }

			  ivLocation.setImageBitmap(GameSurface.getPlayedLetterImage(this, this.hopper.get(3), playedLetterTileSize, 4));
			 this.bLetter4.setImageBitmap(GameSurface.getTrayLetterImage(this, "", letterTileSize));
			 addLetterToCurrentWord(locationForClickedLetter ,this.hopper.get(3));
			 this.hopper.set(3, "");
		} 
		else if (id == R.id.bLetter5){
			 if (this.hopper.get(4).equals("")){ return; }

			ivLocation.setImageBitmap(GameSurface.getPlayedLetterImage(this, this.hopper.get(4), playedLetterTileSize, 5));
			 this.bLetter5.setImageBitmap(GameSurface.getTrayLetterImage(this, "", letterTileSize));
			 addLetterToCurrentWord(locationForClickedLetter ,this.hopper.get(4));
			 this.hopper.set(4, "");
		}
		else if (id == R.id.bLetter6){
			 if (this.hopper.get(5).equals("")){ return; }

			ivLocation.setImageBitmap(GameSurface.getPlayedLetterImage(this, this.hopper.get(5), playedLetterTileSize, 6));
			 this.bLetter6.setImageBitmap(GameSurface.getTrayLetterImage(this, "", letterTileSize));
			 addLetterToCurrentWord(locationForClickedLetter ,this.hopper.get(5));
			 this.hopper.set(5, "");
		}
		else if (id == R.id.bLetter7){
			 if (this.hopper.get(6).equals("")){ return; }

			ivLocation.setImageBitmap(GameSurface.getPlayedLetterImage(this, this.hopper.get(6), playedLetterTileSize, 7));
			 this.bLetter7.setImageBitmap(GameSurface.getTrayLetterImage(this, "", letterTileSize));
			 addLetterToCurrentWord(locationForClickedLetter ,this.hopper.get(6));
			 this.hopper.set(6, "");
		}
		else if (id == R.id.bLetter8){
			 if (this.hopper.get(7).equals("")){ return; }

			ivLocation.setImageBitmap(GameSurface.getPlayedLetterImage(this, this.hopper.get(7), playedLetterTileSize, 8));
			 this.bLetter8.setImageBitmap(GameSurface.getTrayLetterImage(this, "", letterTileSize));
			 addLetterToCurrentWord(locationForClickedLetter ,this.hopper.get(7));
			 this.hopper.set(7, "");
		}
		else if (id == R.id.bLetter9){
			 if (this.hopper.get(8).equals("")){ return; }

			ivLocation.setImageBitmap(GameSurface.getPlayedLetterImage(this, this.hopper.get(8), playedLetterTileSize, 9));
			 this.bLetter9.setImageBitmap(GameSurface.getTrayLetterImage(this, "", letterTileSize));
			 addLetterToCurrentWord(locationForClickedLetter ,this.hopper.get(8));
			 this.hopper.set(8, "");
		}
		else if (id == R.id.bLetter10){
			 if (this.hopper.get(9).equals("")){ return; }

			ivLocation.setImageBitmap(GameSurface.getPlayedLetterImage(this, this.hopper.get(9), playedLetterTileSize, 10));
			 this.bLetter10.setImageBitmap(GameSurface.getTrayLetterImage(this, "", letterTileSize));
			 addLetterToCurrentWord(locationForClickedLetter ,this.hopper.get(9));
			 this.hopper.set(9, "");
		}
		
		this.updatePointsView();
				
	}
	
	private int findFirstOpenTraySlot(){
		int currentSize = this.hopper.size();
		//int location = currentWord.size() + 1;
		for (int i = 0; i < currentSize; i++){
			if (this.hopper.get(i).equals("")){
				return i;
			}
			
		}
		
		return currentSize + 1;
	}
	
	private int findFirstOpenWordSlot(){
		int currentSize = currentWord.size();
		//int location = currentWord.size() + 1;
		for (int i = 0; i < currentSize; i++){
			if (currentWord.get(i).equals("")){
				return i + 1;
			}
		}
		
		return currentSize + 1;
	}
	
	private void addLetterToCurrentWord(int location, String letter){
		if (currentWord.size() - 1 >= location){
			currentWord.set(location, letter);
		}
		else{
			//add to end
			currentWord.add(letter);
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
 		 	}
		 	else{
		 		this.hideInterstitialAd = true;
		 	}
		 		 		
		 	
		 	Logger.d(TAG,  " chartBoost=" + this.isChartBoostActive);
		}
	
		private void handleInterstitialAd(){
	    	this.hasPostAdRun = false; 
	    	if (this.hideInterstitialAd){
	    		this.handlePostAdServer();   		            	 					
	 			}
	 		else{
	 			long hoursSinceLastReminder = PlayerService.getHoursSinceLastInterstitialPurchaseReminder();
	 			
	 			Logger.d(TAG, "handleInterstitialAd hoursSinceLastReminder=" + hoursSinceLastReminder);
	 			
	 			if (hoursSinceLastReminder == Constants.DEFAULT_HIDE_AD_PURCHASE_REMINDER){
	 				//save it for the first time
	 				PlayerService.setLastInterstitialPurchaseReminderTime();
	 			}
	 			
	 			if (hoursSinceLastReminder >= 24  ){
	 				this.customDialog = new CustomButtonDialog(this, 
	 		    			this.getString(R.string.game_surface_interstital_purchase_title), 
	 		    			this.getString(R.string.game_surface_interstital_purchase_text),
	 		    			this.getString(R.string.go_to_store),
	 		    			this.getString(R.string.no_thanks),
	 		    			Constants.RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_OK_CLICKED,
	 		    			Constants.RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_CANCEL_CLICKED,
	 		    			Constants.RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_CLOSE_CLICKED);
	 				
	 				//public static final int RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_OK_CLICKED = 133;	
	 				//public static final int RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_CLOSE_CLICKED = 134;
	 				//public static final int RETURN_CODE_CUSTOM_DIALOG_INTERSTITIAL_REMINDER_CANCEL_CLICKED = 135;	
	 		    
	 				PlayerService.setLastInterstitialPurchaseReminderTime();
	 		    	this.customDialog.show();
	 			}
	 			else{
		 			if (this.isChartBoostActive) {
		 				if (spinner == null){
		 					spinner = new CustomProgressDialog(this);
		 		 			spinner.setMessage(this.getString(R.string.progress_almost_ready));
		 		 			spinner.show();				
		 				}
		 				
			 			this.cb.setTimeout((int)Constants.GAME_SURFACE_INTERSTITIAL_AD_CHECK_IN_MILLISECONDS);
			 			this.cb.showInterstitial();
				    	Logger.d(TAG, "showInterstitial from Chartboost");
	 	 			}
	 			}
  			}
	    }

		
		public void handlePostAdServer(){
		    //	if (!this.hasPostAdRun &&  this.postTurnMessage.length() > 0){  //chartboost dismiss and close both call this, lets make sure its not run twice
	    		 	
		    		//make sure the pre-primed hintlist is cleared
		    	//	this.placedResultsForWordHints.clear();
				//	this.hints.clear();

		    	this.hasPostAdRun = true;
	    		// 	this.unfreezeButtons();
	    		// 	Logger.d(TAG, "unfreeze handlePostAdServer");
	    			if (spinner != null) {
	    		 		spinner.dismiss();
	    		 		spinner = null;
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
	   /*
	   private void handleStartOnClick(){
		   this.handleStartOK();
		   /
	    	this.customDialog = new CustomButtonDialog(this, 
	    			this.getString(R.string.game_surface_start_game_confirmation_title), 
	    			this.getString(R.string.game_surface_start_game_confirmation_text),
	    			this.getString(R.string.yes),
	    			this.getString(R.string.no),
	    			Constants.RETURN_CODE_CUSTOM_DIALOG_START_OK_CLICKED,
					Constants.RETURN_CODE_CUSTOM_DIALOG_START_CANCEL_CLICKED,
					Constants.RETURN_CODE_CUSTOM_DIALOG_START_CLOSE_CLICKED);
	    
	    	this.customDialog.show();	
		/
   	   }
	*/

	   private void handleCancelOK(){
		   GameService.cancel(this.getPlayer(), this.game);
		   
			((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_MAIN);
	 		this.finish();

	   }
	   
	   private void handleStartOnClick(){
		 //make countdown timer a variable so that it can be killed in stop/pause
		   //change game status to started (5)
		   //ApplicationContext.captureTime(TAG, "handleStartOnClick called");
		   
	   		if (this.game.isSpeedRoundType()){
    			if (!StoreService.isSpeedRoundsPurchased(this)){
    				PlayerService.removeAFreeUseFromSpeedRounds(this);
    			}
    		}
	   		else if (this.game.isDoubleTimeType()){
    			if (!StoreService.isDoubleTimePurchased(this)){
    				PlayerService.removeAFreeUseFromDoubleTime(this);
    			}
    		}
    		
	   //		ApplicationContext.captureTime(TAG, "startGame about to be called");
		   GameService.startGame(this.game);
		   
		//   ApplicationContext.captureTime(TAG, "startGame completed");

		   this.loadButtons();
		   
		  // ApplicationContext.captureTime(TAG, "loadbuttons completed");
			
		   //might need a start delay here at some point
	/*	   int timerLength = this.getResources().getInteger(R.integer.gameTypeDashCountdownStart); //2 minutes
		   if (this.game.isDoubleTimeType()){
			   timerLength = this.getResources().getInteger(R.integer.gameTypeDoubleTimeCountdownStart);
		   }
		   else if(this.game.isSpeedRoundType()){
			   timerLength = this.getResources().getInteger(R.integer.gameTypeSpeedRoundsCountdownStart);
		   }
		*/   
		   //ready set go!!
		   
		   this.onStartReady();
		   
		   
		 //  this.setCountdown(this.getTimerStart());
		   
		   ApplicationContext.captureTime(TAG, "setCountdown completed");
				
	   }
	   
	   private void onStartReady(){
		   new CustomToast(this, this.getString(R.string.game_surface_start_game_ready), 500, Constants.RETURN_CODE_CUSTOM_TOAST_READY_FINISHED).show();
	   }
	   private void onStartSet(){
		   new CustomToast(this, this.getString(R.string.game_surface_start_game_set), 500, Constants.RETURN_CODE_CUSTOM_TOAST_SET_FINISHED).show();
	   }
	   private void onStartGo(){
		   new CustomToast(this, this.getString(R.string.game_surface_start_game_go), 500, Constants.RETURN_CODE_CUSTOM_TOAST_GO_FINISHED).show();
	   }
	   private void onStartGoFinished(){
		   this.setCountdown(this.getTimerStart());
	   }
	   
	   
	   private int getTimerStart(){
		   int timerLength = this.getResources().getInteger(R.integer.gameTypeDashCountdownStart); //2 minutes
		   if (this.game.isDoubleTimeType()){
			   timerLength = this.getResources().getInteger(R.integer.gameTypeDoubleTimeCountdownStart);
		   }
		   else if(this.game.isSpeedRoundType()){
			   timerLength = this.getResources().getInteger(R.integer.gameTypeSpeedRoundsCountdownStart);
		   }
		   return timerLength;
	   }
	   
	   private void setCountdown(long startingPointInMilliseconds){
		   Logger.d(TAG, "setCountdown startingPointInMilliseconds=" + startingPointInMilliseconds);
		   
		   this.countdown = new CountDownTimer(startingPointInMilliseconds, 10) {

			   
		     public void onTick(long millisUntilFinished) {
		    	 if (millisUntilFinished%10 == 0) {
			    	 int secondsUntilFinished = (int) millisUntilFinished / 1000;
			    	 int minutesLeft = (int) secondsUntilFinished / 60;
			    	 int secondsLeft = secondsUntilFinished - (minutesLeft * 60);
			    
			    	 GameSurface.this.game.setCountdown(millisUntilFinished);
			    	 
			    	 if (minutesLeft == 0 && secondsLeft < 11){
			    		 GameSurface.this.tvCountdown.setTextColor(Color.parseColor(GameSurface.this.getString(R.color.countdown_finish_text_color)));
			    	 }
			    	
			    	 GameSurface.this.tvCountdown.setText(String.format(GameSurface.this.getString(R.string.scoreboard_countdown), minutesLeft, String.format("%02d", secondsLeft)));
			    	 
		    	 }
		    	 else if(millisUntilFinished == 10){
		    		 GameSurface.this.tvCountdown.setText(GameSurface.this.getString(R.string.scoreboard_countdown_completed));
		    		 GameSurface.this.tvCountdown.setTextColor(Color.parseColor(GameSurface.this.getString(R.color.countdown_text_color)));
		    	 }
		    	 
		     }

		     
		     public void onFinish() {
		    	  Logger.d(TAG, "setCountdown onFinish");
		    	  GameSurface.this.isCountdownRunning = false;
		    	  GameSurface.this.freezeAction = true;
		    	  
		    	  GameSurface.this.game.setCountdown(0);
				   
		        // mTextField.setText("done!");
		    	 //GameSurface.this.restartCountdownView();
		    	 //GameSurface.this.tvCountdown.setText(GameSurface.this.getString(R.string.scoreboard_countdown_completed));
	    		 //GameSurface.this.tvCountdown.setTextColor(Color.parseColor(GameSurface.this.getString(R.color.countdown_text_color)));
		    	 
		    	 if (GameSurface.this.game.isSpeedRoundType()){
		    		GameSurface.this.handleRound(GameSurface.this.game.getRound() + 1); 
		    	 }
		    	 else{
		    		 GameSurface.this.handleCompletion();
		    	 }
		     }
		  };
		  
		  
		  this.countdown.start();
		  this.freezeAction = false;
		  this.isCountdownRunning = true;
		  
		  if (this.game.isSpeedRoundType()){
			  this.tvShortInfo.setText(String.format(this.getString(R.string.round_title), this.game.getRound()));
		  }
  	
		  Logger.d(TAG, "setCountdown autoplay about to be called");
		  this.autoPlay();
	   }
	   
	   private void restartCountdownView(){
		   Logger.d(TAG, "restartCountdownView");
		   this.tvCountdown.setText(this.getString(R.string.scoreboard_countdown_completed));
  		 	this.tvCountdown.setTextColor(Color.parseColor(this.getString(R.color.countdown_text_color)));
	   }
	   
		 private class handleCountdownViewState implements Runnable {
			 private int round; //1 = shuffle, 2 = recall 	
			 
			 public handleCountdownViewState(int round){
			 		this.round = round;
			 	}
			 
			    public void run() {
			    	if (this.round == 2 || this.round == 3){
			    		   GameSurface.this.tvCountdown.setText(GameSurface.this.getString(R.string.scoreboard_countdown_completed));
			    		   GameSurface.this.tvCountdown.setTextColor(Color.parseColor(GameSurface.this.getString(R.color.countdown_text_color)));
			    	}
			    	else {
						 Button bRematch = (Button) GameSurface.this.findViewById(R.id.bRematch);
						 bRematch.setVisibility(View.VISIBLE);
						 bRematch.setText(String.format(GameSurface.this.getString(R.string.game_surface_rematch), GameSurface.this.game.getOpponent().getName()));
						 bRematch.setClickable(true);
						 bRematch.setOnClickListener(GameSurface.this);
						 GameSurface.this.tvCountdown.setVisibility(View.GONE);

			    	}
			    }
		  }
	   
	   private void handleCompletion(){

		   //this.restartCountdownView();
		  // Logger.d(TAG, "handleCompletion this.tvCountdown=" + this.tvCountdown.getText());

		   this.isCompletedThisSession = true;
		   GameService.completeGame(this, this.player, this.game);
		   this.setCompletedState();
		   
		   if (this.autoPlayTask != null){
				this.autoPlayTask.stop();
	    		this.autoPlayTask = null;
	    	}
		   if (this.preloadTask != null){
	    		this.preloadTask = null;
	    	}
		   
		   this.possibleWords.clear();
		   this.opponentWords.clear();
		   
		   //handle Ad or purchase reminder
		   this.handleInterstitialAd();
		   
		   
	   }
	   

	   private void handleRound(int round){
		   //show spinner
		   //update  tray
		   //update possible words
		   //update UI
		   //update game round
		   
		   
		   if (round == 3 || round == 2) {
			   //this.restartCountdownView();   
			   this.currentWord.clear();
			   this.game.setRound(round);
			   if (this.autoPlayTask != null){
					this.autoPlayTask.stop();
		    		this.autoPlayTask = null;
		    	}
		    	
			   //reset hopper
			   this.game.setHopper(AlphabetService.getRaceLetters());
			   
			   GameService.saveGame(this.game);
			   this.initializeHopper();
			   this.initializePlayedTiles();
			   this.initializeTray();
			   this.possibleWords.clear();
			   this.opponentWords.clear();
			   
				this.tvCountdown.setText(this.getString(R.string.scoreboard_countdown_speed_round_start));
				this.tvCountdown.setTextColor(Color.parseColor(this.getString(R.color.countdown_text_color)));
			   String message = this.getString(R.string.progress_setting_up_round_2);
			   if (round == 3){
				   message = this.getString(R.string.progress_setting_up_round_3);
			   }
			   this.game.setNumWordsPlayedThisRound(0);
			   
			   this.setupPreloadTask(message);
		   }
		   else{
			   this.handleCompletion();
		   }
		   
		   
		   //kick off game again
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
			TextView tvOpponentWordListTitle = (TextView)findViewById(R.id.tvOpponentWordListTitle);
			TextView tvPlayerWordListTitle = (TextView)findViewById(R.id.tvPlayerWordListTitle);
			TextView tvWinner = (TextView)this.findViewById(R.id.tvWinner);
			TextView tvWordsLeft = (TextView)this.findViewById(R.id.tvWordsLeft);
			TextView tvType = (TextView)this.findViewById(R.id.tvType);
			Button bRematch = (Button)this.findViewById(R.id.bRematch);
			
			bShuffle.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bPlay.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bCancel.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bRecall.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bStart.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			tvOpponentWordListTitle.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			tvPlayerWordListTitle.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			tvWinner.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			tvWordsLeft.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			tvType.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bRematch.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			this.tvShortInfo.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
		}
		
		private class PlayedWordAdapter extends ArrayAdapter<PlayedWord> {
		   	  private final GameSurface context;
		   	  private List<PlayedWord>  values;
		   	  private  int wordCount;
		   	  LayoutInflater inflater;

		   	  
		   	//  public ArrayList<Integer> selectedIds = new ArrayList<Integer>();

		   	  public PlayedWordAdapter(GameSurface context, List<PlayedWord> values) {
		   		  	super(context, R.layout.wordlistitem, values); 
		    	    this.context = context;
		    	    this.values = values;
		    	    this.wordCount = values.size();
		    	    
		    	    this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	  }

			   	 
		    	  @Override
		    	  public View getView(int position, View rowView, ViewGroup parent) {
		    		 
		    		  if ( rowView == null ) {
		    			  rowView = inflater.inflate(R.layout.wordlistitem, parent, false);
		    		  }
		    		  
			    	   PlayedWord word = values.get(position);
			    	 
			    	   TextView tvWord = (TextView) rowView.findViewById(R.id.tvWord);
			    	   TextView tvPoints = (TextView) rowView.findViewById(R.id.tvPoints);

			    	   
			    	   tvWord.setTypeface(ApplicationContext.getScoreboardFontTypeface());
			    	   tvPoints.setTypeface(ApplicationContext.getScoreboardFontTypeface());
 
			    	   tvWord.setText(word.getWord());
			    	   tvPoints.setText(String.valueOf(word.getPointsScored()));
		 	     	   
			    	   rowView.setTag(word.getWord());
			    	   return rowView;
		    	  }
		    	
			} 		  
		 
		     
}
