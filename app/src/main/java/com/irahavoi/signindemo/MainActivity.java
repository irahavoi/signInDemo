package com.irahavoi.signindemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

public class MainActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private Button signOutButton;
    private Button revokeAccessButton;
    private TextView signInStatusTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInButton = (SignInButton) findViewById(R.id.signInBtn);
        signOutButton = (Button) findViewById(R.id.signOutBtn);
        revokeAccessButton = (Button) findViewById(R.id.revokeAccessBtn);
        signInStatusTextView = (TextView) findViewById(R.id.sign_in_status);

        googleApiClient = buildGoogleApiClient();


    }

    private GoogleApiClient buildGoogleApiClient(){
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(new Scope(Scopes.PROFILE))
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        if(!googleApiClient.isConnecting()){
            //Handling clicks only when google api client is not transferring between connected and not connected
            switch (v.getId()){
                case R.id.signInBtn:
                    signInStatusTextView.setText("Signing In..");
                    resolveSignInError();
                    break;
                case R.id.signOutBtn:
                    Plus.AccountApi.clearDefaultAccount(googleApiClient);
                    googleApiClient.disconnect();
                    googleApiClient.connect();
                    break;
                case R.id.revokeAccessBtn:
                    Plus.AccountApi.clearDefaultAccount(googleApiClient);
                    Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient);
                    googleApiClient = buildGoogleApiClient();
                    googleApiClient.connect();

            }
        }
    }
}
