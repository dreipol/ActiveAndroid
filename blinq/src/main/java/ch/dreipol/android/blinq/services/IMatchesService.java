package ch.dreipol.android.blinq.services;

import java.util.Collection;

import ch.dreipol.android.blinq.services.model.Match;
import ch.dreipol.android.blinq.services.model.Profile;
import rx.Observable;

/**
 * Created by melbic on 18/07/14.
 */
public interface IMatchesService extends IService {
   public  void loadMatches(Observable<Collection<Match>> requestObservable);
}
