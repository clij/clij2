package net.haesleinhuepf.clijx.jython;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.Roi;
import ij.macro.Interpreter;
import ij.process.ImageProcessor;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.clearcl.interfaces.ClearCLImageInterface;
import net.haesleinhuepf.clij.utilities.CLIJOps;
import net.imglib2.Cursor;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.roi.Regions;
import net.imglib2.view.Views;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.Completion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.SortByRelevanceComparator;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

/**
 * ScriptingAutoCompleteProvider
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 07 2018
 */
public class ScriptingAutoCompleteProvider extends DefaultCompletionProvider
{
    private final int maximumSearchResults = 100;

    private static ScriptingAutoCompleteProvider instance = null;

    public static ScriptingAutoCompleteProvider getInstance () {
        if (instance == null) {
            instance = new ScriptingAutoCompleteProvider();
        }
        return instance;
    }

    private ScriptingAutoCompleteProvider() {

        addCompletion(makeListEntry(this, "clijx.create([width, height])", null, "<b>create</b><br>Create a 2D buffer of given dimensions with type Float / 32-bit."));
        addCompletion(makeListEntry(this, "clijx.create([width, height, depth])", null, "<b>create</b><br>Create a 3D buffer of given dimensions with type Float / 32-bit."));
        addCompletion(makeListEntry(this, "clijx.create([width, height, depth], NativeTypeEnum type)", null, "<b>create</b><br>Create a 3D buffer of given dimensions given with."));
        addCompletion(makeListEntry(this, "clijx.Float", null, "<b>Float</b><br>32-bit pixel type."));
        addCompletion(makeListEntry(this, "clijx.UnsignedShort", null, "<b>UnsignedShort</b><br16-bit pixel type."));
        addCompletion(makeListEntry(this, "clijx.UnsignedByte", null, "<b>UnsignedByte/b><br>8-bit pixel type."));

        addCompletion(makeListEntry(this, "clijx.create(ClearCLBuffer buffer)", null, "<b>create</b><br>Create a buffer with dimensions and type as the given buffer."));
        addCompletion(makeListEntry(this, "clijx.getGPUName()", null, "<b>getGPUName</b><br>Returns the name of the currently selected device."));
        addCompletion(makeListEntry(this, "clijx.push(Object objectOnCPU)", null, "<b>push</b><br>Pushes an image to GPU memory and returns the buffer on the GPU."));
        addCompletion(makeListEntry(this, "clijx.pull(Object objectOnGPU)", null, "<b>pull</b><br>Pulls an image out of GPU memory and return an ImagePlus."));
        addCompletion(makeListEntry(this, "clijx.execute(Class anchorClass, String pProgramFilename, String pKernelname, long[] dimensions, long[] globalsizes, HashMap<String, Object> parameters)", null, "<b>execute</b><br>Executes an OpenCL kernel. TODO: Write details."));
        addCompletion(makeListEntry(this, "clijx.show(Object object, String title)", null, "<b>show</b><br>Pulls an image from GPU memory and shows it in a window with the given title."));
        addCompletion(makeListEntry(this, "clijx.getOpenCLVersion()", null, "<b>getOpenCLVersion</b><br>Returns the supported OpenCL version of the selected device."));

        addCompletion(makeListEntry(this, "clijx.release(ClearCLImageInterface image_or_buffer)", null, "<b>release</b><br>Releases a given image or buffer.\n\nNote: You need to call clijx.setKeepReferences(true); to activate this functionality."));
        addCompletion(makeListEntry(this, "from net.haesleinhuepf.clijx import CLIJx;\nclijx = CLIJx.getInstance()", null, "<b>CLIJx.getInstance</b><br>Initialize CLIJx."));
        addCompletion(makeListEntry(this, "clijx.clear()", null, "<b>clear</b><br>Releases all images and buffers currently stored on the GPU. \n\nNote: You need to call clijx.setKeepReferences(true); to activate this functionality."));
        addCompletion(makeListEntry(this, "clijx.reportMemory()", null, "<b>reportMemory</b><br>Returns a report as string listing which images and buffers are currently stored in the GPU.\n\nNote: You need to call clijx.setKeepReferences(true); to activate this functionality."));

        for (BasicCompletion basicCompletion : CLIJxAutoComplete.getCompletions(this)) {
            addCompletion(basicCompletion);
        }

        addClassToAutoCompletion(CLIJOps.class, "clij.op().");
        addClassToAutoCompletion(CLIJ.class, "clij.");
        //addClassToAutoCompletion(CLIJx.class, "clijx.");
        addClassToAutoCompletion(ClearCLBuffer.class, "buffer.");

        addClassToAutoCompletion(ImagePlus.class, "imp.");
        addClassToAutoCompletion(ImageProcessor.class, "ip.");
        addClassToAutoCompletion(IJ.class, "IJ.");
        addClassToAutoCompletion(Roi.class, "roi.");

        addClassToAutoCompletion(ArrayImgs.class, "ArrayImgs.");
        addClassToAutoCompletion(Cursor.class, "cursor.");
        addClassToAutoCompletion(RandomAccess.class, "randomAccess.");
        addClassToAutoCompletion(RandomAccessibleInterval.class, "rai.");
        addClassToAutoCompletion(Img.class, "img.");
        addClassToAutoCompletion(Views.class, "Views.");
        addClassToAutoCompletion(Regions.class, "Regions.");
        addClassToAutoCompletion(ImageJFunctions.class, "ImageJFunctions.");
    }

