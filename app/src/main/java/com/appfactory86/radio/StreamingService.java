package com.appfactory86.radio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StreamingService extends Service {
    public StreamingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}