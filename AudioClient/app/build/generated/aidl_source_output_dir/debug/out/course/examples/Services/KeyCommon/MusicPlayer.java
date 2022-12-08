/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package course.examples.Services.KeyCommon;
public interface MusicPlayer extends android.os.IInterface
{
  /** Default implementation for MusicPlayer. */
  public static class Default implements course.examples.Services.KeyCommon.MusicPlayer
  {
    @Override public void play(int track_num) throws android.os.RemoteException
    {
    }
    @Override public void pause() throws android.os.RemoteException
    {
    }
    @Override public void resume() throws android.os.RemoteException
    {
    }
    @Override public void stop() throws android.os.RemoteException
    {
    }
    @Override public java.lang.String[] getSongs() throws android.os.RemoteException
    {
      return null;
    }
    @Override public boolean isPlaying() throws android.os.RemoteException
    {
      return false;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements course.examples.Services.KeyCommon.MusicPlayer
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an course.examples.Services.KeyCommon.MusicPlayer interface,
     * generating a proxy if needed.
     */
    public static course.examples.Services.KeyCommon.MusicPlayer asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof course.examples.Services.KeyCommon.MusicPlayer))) {
        return ((course.examples.Services.KeyCommon.MusicPlayer)iin);
      }
      return new course.examples.Services.KeyCommon.MusicPlayer.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(descriptor);
      }
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
      }
      switch (code)
      {
        case TRANSACTION_play:
        {
          int _arg0;
          _arg0 = data.readInt();
          this.play(_arg0);
          reply.writeNoException();
          break;
        }
        case TRANSACTION_pause:
        {
          this.pause();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_resume:
        {
          this.resume();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_stop:
        {
          this.stop();
          reply.writeNoException();
          break;
        }
        case TRANSACTION_getSongs:
        {
          java.lang.String[] _result = this.getSongs();
          reply.writeNoException();
          reply.writeStringArray(_result);
          break;
        }
        case TRANSACTION_isPlaying:
        {
          boolean _result = this.isPlaying();
          reply.writeNoException();
          reply.writeInt(((_result)?(1):(0)));
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements course.examples.Services.KeyCommon.MusicPlayer
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public void play(int track_num) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(track_num);
          boolean _status = mRemote.transact(Stub.TRANSACTION_play, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void pause() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_pause, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void resume() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_resume, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void stop() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public java.lang.String[] getSongs() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        java.lang.String[] _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getSongs, _data, _reply, 0);
          _reply.readException();
          _result = _reply.createStringArray();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public boolean isPlaying() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        boolean _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_isPlaying, _data, _reply, 0);
          _reply.readException();
          _result = (0!=_reply.readInt());
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_play = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_pause = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_resume = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_stop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_getSongs = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    static final int TRANSACTION_isPlaying = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
  }
  public static final java.lang.String DESCRIPTOR = "course.examples.Services.KeyCommon.MusicPlayer";
  public void play(int track_num) throws android.os.RemoteException;
  public void pause() throws android.os.RemoteException;
  public void resume() throws android.os.RemoteException;
  public void stop() throws android.os.RemoteException;
  public java.lang.String[] getSongs() throws android.os.RemoteException;
  public boolean isPlaying() throws android.os.RemoteException;
}
