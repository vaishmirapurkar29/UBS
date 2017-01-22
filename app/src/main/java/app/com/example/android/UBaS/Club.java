package app.com.example.android.UBaS;

import static android.R.attr.name;

/**
 * Created by MandipSilwal on 11/25/16.
 */

public class Club {

    public String name;
    public Boolean isMember;

    public Club() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getMember() {
        return isMember;
    }

    public void setMember(Boolean member) {
        isMember = member;
    }

    public Club(String clubName, Boolean isMember) {

        this.name = clubName;
        this.isMember = isMember;

    }
}
