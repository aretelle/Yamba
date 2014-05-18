/* $Id: $
   Copyright 2012, G. Blake Meike

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.twitter.university.android.yamba.sync;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.os.Bundle;

import com.twitter.university.android.yamba.YambaApplication;

/**
 * @author <a href="mailto:blake.meike@gmail.com">G. Blake Meike</a>
 * @version $Revision: $
 */
public class AccountMgr extends AbstractAccountAuthenticator {
    private final YambaApplication app;

    public AccountMgr(YambaApplication app) {
        super(app);
        this.app = app;
    }

    @Override
    public Bundle addAccount(
        AccountAuthenticatorResponse accountAuthenticatorResponse,
        String s,
        String s2,
        String[] strings,
        Bundle bundle) throws NetworkErrorException
    {
        throw new UnsupportedOperationException("Update credentials not supported.");
    }

    @Override
    public Bundle getAuthToken(
        AccountAuthenticatorResponse accountAuthenticatorResponse,
        Account account,
        String s,
        Bundle bundle) throws NetworkErrorException
    {
        throw new UnsupportedOperationException("Update credentials not supported.");
    }

    @Override
    public Bundle editProperties(
        AccountAuthenticatorResponse accountAuthenticatorResponse, String s)
    {
        throw new UnsupportedOperationException("Update credentials not supported.");
    }

    @Override
    public Bundle confirmCredentials(
        AccountAuthenticatorResponse accountAuthenticatorResponse,
        Account account,
        Bundle bundle) throws NetworkErrorException
    {
        throw new UnsupportedOperationException("Update credentials not supported.");
    }

    @Override
    public String getAuthTokenLabel(String s) {
        throw new UnsupportedOperationException("Update credentials not supported.");
    }

    @Override
    public Bundle updateCredentials(
        AccountAuthenticatorResponse accountAuthenticatorResponse,
        Account account,
        String s,
        Bundle bundle) throws NetworkErrorException
    {
        throw new UnsupportedOperationException("Update credentials not supported.");
    }

    @Override
    public Bundle hasFeatures(
        AccountAuthenticatorResponse accountAuthenticatorResponse,
        Account account,
        String[] strings) throws NetworkErrorException
    {
        throw new UnsupportedOperationException("Update credentials not supported.");
    }
}