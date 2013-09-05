package org.eclipse.recommenders.internal.snipmatch.rcp;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    public void initializeDefaultPreferences() {
        IPreferenceStore store = SnipmatchActivator.getDefault().getPreferenceStore();
        // XXX I'd prefer to use some default like the folder below:
        File basedir = new File(SystemUtils.getUserHome(), ".eclipse/recommenders/snipmatch/");
        basedir.mkdirs();
        File snippetsdir = new File(basedir, "snippets");
        store.setDefault(Constants.P_LOCAL_SNIPPETS_REPO, snippetsdir.getAbsolutePath());
    }
}
