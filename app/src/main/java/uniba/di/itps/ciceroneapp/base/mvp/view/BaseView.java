package uniba.di.itps.ciceroneapp.base.mvp.view;
/**
 * Created by H.T. on 01/12/17.
 */

public interface BaseView extends ILoadingView {

    void initPresenter();

    void destroy();
}
