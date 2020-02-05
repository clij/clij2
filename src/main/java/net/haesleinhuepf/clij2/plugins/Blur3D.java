package net.haesleinhuepf.clij2.plugins;


import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import org.scijava.plugin.Plugin;

@Deprecated
@Plugin(type = CLIJMacroPlugin.class, name = "CLIJ2_blur3D")
public class Blur3D extends GaussianBlur3D {
}
