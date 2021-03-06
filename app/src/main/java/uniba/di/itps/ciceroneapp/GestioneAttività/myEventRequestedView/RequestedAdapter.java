package uniba.di.itps.ciceroneapp.GestioneAttività.myEventRequestedView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Map;

import uniba.di.itps.ciceroneapp.GestioneAttività.InterfaceGestioneAttività;
import uniba.di.itps.ciceroneapp.GestioneAttività.PresenterGestioneAttività;
import uniba.di.itps.ciceroneapp.R;
import uniba.di.itps.ciceroneapp.gestioneRichieste.search.GestioneRichiesteInterfaccia;
import uniba.di.itps.ciceroneapp.gestioneRichieste.search.GestioneRichiestePresenter;

public class RequestedAdapter extends RecyclerView.Adapter<MyEventRequestedHolder> {
    private ArrayList<Map<String,Object>> requests;
    private GestioneRichiesteInterfaccia.Presenter presenter;

    public RequestedAdapter(Context context, ArrayList<Map<String, Object>> requests) {
        this.requests = requests;
        this.presenter = new GestioneRichiestePresenter(context);
    }

    @NonNull
    @Override
    public MyEventRequestedHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyEventRequestedHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_requested_item,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyEventRequestedHolder myEventRequestedHolder, int i) {
        presenter.onBindHolderR(myEventRequestedHolder,i,requests);
        myEventRequestedHolder.root.setOnClickListener(v -> presenter.sendEventDetail(i,requests,false));

    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
