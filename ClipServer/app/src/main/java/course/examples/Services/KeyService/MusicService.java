package course.examples.Services.KeyService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import course.examples.Services.KeyCommon.MusicPlayer;

// service for playing songs
public class MusicService extends Service {

	private static final int NOTIFICATION_ID = 1;
	private MediaPlayer mPlayer;  // player to play the music
	private static final String CHANNEL_ID = "Music player";
	Notification notification;


	@Override
	public void onCreate() {
		Log.i("KeyGeneratorService", "Service in away!") ;
		super.onCreate();
		// creates the Notification Channel
		this.createNotificationChannel();

		// creates the notification with no action
		notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
				.setSmallIcon(android.R.drawable.ic_media_play)
				.setOngoing(true).setContentTitle("Clip Server")
				.setContentText("Service Connected")
				.setTicker("Music is not playing!")
				.setContentIntent(null)
				.addAction(R.drawable.ic_launcher, "", null)
				.build();

		startForeground(NOTIFICATION_ID, notification);
	}


	// returns the resource number for audio by track_id
	public int getAudio(int track_id) {
		switch(track_id) {
			case 1:
				return R.raw.cinematic;
			case 2:
				return R.raw.dropit;
			case 3:
				return R.raw.guitar;
			case 4:
				return R.raw.lifelike;
			case 5:
				return R.raw.milkshake;
			case 6:
				return R.raw.bounce;
			default:
				return 0;
		}
	}

	// helper for creating the notification channel
	@RequiresApi(api = Build.VERSION_CODES.O)
	private void createNotificationChannel() {
		CharSequence name = "Music player notification";
		String description = "The channel for music player notifications";
		NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
		channel.setDescription(description);
		// Register the channel with the system
		NotificationManager notificationManager = getSystemService(NotificationManager.class);
		notificationManager.createNotificationChannel(channel);
	}


	//	 Implement the Stub for this Object
	private final MusicPlayer.Stub mBinder = new MusicPlayer.Stub() {

	// plays the audio specified by the track_id using the media player
	@Override
	public void play(int track_id) throws RemoteException {
		if (mPlayer != null) {
			mPlayer.stop();
		}
		if (track_id <= 6 && track_id > 0) {
			mPlayer = MediaPlayer.create(getApplicationContext(), getAudio(track_id));
			if (mPlayer != null) {
				mPlayer.setLooping(false);
				mPlayer.start();
			}
		}
	}

	// pauses the song
	@Override
	public void pause() throws RemoteException {
		if (mPlayer != null) {
			mPlayer.pause();
		}
	}

	// resumes the song
	@Override
	public void resume() throws RemoteException {
		if (mPlayer != null) {
			mPlayer.start();
		}
	}

	// stops the song
	@Override
	public void stop() throws RemoteException {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer = null;
		}
	}

	// returns a list of the song names
	@Override
	public String[] getSongs() throws RemoteException {
		String[] songs = new String[6];
		for (int i = 1; i <= 6; i++) {
			songs[i - 1] = getResources().getResourceEntryName(getAudio(i));
		}
		return songs;
	}


	// check if the mPlayer is initialized (meaning song could be played using it)
	@Override
	public boolean isPlaying() throws RemoteException {
		if (mPlayer != null) {
			return true;
		}
		return false;
	}
};

	// Return the Stub defined above
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

}
