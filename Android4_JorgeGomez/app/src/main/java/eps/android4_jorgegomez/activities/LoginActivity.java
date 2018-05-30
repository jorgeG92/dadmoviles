package eps.android4_jorgegomez.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.firedatabase.FBDataBase;
import eps.android4_jorgegomez.model.RoundRepository;
import eps.android4_jorgegomez.model.RoundRepositoryFactory;

public class LoginActivity extends Activity implements View.OnClickListener {

    private RoundRepository repository;
    private EditText usernameEditText;
    private EditText passwordEditText;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ConectPreferenceActivity.getLogged(LoginActivity.this)){
            startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
            finish();
            return;
        }

        usernameEditText = (EditText) findViewById(R.id.login_username);
        passwordEditText = (EditText) findViewById(R.id.login_password);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        Button cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);

        Button newUserButton = (Button) findViewById(R.id.new_user_button);
        newUserButton.setOnClickListener(this);

        Button switchMode = (Button) findViewById(R.id.switch_game);
        switchMode.setOnClickListener(this);

        TextView textLogin = findViewById(R.id.login_text);

        if(ConectPreferenceActivity.getGameMode(this).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT))
            textLogin.setText(R.string.game_offline);
        else
            textLogin.setText(R.string.game_online);


        repository = RoundRepositoryFactory.createRepository(LoginActivity.this,
                ConectPreferenceActivity.getGameMode(this).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT));

        if (repository == null)
            Toast.makeText(LoginActivity.this, R.string.repository_opening_error,
                    Toast.LENGTH_SHORT).show();
    }

    public void onClick(View v) {
        final String playername = usernameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        RoundRepository.LoginRegisterCallback loginRegisterCallback = new
                RoundRepository.LoginRegisterCallback() {

                    @Override
                    public void onLogin(String playerId) {
                        ConectPreferenceActivity.setPlayerUUID(LoginActivity.this, playerId);
                        ConectPreferenceActivity.setPlayerName(LoginActivity.this, playername);
                        ConectPreferenceActivity.setPlayerPassword(LoginActivity.this, password);
                        ConectPreferenceActivity.setLogged(LoginActivity.this, true);

                        startActivity(new Intent(LoginActivity.this, RoundListActivity.class));
                        finish();
                    }

                    @Override
                    public void onError(String error) {
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle(R.string.login_alert_dialog_title)
                                .setMessage(R.string.login_alert_dialog_message)
                                .setNeutralButton(R.string.login_alert_dialog_neutral_button_text,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();
                    }
                };

        switch (v.getId()) {
            case R.id.login_button:
                repository.login(playername, password, loginRegisterCallback);
                //Una vez logeados modificamos el valor del nombre de preferencias

                break;
            case R.id.cancel_button:
                finish();
                break;

            case R.id.new_user_button:
                repository.register(playername, password, loginRegisterCallback);
                ConectPreferenceActivity.setPlayerName(this, playername);
                if (repository instanceof FBDataBase)
                    repository.setPlayerNameSettings(playername, ConectPreferenceActivity.getPlayerUUID(this));
                break;

            case R.id.switch_game:
                if (ConectPreferenceActivity.getGameMode(this).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT))
                    ConectPreferenceActivity.setGameMode(this, "On-Line");
                else
                    ConectPreferenceActivity.setGameMode(this, ConectPreferenceActivity.GAMEMODE_DEFAULT);
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                finish();

        }
    }
}
