package org.eclipse.recommenders.internal.snipmatch.rcp;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class SnipmatchActivator extends AbstractUIPlugin {

    private static SnipmatchActivator INSTANCE;

    public static SnipmatchActivator getDefault() {
        return INSTANCE;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        INSTANCE = this;
    }

    public void stop(BundleContext context) throws Exception {
        INSTANCE = null;
        super.stop(context);
    }
}
