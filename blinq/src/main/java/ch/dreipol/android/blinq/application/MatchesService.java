package ch.dreipol.android.blinq.application;


import com.activeandroid.exceptions.IllegalUniqueIdentifierException;
import com.activeandroid.exceptions.ModelUpdateException;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.Collection;

import ch.dreipol.android.blinq.services.IMatchesService;
import ch.dreipol.android.blinq.services.impl.BaseService;
import ch.dreipol.android.blinq.services.model.Match;
import ch.dreipol.android.blinq.util.Bog;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by melbic on 18/07/14.
 */
public class MatchesService extends BaseService implements IMatchesService {

    @Override
    public void loadMatches(Observable<ArrayList<Match>> requestObservable) {
//        TODO: CHeck if loadMatches is Already called before
        requestObservable.flatMap(new Func1<Collection<Match>, Observable<Match>>() {
            @Override
            public Observable<Match> call(Collection<Match> matches) {
                return Observable.from(matches);
            }
        }).subscribe(new Action1<Match>() {
            @Override
            public void call(Match match) {
                Match m2;
                try {
                    m2 = Match.createOrUpdate(match);
                } catch (IllegalUniqueIdentifierException e) {
                    e.printStackTrace();
                } catch (ModelUpdateException e) {
                    e.printStackTrace();
                }
                Bog.v(Bog.Category.NETWORKING, match.toString());
            }
        });
    }
}
