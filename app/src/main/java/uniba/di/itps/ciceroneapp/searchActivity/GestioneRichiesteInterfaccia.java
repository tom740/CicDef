package uniba.di.itps.ciceroneapp.searchActivity;

import android.text.TextWatcher;
import android.widget.TextView;

import java.util.ArrayList;

import uniba.di.itps.ciceroneapp.base.mvp.callback.ICallbackListener;
import uniba.di.itps.ciceroneapp.model.Event;

public interface GestioneRichiesteInterfaccia {
    interface MvpView{
    }
    interface Presenter{
        void showCategories(TextView category);
        void respondToQuery(ArrayList<Event>events, String city,String data,String categoria, ICallbackListener listener);
    }
}
