package com.riotapps.wordrace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;
import com.riotapps.wordrace.R;
import com.riotapps.wordrace.hooks.GameService;
import com.riotapps.wordbase.GameHistory;
import com.riotapps.wordbase.hooks.AlphabetService;
import com.riotapps.wordbase.hooks.Opponent;
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
import com.riotapps.wordbase.ui.GameSurfaceView;
import com.riotapps.wordbase.ui.MenuUtils;
import com.riotapps.wordbase.utils.ApplicationContext;
import com.riotapps.wordbase.utils.Constants;
import com.riotapps.wordbase.utils.ImageHelper;
import com.riotapps.wordbase.utils.Logger;
import com.riotapps.wordbase.utils.PreconditionException;
import com.riotapps.wordbase.utils.Utils;
import com.riotapps.wordrace.hooks.Game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameSurface  extends FragmentActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ICloseDialog{
	private static final String TAG = GameSurface.class.getSimpleName();
	
	 private PopupMenu popupMenu;
	 private String gameId = ""; 
	 
	 private Tracker tracker;
	 private Player player;
	 private Game game;
	 private boolean hideInterstitialAd;
	 private boolean isChartBoostActive;
	 private boolean isAdMobActive;
	 private Chartboost cb;
	 private CountDownTimer countdown = null;
	 private TextView tvCountdown;
	 private TextView tvNumPoints;
	 private TextView tvWordsLeft;
	 private TextView tvOpponentScore;
	 private TextView tvPlayerScore;
	 private ListView lvOpponent;
	 private ListView lvPlayer;
	 private PlayedWordAdapter playerListadapter;
 
	private	LayoutInflater inflater;

	 private CustomButtonDialog customDialog = null; 
	private List<String> possibleWords = new ArrayList<String>();
	private PreLoadTask preloadTask = null;
	private int letterTileSize;
	private int playedLetterTileSize;
	
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
	 private List<String> hopperState = new ArrayList<String>();
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
		
		this.setGameId();
	 	this.game = GameService.getGame(gameId); //(Game) i.getParcelableExtra(Constants.EXTRA_GAME);
		
	 	this.loadViews();
	 	this.setupMenu();
	 	this.setupGame();
	 	this.loadLetterViews();
	 	this.loadButtons();
	 	this.initializePlayerWordLists();
	}
	 

	private void loadButtons(){
		Button bStart = (Button)this.findViewById(R.id.bStart);
		Button bCancel = (Button)this.findViewById(R.id.bCancel);
		Button bShuffle = (Button)this.findViewById(R.id.bShuffle);
		Button bRecall = (Button)this.findViewById(R.id.bRecall);
		Button bPlay = (Button)this.findViewById(R.id.bPlay);
		
		bStart.setOnClickListener(this);
		bCancel.setOnClickListener(this);
		bShuffle.setOnClickListener(this);
		bRecall.setOnClickListener(this);
		bPlay.setOnClickListener(this);
		
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
		
		}
	}
	
	private void loadViews(){
		this.inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.tvCountdown = (TextView)this.findViewById(R.id.tvCountdown);
		this.tvNumPoints = (TextView)this.findViewById(R.id.tvNumPoints);
		this.tvWordsLeft = (TextView)this.findViewById(R.id.tvWordsLeft);
		this.tvOpponentScore = (TextView)this.findViewById(R.id.tvOpponentScore);
		this.tvPlayerScore = (TextView)this.findViewById(R.id.tvPlayerScore);
		this.lvOpponent = (ListView)this.findViewById(R.id.lvOpponent);
		this.lvPlayer = (ListView)this.findViewById(R.id.lvPlayer);
		//this.llPlayerWords = (LinearLayout)this.findViewById(R.id.llPlayerWords);
		
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
			if (position == 6) {
				return GameSurface.getPlayedEmpty_6(context, tileSize);
			}
			else if (position == 8) {
				return GameSurface.getPlayedEmpty_8(context, tileSize);
			}
			else if (position == 10) {
				return GameSurface.getPlayedEmpty_10(context, tileSize);
			}
			else {
				return GameSurface.getPlayedEmpty(context, tileSize);
			}
		}

 	}
	
	private void setupGame(){
		// Logger.d(TAG,"setupGame game turn=" + this.game.getTurn());
		spinner = new CustomProgressDialog(this);
		spinner.setMessage(this.getString(R.string.progress_loading));
		spinner.show();
		
		this.preloadTask = new PreLoadTask();
		this.preloadTask.execute();
		
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
			// if (GameSurface.this.game.isActive()){
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
				      
					//save game to save the total number of possibilities
					 if (GameSurface.this.game.getNumPossibleWords() == 0){
						 GameSurface.this.game.setNumPossibleWords(GameSurface.this.possibleWords.size());
						 GameService.saveGame(GameSurface.this.game);
					 }
	    	 
	    	 return null;

	     }

	   //  protected void onProgressUpdate(Integer... progress) {
	   //      setProgressPercent(progress[0]);
	   //  }
	     protected void onPostExecute(Void param) {
	    	 if (GameSurface.this.game.isActive()){
	    		 TextView tvWordsLeft = (TextView)GameSurface.this.findViewById(com.riotapps.wordrace.R.id.tvWordsLeft); 
		    	 if (GameSurface.this.game.getHopper().size() == 1){
			 		 tvWordsLeft.setText(com.riotapps.wordrace.R.string.scoreboard_1_word_left); 
				 }
				 else{
					 tvWordsLeft.setText(String.format(GameSurface.this.getString(com.riotapps.wordrace.R.string.scoreboard_words_left), GameSurface.this.game.getNumPossibleWords() - GameSurface.this.game.getPlayedWords().size()));
				 }
	    	 }
	    	 if (GameSurface.this.spinner != null){
	    		 GameSurface.this.spinner.dismiss();
	    		 GameSurface.this.spinner = null;
	 		}
	     }

	 
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
	 			
	 			this.handleStartOK(); 
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
	 		}
  
	}

	@Override
	public void dialogClose(int resultCode, String returnValue) {
		// TODO Auto-generated method stub
		
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
	 	if (v.getId() == R.id.bStart) {
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
		ivPlayedLetter6.setImageBitmap(GameSurface.getPlayedEmpty_6(this, playedLetterTileSize));
		ivPlayedLetter7.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));
		ivPlayedLetter8.setImageBitmap(GameSurface.getPlayedEmpty_8(this, playedLetterTileSize));
		ivPlayedLetter9.setImageBitmap(GameSurface.getPlayedEmpty(this, playedLetterTileSize));
		ivPlayedLetter10.setImageBitmap(GameSurface.getPlayedEmpty_10(this, playedLetterTileSize));

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
		}
		for (String letter : this.game.getHopper()){
			this.hopperState.add(letter);
		}
	}
	
	private void handleRecall(){
		
		this.initializePlayedTiles();
		this.initializeHopper();
		this.initializeTray();
		this.currentWord.clear();
		this.updatePointsView();
		
		//reset points
		
	}
	private void updatePointsView(){
		int points = this.calculatePoints();
		
		if (points > 0){
 			tvNumPoints.setText(String.format(this.getString(R.string.scoreboard_num_points),points));
 			tvNumPoints.setVisibility(View.VISIBLE);
		}
		else{
 			tvNumPoints.setVisibility(View.INVISIBLE);
		}
	}
	
	
	private int calculatePoints(){
		
		if (this.currentWord.size() < 3){ return 0; }
		
		String word = "";
		
		int points = 0;
		
		for (String letter : this.currentWord){
			points += AlphabetService.getLetterValue(letter);
			word += letter;
		}
		
		//if not a valid word no points are calculated
		if (!this.possibleWords.contains(word.toLowerCase())) { return 0; }
		
		if (this.currentWord.size() == 10){
			points += 30; ///make this a constant on int resource
		}
		else if (this.currentWord.size() >= 8){
			points += 15;
		}
		else if (this.currentWord.size() >= 6){
			points += 5;
		}
		
		return points;
	}
	
	private void handlePlay(){
		//<string name="game_surface_race_word_too_short">words must be at least 3 letters long</string>
		// <string name="game_surface_race_word_opponent_played">%1$s has already played %2$s</string>	
		// <string name="game_surface_race_word_already_played">you have already played %2$s</string> 
		// <string name="game_surface_race_word_invalid">%1$s has already played %2$s</string>
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
		int points = this.calculatePoints();
		
		this.handleRecall();
		this.wordsPlayedByPlayer.add(word);
		 
		
		final PlayedWord playedWord = new PlayedWord();
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
		/* runOnUiThread(new Runnable() {
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
	 	
		this.tvWordsLeft.setText(String.format(this.getString(com.riotapps.wordrace.R.string.scoreboard_words_left), game.getNumPossibleWords() - game.getPlayedWords().size()));
 
		//save game upon completion, not here
	}
	 

	
	private void initializePlayerWordLists(){

	 	this.playerListadapter = new PlayedWordAdapter(this, this.wordListPlayedByPlayer);

		this.lvPlayer.setAdapter(this.playerListadapter); 
	 
	}
	
	private void handleShuffle(){
		//only allow shuffle if all letters are in tray?
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
		 		else if (isAdMob){
		 			this.isAdMobActive = true;
		 		}
		 	}
		 	else{
		 		this.hideInterstitialAd = true;
		 	}
		 		 		
		 	
		 	Logger.d(TAG,  " chartBoost=" + this.isChartBoostActive);
		}
	
		public void handlePostAdServer(){
		    //	if (!this.hasPostAdRun &&  this.postTurnMessage.length() > 0){  //chartboost dismiss and close both call this, lets make sure its not run twice
	    		 	
		    		//make sure the pre-primed hintlist is cleared
		    	//	this.placedResultsForWordHints.clear();
				//	this.hints.clear();

		    	//	this.hasPostAdRun = true;
	    		// 	this.unfreezeButtons();
	    		// 	Logger.d(TAG, "unfreeze handlePostAdServer");
	    		// 	if (spinner != null) {
	    		// 		spinner.dismiss();
	    		// 		spinner = null;
	    		// 	}
	    		// 	DialogManager.SetupAlert(context, this.postTurnTitle , this.postTurnMessage);
	    		 	
	    		 	//lets prime the pump for the next play
	    		 	if (this.game.isActive()){
	    		/* 		this.preAutoplayTask = new PreAutoplayTask();
	    		 		this.preAutoplayTask.execute();
	    		 		
	    		 		if(this.wordHintTask != null){
	    		 			this.isWordHintTaskRunning = false;
	    		 			this.wordHintTask.cancel(true);
	    		 			this.wordHintTask = null;
	    		 		}
	    		 		this.wordHintTask =  new WordHintTask();
	    		 		this.wordHintTask.execute(); */
	    		 	}
	    		 	else if (this.game.isCompleted()){
	    		 		//save completed event
	    		 	/*	if (this.game.getPlayerGames().get(0).getScore() == this.game.getPlayerGames().get(1).getScore()){
	    		 			this.trackEvent(Constants.TRACKER_ACTION_GAME_COMPLETED, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID_DRAW, this.game.getOpponentId()), (int) Constants.TRACKER_SINGLE_VALUE);

	    		 		}
	    		 		else if (this.game.getPlayerGames().get(0).getScore() > this.game.getPlayerGames().get(1).getScore()){
	    		 			this.trackEvent(Constants.TRACKER_ACTION_GAME_COMPLETED, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID_LOSS, this.game.getOpponentId()), this.game.getPlayerGames().get(0).getScore() - this.game.getPlayerGames().get(1).getScore());

	    		 		}
	    		 		else {
	    		 			this.trackEvent(Constants.TRACKER_ACTION_GAME_COMPLETED, String.format(Constants.TRACKER_LABEL_OPPONENT_WITH_ID_WON, this.game.getOpponentId()), this.game.getPlayerGames().get(1).getScore() - this.game.getPlayerGames().get(0).getScore());

	    		 		}*/
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
	   
	   private void handleStartOnClick(){
	    	this.customDialog = new CustomButtonDialog(this, 
	    			this.getString(R.string.game_surface_start_game_confirmation_title), 
	    			this.getString(R.string.game_surface_start_game_confirmation_text),
	    			this.getString(R.string.yes),
	    			this.getString(R.string.no),
	    			Constants.RETURN_CODE_CUSTOM_DIALOG_START_OK_CLICKED,
					Constants.RETURN_CODE_CUSTOM_DIALOG_START_CANCEL_CLICKED,
					Constants.RETURN_CODE_CUSTOM_DIALOG_START_CLOSE_CLICKED);
	    
	    	this.customDialog.show();	

   	   }

	   private void handleCancelOK(){
		   GameService.cancel(this.getPlayer(), this.game);
		   
			((ApplicationContext)this.getApplication()).startNewActivity(this, Constants.ACTIVITY_CLASS_MAIN);
	 		this.finish();

	   }
	   
	   private void handleStartOK(){
		 //make countdown timer a variable so that it can be killed in stop/pause
		   //change game status to started (5)
		   GameService.startGame(this.game);
		   this.loadButtons();
			
		   //might need a start delay here at some point
		   
		   this.countdown = new CountDownTimer(180000, 1000) {

			     public void onTick(long millisUntilFinished) {
			    	 int secondsUntilFinished = (int) millisUntilFinished / 1000;
			    	 int minutesLeft = (int) secondsUntilFinished / 60;
			    	 int secondsLeft = secondsUntilFinished - (minutesLeft * 60);
			    
			         GameSurface.this.tvCountdown.setText(String.format(GameSurface.this.getString(R.string.scoreboard_countdown), minutesLeft, String.format("%02d", secondsLeft)));
			     }

			     public void onFinish() {
			        // mTextField.setText("done!");
			    	 GameSurface.this.tvCountdown.setText(GameSurface.this.getString(R.string.scoreboard_countdown_completed));
			     }
			  };
			  
			  this.countdown.start();
	 		 

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

			bShuffle.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bPlay.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bCancel.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bRecall.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
			bStart.setTypeface(ApplicationContext.getScoreboardButtonFontTypeface());
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
			     	   
			    	   tvWord.setTypeface(ApplicationContext.getScoreboardFontTypeface());
 
			    	   tvWord.setText(word.getWord());
		 	     	   
			    	   rowView.setTag(word.getWord());
			    	   return rowView;
		    	  }
		    	
			} 		  
		 
		     
}
