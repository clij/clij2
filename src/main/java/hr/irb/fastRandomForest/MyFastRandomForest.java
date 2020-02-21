package hr.irb.fastRandomForest;

import hr.irb.fastRandomForest.FastRandomForest;
import hr.irb.fastRandomForest.FastRfBagging;
import weka.classifiers.Classifier;

import java.util.Arrays;

public class MyFastRandomForest extends FastRandomForest {
    public String translateToOcl(int numClasses, int numFeatures) {

        StringBuilder oclCode = new StringBuilder();
        oclCode.append(
                "__kernel void classify_feature_stack(\n" +
                "    IMAGE_dst_TYPE dst,\n" +
                "    IMAGE_src_featureStack_TYPE src_featureStack) {\n");

        //oclCode.append(
        //       "__kernel void classify_feature_stack(\n" +
        //                "    DTYPE_IMAGE_OUT_2D dst,\n" +
        //                "    DTYPE_IMAGE_OUT_3D src_featureStack) {\n");


        oclCode.append("    const sampler_t sampler = CLK_NORMALIZED_COORDS_FALSE | CLK_ADDRESS_CLAMP_TO_EDGE | CLK_FILTER_NEAREST;\n");
        oclCode.append("    int x = get_global_id(0);\n");
        oclCode.append("    int y = get_global_id(1);\n");

        for (int c = 0; c < numClasses; c++) {
            oclCode.append("    float sum" + c + " = 0;\n");
        }

        for (int f = 0; f < numFeatures; f++) {
            oclCode.append("    float featureValue" + f + " = READ_src_featureStack_IMAGE(src_featureStack, sampler, POS_src_featureStack_INSTANCE(x, y, " + f + ", 0)).x;\n");
            //oclCode.append("    double featureValue" + f + " = READ_IMAGE_3D(src_featureStack, sampler, (int4)(x, y, " + f + ", 0)).x;\n");
        }

        int countTrees = 0;
        for (Classifier classifier : m_bagger.getClassifiers()) {

            appendClassifier(oclCode, ((FastRandomTree)classifier), "    ");
            countTrees ++;
        }

        oclCode.append("    int class = 0;\n");
        oclCode.append("    float maxProb = sum0 / " + countTrees + ";\n");
        for (int c = 1; c < numClasses; c++) {
            oclCode.append("    float value = sum" + c + " / " + countTrees + ";\n");
            oclCode.append("    if(maxProb < value) {\n");
            oclCode.append("        maxProb = value;\n");
            oclCode.append("        class = " + c + ";\n");
            oclCode.append("    }\n");
        }
        oclCode.append("    int2 pos = (int2)(x, y);\n");
        //oclCode.append("    WRITE_IMAGE_2D(dst, pos, (DTYPE_OUT)class);\n");
        oclCode.append("    WRITE_dst_IMAGE(dst, POS_dst_INSTANCE(x, y, 0, 0), CONVERT_dst_PIXEL_TYPE(class));\n");
        oclCode.append("};");

        System.out.println(oclCode.toString());

        return oclCode.toString();
    }

    private void appendClassifier(StringBuilder oclCode, FastRandomTree classifier, String spaces) {
        if (classifier.m_Attribute == -1) {
            for (int c = 0; c < classifier.m_ClassProbs.length; c++) {
                oclCode.append(spaces + " sum" + c + " += " + classifier.m_ClassProbs[c] + ";\n");
            }
            //oclCode.append(">> " + Arrays.toString(classifier.m_ClassProbs) + "\n");
        } else {
            oclCode.append(spaces + "if (featureValue" + classifier.m_Attribute + " < " + classifier.m_SplitPoint + ") {\n");
            appendClassifier(oclCode, classifier.m_Successors[0], spaces + "    ");
            oclCode.append(spaces + "} else {\n");
            appendClassifier(oclCode, classifier.m_Successors[1], spaces + "    ");
            oclCode.append(spaces + "}\n");
        }
    }
}
