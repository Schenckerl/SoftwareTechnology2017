package at.thelegend27.timemanagementtool;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import junit.framework.Assert;

/**
 * Created by markusfriedl on 01/06/2017.
 */

public class FragmentTestRule<F extends Fragment> extends ActivityTestRule<TimemanagementActivity> {

    private final Class<F> mFragmentClass;
    private F mFragment;

    public FragmentTestRule(final Class<F> fragmentClass) {
        super(TimemanagementActivity.class, true, false);
        mFragmentClass = fragmentClass;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Instantiate and insert the fragment into the container layout
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    mFragment = mFragmentClass.newInstance();
                    transaction.replace(R.id.content_frame, mFragment);
                    transaction.commit();
                } catch (InstantiationException | IllegalAccessException e) {
                    Assert.fail(String.format("%s: Could not insert %s into TestActivity: %s",
                            getClass().getSimpleName(),
                            mFragmentClass.getSimpleName(),
                            e.getMessage()));
                }
            }
        });
    }
    public F getFragment(){
        return mFragment;
    }
}
