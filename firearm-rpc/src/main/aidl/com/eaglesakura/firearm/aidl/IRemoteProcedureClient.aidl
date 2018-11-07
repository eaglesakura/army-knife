// IRemoteProcedureClient.aidl
package com.eaglesakura.firearm.aidl;

import android.os.Bundle;
import com.eaglesakura.firearm.aidl.IRemoteProcedureServer;

interface IRemoteProcedureClient {

    /**
     * an AIDL Server post data to client.
     */
    Bundle requestFromServer(in Bundle arguments);
}
