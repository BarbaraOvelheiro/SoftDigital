package pt.ipg.a.softdigital;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
            //    return ;
            case 1:

            case 2:

            case 3:
             ;
            case 4:

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
