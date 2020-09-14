package net.haesleinhuepf.clij2.legacy;

import ij.macro.ExtensionDescriptor;
import ij.macro.MacroExtension;
import net.haesleinhuepf.clij.macro.AbstractCLIJPlugin;
import net.haesleinhuepf.clij.macro.CLIJHandler;
import net.haesleinhuepf.clij.macro.CLIJMacroPlugin;
import net.haesleinhuepf.clij.macro.CLIJMacroPluginService;
import org.scijava.plugin.PluginInfo;

import java.util.HashMap;
import java.util.Set;

public class FallBackCLIJMacroPluginService extends CLIJMacroPluginService {

    public FallBackCLIJMacroPluginService() {
        FallBackCLIJMacroPluginServiceInitializer.initialize(this);
    }

    HashMap<String, Class> pluginClassMap = new HashMap<String, Class>();

    public void registerPlugin(String methodName, Class pluginClass) {
        pluginClassMap.put(methodName, pluginClass);
    }

    @Override
    public void initialize() {}

    public Set<String> getCLIJMethodNames() {
        return pluginClassMap.keySet();
    }

    public CLIJMacroPlugin getCLIJMacroPlugin(final String name) {
        Class<CLIJMacroPlugin> klass = pluginClassMap.get(name);

        if (klass == null) {
            return null;
        }

        try {
            return klass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ExtensionDescriptor getPluginExtensionDescriptor(String name){
        final CLIJMacroPlugin plugin = getCLIJMacroPlugin(name);

        String[] parameters = plugin.getParameterHelpText().split(",");
        int[] parameterTypes = new int[0];

        if (parameters.length > 1 || parameters[0].length() > 0) {
            parameterTypes = new int[parameters.length];
            //int i = 0;
            for (int i = 0; i < parameters.length; i++) {
//            for (String parameter : parameters) {
                String parameter = parameters[i].trim();
                boolean byref = false;
                if (parameter.startsWith("ByRef")) {
                    parameter = parameter.substring(5).trim();
                    byref = true;
                }
                byref = byref && CLIJHandler.automaticOutputVariableNaming;

                if (parameter.trim().startsWith("Image")) {
                    parameterTypes[i] = MacroExtension.ARG_STRING;
                } else if (parameter.trim().startsWith("String")) {
                    parameterTypes[i] = MacroExtension.ARG_STRING;
                } else if (parameter.trim().startsWith("Array")) {
                    parameterTypes[i] = MacroExtension.ARG_ARRAY;
                } else {
                    parameterTypes[i] = MacroExtension.ARG_NUMBER;
                }

                if (byref) {
                    parameterTypes[i] = parameterTypes[i] | MacroExtension.ARG_OUTPUT;
                }
                //              i++;
            }
        }
        return new ExtensionDescriptor(name, parameterTypes, CLIJHandler.getInstance());
    }

    @Override
    public Class<CLIJMacroPlugin> getPluginType() {
        return CLIJMacroPlugin.class;
    }

    public String getNameByClass(Class<? extends AbstractCLIJPlugin> aClass) {
        for (String name : getCLIJMethodNames()) {
            if (getCLIJMacroPlugin(name).getClass() == aClass) {
                return name;
            }
        }
        return null;
    }

}
