package course.examples.Services.KeyCommon;
   interface MusicPlayer {
       void play(int track_num);
       void pause();
       void resume();
       void stop();
       String[] getSongs();
       boolean isPlaying();
   }