package eps.android4_jorgegomez.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.Round;
import eps.android4_jorgegomez.model.RoundRepository;
import eps.android4_jorgegomez.model.RoundRepositoryFactory;

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle(R.string.game_over);
        alertDialogBuilder.setMessage(R.string.game_over_message);
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        String size = ConectPreferenceActivity.getBoardSize(getActivity());
                        final Round round = new Round(Integer.parseInt(size));
                        boolean offLineFlag = ConectPreferenceActivity.getGameMode(getActivity()).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT);
                        RoundRepository rr = RoundRepositoryFactory.createRepository(getActivity(),offLineFlag);
                        RoundRepository.BooleanCallback bc = new RoundRepository.BooleanCallback(){

                            public void onResponse(boolean ok){
                                if (activity instanceof RoundListActivity)
                                    ((RoundListActivity) activity).onRoundUpdated();
                                else
                                    ((RoundActivity) activity).finish();
                                dialog.dismiss();
                            }
                        };
                        rr.addRound(round, bc);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (activity instanceof RoundActivity)
                        activity.finish();
                    dialog.dismiss();
                }
            });
        return alertDialogBuilder.create();
    }
}
