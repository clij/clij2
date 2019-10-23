package net.haesleinhuepf.clij.jython;

import org.fife.rsta.ac.AbstractLanguageSupport;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.scijava.module.ModuleService;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.swing.script.LanguageSupportPlugin;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * CLIJAutoCompleteSupportPlugin
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 07 2018
 */
public abstract class CLIJAutoCompleteSupportPlugin extends AbstractLanguageSupport
        implements LanguageSupportPlugin
{
    @Parameter
    ModuleService moduleService;

    private final static int MINIMUM_WORD_LENGTH_TO_OPEN_PULLDOWN = 2;

    @Override
    public void install(final RSyntaxTextArea rSyntaxTextArea) {
        final AutoCompletion ac = createAutoCompletion(getMacroAutoCompletionProvider());
        ac.setAutoActivationDelay(100);
        ac.setAutoActivationEnabled(true);
        ac.setShowDescWindow(true);
        ac.install(rSyntaxTextArea);
        installImpl(rSyntaxTextArea, ac);

        rSyntaxTextArea.addKeyListener(new AutoCompletionKeyListener(ac,
                rSyntaxTextArea));
    }

    private ScriptingAutoCompleteProvider getMacroAutoCompletionProvider() {
        ScriptingAutoCompleteProvider provider = ScriptingAutoCompleteProvider
                .getInstance();

        return provider;
    }

    @Override
    public void uninstall(final RSyntaxTextArea rSyntaxTextArea) {
        uninstallImpl(rSyntaxTextArea);

        final ArrayList<KeyListener> toRemove = new ArrayList<>();
        for (final KeyListener keyListener : rSyntaxTextArea.getKeyListeners()) {
            if (keyListener instanceof AutoCompletionKeyListener) {
                toRemove.add(keyListener);
            }
        }
        for (final KeyListener keyListener : toRemove) {
            rSyntaxTextArea.removeKeyListener(keyListener);
        }

    }

    private class AutoCompletionKeyListener implements KeyListener {

        AutoCompletion ac;
        RSyntaxTextArea textArea;
        ArrayList<Character> disabledChars;

        public AutoCompletionKeyListener(final AutoCompletion ac,
                                              final RSyntaxTextArea textArea)
        {
            this.ac = ac;
            this.textArea = textArea;

            disabledChars = new ArrayList<>();
            disabledChars.add(' ');
            disabledChars.add('\n');
            disabledChars.add('\t');
            disabledChars.add(';');
        }

        @Override
        public void keyTyped(final KeyEvent e) {

        }

        @Override
        public void keyPressed(final KeyEvent e) {

        }

        @Override
        public void keyReleased(final KeyEvent e) {
            SwingUtilities.invokeLater(() -> {
                if (disabledChars.contains(e.getKeyChar())) {
                    if (!e.isControlDown()) {
                        // the pulldown should not be hidden if CTRL+SPACE are pressed
                        ac.hideChildWindows();
                    }
                } else if ((e.isControlDown() && e.getKeyCode() != KeyEvent.VK_SPACE) || // control pressed but not space
                        e.getKeyCode() == KeyEvent.VK_LEFT || // arrow keys left/right were pressed
                        e.getKeyCode() == KeyEvent.VK_RIGHT
                ) {
                    ac.hideChildWindows();
                } else if (e.getKeyCode() >= 65 // a
                        && e.getKeyCode() <= 90 // z
                ) {
                    if (ScriptingAutoCompleteProvider.getInstance().getAlreadyEnteredText(
                            textArea).length() >= MINIMUM_WORD_LENGTH_TO_OPEN_PULLDOWN &&
                            ScriptingAutoCompleteProvider.getInstance()
                                    .getCompletions(textArea).size() > 1)
                    {
                        ac.doCompletion();
                    }
                }
            });
        }
    }

}
