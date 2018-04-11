package eps.android4_jorgegomez.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.Round;
import eps.android4_jorgegomez.model.RoundRepository;


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
                Round round = new Round(Integer.parseInt(ERPreferenceActivity.getBoardSize(getActivity())));
                RoundRepository.get(getActivity()).addRound(round);
                updateUI();
                return true;
            case R.id.menu_item_settings:
                callbacks.onPreferencesSelected();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        RoundRepository repository = RoundRepository.get(getActivity());
        List<Round> rounds = repository.getRounds();
        if (roundAdapter == null) {
            roundAdapter = new RoundAdapter(rounds);
            roundRecyclerView.setAdapter(roundAdapter);
        } else {
            roundAdapter.notifyDataSetChanged();
        }
    }

    public interface Callbacks {
            void onRoundSelected(Round round);
            void onPreferencesSelected();
    }

    private void setCardListener(View view) {
        roundRecyclerView =
                (RecyclerView)view.findViewById(R.id.round_recycler_view);

        RecyclerItemClickListener.OnItemClickListener onItemClickListener =
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Round round =
                                RoundRepository.get(getContext()).getRounds().get(position);
                        callbacks.onRoundSelected(round);
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
            private TextView boardTextView;
            private TextView dateTextView;
            private Round round;

            public RoundHolder(View itemView) {
                super(itemView);
                idTextView = (TextView) itemView.findViewById(R.id.list_item_id);
                boardTextView = (TextView) itemView.findViewById(R.id.list_item_board);
                dateTextView = (TextView) itemView.findViewById(R.id.list_item_date);
            }

            public void bindRound(Round round){
                this.round = round;
                idTextView.setText(round.getTitle());
                boardTextView.setText(round.getBoard().toString());
                dateTextView.setText(String.valueOf(round.getDate()).substring(0,19));
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

    }
}