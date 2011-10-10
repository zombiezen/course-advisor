package edu.calpoly.razsoftware;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UserState
{
    private Set<Course> taken;

    public UserState()
    {
        taken = new HashSet();
    }

    public UserState(Set<Course> taken)
    {
        this.taken = new HashSet<Course>(taken);
    }

    public UserState(File file)
    {
        // TODO(dpanger)
    }

    public void write(File file) throws IOException
    {
        // TODO(dpanger)
    }
}
