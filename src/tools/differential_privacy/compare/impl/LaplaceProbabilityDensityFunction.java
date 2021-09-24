package tools.differential_privacy.compare.impl;

import tools.differential_privacy.compare.ProbabilityCompareFunction;

public class LaplaceProbabilityDensityFunction extends ProbabilityCompareFunction {

    public static double probabilityDensityFunction(double distanceA, double distanceB, double epsilonA, double epsilonB) {
        double distanceDiffer = distanceA - distanceB;
        double result, variableA, variableB;
        if (distanceDiffer == 0) {
            return 0.5;
        } else {
            if (distanceDiffer < 0) {
                variableA = Math.exp(epsilonA * distanceDiffer);
                if (epsilonB == epsilonA) {
                    result = 1 - variableA/2 + epsilonA*variableA*distanceDiffer/4;
                } else {
                    variableB = Math.exp(epsilonB*distanceDiffer);
                    result = 1 - variableA/2 + epsilonA*(1/(epsilonA+epsilonB)+1/(epsilonA-epsilonB))*(variableA-variableB)/4;
                }
            } else {
                variableA = Math.exp(-epsilonA * distanceDiffer);
                if (epsilonB == epsilonA) {
                    result = variableA/2 + epsilonA*variableA*distanceDiffer/4;
                } else {
                    variableB = Math.exp(-epsilonB * distanceDiffer);
                    result = variableA/2 + epsilonA*(1/(epsilonA-epsilonB)+1/(epsilonA+epsilonB))*(variableB - variableA)/4;
                }
            }
        }
        return result;
    }
}
