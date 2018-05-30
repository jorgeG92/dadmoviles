package eps.android4_jorgegomez.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.Round;
import eps.android4_jorgegomez.model.RoundRepository;
import eps.android4_jorgegomez.model.RoundRepositoryFactory;
import eps.android4_jorgegomez.view.ConectView;


public class RoundListFragment extends Fragment {

    private RecyclerView roundRecyclerView;
    private RoundAdapter roundAdapter;
    private Callbacks callbacks;
    private Map<String, String> usersInfo;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_round_list, container, false);

        roundRecyclerView = (RecyclerView) view.findViewById(R.id.round_recycler_view);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerView.setItemAnimator(new DefaultItemAnimator());

        updateUI();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_round:
                if (ConectPreferenceActivity.getRandomMode(getActivity()))
                    addRandomPlayer();
                else
                    addSelectedPlayer();
                return true;

            case R.id.menu_item_settings:
                callbacks.onPreferencesSelected();
                return true;

            case R.id.menu_item_close_session:
                ConectPreferenceActivity.setPlayerName(getActivity(), ConectPreferenceActivity.PLAYERNAME_DEFAULT);
                //ConectPreferenceActivity.setPlayerPassword(getActivity(),ConectPreferenceActivity.PLAYERPASS_KEY);
                ConectPreferenceActivity.setPlayerUUID(getActivity(), ConectPreferenceActivity.PLAYERID_DEFAULT);
                ConectPreferenceActivity.setLogged(getActivity(), ConectPreferenceActivity.LOGGED_DEFAULT);
                callbacks.onCloseSession();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addSelectedPlayer(){
        int size = Integer.parseInt(ConectPreferenceActivity.getBoardSize(getActivity()));
        final Round round = new Round(size);

        round.setFirstPlayerUUID(ConectPreferenceActivity.getPlayerUUID(getActivity()));
        round.setFirstPlayerName(ConectPreferenceActivity.getPlayerName(getActivity()));

        new AlertDialogSelectPlayerFragment(round, usersInfo).show(getActivity().getSupportFragmentManager(),"ALERT DIALOG");
    }

    /**
     *
     */
    private void addRandomPlayer(){
        int size = Integer.parseInt(ConectPreferenceActivity.getBoardSize(getActivity()));
        final Round round = new Round(size);

        if (new Random().nextBoolean()) {
            round.setFirstPlayerUUID(ConectPreferenceActivity.getPlayerUUID(getActivity()));
            round.setFirstPlayerName(ConectPreferenceActivity.getPlayerName(getActivity()));
            round.setSecondPlayerName("El_Random");
            round.setSecondPlayerUUID("0000-0000-0000");
        }else{
            round.setFirstPlayerUUID("0000-0000-0000");
            round.setFirstPlayerName("El_Random");
            round.setSecondPlayerName(ConectPreferenceActivity.getPlayerName(getActivity()));
            round.setSecondPlayerUUID(ConectPreferenceActivity.getPlayerUUID(getActivity()));
        }
        boolean offLineFlag = ConectPreferenceActivity.getGameMode(getActivity()).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT);
        RoundRepository repository =
                RoundRepositoryFactory.createRepository(getActivity(), offLineFlag);
        RoundRepository.BooleanCallback callback =
                new RoundRepository.BooleanCallback() {
                    @Override
                    public void onResponse(boolean response) {
                        if (response == false)
                            Snackbar.make(getView(), R.string.error_adding_round, Snackbar.LENGTH_SHORT).show();
                        else
                            callbacks.onNewRoundAdded(round);
                    }
                };
        repository.addRound(round, callback);
        updateUI();
    }

    /**
     *
     */
    private void updatePlayers(){
        int size = Integer.parseInt(ConectPreferenceActivity.getBoardSize(getActivity()));
        final Round round = new Round(size);
        round.setFirstPlayerUUID(ConectPreferenceActivity.getPlayerUUID(getActivity()));
        round.setFirstPlayerName(ConectPreferenceActivity.getPlayerName(getActivity()));

        //Esta funcion es solo parte online, no es necesario realizar la cmporbacion
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity(), false);
        RoundRepository.UsersCallback usersCallback = new RoundRepository.UsersCallback() {
            @Override
            public void onResponse(Map<String, String> usersIdName) {
                if (usersIdName == null)
                    usersInfo = new HashMap<>();
                else
                    usersInfo = usersIdName;
            }

            @Override
            public void onError(String error) {

            }
        };
        usersCallback.onResponse(repository.getUsers(usersCallback));
    }


    public void updateUI() {
        updatePlayers();
        boolean offLineFlag = ConectPreferenceActivity.getGameMode(getActivity()).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT);
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity(), offLineFlag);
        RoundRepository.RoundsCallback roundsCallback = new RoundRepository.RoundsCallback() {
            @Override
            public void onResponse(List<Round> rounds) {
                if (roundAdapter == null){
                    roundAdapter = new RoundAdapter(rounds);
                    roundRecyclerView.setAdapter(roundAdapter);
                }else{
                    roundAdapter.setRounds(rounds);
                    roundAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String error) {
                Log.d("DEBUG", "No rounds in database");
            }
        };
        String playeruuid = ConectPreferenceActivity.getPlayerUUID(getActivity());
        roundsCallback.onResponse(repository.getRounds(playeruuid, null, null, roundsCallback));
    }


    public interface Callbacks {
            void onRoundSelected(Round round);
            void onPreferencesSelected();
            void onNewRoundAdded(Round round);
            void onNewRoundAdded(Round round, String userId, String userName);
            void onCloseSession();
            void onRoundDeleted();
    }

    public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundHolder>{

        private List<Round> rounds;

        public class RoundHolder extends RecyclerView.ViewHolder{

            private TextView idTextView;
            private TextView dateTextView;
            private TextView secondPlayerTextView;
            private ImageButton playRound, deleteRound;
            private ConectView board;

            public RoundHolder(View itemView) {
                super(itemView);
                idTextView = (TextView) itemView.findViewById(R.id.list_item_id);
                board = (ConectView) itemView.findViewById(R.id.list_item_board);
                dateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
                secondPlayerTextView = (TextView) itemView.findViewById(R.id.list_item_second_player_name);
                deleteRound = (ImageButton) itemView.findViewById(R.id.list_item_delete_round);
                playRound = (ImageButton) itemView.findViewById(R.id.list_item_play_round);
            }

            public void bindRound(final Round round){
                idTextView.setText(round.getTitle());
                board.setBoard(round.getSize(), round.getBoard());
                dateTextView.setText(String.valueOf(round.getDate()).substring(0,19));
                if (ConectPreferenceActivity.getPlayerUUID(getActivity()).equals(round.getFirstPlayerUUID()))
                    secondPlayerTextView.setText(round.getSecondPlayerName());
                else
                    secondPlayerTextView.setText(round.getFirstPlayerName());

                deleteRound.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean offLine = ConectPreferenceActivity.getGameMode(getActivity()).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT);
                                RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity(), offLine);

                                RoundRepository.BooleanCallback booleanCallback = new RoundRepository.BooleanCallback() {
                                    @Override
                                    public void onResponse(boolean ok) {
                                        if (ok) {
                                            callbacks.onRoundDeleted();
                                        }
                                        else
                                            Snackbar.make(getView(), R.string.error_deleting_round, Snackbar.LENGTH_LONG).show();

                                    }
                                };

                                repository.deleteRound(round, booleanCallback);
                            }
                        }
                );

                playRound.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callbacks.onRoundSelected(round);
                            }
                        }
                );

            }

        }

        public RoundAdapter(List<Round> rounds){
            this.rounds = rounds;
        }

        @Override
        public RoundAdapter.RoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_round, parent, false);
            return new RoundAdapter.RoundHolder(view);
        }
        @Override
        public void onBindViewHolder(RoundAdapter.RoundHolder holder, int position) {
            Round round = rounds.get(position);
            holder.bindRound(round);
        }
        @Override
        public int getItemCount() {
            return rounds.size();
        }

        public void setRounds(List<Round> rounds){this.rounds = rounds; }

    }
}