    private void addClassToAutoCompletion(Class c, String prefix) {
        Method[] methods = c.getDeclaredMethods();
        for (Method method : methods) {
            //if (Modifier.isStatic(method.getModifiers()) || prefix.substring(0,0) == prefix.substring(0,0).toLowerCase()) {
                //System.out.println(method.toString());
                String description = "<b>" + method.getName() + "</b><br>";
                String headline = prefix + method.getName() + "(";
                String name = prefix + method.getName();

                for (Parameter parameter : method.getParameters()) {
                    //System.out.println(parameter);
                    if (!headline.endsWith("(")) {
                        headline = headline + ", ";
                    }
                    headline = headline + parameter.getName();
                    description = description + "<li>" + parameter.getType().getSimpleName() + " " + parameter.getName() + "</li>";
                }
                headline = headline + ");";

                description = description + "<br><br>" +
                        "Defined in <br>" +
                        c.getCanonicalName() +"<br>";

                description = description + "<br><br>" +
                        "returns " + method.getReturnType().getSimpleName();

                addCompletion(makeListEntry(this, headline, name, description));
            //}
        }
        if (c.getSuperclass() != null && c.getSuperclass() != Object.class) {
            addClassToAutoCompletion(c.getSuperclass(), prefix);
        }
    }

    protected boolean isValidChar(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_' || ch == '.' || ch == '2';
    }



    private BasicCompletion makeListEntry(
            final ScriptingAutoCompleteProvider provider, String headline,
            final String name, String description)
    {

        if (headline.trim().endsWith("-")) {
            headline = headline.trim();
            headline = headline.substring(0, headline.length() - 2);
        }

        return new BasicCompletion(provider, headline, null, description);
    }

    @Override
    public List<Completion> getCompletionByInputText(String inputText) {
        inputText = inputText.toLowerCase();

        ArrayList<Completion> result = new ArrayList<Completion>();

        int count = 0;
        int secondaryCount = 0;
        for (Completion completion : completions) {
            String text = completion.getInputText().toLowerCase();
            if (text.contains(inputText)) {
                if (text.startsWith(inputText)) {
                    result.add(count, completion);
                    count++;
                } else {
                    result.add(completion);
                    secondaryCount++;
                }
            }
            if (secondaryCount + count > maximumSearchResults) {
                break; // if too many results are found, exit to not annoy the user
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected List<Completion> getCompletionsImpl(JTextComponent comp) {

        List<Completion> retVal = new ArrayList<Completion>();
        String text = getAlreadyEnteredText(comp);

        if (text != null) {
            retVal = getCompletionByInputText(text);
            appendMacroSpecificCompletions(text, retVal, comp);
        }
        return retVal;
    }

    private void appendMacroSpecificCompletions(String input, List<Completion> result, JTextComponent comp) {

        List<Completion> completions = new ArrayList<Completion>();
        String lcaseinput = input.toLowerCase();

        String text = null;
        try {
            text = comp.getDocument().getText(0, comp.getDocument().getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
            return;
        }
        text = text + "\n" + Interpreter.getAdditionalFunctions();

        int linecount = 0;
        String[] textArray = text.split("\n");
        for (String line : textArray){
            String trimmedline = line.trim();
            String lcaseline = trimmedline.toLowerCase();
            if (lcaseline.startsWith("function ") || lcaseline.startsWith("def ")) {
                int len = 8;
                if (lcaseline.startsWith("def ")) {
                    len = 4;
                }
                String command = trimmedline.substring(len).trim().replace("{", "");
                String lcasecommand = command.toLowerCase();
                if (lcasecommand.contains(lcaseinput)) {
                    String description = findDescription(textArray, linecount, "User defined function " + command + "\n as specified in line " + (linecount + 1));
                    completions.add(new BasicCompletion(this, command, null, description));
                }
            }
            if (lcaseline.contains("=")) {
                String command = trimmedline.substring(0, lcaseline.indexOf("=")).trim();
                String lcasecommand = command.toLowerCase();
                if (lcasecommand.contains(lcaseinput) && command.matches("[_a-zA-Z]+")) {
                    String description = "User defined variable " + command + "\n as specified in line " + (linecount + 1);

                    completions.add(new BasicCompletion(this, command, null, description));
                }
            }
            linecount++;
        }

        Collections.sort(completions, new SortByRelevanceComparator());

        result.addAll(0, completions);
    }

    private String findDescription(String[] textArray, int linecount, String defaultDescription) {
        String resultDescription = "";
        int l = linecount - 1;
        while (l > 0) {
            String lineBefore = textArray[l].trim();
            //System.out.println("Scanning B " + lineBefore);
            if (lineBefore.startsWith("//")) {
                resultDescription = lineBefore.substring(2) + "\n" + resultDescription;
            } else {
                break;
            }
            l--;
        }
        l = linecount + 1;
        while (l < textArray.length - 1) {
            String lineAfter = textArray[l].trim();
            //System.out.println("Scanning A " + lineAfter);
            if (lineAfter.startsWith("//")) {
                resultDescription = resultDescription + "\n" + lineAfter.substring(2);
            } else {
                break;
            }
            l++;
        }
        if (resultDescription.length() > 0) {
            resultDescription = resultDescription + "<br><br>";
        }
        resultDescription = resultDescription + defaultDescription;

        return resultDescription;
    }


    @Override
    public List<Completion> getCompletions(JTextComponent comp) {
        List<Completion> completions = this.getCompletionsImpl(comp);
        return completions;
    }

    public static void main(String... args) {
        ScriptingAutoCompleteProvider.getInstance();
    }

}
