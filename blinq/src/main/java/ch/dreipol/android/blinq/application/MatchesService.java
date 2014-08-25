package ch.dreipol.android.blinq.application;


import android.text.TextUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.exceptions.IllegalUniqueIdentifierException;
import com.activeandroid.exceptions.ModelUpdateException;
import com.activeandroid.query.Delete;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        requestObservable.doOnNext(new Action1<ArrayList<Match>>() {
            @Override
            public void call(ArrayList<Match> matches) {
                List<Long> matchIds = Lists.transform(matches, new Function<Match, Long>() {
                    @Override
                    public Long apply(Match input) {
                        return input.getMatchId();
                    }
                });
                String matchIdString = TextUtils.join(", ", matchIds);
                new Delete().from(Match.class).where("MatchId NOT IN (?)", matchIdString).execute();
            }
        }).doOnNext(new Action1<ArrayList<Match>>() {
                        @Override
                        public void call(ArrayList<Match> matches) {
                            ActiveAndroid.beginTransaction();
                            try {
                                for (Match match : matches) {
                                    Match.createOrUpdate(match);
                                }
                                ActiveAndroid.setTransactionSuccessful();
                            } catch (IllegalUniqueIdentifierException e) {
                                e.printStackTrace();
                            } catch (ModelUpdateException e) {
                                e.printStackTrace();
                            } finally {
                                ActiveAndroid.endTransaction();
                            }
                        }
                    }

        ).flatMap(new Func1<Collection<Match>, Observable<Match>>() {
                      @Override
                      public Observable<Match> call(Collection<Match> matches) {
                          ActiveAndroid.beginTransaction();
                          List<Match> newMatches = new ArrayList<Match>();
                          Observable<Match> resultObservable;
                          try {
                              for (Match match : matches) {
                                  boolean isNew = !match.getReceived();
                                  match = Match.createOrUpdate(match);
                                  if (isNew) {
                                      newMatches.add(match);
                                  }
                              }
                              ActiveAndroid.setTransactionSuccessful();
                              resultObservable = Observable.from(newMatches);
                          } catch (IllegalUniqueIdentifierException e) {
                              e.printStackTrace();
                              resultObservable = Observable.error(e);
                          } catch (ModelUpdateException e) {
                              e.printStackTrace();
                              resultObservable = Observable.error(e);
                          } finally {
                              ActiveAndroid.endTransaction();
                          }
                          return resultObservable;
                      }
                  }

        ).subscribe(new Action1<Match>() {
                        @Override
                        public void call(Match match) {
                            Bog.v(Bog.Category.NETWORKING, "New match: " + match.toString());
                            //TODO: show new matches
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Bog.e(Bog.Category.NETWORKING, "Error while receiving matches: " + throwable.toString());
                        }
                    }

        );
    }
}
