package uniba.di.itps.ciceroneapp.gestioneRichieste.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import uniba.di.itps.ciceroneapp.R;
import uniba.di.itps.ciceroneapp.data.DataFetch;
import uniba.di.itps.ciceroneapp.gestioneRichieste.search.AdapterAttivitaRicercate;
import uniba.di.itps.ciceroneapp.gestioneRichieste.search.DettaglioAttivita;
import uniba.di.itps.ciceroneapp.gestioneRichieste.search.GestioneRichiesteInterfaccia;
import uniba.di.itps.ciceroneapp.model.Event;
import uniba.di.itps.ciceroneapp.model.Request;
import uniba.di.itps.ciceroneapp.model.User;

public class GestioneRichiestePresenter implements  GestioneRichiesteInterfaccia.Presenter{

    private Context context;
    private ArrayList<Event> events;
    private AdapterAttivitaRicercate adapter;
    private GestioneRichiesteInterfaccia.MvpView mvpView;

    GestioneRichiestePresenter(Context context) {
        this.context = context;
        events = new ArrayList<>();
        mvpView = new DettaglioAttivita();
    }



    @Override
    public void sendEventDetail(Event event) {
        Intent goToDetail = new Intent(context, DettaglioAttivita.class);
        goToDetail.putExtra("evento", event);
        context.startActivity(goToDetail);

    }

    @Override
    public void setEventDetail(Intent intent,GestioneRichiesteInterfaccia.MvpView mvpView) {
        Event event= (Event) intent.getSerializableExtra("evento");
        mvpView.setTextTitolo(event.getTitolo());
        mvpView.setTextCategoria(event.getCategoria());
        mvpView.setTextLuogo(event.getLuogo());
        mvpView.setTextLingua(event.getLingua());
        mvpView.setTextData(event.getDateEvento());
        mvpView.setTextDescrizione(event.getDescrizione());
        mvpView.setTextIndirizzo(event.getIndirizzo());
        mvpView.setTextOraInizio(event.getOrarioIncontro());
        String[] partsEnd = event.getOrarioFine().split(":");
        String[] partStart =  event.getOrarioInizio().split(":");
        int durata = Integer.valueOf(partsEnd[0]) - Integer.valueOf(partStart[0]);
        mvpView.setTextDurata(String.valueOf(durata));
        if(event.getFoto() != null){
            mvpView.setImmagineAttività(event.getFoto());
        }
        FirebaseFirestore.getInstance().collection(DataFetch.UTENTI).document(event.getIdCicerone()).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                User user = documentSnapshot.toObject(User.class);
                mvpView.setTextNomeC(user.getNome());
                mvpView.setTextCognomeC(user.getCognome());
                    if(user.getFotoprofilo() != null){
                        mvpView.setImmagineProfilo(user.getFotoprofilo());
                        }}
        });

    }


    @Override
    public void initRecyclerViewCerca(RecyclerView recyclerView,String city, String data, String categoria) {
        Query cities;

        //se sono stati scelti sia i filtri per la data che per la categoria esegui la query anche sugli altri due campi
        if(!(data.equals(context.getResources().getString(R.string.Date))) && !(categoria.equals(context.getResources().getString(R.string.category1)))){
            cities = FirebaseFirestore.getInstance().collection("Eventi").
                    whereEqualTo("luogo",city).
                    whereEqualTo("dateEvento",data).
                    whereEqualTo("categoria",categoria);}

        //se è stato sceltro solo il campo data esegui le query solo sul campo data
        else if(!(data.equals(context.getResources().getString(R.string.Date))) && (categoria.equals(context.getResources().getString(R.string.category1)))){
            cities = FirebaseFirestore.getInstance().collection(DataFetch.EVENTI).
                    whereEqualTo("luogo",city).
                    whereEqualTo("dateEvento",data);}

        //se è stato scelto il campo categoria esegui ale query solo sul campo categoria
        else if((data.equals(context.getResources().getString(R.string.Date))) && !(categoria.equals(context.getResources().getString(R.string.category1)))){
            cities = FirebaseFirestore.getInstance().collection(DataFetch.EVENTI).
                    whereEqualTo("luogo",city).
                    whereEqualTo("categoria",categoria);}
        else{
            cities = FirebaseFirestore.getInstance().collection(DataFetch.EVENTI).whereEqualTo("luogo",city);}

        cities.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                DocumentSnapshot document = dc.getDocument();
                Event event = document.toObject(Event.class);
                switch(dc.getType()){
                    case ADDED:
                        if(!(event.getIdCicerone().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) && event.getStato().equals("IN CORSO")){
                            //se non c'è il filtro data
                            events.add(event);

                        }
                        break;
                    case MODIFIED:
                        if(!(event.getIdCicerone().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) && event.getStato().equals("IN CORSO")){
                            events.set(dc.getNewIndex(),event);

                        }
                        break;

                    case REMOVED:
                        if(!(event.getIdCicerone().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) && event.getStato().equals("IN CORSO")){
                            events.remove(dc.getOldIndex());
                        }
                        break;
                }
            }
            adapter = new AdapterAttivitaRicercate(context,events);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
        events.clear();

    }

    @Override
    public void createRequestToDatabase(Intent receive) {
        Event event = (Event) receive.getSerializableExtra("evento");
        String status = "IN ATTESA";
        Request request = new Request(event.getIdCicerone(),event.getId(), FirebaseAuth.getInstance().getCurrentUser().getUid(),status);
        if(request.addRequestToDatabase()){
            mvpView.goToEvent();
        };

    }
}
