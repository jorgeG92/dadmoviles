package eps.android4_jorgegomez.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

    private void updateUI() {
        RoundRepository repository = RoundRepository.get(getActivity());
        List<Round> rounds = repository.getRounds();
        if (roundAdapter == null) {
            roundAdapter = new RoundAdapter(rounds);
            roundRecyclerView.setAdapter(roundAdapter);
        } else {
            roundAdapter.notifyDataSetChanged();
        }
    }

    public class RoundAdapter extends RecyclerView.Adapter<RoundAdapter.RoundHolder>{

        private List<Round> rounds;

        public class RoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView idTextView;
            private TextView boardTextView;
            private TextView dateTextView;
            private Round round;

            public RoundHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
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

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = RoundActivity.newIntent(context, round.getId());
                context.startActivity(intent);
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