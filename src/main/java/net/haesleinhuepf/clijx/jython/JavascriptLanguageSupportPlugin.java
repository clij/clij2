package net.haesleinhuepf.clijx.jython;

import org.scijava.plugin.Plugin;
import org.scijava.ui.swing.script.LanguageSupportPlugin;

@Plugin(type = LanguageSupportPlugin.class)
public class JavascriptLanguageSupportPlugin extends CLIJAutoCompleteSupportPlugin {
    @Override
    public String getLanguageName() {
        return "javascript";
    }
}
