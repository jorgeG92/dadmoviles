package eps.android4_jorgegomez.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.Round;
import eps.android4_jorgegomez.model.RoundRepository;
import eps.android4_jorgegomez.model.RoundRepositoryFactory;

import static android.support.v4.os.LocaleListCompat.create;

@SuppressLint("ValidFragment")
public class AlertDialogSelectPlayerFragment extends DialogFragment{

    Round round;
    Map<String, String> playersInfo;


    @SuppressLint("ValidFragment")
    public AlertDialogSelectPlayerFragment(Round round,  Map<String, String> playersInfo){
        super();
        this.round = round;
        this.playersInfo = playersInfo;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        AlertDialog.Builder builderAux = new AlertDialog.Builder(getActivity());
        final RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity(), ConectPreferenceActivity.getGameMode(getActivity()).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT));

        final List<String> nombres = new ArrayList<>();
        for(String id: playersInfo.keySet()) {
            if (!id.equals(ConectPreferenceActivity.getPlayerUUID(getActivity())))
                nombres.add(playersInfo.get(id));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_user_add_round, nombres);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_player_title).setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String idUser = new String();
                String nameUser = new String();

                for(String id : playersInfo.keySet())
                    if(playersInfo.get(id).equals(nombres.get(which))) {
                        idUser = id;
                        nameUser = playersInfo.get(id);
                        break;
                    }
                ((RoundListActivity) activity).onNewRoundAdded(round, idUser, nameUser);
            }
        });

        return builder.create();
    }

    public interface Callback{
        void onUserList(Map<String, String> playersInfo);
        Map<String, String> getUserList();
    }

}
