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
import android.widget.TextView;

import java.util.List;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_round_list, container, false);

        roundRecyclerView = (RecyclerView) view.findViewById(R.id.round_recycler_view);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerView.setItemAnimator(new DefaultItemAnimator());

        setCardListener(view);
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
                return true;

            case R.id.menu_item_settings:
                callbacks.onPreferencesSelected();
                return true;

            case R.id.menu_item_close_session:
                ConectPreferenceActivity.setPlayerName(getActivity(), ConectPreferenceActivity.PLAYERNAME_DEFAULT);
                ConectPreferenceActivity.setPlayerPassword(getActivity(),ConectPreferenceActivity.PLAYERPASS_KEY);
                ConectPreferenceActivity.setPlayerUUID(getActivity(), ConectPreferenceActivity.PLAYERID_DEFAULT);
                callbacks.onCloseSession();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
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
            void onCloseSession();
    }

    private void setCardListener(View view) {
        roundRecyclerView = (RecyclerView)view.findViewById(R.id.round_recycler_view);

        RecyclerItemClickListener.OnItemClickListener onItemClickListener =
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        boolean offLineFlag = ConectPreferenceActivity.getGameMode(getActivity()).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT);
                        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity(), offLineFlag);
                        RoundRepository.RoundsCallback roundsCallback = new RoundRepository.RoundsCallback()
                        {
                            @Override
                            public void onResponse(List<Round> rounds) {
                                callbacks.onRoundSelected(rounds.get(position));
                            }
                            @Override
                            public void onError(String error) {
                                Snackbar.make(getView(), R.string.error_reading_rounds,
                                        Snackbar.LENGTH_LONG).show();
                            }
                        };
                        String playeruuid = ConectPreferenceActivity.getPlayerUUID(getActivity());
                        repository.getRounds(playeruuid, null, null, roundsCallback);
                    }
                };
        RecyclerItemClickListener listener =
                new RecyclerItemClickListener(getActivity(), onItemClickListener);
        roundRecyclerView.addOnItemTouchListener(listener);
    }

    public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundHolder>{

        private List<Round> rounds;

        public class RoundHolder extends RecyclerView.ViewHolder{

            private TextView idTextView;
            private TextView dateTextView;
            private TextView secondPlayerTextView;
            private ConectView board;

            public RoundHolder(View itemView) {
                super(itemView);
                idTextView = (TextView) itemView.findViewById(R.id.list_item_id);
                board = (ConectView) itemView.findViewById(R.id.list_item_board);
                dateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
                secondPlayerTextView = (TextView) itemView.findViewById(R.id.list_item_second_player_name);
            }

            public void bindRound(Round round){
                idTextView.setText(round.getTitle());
                board.setBoard(round.getSize(), round.getBoard());
                dateTextView.setText(String.valueOf(round.getDate()).substring(0,19));
                String id =  ConectPreferenceActivity.getPlayerUUID(getActivity());
                if (id.equals(round.getFirstPlayerUUID()))
                    secondPlayerTextView.setText(round.getSecondPlayerName());
                else
                    secondPlayerTextView.setText(round.getFirstPlayerName());
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