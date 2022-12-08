package course.examples.Services.KeyClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import course.examples.Services.KeyCommon.MusicPlayer;

// MusicClient, App for calling the ClipServer's Api to play the songs
// The only Activity in the App
public class MusicServiceClient extends Activity {

	// codes for permissions
	protected static final int APP_REQUEST = 0;
	protected static final int NOTIFICATION_REQUEST = 1;

	private MusicPlayer mMusicPlayerService;  // service responsible for playing music
	private boolean mIsStarted = false;
	private boolean mIsBound = false;
	EditText songNumField;  // enter track_id here
	Button startService;
	Button playButton;
	Button pauseButton;
	Button resumeButton;
	Button stopButton;
	Button stopService;
	TextView listSongs;
	Intent musicServiceIntent;


	// handler for unbinding from the service when a song stops playing
	private final Handler mHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(Message msg) {
			try {
				stopMusic();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView title = (TextView) findViewById(R.id.textView1);
		songNumField = (EditText) findViewById(R.id.textView2);
		listSongs = (TextView) findViewById(R.id.textView3);
		listSongs.setMovementMethod(new ScrollingMovementMethod());  // scrolling song list

		startService = findViewById(R.id.startService);
		playButton = findViewById(R.id.play);
		pauseButton = findViewById(R.id.pause);
		resumeButton = findViewById(R.id.resume);
		stopButton = findViewById(R.id.stop);
		stopService = findViewById(R.id.stopService);

		resetButtons();  // default button configuartion before service has started

		// constructing the intent to start the service
		musicServiceIntent = new Intent(MusicPlayer.class.getName());
		ResolveInfo info = getPackageManager().resolveService(musicServiceIntent, PackageManager.MATCH_ALL);
		musicServiceIntent.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

		// event handler for starting the service
		startService.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// check two permissions first
				if (checkSelfPermission("course.examples.Services.KeyService.GEN_ID")
						!= PackageManager.PERMISSION_GRANTED) {
					requestPermissions(new String[]{"course.examples.Services.KeyService.GEN_ID"},
							APP_REQUEST);
				} else if (checkSelfPermission("android.permission.POST_NOTIFICATIONS")
						== PackageManager.PERMISSION_DENIED) {
					requestPermissions(new String[] {"android.permission.POST_NOTIFICATIONS"}, NOTIFICATION_REQUEST);
				} else {  // if all permissions present, then call helper to start the service
					startService();
				}
			}
		});

		// event handler for binding to the service and then playing the music
		playButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (!mIsBound) {  // bind if not already bound
					checkBindingAndBind();
				} else {
					startMusic();
				}
			}
		});

		// event handler for pausing the music that should be currently playing
		pauseButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					mMusicPlayerService.pause();  // remote api call
					pauseButton.setEnabled(false);
					resumeButton.setEnabled(true);
				} catch (RemoteException e) {  // if service gets terminated somehow by the operating service
					mIsBound = false;
					mIsStarted = false;
					resetButtons();
				}
			}
		});

		// event handler for resuming the paused music
		resumeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					mMusicPlayerService.resume();  // remote api call
					resumeButton.setEnabled(false);
					pauseButton.setEnabled(true);
				} catch (RemoteException e) {
					mIsBound = false;
					mIsStarted = false;
					resetButtons();
				}
			}
		});

		// event handler for stopping the music
		stopButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					stopMusic();  // helper for stopping the music
				} catch (RemoteException e) {
					mIsBound = false;
					mIsStarted = false;
					resetButtons();
				}
			}
		});

		// event handler for stopping the service using a Dialog
		stopService.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (mIsStarted) {
					createDialog();  // create and show the dialogue
				}
			}
		});
	}

	// helper for stopping the
	public void stopMusic() throws RemoteException {
		unbindService();  // then unbinds from service
		resumeButton.setEnabled(false);
		pauseButton.setEnabled(false);
		stopButton.setEnabled(false);
	}

	// create and show the dialogue to stop the service
	public void createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MusicServiceClient.this);
		builder.setMessage("Warning!!! \nMusic will Stop Playing")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if user clicks on yes, then unbind from service, and then stop the service
						unbindService();
						stopService(musicServiceIntent);
						mIsStarted = false;
						resetButtons();
					}
				})
				.setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}
				});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	// sets the textview with all the names of the songs
	// and highlights the one that is playing
	public void populateListSongs(int track_id) {
		try {
			String[] songs = mMusicPlayerService.getSongs();  // api call, returns song names
			String text = "Now Playing\n\n";
			for (int i = 0; i < songs.length; i++) {
				if (i + 1 == track_id) {
					text += "-> ";
				}
				text += i + 1 + " " + songs[i].toUpperCase(Locale.ROOT) + "\n";
			}
			listSongs.setText(text);
		} catch (RemoteException e){
			mIsBound = false;
			mIsStarted = false;
			resetButtons();
		}
	}

	// starts the service using startForegroundService
	protected void startService() {
		if (!mIsStarted) {
			startForegroundService(musicServiceIntent);
			mIsStarted = true;
			startService.setEnabled(false);
			playButton.setEnabled(true);
			stopService.setEnabled(true);
		}
	}

	// helper to unbind from the service
	protected void unbindService() {
		try {
			if (mIsBound) {
				listSongs.setText("After Service is Bound, songs will appear here");
				mMusicPlayerService.stop();  // first stops the music
				unbindService(this.mConnection);  // then unbinds from service
				mIsBound = false;
			}
		} catch (RemoteException e) {
			mIsBound = false;
			mIsStarted = false;
			resetButtons();
		}

	}

	// helper to bind to the service if not bound already
	protected void checkBindingAndBind() {
		if (!mIsBound) {
			boolean b ;
			Intent i = new Intent(MusicPlayer.class.getName());
			ResolveInfo info = getPackageManager().resolveService(i, PackageManager.MATCH_ALL);
			i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));
			b = bindService(i, this.mConnection, Context.BIND_AUTO_CREATE);
			if (b) {
				Toast.makeText(this, "BindService() succeeded!", Toast.LENGTH_LONG).show() ;
			} else {
				Toast.makeText(this, "BindService() failed!", Toast.LENGTH_LONG).show() ;
			}
		}
	}

	// callback for user permission selection
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults) ;
		switch (requestCode) {
			case APP_REQUEST: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Permission granted, go ahead and bind to service
					if (checkSelfPermission("android.permission.POST_NOTIFICATIONS")
							== PackageManager.PERMISSION_DENIED) {
						requestPermissions(new String[] {"android.permission.POST_NOTIFICATIONS"}, NOTIFICATION_REQUEST) ;
					}
				} else {
					Toast.makeText(this, "No Permission for Service", Toast.LENGTH_LONG).show() ;
				}
				break;
			}
			case NOTIFICATION_REQUEST: {
				if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					startService();
				}
				else {
					Toast.makeText(this, "No Permission for Notification", Toast.LENGTH_LONG).show() ;
				}
			}
		}
	}

	// when activity destroyed, unbind and stop service
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService();
		stopService(musicServiceIntent);
	}

	// helper for resetting the buttons
	public void resetButtons() {
		startService.setEnabled(true);
		playButton.setEnabled(false);
		pauseButton.setEnabled(false);
		resumeButton.setEnabled(false);
		stopButton.setEnabled(false);
		stopService.setEnabled(false);
	}

	// helper for starting the music
	public void startMusic() {
		try {
			int track_id = Integer.parseInt(songNumField.getText().toString());  // get track_id from textview
			if (track_id <= 0 || track_id > mMusicPlayerService.getSongs().length) {
				throw new NumberFormatException("out of bounds");
			}
			mMusicPlayerService.play(track_id);  // api call
			populateListSongs(track_id);  // populate textview
			MusicCheckerThread thread = new MusicCheckerThread(mMusicPlayerService);  // start the thread
			thread.start();
			pauseButton.setEnabled(true);
			stopButton.setEnabled(true);
		} catch (RemoteException e) {
			mIsBound = false;
			mIsStarted = false;
			resetButtons();
		} catch (NumberFormatException ne) {
			Toast.makeText(getApplicationContext(), "Invalid Track id", Toast.LENGTH_LONG).show() ;
		}
	}


	// ServiceConnection
	private final ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder iservice) {
			mMusicPlayerService = MusicPlayer.Stub.asInterface(iservice);
			mIsBound = true;
			startMusic();
		}
		public void onServiceDisconnected(ComponentName className) {
			mMusicPlayerService = null;
			mIsBound = false;
		}
	};


	// thread for checking if music is still playing, checks every second
	class MusicCheckerThread extends Thread {
		private MusicPlayer mMusicPlayerService;
		MusicCheckerThread(MusicPlayer mMusicPlayerService) {
			this.mMusicPlayerService = mMusicPlayerService;
		}

		public void run() {
			try {
				while(true) {
					Thread.sleep(1000);
					System.out.println("No luck!");
					if (!mMusicPlayerService.isPlaying()) {
						Message msg = mHandler.obtainMessage(0);
						mHandler.sendMessage(msg) ;
						return;
					}
				}
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted!");
			} catch (RemoteException re) {


			}

		}
	}
}



